package raytracer.abstractions;


import raytracer.shapes.Shape;

import java.util.ArrayList;
import java.util.Collections;

public class Group implements Shape {

    public final Shape[] scene;
    public final Transformation transformation;


    public Group(Transformation transformation, Shape... shapes) {
        this.transformation = transformation;
        this.scene = shapes;
    }



    @Override
    public Hit intersect(Ray r) {

        Ray transformedRay = transformation.transformRay(r);

        ArrayList<Hit> hits = new ArrayList<>();

        for (Shape shape : scene) {
            if(shape != null){
                Hit hit = shape.intersect(transformedRay);
                if (hit != null) hits.add(hit);
            }
        }

        if(hits.size() == 0) return null;

        Collections.sort(hits);

        Hit transformedHit = transformation.transformHit(hits.get(0));

        return transformedHit;
    }

}
