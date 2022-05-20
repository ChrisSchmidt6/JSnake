import java.util.ArrayList;
import java.awt.image.BufferStrategy;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;

public class GameCanvas extends Canvas {

    public GameCanvas() {
        setIgnoreRepaint(true);
    }

    public void myRepaint(ArrayList<SnakeSection> snake, CanvasObject target) {
        BufferStrategy strategy = getBufferStrategy();
        Graphics g = strategy.getDrawGraphics();

        // Reset canvas to background color
        g.setColor(Color.white);
        g.fillRect(0, 0, Game.canvas_size, Game.canvas_size);

        g.setColor(target.color);
        g.fillRect(target.step_x * target.size, target.step_y * target.size, target.size, target.size);
        for(int i = 0; i < snake.size(); i++) {
            CanvasObject rect = snake.get(i);
            g.setColor(rect.color);
            g.fillRect(rect.step_x * rect.size, rect.step_y * rect.size, rect.size, rect.size);
        }

        if(g != null) g.dispose();

        strategy.show();
        Toolkit.getDefaultToolkit().sync();
    }
}
