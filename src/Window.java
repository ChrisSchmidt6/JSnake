import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Window extends JFrame implements KeyListener {
    protected Game game;
    protected GameCanvas game_canvas;
    protected int size;

    public Window(Game game) {
        this.game = game;
        size = Game.canvas_size;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game_canvas = new GameCanvas();
        game_canvas.setPreferredSize(new Dimension(size, size));
        add(game_canvas);
        pack();
        setVisible(true);
        game_canvas.setFocusable(true);
        game_canvas.addKeyListener(this);
        game_canvas.createBufferStrategy(2);
    }

    public void repaintCanvas() {
        game_canvas.myRepaint(game.getSnake(), game.getTarget());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        game.changeDirection(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Do nothing
    }
}