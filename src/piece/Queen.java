package piece;

import Main.GamePanel;

import java.awt.*;

public class Queen extends Piece {
    public Queen(int color, int col, int row) {
        super(color, col, row);

        if(color == GamePanel.WHITE){
            image = getImage("res/piece/chess piece images v1.0/w-queen.png");
        }else{
            image = getImage("res/piece/chess piece images v1.0/b-queen.png");
        }
    }
}
