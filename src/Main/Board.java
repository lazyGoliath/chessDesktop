package Main;

import java.awt.Graphics2D;
import java.awt.Color;

public class Board {

    final int MAX_COL = 8;
    final int MAX_ROW = 8;
    public static final int SQUARE_SIZE = 100;
    public static final int HALF_SQUARE_SIZE = SQUARE_SIZE / 2;

    public void draw(Graphics2D g2) {

        boolean c = false;

        for(int row = 0; row < MAX_ROW; row++) {
            for(int col = 0; col < MAX_COL; col++) {

                if(!c){
                    g2.setColor(new Color(210,165,125));
                    c = true;
                } else {
                    g2.setColor(new Color(175,155,70));
                    c = false;
                }
                g2.fillRect(col*SQUARE_SIZE, row*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }

            c = !c;
        }
    }
}
