package com.main;

import java.awt.Color;
import java.awt.Graphics;


public class SnakeBody {

	private int x, y; // coordinates on the canvas


	public SnakeBody(int x, int y) {
		this.x = x;
		this.y = y;
	}


	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}


	public void draw(Graphics g, int cellSize) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, cellSize, cellSize);

		g.setColor(Color.ORANGE);
		g.drawRect(x, y, cellSize, cellSize);
	}

}