package raytracer.materials;

import raytracer.abstractions.Ray;
import cgtools.Vec3;

public class ReflectionProperties {
    public Vec3 albedo;
    public Vec3 emission;
    public Ray ray;

    public ReflectionProperties(Vec3 albedo, Vec3 emission, Ray ray) {
        this.albedo = albedo;
        this.emission = emission;
        this.ray = ray;
    }
}
