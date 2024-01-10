package com.aa.fittracker.presentation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class MenuItem extends View {
    private Bitmap mImage;
    private final Paint mPaint = new Paint();
    private final Point mSize = new Point();
    private final Point mStartPosition = new Point();
    private boolean isBeingDragged = false;

    public MenuItem(Context context) {
        super(context);
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        mImage = image;
        setSize(mImage.getWidth(), mImage.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Point position = getPosition();
        canvas.drawBitmap(mImage, position.x, position.y, mPaint);
        requestLayout();
        invalidate();
    }

    public void setPosition(final Point position) {
        layout(position.x, position.y, position.x + mSize.x, position.y + mSize.y);
        invalidate();
    }

    public Point getPosition() {
        return new Point(getLeft(), getTop());
    }

    public void setSize(int width, int height) {
        mSize.x = width;
        mSize.y = height;
        requestLayout();
    }

    public Point getSize() {
        return mSize;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isViewContains(this, x, y)) {
                    mStartPosition.x = x;
                    mStartPosition.y = y;
                    isBeingDragged = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isBeingDragged) {
                    int deltaX = x - mStartPosition.x;
                    int deltaY = y - mStartPosition.y;
                    int newX = (int) (getX() + deltaX);
                    int newY = (int) (getY() + deltaY);
                    setPosition(new Point(newX, newY));
                    mStartPosition.x = x;
                    mStartPosition.y = y;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                isBeingDragged = false;
                // Implement animation to return to the original position
                // using ObjectAnimator or other animation methods
                // Example: ObjectAnimator.ofFloat(this, "translationX", 0).setDuration(300).start();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean isViewContains(View view, int x, int y) {
        int[] topLeft = new int[2];
        view.getLocationOnScreen(topLeft);
        int left = topLeft[0];
        int top = topLeft[1];
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();
        return (x >= left && x <= right && y >= top && y <= bottom);
    }
}
