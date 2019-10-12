package raytracer.abstractions;

import cgtools.Mat4;
import cgtools.Vec3;
import static cgtools.Vec3.*;

public class Camera {

    public final double angle;
    public final double width;
    public final double height;
    public final Vec3 camPosition;
    public final Mat4 transformation;

    public Camera(double angle, double width, double height, Mat4 transformation){
        this.angle = angle;
        this.width = width;
        this.height = height;
        this.camPosition = vec3(0, 0, 0);
        this.transformation = transformation;
    }

    public Ray generateRay(double x, double y){
        return generateRay(x, y, 0.0001, Double.POSITIVE_INFINITY);
    }

    public Ray generateRay(double x, double y, double t_min, double t_max){

        double rx = x - (width/2);
        double ry = (height/2) - y;
        double rz = -((width/2) / Math.tan(angle/2));
        Vec3 direction = normalize(vec3(rx, ry, rz));

        Vec3 transformedDirection = transformation.transformDirection(direction);
        Vec3 transformedOrigin = transformation.transformPoint(camPosition);

        Ray ray = new Ray(transformedOrigin, t_min, t_max, transformedDirection);

        return ray;
    }
}
