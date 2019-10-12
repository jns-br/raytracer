package raytracer.shapes;


import raytracer.abstractions.Hit;
import raytracer.abstractions.Ray;
import raytracer.shapes.Shape;
import raytracer.materials.Material;
import cgtools.Vec3;

import static cgtools.Vec3.*;

// Source  https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-plane-and-ray-disk-intersection,
//         https://www.cl.cam.ac.uk/teaching/1999/AGraphHCI/SMAG/node2.html

public class Disc implements Shape {

    public final Vec3 center;
    public final Vec3 norm;
    public final double radius;
    public final Material material;

    public Disc(Vec3 center, Vec3 norm, double radius, Material material) {
        this.center = center;
        this.norm = norm;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public Hit intersect(Ray r) {
        double denom = dotProduct(r.norm, norm);

        if (denom != 0) {

            double nom = dotProduct(subtract(center, r.origin), norm);
            double t = nom / denom;

            if (t > r.t_min && t < r.t_max) {
                Vec3 hitPoint = r.pointAt(t);
                if (dotProduct(subtract(hitPoint,center), subtract(hitPoint, center)) > (radius * radius)) {
                    return null;
                } else {
                    Hit hit = new Hit(t, hitPoint, norm, material);
                    return hit;
                }
            } else {
                return null;
            }
        }

        return null;
    }

}

