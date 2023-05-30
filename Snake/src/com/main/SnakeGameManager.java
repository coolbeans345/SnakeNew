package com.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

/**
 * manage all the game objects, drawing and physics/movement
 */
public class SnakeGameManager extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	private static final int CELL_SIZE = 25; // pixels

	private final static int GRID_WIDTH = 50; // cells
	private final static int GRID_HEIGHT = 50; // cells

	private final static int WIDTH = CELL_SIZE * GRID_WIDTH; // width of the screen in pixels
	private final static int HEIGHT = CELL_SIZE * GRID_HEIGHT; // height of the screen in pixels

	private boolean running = false; // true if the game is running
	private boolean paused = false; // updating or not
	private Thread gameThread; // thread where the game is updated AND drawn (single thread game)

	// the actual snake
	private Snake snake;

	// the food
	private SnakeFood food;

	 // Constructor: Create and initialize the JFrame and the canvas
	public SnakeGameManager() {

		canvasSetup();
		initialize();

		newWindow();

		 /* add something that will detect events on the keyboard like key presses and
		  key releases. That thing is called a keyListener
		  */
		this.addKeyListener(new KeyAdapter() {

			
		// What to do when certain keys are pressed
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();

				if (key == KeyEvent.VK_UP) { // up array key presses
					snake.setyDirection((byte) -1);
				} else if (key == KeyEvent.VK_DOWN) { // down array key presses
					snake.setyDirection((byte) 1);
				} else if (key == KeyEvent.VK_RIGHT) { // right array key presses
					snake.setxDirection((byte) 1);
				} else if (key == KeyEvent.VK_LEFT) { // left array key presses
					snake.setxDirection((byte) -1);
				} else if (key == KeyEvent.VK_SPACE) {
					paused = !paused;
				}
			}
		});

		this.setFocusable(true);
	}

	/**
	  Create the window where our Canvas will go (this class inherits from Canvas)
	  The window will be a JFrame
	  https://docs.oracle.com/javase/8/docs/api/java/awt/Frame.html
	 */
	private void newWindow() {
		JFrame frame = new JFrame("Snake");
		
		frame.setBackground(Color.RED);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		

		start(); // start the thread
	}

	/**
	  initialize all our game objects
	 */
	private void initialize() {
		// Initialize snake object
		snake = new Snake(GRID_WIDTH, GRID_HEIGHT, CELL_SIZE);

		// Initialize food object
		food = new SnakeFood(snake.getBody(), GRID_WIDTH, GRID_HEIGHT, CELL_SIZE);
	}

	/**
	  just to setup the canvas to our desired settings and sizes
	 */
	private void canvasSetup() {
		this.setBackground(Color.RED);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
	}

	/**
	  Game loop
	  meat of our game
	 */
	
	public void run() {
		//Game loop code: Look at stackoverflow on what it does
		this.requestFocus();
		final double MAX_FRAMES_PER_SECOND = 30.0;
		final double MAX_UPDATES_PER_SECOND = 15.0;
		long startTime = System.nanoTime();
		final double uOptimalTime = 1000000000 / MAX_UPDATES_PER_SECOND;
		final double fOptimalTime = 1000000000 / MAX_FRAMES_PER_SECOND;
		double uDeltaTime = 0, fDeltaTime = 0;
		int frames = 0, updates = 0;
		long timer = System.currentTimeMillis();
		while (running) {
			long currentTime = System.nanoTime();
			uDeltaTime += (currentTime - startTime) / uOptimalTime;
			fDeltaTime += (currentTime - startTime) / fOptimalTime;
			startTime = currentTime;
			if (uDeltaTime >= 1) {
				update();
				updates++;
				uDeltaTime--;
			}

			if (fDeltaTime >= 1) {
				render();
				frames++;
				fDeltaTime--;
			}
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("UPS: " + updates + ", FPS: " + frames);
				frames = 0;
				updates = 0;
				timer += 1000;
			}
		}

		stop(); // stop the thread 
	}

	/**
	  start the thread and the game
	 */
	public synchronized void start() {
		gameThread = new Thread(this);
		/*
		  since "this" is the "GameManager" Class you are in right now and it
		  implements the Runnable Interface we can give it to a thread constructor.
		 */
		gameThread.start(); // start thread
		running = true;
	}

	/**
	  Stop the thread
	 */
	public void stop() {
		try {
			gameThread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * renders the background and all  objects (using buffers)
	 */
	public void render() {
		// Initialize drawing tools first before drawing

		BufferStrategy buffer = this.getBufferStrategy(); // extract buffer so we can use them
		// a buffer is basically like a blank canvas we can draw on

		if (buffer == null) { // if it does not exist, we can't draw! So create it please
			this.createBufferStrategy(3); // Creating a Triple Buffer
			/*
			 * BufferStrategy:
			 * https://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferStrategy.html
			 */

			return;
		}

		Graphics g = buffer.getDrawGraphics(); 
/*
		// Graphics is class used to draw rectangles, ovals and all sorts of shapes and
		 // pictures so it's a tool used to draw on a buffer
		// Graphics: https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html
		 */

		drawBackground(g);

		snake.draw(g, CELL_SIZE);

		food.draw(g, CELL_SIZE);

		buffer.show();

	}

	// Renders what the background will be like
	private void drawBackground(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}

	
	public void update() {
		if (paused)
			return;

		if (snake.update(food, GRID_WIDTH, GRID_HEIGHT, WIDTH, HEIGHT, CELL_SIZE))
			food = new SnakeFood(snake.getBody(), GRID_WIDTH, GRID_HEIGHT, CELL_SIZE);

	}


	public static void main(String[] args) {
		new SnakeGameManager();
	}
}
