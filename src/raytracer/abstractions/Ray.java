package raytracer.abstractions;

import cgtools.Vec3;
import static cgtools.Vec3.*;

public class Ray {

    public final Vec3 origin;
    public final double t_min;
    public final double t_max;
    public final Vec3 norm;

    public Ray(Vec3 origin, Vec3 norm){
        this(origin, 0, Double.POSITIVE_INFINITY, norm);
    }
    public Ray(Vec3 origin, double t_min, double t_max, Vec3 norm){
        this.origin = origin;
        this.t_min = t_min;
        this.t_max = t_max;
        this.norm = normalize(norm);
    }

    public Vec3 pointAt (double t){
        if(t == 0) return origin;
        else if(t == 1) return add(origin,norm);
        else return add(origin, multiply(t, norm));
    }

    public boolean contains(double t) {
        return t >= t_min && t <= t_max;
    }
}
