package com.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * class contains the snake

 */
public class Snake {
// Code that intializes the stuff needed to create snake
// Check Stackoverflow to see what I am talking about
	private int tailAdd = 4; 
	private int tailStart = 1; 
	private byte xDirection = 0;
	private byte yDirection = 0;
	private int score = 0; 
	private int scoreMult = 30; 

	// All the body parts, the body part at index 0 is the head
	private ArrayList<SnakeBody> body;

	/**
	 * Constructor: create and initialize the snake

	 */
	public Snake(int gridWidth, int gridHeight, int cellSize) {
		score = 0;
		xDirection = 0;
		yDirection = 0;

		body = new ArrayList<SnakeBody>();

		int x = (gridWidth / 2) * cellSize;
		int y = (gridHeight / 2) * cellSize;
// How snake body looks like
		for (int i = 0; i < tailStart; i++) {
			body.add(new SnakeBody(x, y));
		}
	}

	/**
	 * restart from the initial tail length. This function is usually called when
	 * the snake hits itself
	 */
	private void restart() {
		score = 0;

		for (int i = body.size() - 1; i >= tailStart; i--)
			body.remove(i);
	}


	public void setxDirection(byte xDir) {
		if (xDirection != -xDir)
			this.xDirection = xDir;
		yDirection = 0;
	}


	public void setyDirection(byte yDir) {
		if (yDirection != -yDir)
			this.yDirection = yDir;
		xDirection = 0;
	}

	/*
	 add a bunch of new body parts at the end of the tail. They are added at the
	 same position of the last body part to make it look like it is actually
	 growing
	 */
	private void addToTail() {
		SnakeBody last = body.get(body.size() - 1);
		int x = last.getX();
		int y = last.getY();
// Is the fr loop that adds to the tail
		for (int i = 0; i < tailAdd; i++) {
			body.add(new SnakeBody(x, y));
		}
	}

//Body of snake after it eats foood
	private boolean ateFood(SnakeFood food) {
		SnakeBody head = body.get(0);
		return (head.getX() == food.getX() && head.getY() == food.getY());
	}


	private boolean hitTail() {
		SnakeBody head = body.get(0);
		boolean hit = false;

		int i = 1;
		while (!hit && i < body.size()) {
			SnakeBody sb = body.get(i);
			hit = (head.getX() == sb.getX() && head.getY() == sb.getY());
			i++;
		}

		return hit;
	}

// How snake moves within/across the screen
	private void move(int width, int height, int cellSize) {

		SnakeBody head = body.get(0);

		int x = head.getX() + cellSize * xDirection;
		int y = head.getY() + cellSize * yDirection;

		// determine the position of the new head
		SnakeBody newHead = new SnakeBody(x < 0 ? width + x : x % width, y < 0 ? height + y : y % height);

		body.remove(body.size() - 1);
		body.add(0, newHead);

	}


	public ArrayList<SnakeBody> getBody() {
		return body;
	}

//Creates visual represenation of snake
	public void draw(Graphics g, int cellSize) {
		for (SnakeBody bodyPart : body) {
			bodyPart.draw(g, cellSize);
		}

		int txtSize = 30;
		g.setColor(Color.BLACK);
		g.setFont(new Font("Times New Roman", Font.BOLD, txtSize));
		g.drawString("Score: " + score, 30, txtSize);
	}

// updates snake after it eats something and after it hits its tail
	public boolean update(SnakeFood food, int gridWidth, int gridHeight, int width, int height, int cellSize) {
		move(width, height, cellSize);

		if (hitTail())
			restart();

		if (ateFood(food)) {
			addToTail();
			score += tailAdd * scoreMult;

			return true;
		}

		return false;
	}
}