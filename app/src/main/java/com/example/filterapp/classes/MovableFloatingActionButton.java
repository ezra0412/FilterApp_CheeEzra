package com.example.filterapp.classes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MovableFloatingActionButton extends FloatingActionButton implements View.OnTouchListener {

    private final static float CLICK_DRAG_TOLERANCE = 10;

    private float downRawX, downRawY;
    private float dX, dY;

    private CoordinatorLayout.LayoutParams coordinatorLayout;

    public MovableFloatingActionButton(Context context) {
        super(context);
        init();
    }

    public MovableFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MovableFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        View viewParent;
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downRawX = motionEvent.getRawX();
                downRawY = motionEvent.getRawY();
                dX = view.getX() - downRawX;
                dY = view.getY() - downRawY;

                return true;

            case MotionEvent.ACTION_MOVE:
                int viewWidth = view.getWidth();
                int viewHeight = view.getHeight();

                viewParent = (View) view.getParent();
                int parentWidth = viewParent.getWidth();
                int parentHeight = viewParent.getHeight();

                float newX = motionEvent.getRawX() + dX;
                newX = Math.max(0, newX);
                newX = Math.min(parentWidth - viewWidth, newX);

                float newY = motionEvent.getRawY() + dY;
                newY = Math.max(0, newY);
                newY = Math.min(parentHeight - viewHeight, newY);

                view.animate()
                        .x(newX)
                        .y(newY)
                        .setDuration(0)
                        .start();
                return true;

            case MotionEvent.ACTION_UP:
                float upRawX = motionEvent.getRawX();
                float upRawY = motionEvent.getRawY();

                float upDX = upRawX - downRawX;
                float upDY = upRawY - downRawY;

                if ((Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) && performClick())
                    return true;

                View viewParent2 = (View) view.getParent();
                float borderY, borderX;
                float oldX = view.getX(), oldY = view.getY();
                float finalX, finalY;

                borderY = Math.min(view.getY() - viewParent2.getTop(), viewParent2.getBottom() - view.getY());
                borderX = Math.min(view.getX() - viewParent2.getLeft(), viewParent2.getRight() - view.getX());

                float fab_margin = 15;

                if (borderX > borderY) {
                    if (view.getY() > viewParent2.getHeight() / 2) {
                        finalY = viewParent2.getBottom() - view.getHeight();
                        finalY = Math.min(viewParent2.getHeight() - view.getHeight(), finalY) - fab_margin;
                    } else {
                        finalY = viewParent2.getTop();
                        finalY = Math.max(0, finalY) + fab_margin;
                    }
                    finalX = oldX;
                    if (view.getX() + viewParent2.getLeft() < fab_margin)
                        finalX = viewParent2.getLeft() + fab_margin;
                    if (viewParent2.getRight() - view.getX() - view.getWidth() < fab_margin)
                        finalX = viewParent2.getRight() - view.getWidth() - fab_margin;
                } else {
                    if (view.getX() > viewParent2.getWidth() / 2) {
                        finalX = viewParent2.getRight() - view.getWidth();
                        finalX = Math.max(0, finalX) - fab_margin;
                    } else {
                        finalX = viewParent2.getLeft();
                        finalX = Math.min(viewParent2.getWidth() - view.getWidth(), finalX) + fab_margin;
                    }
                    finalY = oldY;
                    if (view.getY() + viewParent2.getTop() < fab_margin)
                        finalY = viewParent2.getTop() + fab_margin;
                    if (viewParent2.getBottom() - view.getY() - view.getHeight() < fab_margin)
                        finalY = viewParent2.getBottom() - view.getHeight() - fab_margin;
                }

                view.animate()
                        .x(finalX)
                        .y(finalY)
                        .setDuration(400)
                        .start();

                return false;

            default:
                return super.onTouchEvent(motionEvent);
        }

    }

    public CoordinatorLayout.LayoutParams getCoordinatorLayout() {
        return coordinatorLayout;
    }

    public void setCoordinatorLayout(CoordinatorLayout.LayoutParams coordinatorLayout) {
        this.coordinatorLayout = coordinatorLayout;
    }
}
