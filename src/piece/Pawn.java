package piece;

import Main.GamePanel;

import java.awt.*;

public class Pawn extends Piece {
    public Pawn(int color, int col, int row) {
        super(color, col, row);

        if( color == GamePanel.WHITE){
            image = getImage("res/piece/chess piece images v1.0/w-pawn.png");
        } else {
            image = getImage("res/piece/chess piece images v1.0/b-pawn.png");
        }

    }
}
