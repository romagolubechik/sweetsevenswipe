package com.luckysvipe.orange;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class ObstacleManager {
    // higher index == lower on screen == higher y value
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    private long startTime;
    private long initTime; //time before we initialize each class

    private int score = 0;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();

        populateObstacles();
    }

    public boolean playerCollide(RectPlayer player) {
        for (Obstacle ob: obstacles) {
            if(ob.playerCollide(player) ) {
                return true;
            }
        }
        return false;
    }

    private void populateObstacles() {
        int currY = -5*Constants.SCREEN_HEIGHT/4; //it is int, so it should go in this sequence 5 *.../4;
        //while (obstacles.get(obstacles.size() - 1).getRectangle().bottom < 0) {
        while (currY < 0) {
            //i.e., while the last obstacle has not appeared on the screen keep generating them,
            //while the walls have not gone into the screen yet
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            // after we generated our starting position of x:
            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap));
            currY += obstacleHeight + obstacleGap;
        }
    }

    public void update() {
        // we want to add new obstacles based on time, but not on frame dependance
        int elapsedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();

        //scaling speed: how far the obstacle moves over time
        float speed = (float)(Math.sqrt(1 + (startTime - initTime)/2000.0) * Constants.SCREEN_HEIGHT/10000.0f);
        //10 sec to move obstacle down the whole screen * for the factor which increases speed with time

        for (Obstacle ob : obstacles) {
            ob.incrementY(speed * elapsedTime);
        }

        // quick check:
        if (obstacles.get(obstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart, obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap, playerGap));
            obstacles.remove(obstacles.size() -1);

            //scoring
            score++;

        }
    }

    public void draw(Canvas canvas) {
        for(Obstacle ob : obstacles) {
            ob.draw(canvas);
        }
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.GREEN);
        canvas.drawText("" + score, 50, 50 + paint.descent() - paint.ascent(), paint);
    }
}
