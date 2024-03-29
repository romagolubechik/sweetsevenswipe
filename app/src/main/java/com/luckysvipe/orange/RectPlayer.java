package com.luckysvipe.orange;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

public class RectPlayer implements GameObject {

    private Rect rectangle;
    private int color;

    private Animation idle;
    private Animation walkright;
    private Animation walkleft;

    private AnimationManager animManager;

    public Rect getRectangle() {
        return rectangle;
    }

    public RectPlayer(Rect rectangle, int color) {
        this.rectangle = rectangle;
        this.color = color;

        BitmapFactory bf = new BitmapFactory();
        Bitmap idleImg = bf. decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sweert);
        Bitmap walk1 = bf. decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sweert);
        Bitmap walk2 = bf. decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sweert);

        idle = new Animation(new Bitmap[] {idleImg}, 10);
        // oanimation containing a bitmap of only single drawing
        // any amount of time would work as the animation doe snot change for idle
        walkright = new Animation(new Bitmap[] {walk1, walk2}, 1.5f);
        // animation changes every 0.5f seconds

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), m, false);

        walkleft = new Animation(new Bitmap[] {walk1, walk2}, 1.5f);

        animManager = new AnimationManager(new Animation[] {idle, walkright, walkleft});
    }

    @Override
    public void draw(Canvas canvas) {
        //Paint paint = new Paint();
        //paint.setColor(color);
        //canvas.drawRect(rectangle, paint);

        animManager.draw(canvas, rectangle);
    }

    @Override
    public void uprate() {
        animManager.update();
    }

    public void update(Point point) {
        float oldLeft = rectangle.left; //left coordinate of rectangle before we modify it


        // center the point in the center of the rectangle, the point will be updated
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);

        int state = 0; //0 - idle, 1 - walkright, 2 - walkleft
        if(rectangle.left - oldLeft > 5)
            //if the difference is 5 pixels, then we are starting an animation
            state = 1;
        else if (rectangle.left - oldLeft < -5)
            state = 2;

        animManager.playAnim(state);

        animManager.update();
    }
}
