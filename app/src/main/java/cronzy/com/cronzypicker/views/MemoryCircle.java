package cronzy.com.cronzypicker.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MemoryCircle extends View{

    final int DEFAULT_COLOR = Color.WHITE;
    private int mColor;
    private Paint mPaint;

    public MemoryCircle(Context context) {
        super(context);
        init();
    }

    public MemoryCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MemoryCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mColor=DEFAULT_COLOR;
        mPaint =new Paint();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(
                getWidth()/2,
                getHeight()/2,
                getWidth()*0.5f,
                mPaint);
    }

    public void setColor(int color){
        mColor = color;
    }

    public int getColor(){
        return mColor;
    }




}
