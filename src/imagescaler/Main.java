package imagescaler;

import java.io.IOException;

public class Main {
    // An example of how to use the ImageScaler
    public static void main(String[] args) {
        try{
            Scaler.scaleImage("test_png_image.png","output","png", 2);
        } catch (IOException e){
            e.printStackTrace();
        }
        
    }
}
