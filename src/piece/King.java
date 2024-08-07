package piece;

import Main.Board;
import Main.GamePanel;

public class King extends Piece {
    public King(int color, int col, int row) {
        super(color, col, row);

        if(color == GamePanel.WHITE){
            image = getImage("res/piece/chess piece images v1.0/w-king.png");
        }else{
            image = getImage("res/piece/chess piece images v1.0/b-king.png");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {

        //checking all 8 directions of possible movements
        if(isWithinBoard(targetCol, targetRow)){
            //checking up,down,left and right
            if(Math.abs(targetCol-preCol) + Math.abs(targetRow-preRow) == 1 ||
                    Math.abs(targetCol-preCol) * Math.abs(targetRow-preRow) == 1){   //checking diagonal left and right
                return isValidSquare(targetCol, targetRow);
            }
        }
        return false;
    }
}
