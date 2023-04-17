package com.abdo.rotateviewquestion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;


@SuppressLint("ViewConstructor")
public class MyView extends View {

    enum ViewState {
        ANCHOR1, ANCHOR2, VIEW
    }

    public static final int RADIUS = 24; // Anchor radius
    public static final int OFFSET = 1;

    private final Paint paint;

    public RectF anchorRect1;
    public RectF anchorRect2;

    private ViewState viewState;

    private float tapX;
    private float tapY;


    public MyView(Context context) {
        super(context);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);

        anchorRect1 = new RectF();
        anchorRect2 = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        var width = getWidth();
        var height = getHeight();

        //Draw line between anchor points
        canvas.drawLine(
                anchorRect1.centerX(), anchorRect1.centerY(),
                anchorRect2.centerX(), anchorRect2.centerY(), paint);

        //Draw anchor circle
        canvas.drawCircle(anchorRect1.centerX(), anchorRect1.centerY(), RADIUS, paint);
        canvas.drawCircle(anchorRect2.centerX(), anchorRect2.centerY(), RADIUS, paint);

        //Draw bounding rect
        canvas.drawRect(OFFSET, OFFSET, width - OFFSET, height - OFFSET, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //Recompute anchors rect
        anchorRect1.set(
                OFFSET, h /2f - RADIUS + OFFSET,
                2*RADIUS, h /2f + RADIUS - OFFSET
        );

        anchorRect2.set(
                w - 2*RADIUS, h /2f - RADIUS + OFFSET,
                w - OFFSET, h /2f + RADIUS - OFFSET
        );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // Measure exactly
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                tapX = event.getX();
                tapY = event.getY();

                // Anchor 1 clicked
                if (anchorRect1.contains(tapX, tapY)) {
                    viewState = ViewState.ANCHOR1;
                    setPivotX(anchorRect2.centerX());
                    setPivotY(anchorRect2.centerY());
                }
                // Anchor 2 clicked
                else if (anchorRect2.contains(tapX, tapY)) {
                    viewState = ViewState.ANCHOR2;
                    setPivotX(anchorRect1.centerX());
                    setPivotY(anchorRect1.centerY());
                }
                // View body clicked
                else {
                    viewState = ViewState.VIEW;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                switch (viewState) {
                    //Anchor 1 clicked
                    case ANCHOR1: {
                        float angle = (float) Math.toDegrees(Math.atan2(event.getY() - anchorRect2.centerY(), getWidth()));
                        setRotation(getRotation() - angle);
                        break;
                    }
                    //Anchor 2 clicked
                    case ANCHOR2: {
                        float angle = (float) Math.toDegrees(Math.atan2(event.getY() - anchorRect1.centerY(), getWidth()));
                        setRotation(getRotation() + angle);
                        break;
                    }
                    //View Clicked
                    case VIEW: {
                        float deltaX = event.getX() - tapX;
                        float deltaY = event.getY() - tapY;

                        // Transform point
                        PointF transformedPoint = transformPoint(deltaX, deltaY, getRotation());

                        //Perform the rotation
                        setX(getX() + transformedPoint.x);
                        setY(getY() + transformedPoint.y);
                        break;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                break;
            }
        }
        return true;
    }

    private PointF transformPoint(float x, float y, float degree) {
        //https://en.wikipedia.org/wiki/Rotation_matrix
        double radian = Math.toRadians(degree);
        float xp = (float) (x*Math.cos(radian) - y*Math.sin(radian));
        float yp = (float) (x*Math.sin(radian) + y*Math.cos(radian));
        return new PointF(xp, yp);
    }
}
