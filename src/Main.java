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
        direction = KeyEvent.VK_RIGHT;

        Timer timer = new Timer(GAME_SPEED, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
        initializeGame();
    }

    private void initializeGame() {
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
            initializeGame();
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

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw grid
        g.setColor(Color.GRAY);
        for (int i = 0; i < GRID_SIZE; i++) {
            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, getHeight());
            g.drawLine(0, i * TILE_SIZE, getWidth(), i * TILE_SIZE);
        }
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

        if ((newDirection == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) ||
                (newDirection == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) ||
                (newDirection == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) ||
                (newDirection == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT)) {
            direction = newDirection;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
