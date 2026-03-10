package imagescaler;

import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;


public class Scaler {
    public static BufferedImage scaleImage(String imagePath,int scale){
        return scaleImage(imagePath,null,null,scale,false);
    }

    public static BufferedImage scaleImage(String imagePath,int scale,boolean isGreyscale){
        return scaleImage(imagePath,null,null, scale,isGreyscale);
    }

    public static BufferedImage scaleImage(String imagePath,String savePath,int scale){
        return scaleImage(imagePath,savePath,"bmp",scale,false);
    }

    public static BufferedImage scaleImage(String imagePath,String savePath,String fileFormat,int scale){
        return scaleImage(imagePath,savePath,fileFormat,scale,false);
    }

    public static BufferedImage scaleImage(String imagePath,String savePath,String fileFormat,int scale,boolean isGreyscale){
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

    private static int[][] getValuesGreyscale(String path){
        try{
            BufferedImage image = ImageIO.read(new File(path));

            int width = image.getWidth();
            int height = image.getHeight();

            int [][] values = new int[width][height];

            int row = 0;
            for (int x =0; x<width; x++){
                int column = 0;
                for (int y=0; y<height; y++){
                    Color color = new Color(image.getRGB(x, y));
                    values[row][column] = color.getRed();

                    column++;
                }
                row++;
            }

            return values;

        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static int[][][] getValues(String path){
        try{
            BufferedImage image = ImageIO.read(new File(path));

            int width = image.getWidth();
            int height = image.getHeight();

            int [][][] values = new int[height][width][3];

            int row = 0;
            for (int y=0; y<height; y++){
                int column = 0;
                for (int x=0; x<width; x++){
                    Color color = new Color(image.getRGB(x, y));
                    values[row][column][0] = color.getRed();
                    values[row][column][1] = color.getGreen();
                    values[row][column][2] = color.getBlue();

                    column++;
                }
                row++;
            }
            return values;

        } catch (IOException e){
            e.printStackTrace();
        }
        return null;  
    }

    private static int[][] resampleGreyscaleImage(int[][] initalImage,int scale){
        int[][] scaledImage = LinearInterpolation.resample(initalImage,scale);
        return scaledImage;
    }

    private static int[][][] resampleImage(int[][][] initalImage, int scale){
        int [][] redValues = new int[initalImage.length][initalImage[0].length];
        int [][] greenValues = new int[initalImage.length][initalImage[0].length];
        int [][] blueValues = new int[initalImage.length][initalImage[0].length];

        for (int row=0;row<initalImage.length;row++){
            for (int col=0;col<initalImage[0].length;col++){
                redValues[row][col] = initalImage[row][col][0];
                greenValues[row][col] = initalImage[row][col][1];
                blueValues[row][col] = initalImage[row][col][2];
            }
        }

        int[][] scaledRed = LinearInterpolation.resample(redValues,scale);
        int[][] scaledGreen = LinearInterpolation.resample(greenValues,scale);
        int[][] scaledBlue = LinearInterpolation.resample(blueValues,scale);

        int[][][] scaledImage = new int[scaledRed.length][scaledRed[0].length][3];

        for (int row=0;row<scaledImage.length;row++){
            for (int col=0;col<scaledImage[0].length;col++){
                scaledImage[row][col][0] = scaledRed[row][col];
                scaledImage[row][col][1] = scaledGreen[row][col];
                scaledImage[row][col][2] = scaledBlue[row][col];
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
                int rgb = image[y][x];
                rgb = (rgb << 8) + image[y][x];
                rgb = (rgb << 8) + image[y][x];

                newImage.setRGB(x, y, rgb);
            }
        }

        return newImage;
    }

    private static BufferedImage createImage(int[][][] image){
        int width = image[0].length;
        int height = image.length;

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image[y][x][0];
                rgb = (rgb << 8) + image[y][x][1];
                rgb = (rgb << 8) + image[y][x][2];

                newImage.setRGB(x, y, rgb);
            }
        }

        return newImage;
    }

    private static void saveImage(BufferedImage image,String savePath,String fileFormat){
        try{
            ImageIO.write(image, fileFormat, new File(savePath+"."+fileFormat));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
