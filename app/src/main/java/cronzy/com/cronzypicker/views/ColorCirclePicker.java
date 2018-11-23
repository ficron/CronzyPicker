package cronzy.com.cronzypicker.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cronzy.com.cronzypicker.constants.ProjectConstants;
import cronzy.com.cronzypicker.utils.ColorUtils;
import cronzy.com.cronzypicker.utils.Converters;

public class ColorCirclePicker extends View implements View.OnTouchListener {

    private OnColorChangeListener listener;
    private Context gContext;

    private int mMode;

    private float coordinateCentrX;
    private float coordinateCentrY;

    private float raidus_gradient;
    private float radius_centr;
    private float radius_rotate_border;
    private float radius_rotate_inner;

    private int size;

    private Paint p_gradient = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_center = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_direct = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_white = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float deg_col; // углы поворота
    private float[] hsv = new float[]{0, 1f, 1f};
    private int[] argb = new int[]{255, 0, 0, 0};

    private int mColor;
    private String showedRGB;
    private int mWidth;
    private int mHeight;




    public ColorCirclePicker(Context context) {
        this(context, null);
    }

    public ColorCirclePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorCirclePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setOnColorChangeListener(OnColorChangeListener l) {
        this.listener = l;
    }

    public String getRGB() {
        return showedRGB;
    }

    public int getRGBHEx(){
        Integer r = Integer.parseInt(showedRGB.substring(0, 3));
        Integer g = Integer.parseInt(showedRGB.substring(3, 6));
        Integer b = Integer.parseInt(showedRGB.substring(6, 9));

        Log.d("MyLogDDD ",showedRGB);
        Log.d("MyLogDDD ",Color.rgb(r, g, b)+"");

        //
        return (Color.rgb(r, g, b));
    }



    public void setSaturation(float saturation) {
        setColScale(hsv[0], saturation, hsv[2]);
        Log.d("SeekBar+", "saturation " + saturation);
    }

    public void setValue(float value) {
        setColScale(hsv[0], hsv[1], value);
        Log.d("SeekBar+", "value " + value);
    }

    public void setHSV(float h, float s, float v){
        setColScale(h,s,v);
    }


    public int getColor() {
        return mColor;
    }

    public int getSaturation(){
        return Math.round(hsv[1]*100);
    }

    public int getValue(){
        return Math.round(hsv[2]*100);
    }


    public void setUsedColor(String RGBcolor) {
        Log.d(ProjectConstants.TAG, "setUsedColor: " + RGBcolor);
        float innerhsv[] = new Converters().HValue(RGBcolor);
        if (innerhsv[0] != -1) {
            setUsedColorFromMass(innerhsv);
        }
    }


    public void setColor(int color){
        float innerhsv[] = new float[3];
        Color.colorToHSV(color,innerhsv);
        if (innerhsv[0] != -1) {
            setUsedColorFromMass(innerhsv);
        }
    }


    public void setUsedColorFromMass(float[] hsv){
        mColor = Color.HSVToColor(hsv);
        setColScale(hsv[0], hsv[1], hsv[2]);
    }


    private void init(Context context) {
        setFocusable(true);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        gContext = context;
        p_gradient.setStyle(Paint.Style.STROKE);
        p_center.setStyle(Paint.Style.FILL_AND_STROKE);
        p_direct.setStyle(Paint.Style.FILL_AND_STROKE);
        p_white.setStyle(Paint.Style.FILL_AND_STROKE);
        p_white.setStrokeWidth(12);
        p_white.setColor(Color.WHITE);
        setOnTouchListener(this);
    }


    private void calculateSizes() {
        float mKoef = size*0.5f;
        raidus_gradient = size * ProjectConstants.RAD_OUTER_SHADER_CIRCLE;

        radius_centr = size * ProjectConstants.RAD_INNER_MAIN_CIRCLE;

        radius_rotate_border = size * ProjectConstants.RAD_OUTER_ROTARY_CIRCLE;
        radius_rotate_inner = size * ProjectConstants.RAD_INNER_ROTARY_CIRCLE;

        p_gradient.setStrokeWidth(size * ProjectConstants.WIDTH_SHADER_BORDER*1.5f);
        coordinateCentrX = mKoef;
        coordinateCentrY = mKoef-radius_rotate_border;
    }

