package imagescaler;

// Currently in .gitignore as this is just the method to test the classes 
public class Main {
    public static void main(String[] args) {
        Scaler.scaleImage("src\\testImage.png","output","png", 3);
    }
}


// Make work with png alpha values (colour.getAlpha(), interpolate alpha values? int[][][] but last list is of size 4 now?)