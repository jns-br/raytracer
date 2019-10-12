package raytracer.materials;

import raytracer.abstractions.Hit;
import raytracer.abstractions.Ray;
import raytracer.materials.Material;
import raytracer.materials.ReflectionProperties;

import static cgtools.Vec3.*;

import cgtools.Vec3;

public class ReflectionMaterial implements Material {

    private Vec3 emission;
    private Vec3 albedo;
    private double scatterFac;

    public ReflectionMaterial(Vec3 albedo, double scatterFac) {
        this(vec3(0, 0, 0), albedo, scatterFac);
    }

    public ReflectionMaterial(Vec3 emission, Vec3 albedo, double scatterFac) {
        this.emission = emission;
        this.albedo = albedo;
        this.scatterFac = scatterFac;
    }

    @Override
    public ReflectionProperties properties(Ray r, Hit h) {

        Vec3 reflectedNorm = subtract(r.norm, multiply(2, multiply(dotProduct(h.n, r.norm), h.n)));

        Vec3 randomVec = vec3(cgtools.Random.random() * 2 - 1, cgtools.Random.random() * 2 -1, cgtools.Random.random() * 2 - 1);

        while (randomVec.length() > 1){
            randomVec = vec3(cgtools.Random.random() * 2 - 1, cgtools.Random.random() * 2 -1, cgtools.Random.random() * 2 - 1);
        }

        reflectedNorm = add(reflectedNorm, multiply(scatterFac, randomVec));

        Ray ray = null;
        if(dotProduct(reflectedNorm, h.n) > 0) {
            ray = new Ray(h.x, 0.0001, Double.POSITIVE_INFINITY, reflectedNorm);
        }

        return new ReflectionProperties(albedo, emission, ray);

    }
}
