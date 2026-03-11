package imagescaler;

import java.awt.image.*;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;


public class Scaler {
    public static BufferedImage scaleImage(String imagePath,int scale) throws IOException{
        return scaleImage(imagePath,null,null,scale,false);
    }

    public static BufferedImage scaleImage(String imagePath,int scale,boolean isGreyscale) throws IOException{
        return scaleImage(imagePath,null,null, scale,isGreyscale);
    }

    public static BufferedImage scaleImage(String imagePath,String savePath,int scale) throws IOException{
        return scaleImage(imagePath,savePath,"bmp",scale,false);
    }

    public static BufferedImage scaleImage(String imagePath,String savePath,String fileFormat,int scale) throws IOException{
        return scaleImage(imagePath,savePath,fileFormat,scale,false);
    }

    public static BufferedImage scaleImage(String imagePath,String savePath,String fileFormat,int scale,boolean isGreyscale) throws IOException{
        // Note if isGreyscale == true, alpha values will be ignored
        BufferedImage scaledImage;

        if (isGreyscale){
            int[][] pixelValues = getValuesGreyscale(imagePath);
            int[][] respampledImage = resampleGreyscaleImage(pixelValues, scale);
            scaledImage = createGreyscaleImage(respampledImage);
        } else{
            int[][][] pixelValues = getValues(imagePath);
            int[][][] respampledImage = resampleImage(pixelValues, scale);
            scaledImage = createImage(respampledImage);
        }

        if (savePath != null){
            saveImage(scaledImage,savePath,fileFormat);
        }
        return scaledImage;
    }

    private static int[][] getValuesGreyscale(String path) throws IOException{
        BufferedImage image = ImageIO.read(new File(path));

        int width = image.getWidth();
        int height = image.getHeight();
        int [][] values = new int[width][height];

        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                Color color = new Color(image.getRGB(x, y));
                values[y][x] = color.getRed();

            }
        }

        return values;
    }

    private static int[][][] getValues(String path) throws IOException{
        BufferedImage image = ImageIO.read(new File(path));
        boolean containsAlpha = image.getTransparency() == 3.0 ? true : false;

        int width = image.getWidth();
        int height = image.getHeight();
        int[][][] values = new int[height][width][];

        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                int rgb = new Color(image.getRGB(x, y),containsAlpha).getRGB();
                // (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue)
                int[] rgbList = {(rgb >> 16) & 0xFF,(rgb >> 8) & 0xFF,(rgb >> 0) & 0xFF};
                int[] rgbaList = {(rgb >> 16) & 0xFF,(rgb >> 8) & 0xFF,(rgb >> 0) & 0xFF,(rgb >> 24) & 0xFF};
                
                values[y][x] = containsAlpha ? rgbaList: rgbList;
            }
        }
        return values;
    }

    private static int[][] resampleGreyscaleImage(int[][] initalImage,int scale){
        int[][] scaledImage = LinearInterpolation.resample(initalImage,scale);
        return scaledImage;
    }

    private static int[][][] resampleImage(int[][][] initalImage, int scale){
        int rows = initalImage.length;
        int cols = initalImage[0].length;// 4 if image contained alpha values, else 3

        // Creates an empty list for all red,green,blue and possibly alpha values
        int[][][] values = new int[initalImage[0][0].length][rows][cols];
        
        // Fills values with rgb/rgba values from inital image
        for (int row=0;row<rows;row++){
            for (int col=0;col<cols;col++){
                for (int i=0;i<values.length;i++){
                    values[i][row][col] = initalImage[row][col][i];
                }
            }
        }

        int[][][] scaledValues = new int[values.length][][];

        for (int i=0; i<scaledValues.length;i++){
            scaledValues[i] = LinearInterpolation.resample(values[i],scale);
        }

        int[][][] scaledImage = new int[scaledValues[0].length][scaledValues[0][0].length][scaledValues.length];

        for (int row=0; row<scaledImage.length; row++){
            for (int col=0; col<scaledImage[0].length; col++){
                for (int i=0; i<scaledValues.length; i++){
                    scaledImage[row][col][i] = scaledValues[i][row][col];
                }
            }
        }
        return scaledImage;
    }

    private static BufferedImage createGreyscaleImage(int[][] image){
        int width = image[0].length;
        int height = image.length;

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Gets red value and sets all rgb bits to this 
                // (as all rbg values are the same)
                int red = image[y][x];
                // Essentially concatenates bits to make rgb int
                int rgb = (red << 16) | (red << 8) | red; 
                newImage.setRGB(x, y, rgb);
            }
        }

        return newImage;
    }

    private static BufferedImage createImage(int[][][] image){
        int width = image[0].length;
        int height = image.length;

        BufferedImage newImage;
        if (image[0][0].length == 4){
            newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        } else{
            newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int red = image[y][x][0];
                int green = image[y][x][1];
                int blue = image[y][x][2];

                if (newImage.getType() == BufferedImage.TYPE_INT_ARGB){
                    int alpha = image[y][x][3];
                    // Essentially concatenates bits to make argb int
                    int argb = (alpha << 24) | (red << 16) | (green << 8) | blue; 
                    newImage.setRGB(x, y, argb);
                } else{
                    // Essentially concatenates bits to make rgb int
                    int rgb = (red << 16) | (green << 8) | blue; 
                    newImage.setRGB(x, y, rgb);
                }
            }
        }

        return newImage;
    }

    private static void saveImage(BufferedImage image,String savePath,String fileFormat) throws IOException{
        ImageIO.write(image, fileFormat, new File(savePath+"."+fileFormat));
    }
}
