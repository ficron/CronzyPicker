package cronzy.com.cronzypicker.utils;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cronzy.com.cronzypicker.R;
import cronzy.com.cronzypicker.constants.ProjectConstants;
import cronzy.com.cronzypicker.utils.ArchieveColors;
import cronzy.com.cronzypicker.views.ColorCirclePicker;
import cronzy.com.cronzypicker.views.MemoryCircle;

/**
 * Created by Олександр on 03.10.2018.
 */
public class MemoryPallete  {

    private List<MemoryCircle> circlesList;
    private LinearLayout conteinerForViews;
    private ColorCirclePicker mColorCirclePickerView;
    private Activity activity;

    public MemoryPallete(Activity activity, ColorCirclePicker mColorCirclePickerView) {
        this.activity = activity;
        this.mColorCirclePickerView = mColorCirclePickerView;

        circlesList = new ArrayList<>();
        conteinerForViews = (LinearLayout) activity.findViewById(R.id.conteinerForViews);

    }

    public void init() {
        initList();
        initListeners();
        setMinDemension();
        updateViewColors();
    }


    private void initList() {
        circlesList.add((MemoryCircle) activity.findViewById(R.id.mw1));
        circlesList.add((MemoryCircle) activity.findViewById(R.id.mw2));
        circlesList.add((MemoryCircle) activity.findViewById(R.id.mw3));
        circlesList.add((MemoryCircle) activity.findViewById(R.id.mw4));
        circlesList.add((MemoryCircle) activity.findViewById(R.id.mw5));
        circlesList.add((MemoryCircle) activity.findViewById(R.id.mw6));

    }

    private void initListeners() {

        View.OnClickListener mwListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.mw1) {
                    mColorCirclePickerView.setUsedColorFromMass(converter(circlesList.get(0).getColor()));
                } else if (v.getId() == R.id.mw2) {
                    mColorCirclePickerView.setUsedColorFromMass(converter(circlesList.get(1).getColor()));
                } else if (v.getId() == R.id.mw3) {
                    mColorCirclePickerView.setUsedColorFromMass(converter(circlesList.get(2).getColor()));
                } else if (v.getId() == R.id.mw4) {
                    mColorCirclePickerView.setUsedColorFromMass(converter(circlesList.get(3).getColor()));
                } else if (v.getId() == R.id.mw5) {
                    mColorCirclePickerView.setUsedColorFromMass(converter(circlesList.get(4).getColor()));
                } else if (v.getId() == R.id.mw6) {
                    mColorCirclePickerView.setUsedColorFromMass(converter(circlesList.get(5).getColor()));
                }
            }

            private float[] converter(int color) {
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                float[] hsv = new float[3];
                Color.RGBToHSV(r, g, b, hsv);
                return hsv;
            }
        };


        Iterator<MemoryCircle> it = circlesList.iterator();
        while (it.hasNext()) {
            MemoryCircle mc = it.next();
            mc.setOnClickListener(mwListener);

        }


    }

    public void setMinDemension() {
        Iterator<MemoryCircle> it = circlesList.iterator();
        int d = conteinerForViews.getWidth() / 7;

        while (it.hasNext()) {
            MemoryCircle mc = it.next();
            mc.setMinimumWidth(d);
            mc.setMinimumHeight(d);
        }
    }


    public void updateViewColors(ArchieveColors archieveColors) {
        mUpdateVievwColors(archieveColors.getListOfColors());

    }

    public void updateViewColors() {
        List<Integer> list = new ArrayList<>();
        for (int i : ProjectConstants.defaultColors) list.add(i);
        mUpdateVievwColors(list);
    }

    private void mUpdateVievwColors(List<Integer> hexColors) {
        Log.d(ProjectConstants.TAG, "setViewColors()");
        Iterator<MemoryCircle> it = circlesList.iterator();
        int i = 1;
        while (it.hasNext()) {
            MemoryCircle mc = it.next();
            mc.setColor(hexColors.get(hexColors.size() - i));
            i++;
            mc.invalidate();

        }
    }

}
