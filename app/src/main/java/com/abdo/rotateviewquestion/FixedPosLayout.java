package com.abdo.rotateviewquestion;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FixedPosLayout extends ViewGroup {

    public FixedPosLayout(Context context) {
        super(context);
    }

    public FixedPosLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedPosLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();

        for (int i = 0; i<count; ++i) {
            View child = getChildAt(i);

            FixedPosLayout.LayoutParams params =
                    (FixedPosLayout.LayoutParams) child.getLayoutParams();

            int left = params.x;
            int top = params.y;

            child.layout(left, top,
                    left + child.getMeasuredWidth(),
                    top + child.getMeasuredHeight());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Measure this view
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Measure children
        int count = getChildCount();
        for (int i = 0; i<count; ++i) {
            View child = getChildAt(i);

            FixedPosLayout.LayoutParams lp =
                    (FixedPosLayout.LayoutParams) child.getLayoutParams();

            int childWidthMeasureSpec =
                    MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);

            int childHeightMeasureSpec =
                    MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int x;
        public int y;

        public LayoutParams(int width, int height, int x, int y) {
            super(width, height);
            this.x = x;
            this.y = y;
        }
    }
}
