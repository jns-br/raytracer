package raytracer;

import raytracer.abstractions.Sphere;
import raytracer.shapes.Background;
import raytracer.abstractions.Group;
import raytracer.shapes.Plane;
import raytracer.shapes.Shape;
import raytracer.materials.DiffuseMaterial;
import raytracer.materials.Material;
import raytracer.materials.RefractiveMaterial;
import raytracer.shapes.Cylinder;
import raytracer.shapes.Disc;
import raytracer.abstractions.Transformation;
import cgtools.Random;
import cgtools.Vec3;

import java.util.ArrayList;
import java.util.List;

import static raytracer.Helpers.rgbVec;
import static cgtools.Vec3.vec3;
import static cgtools.Mat4.*;


public class Models {

    public static Shape makeTree(Vec3 center, double treeHeight, double treeRadius, int treeLines){

        return makeTree(new Transformation(identity), center, treeHeight, treeRadius, treeLines);

    }

    public static Shape makeTree(Transformation transformation, Vec3 center, double treeHeight, double treeRadius, int treeLines){

        Shape[] scene = new Shape[treeLines * 2 + 2];

        Material stemMaterial = new DiffuseMaterial(vec3(139* (1.0/255.0),20* (1.0/255.0),19* (1.0/255.0)));

        Shape stem = new Cylinder(center, treeHeight/2, treeRadius, stemMaterial);

        double treeLineRadius = treeRadius + treeLines/2;
        double treeLineHeight = treeHeight/2/treeLines;
        Vec3 treeLineCenter = vec3(center.x, center.y + treeHeight/2, center.z);
        Material treeLineMaterial = new DiffuseMaterial(vec3(34* (1.0/255.0),139* (1.0/255.0),34* (1.0/255.0)));

        Shape bottom = new Disc(treeLineCenter, vec3(0,1, 0), treeLineRadius, treeLineMaterial);

        for(int i = 0; i <= treeLines + 2; i+=2){
            double centerY = i/2 * treeLineHeight;

            Cylinder treeLine = new Cylinder(vec3(treeLineCenter.x, treeLineCenter.y + centerY, treeLineCenter.z), treeLineHeight, treeLineRadius - i/2, treeLineMaterial);
            Shape top = new Disc(vec3(treeLine.center.x, treeLine.center.y + treeLineHeight, treeLine.center.z), vec3(0, 1, 0), treeLine.radius, treeLineMaterial);
            scene[i] = treeLine;
            scene[i + 1] = top;
        }

        scene[scene.length - 2] = bottom;
        scene[scene.length - 1] = stem;

        Shape tree = new Group(transformation, scene);
        return tree;
    }

    public static Shape makeMonolith(Vec3 center, double height, double radius, Material material) {

        Cylinder monolith = new Cylinder(center, height, radius, material);
        Shape monolithTop = new Disc(vec3(monolith.center.x, monolith.center.y + monolith.height, monolith.center.z),
                vec3(0, 1, 0), monolith.radius, material);

        Shape[] scene = {monolith, monolithTop};

        Shape group = new Group(new Transformation(), scene);

        return group;
    }

    public static Shape makeSphere(Vec3 center, double radius, Material material){

        return new Sphere(center, radius, material);

    }

    public static Shape makePlane(Vec3 anchor, Vec3 norm, Material material){

        return new Plane(anchor, norm, material);

    }

    public static Background makeBackground(Material material) {

        return new Background(material);

    }

    public static Shape makeForest(Vec3 start, int rows, double treeHeightMin, double treeHeightMax, int treeLines, double spaceBetween){

        return makeForest(start, rows, treeHeightMin, treeHeightMax, treeLines, new Transformation(), spaceBetween);
    }

    public static Shape makeForest(Vec3 start, int rows, double treeHeightMin, double treeHeightMax, int treeLines, Transformation forestTransformation, double spaceBetween) {

        int trees = rows * 2 - 1;
        double treeRadius = 1.5;
        start = vec3(start.x, start.y, start.z + spaceBetween);

        return recursiveForest(start, rows, new ArrayList<>(), trees, treeHeightMin, treeHeightMax, treeRadius, treeLines, forestTransformation, spaceBetween);
    }

