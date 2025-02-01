package com.monaco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

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
    private int id;

    private static final int WIDTH = 24;
    private static final int HEIGHT = 24;

    private Rectangle bounds;  // Used for collision detection

    public Player(int startX, int startY, int id) {
        this.x = startX;
        this.y = startY;
        this.id = id;
        this.texture = new Texture("player.jpg");
        this.bounds = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void move() {
        int currentSpeed = sprinting ? SPRINT_SPEED : NORMAL_SPEED;

        // Apply movement and prevent the player from moving off-screen
        if (up && y + HEIGHT < Gdx.graphics.getHeight()) y += currentSpeed;
        if (down && y > 0) y -= currentSpeed;
        if (left && x > 0) x -= currentSpeed;
        if (right && x + WIDTH < Gdx.graphics.getWidth()) x += currentSpeed;

        // Update bounds after movement for collision checks
        bounds.setPosition(x, y);
    }

    public void update(float deltaTime) {
        if (sprinting) {
            sprintTime += deltaTime;
            if (sprintTime >= SPRINT_DURATION) {
                sprinting = false;
                sprintAvailable = false;
                sprintTime = 0;
            }
        } else if (!sprintAvailable) {
            rechargeTime += deltaTime;
            if (rechargeTime >= RECHARGE_TIME) {
                sprintAvailable = true;
                rechargeTime = 0;
            }
        }

        move();
    }

    public void handleKeyPress(int keycode) {
        if (id == 1) {
            if (keycode == Input.Keys.UP) up = true;
            if (keycode == Input.Keys.DOWN) down = true;
            if (keycode == Input.Keys.LEFT) left = true;
            if (keycode == Input.Keys.RIGHT) right = true;
            if (keycode == Input.Keys.I && sprintAvailable) sprinting = true;
        } else if (id == 2) {
            if (keycode == Input.Keys.W) up = true;
            if (keycode == Input.Keys.S) down = true;
            if (keycode == Input.Keys.A) left = true;
            if (keycode == Input.Keys.D) right = true;
            if (keycode == Input.Keys.U && sprintAvailable) sprinting = true;
        }
    }

    public void handleKeyRelease(int keycode) {
        if (id == 1) {
            if (keycode == Input.Keys.UP) up = false;
            if (keycode == Input.Keys.DOWN) down = false;
            if (keycode == Input.Keys.LEFT) left = false;
            if (keycode == Input.Keys.RIGHT) right = false;
            if (keycode == Input.Keys.I) sprinting = false;
        } else if (id == 2) {
            if (keycode == Input.Keys.W) up = false;
            if (keycode == Input.Keys.S) down = false;
            if (keycode == Input.Keys.A) left = false;
            if (keycode == Input.Keys.D) right = false;
            if (keycode == Input.Keys.U) sprinting = false;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, WIDTH, HEIGHT);
    }

    public void dispose() {
        texture.dispose();
    }

    // Getter for player bounds (used for collision checks)
    public Rectangle getBounds() {
        return bounds;
    }

    // Collision with another player
    public boolean checkCollision(Player otherPlayer) {
        return this.bounds.overlaps(otherPlayer.getBounds());
    }

    // Collision handling - if there's a collision, stop movement
    public void handleCollision(Player otherPlayer) {
        if (this.checkCollision(otherPlayer)) {
            // If the players collide, stop their movement by unsetting directions
            if (this.id == 1) {
                // Stop player 1's movement
                up = down = left = right = false;
            } else if (this.id == 2) {
                // Stop player 2's movement
                up = down = left = right = false;
            }
        }
    }

    public void stopMovement() {
        up = down = left = right = false; // Stop all movement
    }
}
