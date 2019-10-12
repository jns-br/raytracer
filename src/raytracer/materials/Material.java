package raytracer.materials;

import raytracer.abstractions.Hit;
import raytracer.abstractions.Ray;

public interface Material {
    ReflectionProperties properties(Ray r, Hit h);
}
