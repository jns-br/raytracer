package raytracer.materials;

import raytracer.abstractions.Hit;
import raytracer.abstractions.Ray;
import cgtools.Vec3;

public class BackgroundMaterial implements Material{

    Vec3 emission;
    Vec3 albedo;

    public BackgroundMaterial(Vec3 emission) {
        this.emission = emission;
        this.albedo = null;
    }

    @Override
    public ReflectionProperties properties(Ray r, Hit h) {
        return new ReflectionProperties(albedo, emission, null);
    }
}
