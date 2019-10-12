package raytracer;

import raytracer.abstractions.Camera;
import cgtools.Mat4;
import cgtools.Vec3;

import java.io.IOException;

import static cgtools.Vec3.multiply;
import static cgtools.Vec3.vec3;
import static cgtools.Mat4.*;

public class Helpers {

    public static void write(Image image, String filename) {
        try {
            image.write(filename);
            System.out.println("Wrote image: " + filename);
        } catch (IOException error) {
            System.out.println(String.format("Something went wrong writing: %s: %s", filename, error));
        }
    }

    public static Vec3 rgbVec(double red, double green, double blue){
        double rgbFac = 1.0/255.0;
        return vec3(red * rgbFac, green * rgbFac, blue * rgbFac );
    }

    public static Camera makePositionedCamera(double angle, int width, int height, Mat4 translation, Mat4... rotations) {
        Mat4 rotation = rotations[0];
        for (int i = 1; i < rotations.length; i++){
            rotation = rotation.multiply(rotations[i]);
        }

        Mat4 transformation = translation;
        transformation = transformation.multiply(rotation);

        return new Camera(angle, width, height, transformation);
    }

    public static Mat4 rotateAroundPoint(Vec3 point, Vec3 axis, double angle) {

        return translate(point).multiply(rotate(axis, angle)).multiply(translate(multiply(-1, point)));

    }
}
