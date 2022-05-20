import java.awt.Color;

public class SnakeSection extends CanvasObject {
    protected int prev_step_x;
    protected int prev_step_y;
    protected int next_step_x;
    protected int next_step_y;

    public SnakeSection(int x, int y, int nx, int ny, int object_size, Color c) {
        super(x, y, object_size, c);
        prev_step_x = x;
        prev_step_y = y;
        next_step_x = nx;
        next_step_y = ny;
    }

    // Overloaded constructor in case color is not passed (optional)
    public SnakeSection(int x, int y, int nx, int ny, int object_size) {
        super(x, y, object_size, Color.black);
        prev_step_x = x;
        prev_step_y = y;
        next_step_x = nx;
        next_step_y = ny;
    }

    public void move(int nx, int ny) {
        prev_step_x = step_x;
        prev_step_y = step_y;
        step_x = next_step_x;
        step_y = next_step_y;
        next_step_x = nx;
        next_step_y = ny;
    }
}
