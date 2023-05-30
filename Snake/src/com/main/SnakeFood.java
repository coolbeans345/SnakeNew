//All packages imported
// Ctrl + Shift + O


package com.main;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class SnakeFood {

	private int x, y; // coordinates on the canvas
	private Color color; // color of the food

//Creates the food and arraylist in order for the code to work
	public SnakeFood(ArrayList<SnakeBody> body, int gridWidth, int gridHeight, int cellSize) {

		boolean same = true;
		x = y = -1;

		while (same) {
			x = ((int) (gridWidth * Math.random())) * cellSize;
			y = ((int) (gridHeight * Math.random())) * cellSize;

			int i = 0;
			same = false;
			while (!same && i < body.size()) {
				same = (body.get(i).getX() == x && body.get(i).getY() == y);
				i++;
			}
		}

		color = randomColor();

	}

	// get a random color for the pixel that the snake eats
	 
	private Color randomColor() {
		double random = Math.random();

		if (random < 0.30)
			return Color.YELLOW;
		else if (random < 0.60)
			return Color.BLACK;
		else if (random < 0.95)
			return Color.RED;
		else
			return Color.GREEN;
	}


	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}


//Draws the food and other graphics needed in order for code to work
	public void draw(Graphics g, int cellSize) {
		g.setColor(color);
		g.fillRect(x, y, cellSize, cellSize);
	}
}