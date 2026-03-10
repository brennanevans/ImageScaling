package imagescaler;

public class LinearInterpolation {
    // Resample a row or column
    private static int[] resample(int[] datapoints){ // This is used when scale = 2 as more efficient
        int[] resampledData = new int[(datapoints.length*2)-1];
        
        int index = 0;
        for (int i=0; i<datapoints.length-1; i++){
            resampledData[index] = datapoints[i];
            index ++;

            int interpolatedValue = Math.round(((float)datapoints[i] + datapoints[i+1])/2);
            resampledData[index] = (interpolatedValue);
            index ++;
        }
        resampledData[resampledData.length-1] = datapoints[datapoints.length-1];

        return resampledData;
    }

    // Resample a row or column - with scale
    private static int[] resample(int[] datapoints, int scale){
        if (scale == 2){
            return resample(datapoints);
        }

        int newLength = datapoints.length + (scale-1)*(datapoints.length-1);
        int[] resampledData = new int[newLength];

        // x = index of element
        // y = value of element
        int index = 0;
        for (int i=0; i<datapoints.length-1; i++){
            // Floats as else equation always floors intermediate answers
            // Needs to round properly for interpolation to work correctly
            float y0 = datapoints[i];
            float y1 = datapoints[i+1];

            float x0 = i;
            float x1 = i+scale;

            resampledData[index] = datapoints[i];
            index ++;

            for (int x = i+1;x < i+scale;x++){
                int y = Math.round(y0 + ((x-x0) * (y1-y0)/(x1-x0)));
                resampledData[index] = y;
                index ++;
            }
        }
        resampledData[resampledData.length-1] = datapoints[datapoints.length-1];

        return resampledData;
    }

    // Resample an image
    public static int[][] resample(int[][] image){
        return resample(image,2);
    }

    // Resample an image with scale
    public static int[][] resample(int[][] image,int scale){
        int rowNum = image.length + (scale-1)*(image.length-1);
        int columnNum = image[0].length + (scale-1)*(image[0].length-1);

        int [][] resampledRows = new int[rowNum][columnNum];
        int [][] resampledImage = new int[rowNum][columnNum];

        // Fill a row every (scale) rows E.g. scale = 2, fill every other rows
        int index = 0;
        for (int i=0; i<image.length; i++){
            resampledRows[index] = resample(image[i],scale);
            index +=scale;
        }

        for (int columnIndex=0; columnIndex<resampledRows[0].length; columnIndex++){
            int[] column = new int[image.length];
            int[] resampledColumn = new int[rowNum];
            
            // Add elements in resampled rows to columns
            // Result is columns with gaps of length scale (required)
            index = 0;
            for (int rowIndex=0; rowIndex<resampledRows.length; rowIndex+=scale){
                column[index] = resampledRows[rowIndex][columnIndex];
                index ++;
            }

            resampledColumn = resample(column,scale);

            // Creates image using columns as these are now full
            for (int rowIndex=0; rowIndex<resampledImage.length; rowIndex++){
                resampledImage[rowIndex][columnIndex] = resampledColumn[rowIndex];
            }
        }
 
        return resampledImage;
    }
}
