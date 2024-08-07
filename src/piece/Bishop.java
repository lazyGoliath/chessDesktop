package piece;

import Main.GamePanel;

import java.awt.*;

public class Bishop extends Piece {
    public Bishop(int color, int col, int row) {
        super(color, col, row);

        if(color == GamePanel.WHITE){
            image = getImage("res/piece/chess piece images v1.0/w-bishop.png");
        }else{
            image = getImage("res/piece/chess piece images v1.0/b-bishop.png");
        }
    }
}
