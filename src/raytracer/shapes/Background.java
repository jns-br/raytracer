package raytracer.shapes;

import raytracer.abstractions.Hit;
import raytracer.abstractions.Ray;
import raytracer.materials.Material;
import cgtools.Vec3;

public class Background implements Shape {

    Material material;

    public Background(Material material){
        this.material = material;
    }

    @Override
    public Hit intersect(Ray r) {

        double t = Double.POSITIVE_INFINITY;
        Vec3 hitPoint = r.pointAt(t);

        return new Hit(t, hitPoint, hitPoint, material);
    }

}
