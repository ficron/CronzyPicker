package cronzy.com.cronzypicker.constants;

import android.graphics.Color;

import java.util.UUID;

public class ProjectConstants {


    public static float WIDTH_SHADER_BORDER = 0.02266f;
    public static float RAD_OUTER_ROTARY_CIRCLE = 0.037f;
    public static float RAD_INNER_ROTARY_CIRCLE = 0.037f;
    public static float RAD_OUTER_SHADER_CIRCLE = 0.421f;
    public static float RAD_INNER_MAIN_CIRCLE = 0.14136f;
    public final static int SET_COLOR = 0;
    public final static int SET_INNER = 1;
    public static  int defaultColors[] = new int[]{Color.LTGRAY, Color.BLUE, Color.CYAN, Color.YELLOW, Color.RED, Color.DKGRAY, Color.BLACK, Color.GREEN};

    //Shared Prederences
    public static String APP_PREFERENCES = "appsettings";
    public static String APP_PREFERENCES_NAME = "bluetoothadress";
    public static String APP_DEFAULT_NAME_VALUE="default";


    //Статус для Handler
    public final static int RECIEVE_MESSAGE = 0;
    public static int REQUEST_ENABLE_BT = 1;



    //Log
    public static String TAG = "AppLog";
    public static String TAG_BT = "AppLogBT";
    public static String TAG_Handle = "MyLogHandle";


    // SPP UUID сервиса
    public static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

}
