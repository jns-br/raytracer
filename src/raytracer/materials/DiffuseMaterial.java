package raytracer.materials;

import raytracer.abstractions.Hit;
import raytracer.abstractions.Ray;
import cgtools.Vec3;
import static cgtools.Vec3.*;

public class DiffuseMaterial implements Material {

    private Vec3 emission;
    private Vec3 albedo;

    public DiffuseMaterial(Vec3 albedo) {
        this(vec3(0, 0, 0), albedo);
    }

    public DiffuseMaterial(Vec3 emission, Vec3 albedo){
        this.emission = emission;
        this.albedo = albedo;
    }

    @Override
    public ReflectionProperties properties(Ray r, Hit h) {
        Vec3 randomVec = vec3(cgtools.Random.random() * 2 - 1, cgtools.Random.random() * 2 - 1, cgtools.Random.random() * 2 - 1);
        while (randomVec.length() > 1){
            randomVec = vec3(cgtools.Random.random() * 2 - 1, cgtools.Random.random() * 2 - 1, cgtools.Random.random() * 2 - 1);
        }
        Ray ray = new Ray(h.x, 0.0001, Double.POSITIVE_INFINITY, add(h.n, randomVec));
        ReflectionProperties properties = new ReflectionProperties(albedo, emission, ray);
        return properties;
    }
}
