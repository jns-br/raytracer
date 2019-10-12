package raytracer.shapes;


import raytracer.abstractions.Hit;
import raytracer.abstractions.Ray;

public interface Shape {
    Hit intersect(Ray r);

}
