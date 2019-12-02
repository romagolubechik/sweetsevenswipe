package com.luckysvipe.orange;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Scene {
    public void update();
    public void draw(Canvas canvas);
    public void terminate(); // when the scene is supposed to end, this will be called
    public void receiveTouch(MotionEvent event);

}
