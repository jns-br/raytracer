package raytracer;

import raytracer.abstractions.Camera;
import raytracer.abstractions.Hit;
import raytracer.abstractions.Ray;
import raytracer.abstractions.Group;
import raytracer.shapes.Shape;
import raytracer.materials.ReflectionProperties;
import raytracer.abstractions.Transformation;
import cgtools.Vec3;
import me.tongfei.progressbar.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static cgtools.Vec3.*;

public class Tracer {


    public static Image raytrace(Camera camera, Shape[] scene, int samplingRate, int maxDepth) {

        int cores = Runtime.getRuntime().availableProcessors();

        return raytrace(camera, scene, samplingRate, maxDepth, cores);
    }

    public static Image raytrace(Camera camera, Shape[] scene, int samplingRate, int maxDepth, int maxThreads) {
        int width = (int) camera.width;
        int height = (int) camera.height;

        Image returnImage = new Image(width, height);

        Transformation transformation = new Transformation();

        Group group = new Group(transformation, scene);

        try(ProgressBar progressBar = new ProgressBar("Image Rendering with " + maxThreads + " Thread(s)", width)){

            ExecutorService pool = Executors.newFixedThreadPool(maxThreads);
            List<Future<List<Vec3>>> futureList = new ArrayList<>();

            for (int x = 0; x != width; x++) {

                final int xf = x;

                Callable<List<Vec3>> calculateOneColumn = () -> {
                    List<Vec3> list = new ArrayList<>();
                    for (int y = 0; y != height; y++){
                        Vec3 color = supersample(xf, y, camera, group, samplingRate, maxDepth);
                        list.add(color);
                    }
                    return list;
                };

                Future<List<Vec3>> column = pool.submit(calculateOneColumn);
                futureList.add(column);
            }

            for(int x = 0; x != width; x++){

                Future<List<Vec3>> columnFuture = futureList.get(x);
                List<Vec3> columnList = columnFuture.get();

                for(int y = 0; y != height; y++) {
                    returnImage.setPixel(x, y, columnList.get(y));
                }

                progressBar.step();
            }

            pool.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return returnImage;
    }

    private static Vec3 supersample(int x, int y, Camera camera, Group group, int samplingRate, int maxDepth) {
        Vec3 returnColor = vec3(0, 0, 0);

        for (int xi = 0; xi < samplingRate; xi++) {
            for (int yi = 0; yi < samplingRate; yi++) {
                double rx = cgtools.Random.random();
                double ry = cgtools.Random.random();
                double xs = x + (xi + rx) / samplingRate;
                double ys = y + (yi + ry) / samplingRate;

                Ray ray = camera.generateRay(xs, ys);


                Vec3 currentColor = radiance(ray, group, maxDepth);
                returnColor = add(returnColor, currentColor);


            }
        }
        double squared = samplingRate * samplingRate;
        return divide(returnColor, squared);
    }

    private static Vec3 radiance(Ray ray, Group group, int maxDepth) {
        if (maxDepth == 0) return black;

        Hit hit = group.intersect(ray);

        ReflectionProperties properties = hit.material.properties(ray, hit);

        if (properties.ray != null) {
            return add(properties.emission, multiply(properties.albedo, radiance(properties.ray, group, maxDepth - 1)));
        } else {
            return properties.emission;
        }

    }
}

