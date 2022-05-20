import java.awt.Color;

public class CanvasObject {
    protected int step_x;
    protected int step_y;
    protected int size;
    protected Color color;

    public CanvasObject(int x, int y, int object_size, Color c) {
        step_x = x;
        step_y = y;
        size = object_size;
        color = c;
    }

    public void move(int x, int y) {
        step_x = x;
        step_y = y;
    }
}
