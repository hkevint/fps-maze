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

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
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

        // Create a ScreenViewport to handle screen resizing
        viewport = new ScreenViewport();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (inMenu) {
                    // Check for Enter key press to start the game
                    if (keycode == Input.Keys.ENTER) {
                        inMenu = false; // Exit the menu
                    }
                } else {
                    // Handle player input if not in the menu
                    player1.handleKeyPress(keycode);
                    player2.handleKeyPress(keycode);
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (!inMenu) {
                    // Handle player input if not in the menu
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

        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the viewport and apply it
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        if (inMenu) {
            // Render the start menu
            renderMenu();
        } else {
            // Update players' positions
            player1.update(deltaTime);
            player2.update(deltaTime);

            // Check for collisions between players
            if (checkCollision(player1, player2)) {
                handleCollision(player1, player2); // Handle the collision
            }

            batch.begin();
            player1.render(batch);
            player2.render(batch);
            batch.end();
        }
    }

    private void renderMenu() {
        batch.begin();

        // Display "Start" text at the bottom of the screen
        String text = "Press ENTER to Start";
        float textWidth = font.getLineHeight() * text.length(); // Approximate text width
        float x = (viewport.getWorldWidth() - textWidth) / 2; // Center horizontally
        float y = 50; // Position at the bottom of the screen
        font.draw(batch, text, x, y);

        batch.end();
    }

    // Collision detection between two players
    private boolean checkCollision(Player player1, Player player2) {
        // Simple collision check by comparing bounding boxes
        return player1.getBounds().overlaps(player2.getBounds());
    }

    // Handle collision between two players (e.g., stop movement, bounce back)
    private void handleCollision(Player player1, Player player2) {
        // For now, just stop both players from moving when they collide
        player1.stopMovement();
        player2.stopMovement();
    }

    @Override
    public void resize(int width, int height) {
        // Update the viewport when the screen is resized
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        player1.dispose();
        player2.dispose();
        font.dispose(); // Dispose of the font
    }
}
