package com.pallas.ImageLoading;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Base64;

/**
 * A class for loading/creating images
 */
public class ImageHandler {

    /**
     * Returns the image located at the URL
     * @param url The URL to retrieve the image from
     * @return The image at the URL
     * @throws IOException If the image cannot be read
     */
    public static BufferedImage getImage(URL url) {
        try {
            return ImageIO.read(url);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Saves an image to a specified file
     * @param image The image to save
     * @param filename The name of the file to save the image to
     * @return True if the image was saved successfully, false otherwise.
     */
    public static boolean saveImageToFile(BufferedImage image, String filename) {
        if(image == null) {
            return false;
        }

        String formatType = filename.substring(filename.indexOf(".") + 1);

        try {
            BufferedImage imageToSave = image;
            File file = new File(filename);
            ImageIO.write(imageToSave, formatType, file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns a 2D array of {@link Pixel} objects representing the image.
     * @param image The image to get the pixel matrix of.
     * @return A 2D array of {@link Pixel} objects representing the image.
     */
    public static Pixel[][] getImageMatrix(BufferedImage image) {
        Pixel[][] matrix = new Pixel[image.getWidth()][image.getHeight()];

        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {

                int value = image.getRGB(i, j);
                Pixel p = new Pixel(value);

                matrix[i][j] = p;
            }
        }

        return matrix;
    }

    /**
     * Creates an image from a 2D array of pixel objects.
     * @param matrix The matrix to generate the image from.
     * @return The generated image.
     */
    public static BufferedImage createImageFromMatrix(Pixel[][] matrix) {
        try {
            BufferedImage image = new BufferedImage(matrix.length, matrix[0].length, BufferedImage.TYPE_INT_RGB);
            for(int i = 0; i < matrix.length; i++) {
                for(int j = 0; j < matrix[i].length; j++) {
                    image.setRGB(i, j, matrix[i][j].getValue());
                }
            }
            return image;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates an image from a 2D array of integer values. This method always generates a grayscaled image.
     * @param matrix The matrix to generate the image from.
     * @return The generated image.
     */
    public static BufferedImage createImageFromMatrix(int[][] matrix) {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = Math.max(Math.min(matrix[i][j], 255), 0);
            }
        }

        try {
            BufferedImage image = new BufferedImage(matrix.length, matrix[0].length, BufferedImage.TYPE_INT_RGB);
            for(int i = 0; i < matrix.length; i++) {
                for(int j = 0; j < matrix[i].length; j++) {
                    int c = matrix[i][j];
                    Color newColor = new Color(c, c, c);
                    image.setRGB(i, j, newColor.getRGB());
                }
            }
            return image;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Converts an image to Base64 with the default file type of .png
     * @param image The image to convert.
     * @return The Base64 String representation of the image.
     */
    public static String toBase64(BufferedImage image) {
        return toBase64(image, "png");
    }

    /**
     * Converts an image to Base64.
     * @param image The image to convert.
     * @param fileExtension The image's file extension (e.g. png, jpg, gif, etc).
     * @return The Base64 String representation of the image.
     */
    public static String toBase64(BufferedImage image, String fileExtension) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, fileExtension, os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    /**
     * Returns a deep copy of a BufferedImage object.
     * @param image The image to create a copy of.
     * @return The copy of the image.
     */
    public static BufferedImage deepCopy(BufferedImage image) {
        ColorModel model = image.getColorModel();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(model, raster, model.isAlphaPremultiplied(), null);
    }
}
