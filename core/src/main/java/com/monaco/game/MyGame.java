package com.monaco.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private Player player1;
    private Player player2;
    private BitmapFont font; // For rendering text
    private boolean inMenu; // Tracks whether we're in the menu state
    private Viewport viewport; // Handles screen resizing

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        player1 = new Player(100, 100, 1);
        player2 = new Player(50, 100, 2);
        font = new BitmapFont(); // Load the default font
        inMenu = true; // Start in the menu state

        viewport = new ScreenViewport();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (inMenu) {
                    // Press ENTER to start the game.
                    if (keycode == Input.Keys.ENTER) {
                        inMenu = false;
                    }
                } else {
                    player1.handleKeyPress(keycode);
                    player2.handleKeyPress(keycode);
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (!inMenu) {
                    player1.handleKeyRelease(keycode);
                    player2.handleKeyRelease(keycode);
                }
                return true;
            }
        });
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        if (inMenu) {
            batch.begin();
            String text = "Press ENTER to Start";
            float textWidth = font.getLineHeight() * text.length();
            float x = (viewport.getWorldWidth() - textWidth) / 2;
            float y = 50;
            font.draw(batch, text, x, y);
            batch.end();
        } else {
            player1.update(deltaTime);
            player2.update(deltaTime);

            batch.begin();
            player1.render(batch);
            player2.render(batch);
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        player1.dispose();
        player2.dispose();
        font.dispose();
    }
}
