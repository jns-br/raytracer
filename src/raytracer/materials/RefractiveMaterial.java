package raytracer.materials;

import raytracer.abstractions.Hit;
import raytracer.abstractions.Ray;
import raytracer.materials.Material;
import raytracer.materials.ReflectionProperties;
import cgtools.Vec3;

import static cgtools.Vec3.*;

public class RefractiveMaterial implements Material {

    private Vec3 emission;
    private Vec3 albedo;
    private double refractionFac;


    public RefractiveMaterial(Vec3 albedo, double refractionFac) {
        this(vec3(0, 0, 0), albedo, refractionFac);
    }

    public RefractiveMaterial(Vec3 emission, Vec3 albedo, double refractionFac){
        this.emission = emission;
        this.albedo = albedo;
        this.refractionFac = refractionFac;
    }

    @Override
    public ReflectionProperties properties(Ray r, Hit h) {

        double inFac = 1.0;
        double outFac = this.refractionFac; // Glass 1.5

        Vec3 rayDir = r.norm;
        Vec3 hitNorm = h.n;

        if (dotProduct(hitNorm, rayDir) > 0) {
            hitNorm = multiply(-1, hitNorm);
            inFac = this.refractionFac;
            outFac = 1.0;
        }

        Vec3 dt = refract(rayDir, hitNorm, inFac, outFac);


        Ray ray = new Ray(h.x, 0.0001, Double.POSITIVE_INFINITY, dt);

        ReflectionProperties properties = new ReflectionProperties(albedo, emission, ray);
        return properties;
    }


    private Vec3 refract(Vec3 rayDir, Vec3 hitNorm, double inFac, double outFac) {
        double ref = inFac / outFac;
        double c = -(dotProduct(hitNorm, rayDir));

        double discriminant = 1 - (ref * ref) * (1 - (c * c));

        if(discriminant > 0){
            if(cgtools.Random.random() > schlick(rayDir, hitNorm, inFac, outFac)){
                Vec3 refractedDirection = add(multiply(ref, rayDir), multiply((ref*c) - Math.sqrt(discriminant), hitNorm));
                return refractedDirection;
            }else {
                return reflect(rayDir, hitNorm);
            }
        }else {
            return reflect(rayDir, hitNorm);
        }
    }

    private Vec3 reflect(Vec3 rayDir, Vec3 hitNorm) {

        return subtract(rayDir, multiply(2, multiply(dotProduct(rayDir, hitNorm), hitNorm)));
    }

    private double schlick(Vec3 rayDir, Vec3 hitNorm, double inFac, double outFac) {
        double r0 = ((inFac - outFac) / (inFac + outFac)) * ((inFac - outFac) / (inFac + outFac));
        return r0 + (1 - r0) * Math.pow(1 + dotProduct(hitNorm, rayDir), 5);
    }

}
