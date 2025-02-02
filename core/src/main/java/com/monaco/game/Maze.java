package com.monaco.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Maze {
    public int[][] grid;
    public int rows, cols;
    private Random rand;

    // Constructor: Use odd numbers for rows and cols for a proper maze layout.
    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new int[rows][cols];
        rand = new Random();

        // Fill the grid with walls (1s)
        for (int i = 0; i < rows; i++) {
            Arrays.fill(grid[i], 1);
        }

        // Start generating maze from (1,1)
        generateMaze(1, 1);
    }

    // Recursive backtracking algorithm
    private void generateMaze(int r, int c) {
        grid[r][c] = 0; // Mark cell as open

        // Define the possible directions (2 cells away)
        List<int[]> directions = Arrays.asList(
            new int[]{0, 2},
            new int[]{0, -2},
            new int[]{2, 0},
            new int[]{-2, 0}
        );
        // Shuffle to ensure randomness
        Collections.shuffle(directions, rand);

        for (int[] d : directions) {
            int nr = r + d[0];
            int nc = c + d[1];
            // Check if the new cell is within bounds and still a wall
            if (nr > 0 && nr < rows && nc > 0 && nc < cols && grid[nr][nc] == 1) {
                // Carve a passage between the current cell and the new cell
                grid[r + d[0] / 2][c + d[1] / 2] = 0;
                generateMaze(nr, nc);
            }
        }
    }
}
