package raytracer.abstractions;

import raytracer.shapes.Shape;
import raytracer.materials.Material;
import cgtools.Vec3;

import static cgtools.Vec3.*;

public class Sphere implements Shape {

    private Vec3 center;
    private double radius;
    private Material material;

    public Sphere(Vec3 center, double radius, Material material) {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public Hit intersect(Ray r) {

        Vec3 x0 = subtract(r.origin, center);

        double a = dotProduct(r.norm, r.norm);
        double b = 2 * dotProduct(x0, r.norm);
        double c = dotProduct(x0, x0) - radius * radius;

        double tmpResult = b * b - 4 * a * c;


        if (tmpResult > 0) {

            double t1 = (-b + Math.sqrt(tmpResult)) / (2 * a); //far hit
            double t2 = (-b - Math.sqrt(tmpResult)) / (2 * a); //near hit

            if (r.contains(t2)) {

                Vec3 hitPoint = r.pointAt(t2);
                Vec3 norm = divide(subtract(hitPoint, center), radius);
                return new Hit(t2, hitPoint, norm, material);

            } else if (r.contains(t1)) {

                Vec3 hitPoint = r.pointAt(t1);
                Vec3 norm = divide(subtract(hitPoint, center), radius);
                return new Hit(t1, hitPoint, norm, material);

            }else {
                return null;
            }
        }
        return null;
    }

}