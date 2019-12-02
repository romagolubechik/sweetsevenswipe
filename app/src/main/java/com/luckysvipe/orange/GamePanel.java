package com.luckysvipe.orange;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private SceneManager manager;

    // Let us define our constructor first
    public GamePanel (Context context) {
        super(context);

        getHolder().addCallback(this);

        Constants.CURRENT_CONTEXT = context;

        thread = new MainThread(getHolder(), this);

        manager = new SceneManager();

        setFocusable(true);
    }

    //Now let us override few methods

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    boolean retry = true;
    while (true) {
        try {
            //trying to catch if the game loop is stopped nd pint out the error
            thread.setRunning(false);
            thread.join();
        } catch (Exception e) {e.printStackTrace();}
        retry = false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        manager.receiveTouch(event);

        return true;
        //return super.onTouchEvent(event);
    }

    // this method will update our game frame by frame
    public void update() {
        manager.update();

    }

    //finally we want to override canvas
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        manager.draw(canvas);
    }
}
