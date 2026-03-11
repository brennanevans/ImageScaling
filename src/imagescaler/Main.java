package imagescaler;

<<<<<<< HEAD
import java.io.IOException;

// Currently in .gitignore as this is just the method to test the classes 
=======
>>>>>>> 387a262ecb9dd6087486ab3bef9441f634295427
public class Main {
    // An example of how to use the ImageScaler
    public static void main(String[] args) {
        try{
            Scaler.scaleImage("test_png_image.png","output","png", 3);
        } catch (IOException e){
            e.printStackTrace();
        }
        
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 387a262ecb9dd6087486ab3bef9441f634295427
