package com.monaco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    private int x, y;
    private int speed = 5;
    private boolean up, down, left, right;
    private long lastPressUp, lastPressDown, lastPressLeft, lastPressRight;
    private static final int NORMAL_SPEED = 5;
    private static final int SPRINT_SPEED = 10;
    private static final long DOUBLE_PRESS_TIME = 300; // 300ms for double press detection

    private Texture texture;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.texture = new Texture("player.jpg");
    }

    public void move() {
        int currentSpeed = speed;

        // Sprinting Logic
        if (up && System.currentTimeMillis() - lastPressUp < DOUBLE_PRESS_TIME) {
            currentSpeed = SPRINT_SPEED;
        }
        if (down && System.currentTimeMillis() - lastPressDown < DOUBLE_PRESS_TIME) {
            currentSpeed = SPRINT_SPEED;
        }
        if (left && System.currentTimeMillis() - lastPressLeft < DOUBLE_PRESS_TIME) {
            currentSpeed = SPRINT_SPEED;
        }
        if (right && System.currentTimeMillis() - lastPressRight < DOUBLE_PRESS_TIME) {
            currentSpeed = SPRINT_SPEED;
        }

        // Apply movement
        if (up) y += currentSpeed;
        if (down) y -= currentSpeed;
        if (left) x -= currentSpeed;
        if (right) x += currentSpeed;
    }

    public void handleKeyPress(int keycode) {
        if (keycode == Input.Keys.W) {
            up = true;
            lastPressUp = System.currentTimeMillis();
        }
        if (keycode == Input.Keys.S) {
            down = true;
            lastPressDown = System.currentTimeMillis();
        }
        if (keycode == Input.Keys.A) {
            left = true;
            lastPressLeft = System.currentTimeMillis();
        }
        if (keycode == Input.Keys.D) {
            right = true;
            lastPressRight = System.currentTimeMillis();
        }
    }

    public void handleKeyRelease(int keycode) {
        if (keycode == Input.Keys.W) up = false;
        if (keycode == Input.Keys.S) down = false;
        if (keycode == Input.Keys.A) left = false;
        if (keycode == Input.Keys.D) right = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, 24, 24);
    }

    public void dispose() {
        texture.dispose();
    }
}
