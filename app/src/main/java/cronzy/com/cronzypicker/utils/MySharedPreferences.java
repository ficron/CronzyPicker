package cronzy.com.cronzypicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import cronzy.com.cronzypicker.constants.ProjectConstants;

public class MySharedPreferences  {

    private SharedPreferences preferences;

    public MySharedPreferences(Activity activity){
        preferences = activity.getSharedPreferences(ProjectConstants.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void resetBluetoothAddress(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ProjectConstants.APP_PREFERENCES_NAME, ProjectConstants.APP_DEFAULT_NAME_VALUE);
        editor.apply();
    }

    public String getBluetoothAddress(){
        if (preferences.contains(ProjectConstants.APP_PREFERENCES_NAME)) {
            return preferences.getString(ProjectConstants.APP_PREFERENCES_NAME, ProjectConstants.APP_DEFAULT_NAME_VALUE);
        }
        return ProjectConstants.APP_DEFAULT_NAME_VALUE;
    }

    public void setBluetoothAddress(String bluetothAdress){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ProjectConstants.APP_PREFERENCES_NAME, bluetothAdress);
        editor.apply();
    }

    public void saveDataListOfColors(String key, ArrayList<Integer> intList) {
        if (key==null)throw new NullPointerException();
        Integer[] myIntList = intList.toArray(new Integer[intList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myIntList)).apply();
    }

    public ArrayList<Integer> getDataListOfColors(String key) {
        String[] myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> arrayToList = new ArrayList<String>(Arrays.asList(myList));
        ArrayList<Integer> newList = new ArrayList<Integer>();

        for (String item : arrayToList)
            newList.add(Integer.parseInt(item));

        return newList;
    }

}
