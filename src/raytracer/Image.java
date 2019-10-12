package raytracer;

import cgtools.ImageWriter;
import cgtools.Vec3;

import java.io.IOException;

public class Image {
    private int width;
    private int height;
    private double[] data;
    private final double gamma;

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = new double[width * height * 3];
        this.gamma = 2.2;
    }

    public void setPixel(int x, int y, Vec3 color) {

        double red = color.x;
        double green = color.y;
        double blue = color.z;

        red = Math.pow(red, 1 / gamma);
        green = Math.pow(green, 1 / gamma);
        blue = Math.pow(blue, 1 / gamma);

        int genPosition = y * width * 3 + x * 3;
        data[genPosition] = red;
        data[genPosition + 1] = green;
        data[genPosition + 2] = blue;

    }

    public void write(String filename) throws IOException {
        ImageWriter.write(filename, data, width, height);

    }
}
