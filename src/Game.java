import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.lang.Math;
import javax.swing.Timer;

public class Game {
    static final int tile = 40; // Size of steps and of CanvasObjects
    static final int canvas_size = tile * 11; // Width and height
    protected final int speed = 125;

    protected Timer timer;
    protected Window window;

    protected ArrayList<SnakeSection> snake;
    protected CanvasObject target;

    protected boolean paused;
    protected boolean game_over;
    protected String direction;
    protected String queued_direction;
    protected int score;

    public Game() {
        setup();
        window = new Window(this);
        GameLoop loop = new GameLoop(window);
        timer = new Timer(speed, loop);
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
    
    public void setup() {
        paused = false;
        game_over = false;
        direction = "down";
        queued_direction = direction;
        score = 0;
        // Cast value to an int which is the same as rounding down (drops decimal values)
        int top_center_tile_x = (int)(canvas_size / tile / 2);
        // Set snake to an empty ArrayList of SnakeSections
        snake = new ArrayList<SnakeSection>();
        SnakeSection head = new SnakeSection(top_center_tile_x, 0, top_center_tile_x, 1, tile, Color.red);
        SnakeSection tail1 = new SnakeSection(head.step_x, head.step_y - 1, head.step_x, head.step_y, tile);
        SnakeSection tail2 = new SnakeSection(tail1.step_x, tail1.step_y - 1, tail1.step_x, tail1.step_y, tile);
        snake.add(head);
        snake.add(tail1);
        snake.add(tail2);

        createNewTarget();
    }

    public void addSnakeSection() {
        SnakeSection last_section = snake.get(snake.size() - 1);
        snake.add(new SnakeSection(last_section.prev_step_x, last_section.prev_step_y, last_section.step_x, last_section.step_y, tile));
    }

    public void createNewTarget() {
        int total_tiles = (canvas_size / tile) * (canvas_size / tile);
        int highest_possible_score = total_tiles - 3;
        if(score == highest_possible_score) {
            System.out.println("You beat the game!");
            stop();
        } else {
            ArrayList<Integer[]> valid_coordinates = new ArrayList<Integer[]>();
            // Create list of all possible coordinates on the map
            // x < width, y < height (in steps)
            for(int x = 0; x < (canvas_size / tile); x++){
                for(int y = 0; y < (canvas_size / tile); y++) {
                    // valid_coordinates stores arrays of two Integers [x, y]
                    valid_coordinates.add(new Integer[]{x, y});
                }
            }
            // Loop through each snake section and remove their
            // coordinates from the valid_coordinates ArrayList
            for(int i = 0; i < snake.size(); i++) {
                SnakeSection section = snake.get(i);
                // Make sure snake coordinates are in bounds
                if(section.next_step_x >= 0 && section.next_step_y >= 0) {
                    // Remove if Arrays.deepEquals is true (required to compare two arrays)
                    valid_coordinates.removeIf(coords -> Arrays.deepEquals(coords, new Integer[]{section.next_step_x, section.next_step_y}));
                }
            }

            int new_coords_index = (int)(Math.random() * valid_coordinates.size());
            Integer[] new_coords = valid_coordinates.get(new_coords_index);

            target = new CanvasObject(new_coords[0], new_coords[1], tile, Color.green);
        }
    }

    public void checkCollision(int next_x, int next_y) {
        // Collision with outer boundaries
        int left_top_bounds = -1; // Out of bounds for left and top edges
        int right_bottom_bounds = canvas_size / tile; // Out of bounds for right and bottom edges
        if(next_x == left_top_bounds || next_y == left_top_bounds || next_x == right_bottom_bounds || next_y == right_bottom_bounds){
            paused = true;
            game_over = true;
            stop();
            System.out.println("Oops, you failed!");
        }

        // Collision with self
        for(int i = 1; i < snake.size(); i++) {
            SnakeSection section = snake.get(i);
            if(next_x == section.next_step_x && next_y == section.next_step_y) {
                paused = true;
                game_over = true;
                stop();
                System.out.println("Oops, you failed!");
            }
        }

        // Collision with target
        if(next_x == target.step_x && next_y == target.step_y) {
            score += 1;
            System.out.println("Hit target " + score + "!");
            addSnakeSection();
            createNewTarget();
        }
    }

    public void moveSnake() {
        direction = queued_direction;
        for(int i = 0; i < snake.size(); i++) {
            SnakeSection section = snake.get(i);
            // If head, move based on direction. If anything else, move based on next section
            if(i == 0) {
                // Start by checking for collision
                checkCollision(section.next_step_x, section.next_step_y);
                if(direction == "up") {
                    section.move(section.next_step_x, section.next_step_y - 1);
                } else if(direction == "left") {
                    section.move(section.next_step_x - 1, section.next_step_y);
                } else if(direction == "down") {
                    section.move(section.next_step_x, section.next_step_y + 1);
                } else if(direction == "right") {
                    section.move(section.next_step_x + 1, section.next_step_y);
                }
            } else {
                SnakeSection next_section = snake.get(i - 1);
                section.move(next_section.step_x, next_section.step_y);
            }
        }
    }

    public void changeDirection(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                if(direction != "down") {
                    queued_direction = "up";
                }
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if(direction != "right") {
                    queued_direction = "left";
                }
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if(direction != "up") {
                    queued_direction = "down";
                }
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if(direction != "left") {
                    queued_direction = "right";
                }
                break;
            case KeyEvent.VK_BACK_SPACE:
                if(!paused) {
                    paused = true;
                    stop();
                }
                break;
            case KeyEvent.VK_SPACE:
                if(paused) {
                    if(game_over) {
                        setup(); // Restart game
                    } else {
                        paused = false;
                    }
                    start();
                }
                break;
            default:
                break;
        }
    }

    public ArrayList<SnakeSection> getSnake() {
        return snake;
    }

    public CanvasObject getTarget() {
        return target;
    }

}