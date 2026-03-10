# ImageScaling

This is a Java Project which scales an image using bilinear interpolation.

## How it works
- (In the below explanation scale = the mutiplier applied to the image's size e.g. 2)
>
- The pixel data is retrieved and placed inside a 2D/3D list (depending on whether the image is greyscale)
- Then a new empty 2D/3D list is created with (scale-1) extra rows and columns between each of the original rows and columns
- Now each row of the pixel data is resampled to have (scale-1) points between each original point and these rows are added to every (scale) row in the new 2D/3D list
- Finally, every column of the new 2D/3D list is resampled and the 2D/3D list is overwritten with these resampled columns
>
- The resulting 3D list will have (scale -1) points between every original point and all its ajacent points
- These points gradually change the RGB values to convert from the original 1st point to the original second point
- This results in an unoticeable gradient to expand images (for sufficiently large images) 
