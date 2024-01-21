// Import necessary packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

// SnakeGame class that extends JPanel and implements ActionListener
public class SnakeGame extends JPanel implements ActionListener {
    // Constants for the game window size, unit size, game units, and delay
    private static final int WIDTH = 960;
    private static final int HEIGHT = 540;
    private static final int UNIT_SIZE = 10;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 75;

    // Arrays to store the x and y coordinates of the snake's body parts
    private final int[] x = new int[GAME_UNITS];
    private final int[] y = new int[GAME_UNITS];

    // Initial number of body parts, apples eaten, and initial direction
    private int bodyParts = 6;
    private int applesEaten = 0;
    private int appleX;
    private int appleY;
    private char direction = 'R'; // R for right

    // Game state variables
    private boolean running = false;
    private Timer timer;
    private String username;

    // Score-related variables
    private int score = 0;
    private int highScore = 1020; // Initial high score
    private boolean gamePaused = false;
    private boolean gameOver = false;

    // Constructor that initializes the game with a specified username
    public SnakeGame(String username) {
        this.username = username;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        setFocusable(true); // Ensure the panel receives focus
        requestFocusInWindow();
        startGame();
    }

    // Method to start the game
    public void startGame() {
        newApple(); // Place a new apple on the game board
        running = true; // Set the game to running state
        timer = new Timer(DELAY, this); // Create a timer for game updates
        timer.start(); // Start the timer
    }

    // ActionListener method for handling game events
    public void actionPerformed(ActionEvent e) {
        if (running && !gamePaused) {
            move(); // Move the snake
            checkApple(); // Check if the snake has eaten an apple
            checkCollision(); // Check for collisions with the walls or itself
        }
        repaint(); // Repaint the game board
    }

    // Method to paint the game components on the panel
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g); // Draw the game components
    }

    // Method to draw the game components on the panel
    public void draw(Graphics g) {
        if (running) {
            // Draw the apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Draw the snake body
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green); // Head of the snake
                } else {
                    g.setColor(new Color(45, 180, 0)); // Body parts
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Draw the score information
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 20);
            g.drawString("High Score: " + highScore, 200, 20);
        } else if (gameOver) {
            // Draw the game over message and final score
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            g.drawString("Your Score: " + score, (WIDTH - metrics.stringWidth("Your Score: " + score)) / 2, HEIGHT / 2 + 40);
        }
    }

    // Method to generate a new random apple on the game board
    public void newApple() {
        appleX = new Random().nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = new Random().nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    // Method to move the snake based on its current direction
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        // Move the head of the snake based on the current direction
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

    // Method to check if the snake has eaten an apple
    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            score += 10;
            newApple(); // Place a new apple on the game board
        }
    }

    // Method to check for collisions with walls or itself
    public void checkCollision() {
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false; // Game over if the head collides with the body
            }
        }

        // Game over if the head hits the walls
        if (x[0] >= WIDTH || x[0] < 0 || y[0] >= HEIGHT || y[0] < 0) {
            running = false;
        }

        // If the game is over, stop the timer, update the high score, and set game over flag
        if (!running) {
            timer.stop();
            if (score > highScore) {
                highScore = score;
            }
            gameOver = true;
        }
    }

    // Inner class for handling keyboard input
    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!gameOver) {
                // Update direction based on arrow keys and handle pause with escape key
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') {
                            direction = 'L';
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') {
                            direction = 'R';
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != 'D') {
                            direction = 'U';
                        }
                        break;
                    case KeyEvent.VK_DOWN

        //github @ astro-rna
