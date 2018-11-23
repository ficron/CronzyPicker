package cronzy.com.cronzypicker.main;

import android.os.Handler;
import android.util.Log;

import cronzy.com.cronzypicker.constants.ProjectConstants;
import cronzy.com.cronzypicker.views.ColorCirclePicker;

/**
 * Created by Олександр on 02.10.2018.
 */
public class MyHandler extends Handler  {
    private StringBuilder sb;
    private ColorCirclePicker mColorCirclePickerView;

    public MyHandler(ColorCirclePicker cp) {
        sb = new StringBuilder();
        mColorCirclePickerView = cp;
    }


    @Override
    public void handleMessage(android.os.Message msg) {
        Log.d(ProjectConstants.TAG_Handle,"i am in handleMessage()");
        Log.d(ProjectConstants.TAG_Handle,"msg.what "+msg.what);

        switch (msg.what) {
            case ProjectConstants.RECIEVE_MESSAGE:                                                   // если приняли сообщение в Handler
                byte[] readBuf = (byte[]) msg.obj;
                Log.d(ProjectConstants.TAG_BT, " case RECIEVE_MESSAGE readBuf.length: " + readBuf.length);
                String strIncom = new String(readBuf, 0, msg.arg1);
                //String strIncom = new String(readBuf);
                sb.append(strIncom);                                                // формируем строку
                int endOfLineIndex = sb.indexOf("q");                            // определяем символы конца строки

                String localString = "";
                Log.d(ProjectConstants.TAG_BT, "sb.toString():"+sb.toString());
                if (endOfLineIndex > 0) {                                            // если встречаем конец строки,
                    localString = sb.substring(0, endOfLineIndex);               // то извлекаем строку
                    sb.delete(0, sb.length());                                      // и очищаем sb
                    //Toast.makeText(getBaseContext(), "Получено: " + sbprint, Toast.LENGTH_SHORT).show();
                    //Log.d(TAG, "Получено, method handleMessage(): " + sbprint);
                }
                if (localString.length() > 8) {
                    Log.d(ProjectConstants.TAG_BT, "localString " + localString);
                    localString = localString.substring(localString.length() - 9,
                            localString.length());
                }
                Log.d(ProjectConstants.TAG_BT, "...Строка: " + localString + " Байт:" + msg.arg1);

                mColorCirclePickerView.setUsedColor(localString);
                break;
        }
    }


}
