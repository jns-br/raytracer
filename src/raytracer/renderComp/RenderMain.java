package raytracer.renderComp;

import raytracer.Image;
import raytracer.abstractions.Camera;
import raytracer.shapes.Shape;
import raytracer.materials.BackgroundMaterial;
import raytracer.materials.DiffuseMaterial;
import raytracer.materials.Material;
import raytracer.abstractions.Transformation;
import cgtools.Mat4;

import java.util.ArrayList;
import java.util.List;

import static raytracer.Helpers.*;
import static cgtools.Mat4.*;
import static cgtools.Vec3.*;
import static raytracer.Models.*;
import static raytracer.Tracer.*;

public class RenderMain {

    public static void main(String[] args) {
        final int samplingRate = 10;
        final int maxDepth = 15;
        final int width = 1280;
        final int height = 720;
        final double angle = Math.PI/2;

        Mat4 cameraTranslation = translate(3, 5, 0);
        Mat4 cameraRotation = rotate(1, 0, 0, -20);

        Camera camera = makePositionedCamera(angle, width, height, cameraTranslation, cameraRotation);

        List<Shape> sceneList = new ArrayList<>();

        Material backgroundMaterial = new BackgroundMaterial(rgbVec(255, 172, 172));
        Material planeMaterial = new DiffuseMaterial(rgbVec(207, 174, 117));

        Shape background = makeBackground(backgroundMaterial);
        sceneList.add(background);
        Shape plane = makePlane(vec3(0, -1, -130), vec3(0, 1, 0), planeMaterial);
        sceneList.add(plane);

        Shape lake = makeLake(vec3(0, -1, -15), 30);
        sceneList.add(lake);

        Shape firstForest = makeForest(vec3(-45, -1, -25), 5, 10, 15, 5,  7);
        sceneList.add(firstForest);

        Transformation forestRotation = new Transformation(rotate(0, 1, 0, 180));
        Shape firstForestMirrored = makeForest(vec3(-45, -1, -32), 5, 10, 15, 5, forestRotation, 7);
        sceneList.add(firstForestMirrored);

        Transformation pointRotation = new Transformation(rotateAroundPoint(vec3(-45 , -1, -95), vec3(0, 1, 0), 180));
        Shape forestBehind = makeForest(vec3(-45, -1, -95), 5, 10, 15, 5, pointRotation, 7);
        sceneList.add(forestBehind);

        Shape secondForest = makeForest(vec3(45, -1, -25), 5, 10, 15, 5,  7);
        sceneList.add(secondForest);

        Shape secondForestMirrored = makeForest(vec3(45, -1, -32), 5, 10, 15, 5, forestRotation, 7);
        sceneList.add(secondForestMirrored);

        Transformation pointRotation2 = new Transformation(rotateAroundPoint(vec3(45 , -1, -95), vec3(0, 1, 0), 180));
        Shape forestBehind2 = makeForest(vec3(45, -1, -95), 5, 10, 15, 5, pointRotation2, 7);
        sceneList.add(forestBehind2);

        Material sunMaterial = new DiffuseMaterial(rgbVec(255, 103, 0));
        Shape sun = makeSphere(vec3(0, -25, -150), 50, sunMaterial);
        sceneList.add(sun);

        Shape[] scene = sceneList.toArray(new Shape[sceneList.size()]);

        Image image = raytrace(camera, scene, samplingRate, maxDepth);

        write(image, "doc/rendering-comp-final.png");

    }
}
