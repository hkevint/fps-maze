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
    private Player player1;
    private Player player2;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        player1 = new Player(100, 100, 1);
        player2 = new Player(50, 100, 2);


        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean keyDown(int keycode){
                player1.handleKeyPress(keycode);
                player2.handleKeyPress(keycode);
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                player1.handleKeyRelease(keycode);
                player2.handleKeyRelease(keycode);
                return true;
            }
        });

    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player1.update(deltaTime);
        player2.update(deltaTime);

        batch.begin();
        player1.render(batch);
        player2.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        player1.dispose();
        player2.dispose();
    }
}
