package imagescaler;

public class LinearInterpolation {
    public static void main(String[] args) {
        int[] testdata = {1,5,7,8};
        System.out.println(arrayToString(resample(testdata)));
    }

    // Will be deleted once finished refactoring - for testing only
    private static String arrayToString(int[] array){
        String stringRepresentation = "";
        
        for (int i=0;i<array.length;i++){
            stringRepresentation += array[i] + ",";
        }
        
        return stringRepresentation.substring(0, stringRepresentation.length()-1);
    }


    public static int[] resample(int[] datapoints){ // This is used when scale = 2 as more efficient
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

    public static int[] resample(int[] datapoints, int scale){
        if (scale == 2){
            return resample(datapoints);
        }


        int newLength = datapoints.length + (scale-1)*(datapoints.length-1);
        int[] resampledData = new int[newLength];

        // x = index of element
        // y = value of element
        for (int i=0;i<datapoints.length-1;i++){
            float x0 = i;
            float y0 = datapoints[i];
            float x1 = i+scale;
            float y1 = datapoints[i+1];

            resampledData[(scale*i)] = (int)y0;

            int actualPosition = (scale*i)+1;
            for (int x = i+1;x<i+scale;x++){
                int y = Math.round(y0 + ((x-x0) * (y1-y0)/(x1-x0))) ;
                resampledData[actualPosition] = y;
                actualPosition++;
            }
        }
        resampledData[resampledData.length-1] = datapoints[datapoints.length-1];

        return resampledData;
    }

    public static int[][] resample(int[][] image){ // Scale version was based on this, this can be replaced by call to scale version, scale = 2
        int rowNum = (image.length*2)-1;
        int columnNum = (image[0].length*2)-1;

        int [][] resampledRows = new int[rowNum][columnNum];
        int [][] resampledImage = new int[rowNum][columnNum];

        int index = 0;
        for (int i = 0; i<image.length;i++){
            //System.out.println(arrayToString(image[i]));
            // System.out.println(arrayToString(resample(image[i])));
            resampledRows[index] = resample(image[i]);
            index +=2;
        }

        for (int columnIndex = 0;columnIndex < resampledRows[0].length;columnIndex++){
            int[] column = new int[image.length];
            int[] resampledColumn = new int[rowNum];

            index = 0;
            for (int rowIndex = 0; rowIndex<resampledRows.length;rowIndex+=2){
                //System.out.println(resampledImage[rowIndex][columnIndex]);
                column[index] = resampledRows[rowIndex][columnIndex];
                index ++;
            }
            // System.out.println(arrayToString(column));
            resampledColumn = resample(column);
            // System.out.println(arrayToString(resampledColumn));
            for (int rowIndex = 0; rowIndex<resampledImage.length;rowIndex++){
                resampledImage[rowIndex][columnIndex] = resampledColumn[rowIndex];
            }
        }
        
        // for (int i=0; i<resampledImage.length;i++){
        //     System.out.println(arrayToString(resampledImage[i]));
        // }
    
        return resampledImage;
    }

    public static int[][] resample(int[][] image,int scale){
        int rowNum = image.length + (scale-1)*(image.length-1);
        int columnNum = image[0].length + (scale-1)*(image[0].length-1);

        int [][] resampledRows = new int[rowNum][columnNum];
        int [][] resampledImage = new int[rowNum][columnNum];

        int index = 0;
        for (int i = 0; i<image.length;i++){
            //System.out.println(arrayToString(image[i]));
            // System.out.println(arrayToString(resample(image[i])));
            resampledRows[index] = resample(image[i],scale);
            index +=scale;
        }

        for (int columnIndex = 0;columnIndex < resampledRows[0].length;columnIndex++){
            int[] column = new int[image.length];
            int[] resampledColumn = new int[rowNum];

            index = 0;
            for (int rowIndex = 0; rowIndex<resampledRows.length;rowIndex+=scale){
                //System.out.println(resampledImage[rowIndex][columnIndex]);
                column[index] = resampledRows[rowIndex][columnIndex];
                index ++;
            }
            // System.out.println(arrayToString(column));
            resampledColumn = resample(column,scale);
            // System.out.println(arrayToString(resampledColumn));
            for (int rowIndex = 0; rowIndex<resampledImage.length;rowIndex++){
                resampledImage[rowIndex][columnIndex] = resampledColumn[rowIndex];
            }
        }
        
        return resampledImage;
    }
    
}
