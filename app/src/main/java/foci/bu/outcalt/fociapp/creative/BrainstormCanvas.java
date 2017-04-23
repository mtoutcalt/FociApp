package foci.bu.outcalt.fociapp.creative;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mark on 4/22/2017.
 */
public class BrainstormCanvas extends View {

    private Path path;
    Context context;
    private Paint paint;
    private float xPos, yPos;
    private static final float PAINT_SCREEN_BOUNDARY = 5;

    public BrainstormCanvas(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        path = new Path();
        setupPaint();
    }

    private void setupPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(4f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    private void touch(float x, float y) {
        path.moveTo(x, y);
        xPos = x;
        yPos = y;
    }

    private void move(float x, float y) {
        float xDiff = Math.abs(x - xPos);
        float yDiff = Math.abs(y - yPos);
        if (xDiff >= PAINT_SCREEN_BOUNDARY || yDiff >= PAINT_SCREEN_BOUNDARY) {
            path.quadTo(xPos, yPos, (x + xPos) / 2, (y + yPos) / 2);
            xPos = x;
            yPos = y;
        }
    }

    private void stopTouch() {
        path.lineTo(xPos, yPos);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldwidth, int oldheight) {
        super.onSizeChanged(width, height, oldwidth, oldheight);
        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(canvasBitmap);
    }

    public void clearCanvas() {
        path.reset();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xCoord = event.getX();
        float yCoord = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch(xCoord, yCoord);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                move(xCoord, yCoord);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                stopTouch();
                invalidate();
                break;
        }
        return true;
    }

}
