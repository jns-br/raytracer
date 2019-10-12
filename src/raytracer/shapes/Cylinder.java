package raytracer.shapes;

import raytracer.abstractions.Hit;
import raytracer.abstractions.Ray;
import raytracer.shapes.Shape;
import raytracer.materials.Material;
import cgtools.Vec3;
import static cgtools.Vec3.*;

//Sources https://www.cl.cam.ac.uk/teaching/1999/AGraphHCI/SMAG/node2.html, http://woo4.me/wootracer/cylinder-intersection/

public class Cylinder implements Shape {

    public final Vec3 center;
    public final double height;
    public final double radius;
    public final Material material;

    public Cylinder(Vec3 center, double height, double radius, Material material) {
        this.center = center;
        this.height = height;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public Hit intersect(Ray r) {
        //Cylinder should be perpendicular -> y == 0
        Vec3 y0 = vec3(1, 0 ,1);

        Vec3 zNorm = multiply(r.norm, y0);
        Vec3 zOrigin = multiply(r.origin, y0);
        Vec3 zCenter = multiply(center,y0);

        //Adjusting center like sphere description
        Vec3 x0 = subtract(zOrigin, zCenter);

        //Formula for cylinder, ignoring parts with y as they will always be 0
        double a = dotProduct(zNorm, zNorm);
        double b = 2* dotProduct(x0, zNorm);
        double c = dotProduct(x0, x0) - (radius * radius);

        double tmpResult = (b * b) - 4 * a * c;

        if(tmpResult > 0){
            double t1 = (-b + Math.sqrt(tmpResult)) / (2 * a); //far hit
            double t2 = (-b - Math.sqrt(tmpResult)) / (2 * a); //near hit

            if(r.contains(t2)){
                Vec3 hitPoint = r.pointAt(t2);
                if(hitPoint.y > center.y + height || hitPoint.y < center.y){
                    return null;
                }else{
                    Vec3 norm = subtract(hitPoint, center);
                    Hit hit = new Hit(t2, hitPoint, norm, material);
                    return hit;
                }
            }else if(r.contains(t1)){
                Vec3 hitPoint = r.pointAt(t1);
                if(hitPoint.y > center.y + height || hitPoint.y < center.y){
                    Vec3 norm = subtract(hitPoint, center);
                    Hit hit = new Hit(t1, hitPoint, norm, material);
                    return hit;
                }
            }
        }
        return null;
    }

}
