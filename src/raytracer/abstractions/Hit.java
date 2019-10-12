package raytracer.abstractions;

import raytracer.materials.Material;
import cgtools.Vec3;
import static cgtools.Vec3.*;

public class Hit implements Comparable<Hit> {

    public final double t;
    public final Vec3 x;
    public final Vec3 n;
    public final Material material;


    public Hit(double t, Vec3 x, Vec3 n, Material material){
        this.t = t;
        this.x = x;
        this.n = normalize(n);
        this.material = material;
    }

    @Override
    public int compareTo(Hit hit) {
        if(this.t > hit.t) return 1;
        else if(this.t < hit.t) return -1;
        else return 0;
    }
}
