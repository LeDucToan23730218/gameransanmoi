package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

class SwingSnakeGame extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 100;

    private final ArrayList<Point> snake;
    private Point apple;
    private Point obstacle;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;
    private int applesEaten;
    private int level = 1;

    public SwingSnakeGame() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(this);
        snake = new ArrayList<>();
        startGame();
    }

    public void startGame() {
        snake.clear();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));
        applesEaten = 0;
        level = 1;
        direction = 'R';
        running = true;
        newApple();
        newObstacle();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // Draw snake
            for (int i = 0; i < snake.size(); i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(snake.get(i).x, snake.get(i).y, UNIT_SIZE, UNIT_SIZE);
            }

            // Draw apple
            g.setColor(Color.red);
            g.fillOval(apple.x, apple.y, UNIT_SIZE, UNIT_SIZE);

            // Draw obstacle
            g.setColor(Color.gray);
            g.fillRect(obstacle.x, obstacle.y, UNIT_SIZE, UNIT_SIZE);

            // Draw score and level
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
            g.drawString("Level: " + level, (WIDTH - metrics.stringWidth("Level: " + level)) / 2, HEIGHT - g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        apple = new Point(random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE,
                random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE);
    }

    public void newObstacle() {
        do {
            obstacle = new Point(random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE,
                    random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE);
        } while (obstacle.equals(apple) || snake.contains(obstacle));
    }

    public void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case 'U':
                newHead.y -= UNIT_SIZE;
                break;
            case 'D':
                newHead.y += UNIT_SIZE;
                break;
            case 'L':
                newHead.x -= UNIT_SIZE;
                break;
            case 'R':
                newHead.x += UNIT_SIZE;
                break;
        }

        snake.add(0, newHead);

        if (newHead.equals(apple)) {
            applesEaten++;
            if (applesEaten % 5 == 0) {
                level++;
                timer.setDelay(Math.max(50, DELAY - (level * 10)));
            }
            newApple();
            newObstacle();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    public void checkCollision() {
        Point head = snake.get(0);

        // Check if head collides with body
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
                break;
            }
        }

        // Check if head touches borders
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            running = false;
        }

        // Check if head collides with obstacle
        if (head.equals(obstacle)) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics1.stringWidth("Game Over")) / 2, HEIGHT / 2);

        // Score
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());

        // Restart prompt
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.drawString("Press R to Restart", (WIDTH - metrics2.stringWidth("Press R to Restart")) / 2, HEIGHT - 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
            case KeyEvent.VK_R:
                if (!running) {
                    startGame();
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SwingSnakeGame game = new SwingSnakeGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}

