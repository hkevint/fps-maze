package com.monaco.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private final float SPEED = 500f;
    private Vector2 position;
    private Vector2 velocity;
    private Texture texture;

    public Bullet(float position_x, float position_y, float angle) {
        position = new Vector2(position_x, position_y);
        velocity = new Vector2(SPEED * (float)Math.cos(angle), SPEED * (float)Math.sin(angle));
        texture = new Texture("Bullet.png");
    }

    public void update(float deltaT) {
        position.add(velocity.x * deltaT, velocity.y * deltaT);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    public void setVelocity(float v_x, float v_y) {
        this.velocity.x = v_x;
        this.velocity.y = v_y;
    }

    public void dispose() {
        texture.dispose();
    }
}

