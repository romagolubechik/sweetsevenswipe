package com.luckysvipe.orange;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

    // the class which is going to be the meat of our game
    public static final int MAX_FPS = 30;
    public double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public void setRunning(boolean running) {
        this.running = running;
    }

    // now we are going to create our constructor
    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {

        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {

                    // these two methods are update and draw are the meat of our game:
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // even if an error has occurred, it will still call the finally block
                if (canvas!=null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {e.printStackTrace();}
                }

            }

            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;
            try {
                if (waitTime>0) {
                    // i.e., if we finished te frame earlier than the target time
                    this.sleep(waitTime);
                }

            } catch (Exception e) {e.printStackTrace();} // basically, keeping the frame

            totalTime+= System.nanoTime() - startTime;
            frameCount++;

            if (frameCount == MAX_FPS) {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }
    }
}
