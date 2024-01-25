import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class Main extends JFrame implements ActionListener, KeyListener {

    private static final int GRID_SIZE = 20;
    private static final int TILE_SIZE = 20;
    private static final int GAME_SPEED = 150;

    private enum GameState {
        START, RUNNING, PAUSED, GAME_OVER
    }

    private GameState currentState;
    private final LinkedList<Point> snake;
    private Point food;
    private int direction;

    public Main() {

        setTitle("Snake Game");
        setSize(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        snake = new LinkedList<>();
        currentState = GameState.START;

        addKeyListener(this);
        setFocusable(true);

        Timer timer = new Timer(GAME_SPEED, this);
        timer.start();

        showStartScreen();
    }

    private void showStartScreen() {
        currentState = GameState.START;
        repaint();
    }

    private void showPauseScreen() {
        currentState = GameState.PAUSED;
        repaint();
    }

    private void showGameOverScreen() {
        currentState = GameState.GAME_OVER;
        repaint();
    }

    private void initializeGame() {
        currentState = GameState.RUNNING;
        snake.clear();
        snake.add(new Point(5, 5));
        direction = KeyEvent.VK_RIGHT;
        generateFood();
    }

    private void generateFood() {
        Random random = new Random();
        int x, y;

        do {
            x = random.nextInt(GRID_SIZE);
            y = random.nextInt(GRID_SIZE);
        } while (snake.contains(new Point(x, y)));

        food = new Point(x, y);
    }

    private void move() {
        if (currentState != GameState.RUNNING) {
            return;
        }

        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case KeyEvent.VK_UP:
                newHead.y = (newHead.y - 1 + GRID_SIZE) % GRID_SIZE;
                break;
            case KeyEvent.VK_DOWN:
                newHead.y = (newHead.y + 1) % GRID_SIZE;
                break;
            case KeyEvent.VK_LEFT:
                newHead.x = (newHead.x - 1 + GRID_SIZE) % GRID_SIZE;
                break;
            case KeyEvent.VK_RIGHT:
                newHead.x = (newHead.x + 1) % GRID_SIZE;
                break;
        }

        snake.addFirst(newHead);

        if (newHead.equals(food)) {
            generateFood();
        } else {
            snake.removeLast();
        }

        if (collision()) {
            showGameOverScreen();
        }

        repaint();
    }

    private boolean collision() {
        Point head = snake.getFirst();
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        switch (currentState) {
            case START:
                drawStartScreen(g);
                break;
            case RUNNING:
                drawRunningScreen(g);
                break;
            case PAUSED:
                drawPausedScreen(g);
                break;
            case GAME_OVER:
                drawGameOverScreen(g);
                break;
        }
    }

    private void drawStartScreen(Graphics g) {
        // Draw the start screen here
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Press Enter to Start", 150, 200);
    }

    private void drawRunningScreen(Graphics g) {
        // Draw snake
        g.setColor(new Color(122, 255, 122));
        for (Point point : snake) {
            g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw food
        g.setColor(new Color(255, 0, 58));
        g.fillOval(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private void drawPausedScreen(Graphics g) {
        // Draw the paused screen here
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Game Paused", 180, 200);
        g.drawString("Press P to Resume", 160, 240);
    }

    private void drawGameOverScreen(Graphics g) {
        // Draw the game over screen here
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Game Over", 180, 200);
        g.drawString("Press Enter to Restart", 150, 240);
    }

    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int newDirection = e.getKeyCode();

        if (currentState == GameState.START || currentState == GameState.GAME_OVER) {
            if (newDirection == KeyEvent.VK_ENTER) {
                initializeGame();
            }
        } else if (currentState == GameState.RUNNING) {
            if (newDirection == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN ||
                    newDirection == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP ||
                    newDirection == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT ||
                    newDirection == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) {
                direction = newDirection;
            } else if (newDirection == KeyEvent.VK_P) {
                showPauseScreen();
            }
        } else if (currentState == GameState.PAUSED) {
            if (newDirection == KeyEvent.VK_P) {
                currentState = GameState.RUNNING;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