    private int measure(int measureSpec) {
        int result;
        int specMoge = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMoge == MeasureSpec.UNSPECIFIED) {
            result = 200;
        } else {
            result = specSize;
        }
        return result;
    }




    protected float getAngle(float x, float y) {
        float deg = 0;
        if (x != 0) deg = y / x;
        deg = (float) Math.toDegrees(Math.atan(deg));
        if (x < 0) deg += 180;
        else if (x > 0 && y < 0) deg += 360;
        return deg;
    }


    private void drawCenterCircle(Canvas canvas) {
        //canvas.drawOval(new RectF(coordinateCentrX - raidus_gradient , coordinateCentrY - raidus_gradient, coordinateCentrX + raidus_gradient , coordinateCentrY + raidus_gradient), p_center);
        //p_center.setShadowLayer(radius_centr+10,0,0,Color.BLACK);
        canvas.drawCircle(coordinateCentrX, coordinateCentrY, radius_centr, p_center);



    }

    private void drawRotaryCircles(Canvas c) {
        float d = deg_col;
        c.rotate(d, coordinateCentrX, coordinateCentrY);
        c.drawCircle(coordinateCentrX + raidus_gradient, coordinateCentrY, radius_rotate_border, p_white);
        c.drawCircle(coordinateCentrX + raidus_gradient, coordinateCentrY, radius_rotate_inner, p_direct);
        c.rotate(-d, coordinateCentrX, coordinateCentrY);
    }

    private void drawText(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(mColor);
        p.setStrokeWidth(200);
        p.setTextSize(size/15);
        p.setTextAlign(Paint.Align.CENTER);
        String ahex = Integer.toHexString(mColor).toUpperCase();

        if (ahex.length() > 5) {
            String h = ahex.substring(2, 4);
            String s = ahex.substring(4, 6);
            String v = ahex.substring(6);
            String stingHc = "000" + Integer.parseInt(h, 16);
            String stringSc = "000" + Integer.parseInt(s, 16);
            String stringVc = "000" + Integer.parseInt(v, 16);
            showedRGB = stingHc.substring(stingHc.length() - 3) +
                    stringSc.substring(stringSc.length() - 3) +
                    stringVc.substring(stringVc.length() - 3);
        }
        if (ahex.length() > 6) {
            String hex = "#" + ahex.substring(2, ahex.length());
            Integer mKoef = Math.round(raidus_gradient / 2) + Math.round(radius_centr / 2);
            canvas.drawText(hex, coordinateCentrX - 6, coordinateCentrY + mKoef+60, p);
            canvas.drawText(
                    new ColorUtils().getColorNameFromHex(mColor), coordinateCentrX - 6,
                    coordinateCentrY - mKoef+size/15, p);
        }
    }

    private void drawGradinetCircle(Canvas c) {

        SweepGradient s;
        int[] sg = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.RED};
        s = new SweepGradient(coordinateCentrX, coordinateCentrY, sg, null);
        p_gradient.setShader(s);
        c.drawCircle(coordinateCentrX, coordinateCentrY, raidus_gradient, p_gradient);

        /*
        Bitmap mBitmap;
        Resources res = this.getResources();
        mBitmap = BitmapFactory.decodeResource(res, R.drawable.logo24n);
        c.drawBitmap(mBitmap, coordinateCentrX - mBitmap.getHeight() / 2,
                coordinateCentrY - mBitmap.getWidth() / 2, p_gradient);
         */

    }



    private void setColScale(float h, float s, float v) {
        deg_col = h;
        hsv[0] = h;
        hsv[1] = s;
        hsv[2] = v;

        mColor = Color.HSVToColor(argb[0], hsv);
        p_center.setShadowLayer(radius_centr+20,0,0,mColor);
        p_center.setColor(mColor);
        p_direct.setColor(mColor);

        invalidate();
    }


    public interface OnColorChangeListener {
        void onDismiss(int val, float alpha);
        void onColorChanged(int val, float alpha);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCenterCircle(canvas);
        drawGradinetCircle(canvas);
        drawRotaryCircles(canvas);
        drawText(canvas);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = measure(widthMeasureSpec);
        mHeight = measure(heightMeasureSpec);
        Log.d(ProjectConstants.TAG, "mWidth=" + mWidth + " /mHeight=" + mHeight);
        size = Math.min(mWidth, mHeight);
        int fdd = Math.round(size*ProjectConstants.RAD_OUTER_ROTARY_CIRCLE);
        setMeasuredDimension(size, size-fdd*2);
        calculateSizes();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float a = Math.abs(motionEvent.getX() - coordinateCentrX);
                float b = Math.abs(motionEvent.getY() - coordinateCentrY);
                float virtualRadius = (float) Math.sqrt(a * a + b * b);

                if (virtualRadius > radius_centr) {
                    mMode = ProjectConstants.SET_COLOR;
                    listener.onDismiss(mColor, 1);
                } else {
                    mMode = ProjectConstants.SET_INNER;
                }

                break;

            case MotionEvent.ACTION_MOVE:
                float x = motionEvent.getX() - coordinateCentrX;
                float y = motionEvent.getY() - coordinateCentrY;

                switch (mMode) {
                    case ProjectConstants.SET_COLOR:
                        setColScale(getAngle(x, y), 1f, 1f);
                        listener.onColorChanged(mColor, 1);
                        break;
                    case ProjectConstants.SET_INNER:
                        break;
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                v.performClick();
                break;
        }
        return true;
    }











    /*
        private  SharedPreferences getPrefs() {
        String PREF_NAME = "prefs";
        return gContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public  void setRGB(String input) {
        String SAVED_RGB = "RGB";
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(SAVED_RGB, input);
        Log.d("MyLog","SAVED_RGB:"+input);
        editor.apply();
    }
     */


}
