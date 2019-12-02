package com.luckysvipe.orange;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class GameplayScene implements Scene {
    private Rect r = new Rect(); // we will not really use it, just to draw a textbox after the gameover

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private boolean movingPlayer = false;

    private boolean gameOver = false;
    private long gameOverTime;

    public GameplayScene() {
        player = new RectPlayer(new Rect(150, 150, 250, 250), Color.rgb(255, 0,0));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200, 350, 75, Color.parseColor("#2C092E"));
    }

    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200, 350, 75, Color.parseColor("#2C092E"));

        movingPlayer = false;
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && player.getRectangle().contains((int)event.getX(), (int)event.getY())) {
                    //if we tap on the screen and it contains the rectange of the player, then move the player
                    movingPlayer = true;
                }
                if(gameOver && System.currentTimeMillis() - gameOverTime >=2000) {
                    //if 2 seconds passed since the game over, we can continue playing
                    reset(); //resetting our game
                    gameOver = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(movingPlayer && !gameOver) {
                    playerPoint.set((int) event.getX(), (int) event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;

        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#FCBD00"));

        player.draw(canvas);
        obstacleManager.draw(canvas);

        if(gameOver) {
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "Game Over");
        }
    }

    @Override
    public void update() {
        if(!gameOver) {
            player.update(playerPoint);
            obstacleManager.update();
            //check if player has collided with an obstacle
            if(obstacleManager.playerCollide(player)) {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}
