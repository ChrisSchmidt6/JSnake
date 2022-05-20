import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameLoop implements ActionListener {
    Window window;

    GameLoop(Window window) {
        this.window = window;
    }

    public void actionPerformed(ActionEvent arg0) {
        window.game.moveSnake();
        window.repaintCanvas();
    }

}