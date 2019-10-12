package raytracer.abstractions;

import raytracer.abstractions.Hit;
import raytracer.abstractions.Ray;
import cgtools.Mat4;
import cgtools.Vec3;
import static cgtools.Mat4.*;

public class Transformation {

    public final Mat4 transformationMat;
    public final Mat4 transformationMatInv;
    public final Mat4 transformationMatInvTransposed;

    public Transformation(){
        this(identity);
    }

    public Transformation(Mat4 transformationMat){
        this.transformationMat = transformationMat;
        this.transformationMatInv = transformationMat.invertFull();
        this.transformationMatInvTransposed = transformationMat.invertFull().transpose();
    }

    public Ray transformRay(Ray ray){
        Vec3 transformedDirection = transformationMatInv.transformDirection(ray.norm);
        Vec3 transformedOrigin = transformationMatInv.transformPoint(ray.origin);
        return new Ray(transformedOrigin, ray.t_min, ray.t_max, transformedDirection);
    }

    public Hit transformHit(Hit hit){
        Vec3 transformedNorm = transformationMatInvTransposed.transformDirection(hit.n);
        Vec3 transformedHitpoint = transformationMat.transformPoint(hit.x);
        return new Hit(hit.t, transformedHitpoint, transformedNorm, hit.material);
    }
}
