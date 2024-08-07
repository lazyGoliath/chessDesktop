package Main;

import java.awt.event.MouseEvent;

import java.awt.event.MouseAdapter;

public class Mouse extends MouseAdapter {

    public int x = 0, y = 0;  //store mouse movement coordinates
    public boolean pressed;  //detects if pressed

    public void mousePressed(MouseEvent e) {
        pressed = true;
        x = e.getX();
        y = e.getY();
    }

    public void mouseReleased(MouseEvent e) {
        pressed = false;
        x = e.getX();
        y = e.getY();
    }

    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
    public void mouseMove(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
}
