package com.monaco.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MazeRenderer {
    private Maze maze;
    private Texture wallTexture;
    private int tileSize;

    // Constructor: Pass in the generated maze and the desired tile size.
    public MazeRenderer(Maze maze, int tileSize) {
        this.maze = maze;
        this.tileSize = tileSize;
        wallTexture = new Texture("wall.png"); // Ensure "wall.png" exists in your assets
    }

    // Render the maze by drawing a wall for every cell with a value of 1.
    public void render(SpriteBatch batch) {
        for (int row = 0; row < maze.rows; row++) {
            for (int col = 0; col < maze.cols; col++) {
                if (maze.grid[row][col] == 1) {
                    batch.draw(wallTexture, col * tileSize, row * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    public void dispose() {
        wallTexture.dispose();
    }
}
