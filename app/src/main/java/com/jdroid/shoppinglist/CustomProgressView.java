package com.jdroid.shoppinglist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by JoséDaniel on 21/08/2014.
 */
public class CustomProgressView extends View {

    Paint paint;

    Bitmap cartIcon;

    int percentage = 0;

    public CustomProgressView(Context context) {
        super(context);
        init();
    }

    public CustomProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        //super.onMeasure(wMeasureSpec, hMeasureSpec);

        int hSpecMode = MeasureSpec.getMode(hMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(hMeasureSpec);
        int myHeight = hSpecSize;

        if(hSpecMode == MeasureSpec.EXACTLY)
            myHeight = hSpecSize;
        else if (hSpecMode == MeasureSpec.AT_MOST){
            // Wrap Content
        }


        int wSpecMode = MeasureSpec.getMode(wMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(wMeasureSpec);
        int myWidth = wSpecSize;

        if(wSpecMode == MeasureSpec.EXACTLY)
            myWidth = hSpecSize;
        else if (wSpecMode == MeasureSpec.AT_MOST){
            // Wrap Content
        }

        setMeasuredDimension(myWidth, myHeight);
    }

    public void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        cartIcon = BitmapFactory.decodeResource(getResources(), R.drawable.cart_progress);
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        canvas.drawRect(getMeasuredWidth()/6, 5, getMeasuredWidth()-(getMeasuredWidth()/6), 100, paint);
        paint.setStrokeWidth(0);
        paint.setColor(Color.WHITE);
        canvas.drawRect(getMeasuredWidth()/6 +3, 3+5, getMeasuredWidth()-(getMeasuredWidth()/6)-3, 97, paint);
        paint.setColor(Color.GREEN);
        canvas.drawRect(getMeasuredWidth()/6 +3, 3+5, getMeasuredWidth()/6 + percentage-3, 97, paint );
        Bitmap scaled = Bitmap.createScaledBitmap(cartIcon,110,110,false);
        canvas.drawBitmap(scaled,getMeasuredWidth()/6 +percentage-50,0 ,paint);
        invalidate();

    }

    public void setPercentage (int p){

        int i = (p*(getMeasuredWidth()-(2*(getMeasuredWidth()/6)))-3)/100;


        percentage = i;




    }
}


