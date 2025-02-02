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
    private boolean inMenu; // Tracks whether we're in the start menu state
    private boolean isPaused; // Tracks whether the game is paused
    private int selectedOption; // Tracks the selected option in the pause menu (0: RESUME, 1: EXIT)
    private Viewport viewport; // Handles screen resizing

    private Maze maze;
    private MazeRenderer mazeRenderer;
    private int tileSize = 32;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        player1 = new Player(100, 100, 1);
        player2 = new Player(50, 100, 2);
        font = new BitmapFont(); // Load the default font
        inMenu = true; // Start in the start menu state
        isPaused = false; // Game is not paused initially
        selectedOption = 0; // Default to RESUME option


        // Create a ScreenViewport to handle screen resizing
        viewport = new ScreenViewport();

        maze = new Maze(21, 21);
        mazeRenderer = new MazeRenderer(maze, tileSize);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (inMenu) {
                    // Check for Enter key press to start the game
                    if (keycode == Input.Keys.ENTER) {
                        inMenu = false; // Exit the start menu
                    }
                } else if (isPaused) {
                    // Handle pause menu navigation
                    if (keycode == Input.Keys.UP) {
                        selectedOption = 0; // Select RESUME
                    } else if (keycode == Input.Keys.DOWN) {
                        selectedOption = 1; // Select EXIT
                    } else if (keycode == Input.Keys.ENTER) {
                        if (selectedOption == 0) {
                            isPaused = false; // Resume the game
                        } else if (selectedOption == 1) {
                            Gdx.app.exit(); // Exit the game
                        }
                    }
                } else {
                    // Handle player input if not in the menu or paused
                    if (keycode == Input.Keys.ESCAPE) {
                        isPaused = true; // Pause the game
                    } else {
                        player1.handleKeyPress(keycode);
                        player2.handleKeyPress(keycode);
                    }
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (!inMenu && !isPaused) {
                    // Handle player input if not in the menu or paused
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
        } else if (isPaused) {
            // Render the pause menu
            renderPauseMenu();
        } else {
            // Render the main game
            player1.update(deltaTime);
            player2.update(deltaTime);

            batch.begin();
            mazeRenderer.render(batch);
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

    private void renderPauseMenu() {
        batch.begin();

        // Render a semi-transparent black background
        batch.setColor(0, 0, 0, 0.5f); // Semi-transparent black
        batch.draw(image, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.setColor(1, 1, 1, 1); // Reset color

        // Display "RESUME" and "EXIT" options
        String resumeText = "RESUME";
        String exitText = "EXIT";
        float textWidth = font.getLineHeight() * Math.max(resumeText.length(), exitText.length());
        float x = (viewport.getWorldWidth() - textWidth) / 2; // Center horizontally
        float y = viewport.getWorldHeight() / 2; // Center vertically

        // Highlight the selected option
        if (selectedOption == 0) {
            font.setColor(1, 1, 0, 1); // Yellow for selected option
        } else {
            font.setColor(1, 1, 1, 1); // White for unselected option
        }
        font.draw(batch, resumeText, x, y + 20);

        if (selectedOption == 1) {
            font.setColor(1, 1, 0, 1); // Yellow for selected option
        } else {
            font.setColor(1, 1, 1, 1); // White for unselected option
        }
        font.draw(batch, exitText, x, y - 20);

        batch.end();
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
        mazeRenderer.dispose();
        player1.dispose();
        player2.dispose();
        font.dispose(); // Dispose of the font
    }
}
