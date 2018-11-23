package cronzy.com.cronzypicker.utils;

import android.graphics.Color;
import android.util.Log;

import cronzy.com.cronzypicker.constants.ProjectConstants;

public class Converters   {


    public float[] HValue(String rgb){
        int r,g,b;
        float[] hsv = new float[3];
        hsv[0]=-1;

        try {
            Log.d(ProjectConstants.TAG +" "+getClass()+" ","Converters: rgb_HValue "+rgb);
            int rgbI = Integer.valueOf(rgb);
            Log.d(ProjectConstants.TAG+" "+getClass()+" ","Converters: rgbI_HValue "+rgbI);
            if(rgb.length()==9){
                Log.d(ProjectConstants.TAG+" "+getClass()+" ", "R"+rgb.substring(0,3));
                r =Integer.valueOf(rgb.substring(0,3));
                Log.d(ProjectConstants.TAG+" "+getClass()+" ", "G"+rgb.substring(3,6));
                g=Integer.valueOf(rgb.substring(3,6));
                Log.d(ProjectConstants.TAG+" "+getClass()+" ", "B"+rgb.substring(6,rgb.length()));
                b=Integer.valueOf(rgb.substring(6,rgb.length()));
                Color.RGBToHSV(r,g,b,hsv);
            }
        } catch (Exception nfeEx){
            Log.d(ProjectConstants.TAG+" "+getClass()+" ","Exception "+nfeEx.getMessage());
        }
        return hsv;
    }

    public float[] HValue(int r, int g,int b){
        float[] hsv = new float[3];
        hsv[0]=-1;
        Color.RGBToHSV(r,g,b,hsv);
        return hsv;
    }



    /*
        public String convertToHex(int intColor) {
        String hexColor = String.format("#%06X", (0xFFFFFF & intColor));
        return hexColor;
    }
     */
    /*
        public float[] colorToRgb(int Color){
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        float[] hsv = new float[3];
        Color.RGBToHSV(r, g, b, hsv);
        return hsv;
    }

     */




}
