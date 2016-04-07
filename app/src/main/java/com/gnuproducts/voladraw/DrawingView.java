package com.gnuproducts.voladraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by annatina.christ on 07.04.2016.
 */
public class DrawingView extends View {

    //drawing path
    private Path drawPath;

    //drawing and canvas path
    private Paint drawPaint, canvasPaint;

    //initial color
    private int paintColor = 0xFF660000;

    //Canvas
    private Canvas drawCanvas;

    //canvas  Bitmap
    private Bitmap canvasBitmap;

    //initial brush size
    private float brushSize = 20;

    //set erase to false
    private boolean erase = false;

    //paint alpha
    private int paintAlpha = 255;




    public DrawingView(Context context, AttributeSet attrs) {

        super(context, attrs);
        setupDrawing();

    }

    public void setupDrawing(){

        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);


        canvasPaint = new Paint(Paint.DITHER_FLAG);
        brushSize = 20;


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        float touchX =  event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;


    }

    public void setBrushSize(float newSize){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setErase(boolean isErase){
        //set erase true or false
        erase = isErase;
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);



    }

    public void setColor(int color){

        paintColor = color;
        drawPaint.setColor(color);
    }

    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public  int getPaintAlpha(){
        return Math.round((float)paintAlpha/255*100);

    }

    public void setPaintAlpha(int newAlpha){
        paintAlpha = Math.round((float)paintAlpha/255*100);
        drawPaint.setColor(paintColor);
        drawPaint.setAlpha(paintAlpha);

    }


    public void setPencil(){



        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(2);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.SQUARE);




        canvasPaint = new Paint(Paint.DITHER_FLAG);
        brushSize = 1;

    }

    public void setPattern(String newPattern){
        invalidate();
        int patternID = getResources().getIdentifier(newPattern, "drawable", "com.gnuproducts.DrawSomething");
        Bitmap patternBmp = BitmapFactory.decodeResource(getResources(), patternID);
        BitmapShader bmpShader = new BitmapShader(patternBmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);



        drawPaint.setColor(0xFFFFFF);

        drawPaint.setShader(bmpShader);


    }


}
