package com.monaco.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MyGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private Player player;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        player = new Player(100, 100);

        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean keyDown(int keycode){
                player.handleKeyPress(keycode);
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                player.handleKeyRelease(keycode);
                return true;
            }
        });

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.move(); // Updates movement
        batch.begin();
        player.render(batch); // Draws the player
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        player.dispose();
    }
}
