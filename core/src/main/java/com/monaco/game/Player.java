package com.monaco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    private int x, y;
    private boolean up, down, left, right, sprinting, sprintAvailable = true;
    private static final int NORMAL_SPEED = 3;
    private static final int SPRINT_SPEED = 5;
    private static final float SPRINT_DURATION = 3.0f;
    private static final float RECHARGE_TIME = 3.0f;
    private static final int MAGAZINE_CAPACITY = 10; // Maximum bullets per magazine

    private float sprintTime = 0;
    private float rechargeTime = 0;

    private Texture texture;
    private int id;
    private Rectangle bounds;  // Used for collision detection

    private static final int WIDTH = 24;
    private static final int HEIGHT = 24;

    // For ammo management:
    private ArrayList<Bullet> ammo;
    private ArrayList<Bullet> activeBullets;

    // Fields to store the last movement direction.
    // We'll use a default of (1,0) so the player initially faces right.
    private float lastDirX = 1;
    private float lastDirY = 0;

    public Player(int startX, int startY, int id) {
        this.x = startX;
        this.y = startY;
        this.id = id;
        this.texture = new Texture("player.jpg");
        ammo = new ArrayList<Bullet>();
        activeBullets = new ArrayList<Bullet>();
        reload(); // Fill magazine at start
        this.bounds = new Rectangle(x, y, WIDTH, HEIGHT);

    }

    // Reload: fill the ammo list with new Bullet objects.
    public void reload() {
        ammo.clear();
        for (int i = 0; i < MAGAZINE_CAPACITY; i++) {
            ammo.add(new Bullet(500, 1));
        }
        System.out.println("Player " + id + " reloaded. Ammo count: " + ammo.size());
    }

    // Modified move() method that also updates the last movement direction.
    public void move() {
        int currentSpeed = sprinting ? SPRINT_SPEED : NORMAL_SPEED;
        int dx = 0, dy = 0;
        if (up)    dy += 1;
        if (down)  dy -= 1;
        if (left)  dx -= 1;
        if (right) dx += 1;

        // Update the last direction if there is any movement.
        if (dx != 0 || dy != 0) {
            lastDirX = dx;
            lastDirY = dy;
        }

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
        }

        else if (!sprintAvailable) {
            rechargeTime += deltaTime;
            if (rechargeTime >= RECHARGE_TIME) {
                sprintAvailable = true;
                rechargeTime = 0;
            }
        }

        // Shooting: if the shoot key is pressed and there is ammo.
        if (shouldShoot() && !ammo.isEmpty()){
            shoot();
        }

        // Reload if ammo is empty and the reload key (R) is pressed.
        if (Gdx.input.isKeyJustPressed(Input.Keys.R) && ammo.isEmpty()){
            reload();
        }

        // Update active bullets
        for (int i = 0; i < activeBullets.size(); i++) {
            Bullet b = activeBullets.get(i);
            b.update(deltaTime);
            if (!b.active) {
                activeBullets.remove(i);
                i--;
            }
        }

        move();
    }

    public void handleKeyPress(int keycode) {
        if (id == 1) {
            if (keycode == Input.Keys.UP)    up = true;
            if (keycode == Input.Keys.DOWN)  down = true;
            if (keycode == Input.Keys.LEFT)  left = true;
            if (keycode == Input.Keys.RIGHT) right = true;
            if (keycode == Input.Keys.I && sprintAvailable) sprinting = true;
        }

        else if (id == 2){
            if (keycode == Input.Keys.W) up = true;
            if (keycode == Input.Keys.S) down = true;
            if (keycode == Input.Keys.A) left = true;
            if (keycode == Input.Keys.D) right = true;
            if (keycode == Input.Keys.U && sprintAvailable) sprinting = true;
        }
    }

    public void handleKeyRelease(int keycode) {
        if (id == 1) {
            if (keycode == Input.Keys.UP)    up = false;
            if (keycode == Input.Keys.DOWN)  down = false;
            if (keycode == Input.Keys.LEFT)  left = false;
            if (keycode == Input.Keys.RIGHT) right = false;
            if (keycode == Input.Keys.I) sprinting = false;
        }

        else if (id == 2){
            if (keycode == Input.Keys.W) up = false;
            if (keycode == Input.Keys.S) down = false;
            if (keycode == Input.Keys.A) left = false;
            if (keycode == Input.Keys.D) right = false;
            if (keycode == Input.Keys.U) sprinting = false;
        }
    }

    // Determines if the player is trying to shoot.
    // For example, player 1 uses the O key; player 2 uses the Y key.
    private boolean shouldShoot() {
        if (id == 1 && Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            return true;
        } else if (id == 2 && Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            return true;
        }
        return false;
    }

    // Shoot: remove one bullet from the ammo list, set its properties (including direction),
    // and add it to activeBullets.
    private void shoot() {
        // Remove a bullet from the magazine.
        Bullet bulletToFire = ammo.remove(0);
        // Position it at the player's location.
        bulletToFire.x = x;
        bulletToFire.y = y;
        // Set its velocity based on the player's last movement direction.
        bulletToFire.dx = lastDirX;
        bulletToFire.dy = lastDirY;
        bulletToFire.active = true;
        // Add it to the list of active bullets.
        activeBullets.add(bulletToFire);
        System.out.println("Player " + id + " fired a bullet. Ammo left: " + ammo.size());
    }

    public void render(SpriteBatch batch) {
        // Draw the player.
        batch.draw(texture, x, y, 24, 24);
        // Render all active bullets.
        for (Bullet b : activeBullets) {
            b.render(batch);
        }
    }

    public void dispose() {
        texture.dispose();
        // Dispose all bullet textures from activeBullets and ammo.
        for (Bullet b : activeBullets) {
            b.dispose();
        }
        for (Bullet b : ammo) {
            b.dispose();
        }
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
