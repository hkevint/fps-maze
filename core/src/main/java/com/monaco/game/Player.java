package com.monaco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    private int x, y;
    private boolean up, down, left, right, sprinting, sprintAvailable = true;
    private static final int NORMAL_SPEED = 3;
    private static final int SPRINT_SPEED = 5;
    private static final float SPRINT_DURATION = 3.0f;
    private static final float RECHARGE_TIME = 3.0f;

    private float sprintTime = 0;
    private float rechargeTime = 0;

    private Texture texture;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.texture = new Texture("player.jpg");
    }

    public void move() {
        int currentSpeed = sprinting ? SPRINT_SPEED : NORMAL_SPEED;

        // Apply movement
        if (up) y += currentSpeed;
        if (down) y -= currentSpeed;
        if (left) x -= currentSpeed;
        if (right) x += currentSpeed;
    }

    public void update(float deltaTime) {
        if (sprinting) {
            sprintTime += deltaTime;
            if (sprintTime >= SPRINT_DURATION) {
                sprinting = false;
                sprintAvailable = false;
                sprintTime = 0;
            }
        }

        else if (!sprintAvailable) {
            rechargeTime += deltaTime;
            if (rechargeTime >= RECHARGE_TIME) {
                sprintAvailable = true;
                rechargeTime = 0;
            }
        }

        move();
    }

    public void handleKeyPress(int keycode) {
        if (keycode == Input.Keys.W) up = true;
        if (keycode == Input.Keys.S) down = true;
        if (keycode == Input.Keys.A) left = true;
        if (keycode == Input.Keys.D) right = true;
        if (keycode == Input.Keys.U && sprintAvailable) sprinting = true;
    }

    public void handleKeyRelease(int keycode) {
        if (keycode == Input.Keys.W) up = false;
        if (keycode == Input.Keys.S) down = false;
        if (keycode == Input.Keys.A) left = false;
        if (keycode == Input.Keys.D) right = false;
        if (keycode == Input.Keys.U) sprinting = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, 24, 24);
    }

    public void dispose() {
        texture.dispose();
    }
}