    private static Shape recursiveForest(Vec3 start, int rows, List<Shape> list, int trees, double treeHeightMin, double treeHeightMax,
                                         double treeRadius, int treeLines, Transformation forestTransformation, double spaceBetween) {

        if(rows == 0){

            Shape[] array =  list.toArray(new Shape[list.size()]);
            return new Group(forestTransformation, array);

        }else {

            Transformation initialTranslation = new Transformation(translate((rows - 1) * -spaceBetween, 0, 0));
            Transformation rowTranslation = new Transformation(translate(0, 0, rows * -spaceBetween));

            Shape treeRow = recursiveTreeRow(rowTranslation, new ArrayList<>(), initialTranslation, start, trees, treeHeightMin, treeHeightMax, treeRadius, treeLines, spaceBetween);
            list.add(treeRow);

            return recursiveForest(start, rows - 1, list, trees - 2, treeHeightMin, treeHeightMax, treeRadius, treeLines, forestTransformation, spaceBetween);

        }
    }

    private static Shape recursiveTreeRow(Transformation rowTranslation, List<Shape> list, Transformation initialTranslation, Vec3 start,
                                          int trees, double treeHeightMin, double treeHeightMax, double treeRadius, int treeLines, double spaceBetween) {

        if(trees == 0){
            Shape[] rowArray = list.toArray(new Shape[list.size()]);
            return new Group(rowTranslation, rowArray);
        } else {
            Transformation recTransformation = new Transformation(initialTranslation.transformationMat.multiply(translate(spaceBetween * (trees - 1), 0, 0)));
            Random random = new Random();
            double randomHeight = random.nextInt(((int) treeHeightMax - (int) treeHeightMin) + 1 ) + (int) treeHeightMin;

            Shape tree = makeTree(recTransformation, start, randomHeight, treeRadius, treeLines);
            list.add(tree);
            return recursiveTreeRow(rowTranslation, list, initialTranslation, start, trees - 1, treeHeightMin, treeHeightMax, treeRadius, treeLines, spaceBetween);
        }

    }

    public static Shape makeTreeCircle(double treeHeightMin, double treeRadius, int treeLinesMin, Vec3 circleCenter, double circleRadius) {

        double startAngle = 270;
        double increment = 360/10;

        List<Shape> list = new ArrayList<>();

        Random random = new Random();

        for(int i = 0; i <20 ; i++){

            double angle = startAngle + increment * i;
            double rads = angle * Math.PI/360;

            double randomHeight = random.nextInt((20 - (int) treeHeightMin) + 1 ) + (int) treeHeightMin;
            double randomLines = random.nextInt((10 - treeLinesMin) + 1) + treeLinesMin;

            double centerX = circleCenter.x + circleRadius * Math.cos(rads);
            double centerY = circleCenter.y;
            double centerZ = circleCenter.z + circleRadius * Math.sin(rads);

            Vec3 treeCenter = vec3(centerX, centerY, centerZ);

            Shape tree = makeTree(treeCenter, randomHeight, treeRadius, (int) randomLines);
            list.add(tree);
        }

        Shape[] fullCircle = list.toArray(new Shape[list.size()]);

        Shape group = new Group(new Transformation(), fullCircle);

        return group;
    }

    public static Shape recursiveTreeCircles(int treeRows, double treeHeight, double treeRadius, int treeLines, Vec3 circleCenter, double radius){

        return recursiveTreeCircles(treeRows, treeHeight, treeRadius, treeLines, circleCenter, radius, new ArrayList<>());

    }

    public static Shape recursiveTreeCircles(int treeRows, double treeHeight, double treeRadius, int treeLines, Vec3 circleCenter, double radius, List<Shape> lineList){

        Shape treeCircle = makeTreeCircle(treeHeight, treeRadius, treeLines, circleCenter, radius + treeRows * 10);
        lineList.add(treeCircle);

        if(treeRows == 0) {
            Shape[] treeCircles = lineList.toArray(new Shape[lineList.size()]);
            Shape group = new Group(new Transformation(), treeCircles);
            return group;
        }else {
            return recursiveTreeCircles(treeRows - 1, treeHeight, treeRadius, treeLines, circleCenter, radius, lineList);
        }

    }

    public static Shape makeLake(Vec3 center, double radius) {
        Material lakeMaterial = new RefractiveMaterial(rgbVec(68, 83, 248), rgbVec(68, 83, 248), 1.3);
        Vec3 adjustedCenter = vec3(center.x, center.y + 0.1, center.z);
        Shape lake = new Disc(adjustedCenter, vec3(0, 1, 0), radius, lakeMaterial);
        return lake;
    }


}
