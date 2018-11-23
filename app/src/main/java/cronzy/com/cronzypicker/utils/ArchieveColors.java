package cronzy.com.cronzypicker.utils;

import android.app.Activity;

import java.util.ArrayList;

import cronzy.com.cronzypicker.constants.ProjectConstants;

public class ArchieveColors {

    ArrayList<Integer> archiveList;
    MySharedPreferences preferences;
    String key = "SAVED_LIST";

    public ArchieveColors(Activity activity){
        archiveList = new ArrayList<>();
        preferences = new MySharedPreferences(activity);
        completeList();
    }

    public void addColorToArchieve(int color){
        if(archiveList.get(archiveList.size()-1)!=color){
            archiveList.add(color);
        }
        preferences.saveDataListOfColors(key,archiveList);
    }

    public ArrayList<Integer> getListOfColors(){
        if(archiveList.size()<6) {
            completeList();
        }
        return archiveList;
    }


    private void completeList(){
        archiveList=preferences.getDataListOfColors(key);
        if (archiveList.size()==0){
            for(int b=0; archiveList.size()<6;b++){
                archiveList.add(ProjectConstants.defaultColors[b]);
            }
        }

    }

    public void resetData (){
        archiveList = new ArrayList<>();
        for(int b=0; archiveList.size()<6;b++){
            archiveList.add(ProjectConstants.defaultColors[b]);
        }
    }

    public int[] getMassOfColors(){
        if(archiveList.size()<6) {
            completeList();
        }

        int[] output = new int[archiveList.size()];
        for (int i = 0; i != archiveList.size(); i++) {
            output[i] = archiveList.get(i);
        }
        return output;
    }


}
