package raytracer.shapes;

import raytracer.abstractions.Hit;
import raytracer.abstractions.Ray;
import raytracer.materials.Material;
import cgtools.Vec3;

import static cgtools.Vec3.*;

public class Plane implements Shape {

    public final Vec3 anchor;
    public final Vec3 norm;
    public final Material material;

    public Plane(Vec3 anchor, Vec3 norm, Material material) {
        this.anchor = anchor;
        this.norm = norm;
        this.material = material;
    }

    @Override
    public Hit intersect(Ray r) {

        double denom = dotProduct(r.norm, norm);

        if (denom != 0) {
            double nom = dotProduct(norm, subtract(anchor, r.origin));
            double t = nom / denom;
            if (r.contains(t)) {

                Vec3 hitpoint = r.pointAt(t);

                Hit hit = new Hit(t, hitpoint, norm, material);

                return hit;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
