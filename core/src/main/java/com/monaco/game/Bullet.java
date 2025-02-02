package com.monaco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
// Bullet(1,1,

public class Bullet {
    // Position (can be used as a prototype reference)
    public float x, y;
    // Velocity direction (for when the bullet is fired)
    public float dx, dy;
    // Bullet attributes
    public float speed;    // How fast the bullet travels
    public int damage;     // How much damage it deals
    public Texture texture;

    // A flag to indicate if this bullet is “active” (in flight)
    public boolean active = false;

    public Bullet(float speed, int damage) {
        this.speed = speed;
        this.damage = damage;
        this.texture = new Texture("bullet.png");
    }

    // This method updates the bullet's position when it is fired.
    public void update(float deltaTime) {
        if(active) {
            x += dx * speed * deltaTime;
            y += dy * speed * deltaTime;

            if (x > Gdx.graphics.getWidth() || x < 0 ||
                y > Gdx.graphics.getHeight() || y < 0) {
                active = false;
            }
        }
    }

    public void render(SpriteBatch batch) {
        if(active) {
            batch.draw(texture, x, y);
        }
    }

    public void dispose() {
        texture.dispose();
    }
}
