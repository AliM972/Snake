import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
	
	// Constants for game settings
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75;
	
	// Arrays to hold the coordinates of the snake's parts
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	
	// Game variables
	int bodyParts = 6;
	int applesEaten = 0;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running  = false;
	Color snakeColor = new Color(45,180,0);
	Timer timer;
	Random random;
	
	// Constructor
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // Set the size of the panel
		this.setBackground(Color.black); // Set the background color
		this.setFocusable(true); // Make the panel focusable to receive key inputs
		this.addKeyListener(new MyKeyAdapter()); // Add the key listener for controlling the snake
		startGame(); // Start the game
	}
	
	// Start the game
	public void startGame() {
		spawnApple(); // Spawn the first apple
		running = true; // Set game status to running
		timer = new Timer(DELAY, this); // Create a new timer to control game speed
		timer.start(); // Start the timer
	}
	
	// Method to paint the component
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Call the superclass method
		draw(g); // Draw game components
	}
	
	// Draw the game components
	public void draw(Graphics g) {
		if(running) {
			// Draw the grid
			for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			
			// Draw the apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			// Draw the snake
			for(int i = 0; i < bodyParts; i++) {
				if(i == 0) {
					g.setColor(Color.green); // Head of the snake
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(snakeColor); // Body of the snake
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			// Draw the score
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g); // Show game over screen
		}
	}
	
	// Spawn a new apple
	public void spawnApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
	}
	
	// Move the snake
	public void move() {
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1]; // Shift the body parts
			y[i] = y[i - 1];
		}
		
		// Change the direction of the head of the snake
		switch (direction) {
		case 'U':
			y[0] -= UNIT_SIZE;
			break;
		case 'D':
			y[0] += UNIT_SIZE;
			break;
		case 'L':
			x[0] -= UNIT_SIZE;
			break;
		case 'R':
			x[0] += UNIT_SIZE;
			break;
		}
	}
	
	// Check if the apple is eaten
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++; // Increase the size of the snake
			applesEaten++; // Increase the score
			snakeColor = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)); // Change snake color
			spawnApple(); // Spawn another apple
		}
	}
	
	// Check for collisions with the snake's body or the borders
	public void checkCollisions() {
		for(int i = bodyParts; i > 0; i--) {
			if((x[0] == x[i]) && y[0] == y[i]) {
				running = false; // Collision detected, end game
			}
		}
		if(x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
			running = false; // Collision with border detected, end game
		}
		if(!running) {
			timer.stop(); // Stop the game
		}
	}
	
	// Display the game over screen
	public void gameOver(Graphics g) {
		// Display final score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Final Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Final Score: "+applesEaten))/2, g.getFont().getSize());
		
		// Display game over message
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move(); // Move the snake
			checkApple(); // Check if the apple is eaten
			checkCollisions(); // Check for collisions
		}
		repaint(); // Repaint the panel
	}
	
	// Inner class to handle keyboard inputs
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L'; // Change direction to left
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R'; // Change direction to right
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U'; // Change direction to up
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D'; // Change direction to down
				}
				break;
			}
		}
	}
}
