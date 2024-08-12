package piece;

import Main.GamePanel;
import Main.Type;

public class Knight extends Piece {
    public Knight(int color, int col, int row) {
        super(color, col, row);

        // assigning ID to each KNIGHT
        type = Type.KNIGHT;

        if(color == GamePanel.WHITE){
            image = getImage("res/piece/chess piece images v1.0/w-knight.png");
        }else{
            image = getImage("res/piece/chess piece images v1.0/b-knight.png");
        }
    }

    //knight has a movement ratio of 1:2 or 2:1 - 8 total moves
    public boolean canMove(int targetCol, int targetRow) {
        if(isWithinBoard(targetCol, targetRow)){
            //position already updates so, previous coordinates are used
            if(Math.abs(targetCol-preCol) * Math.abs(targetRow-preRow) == 2){
                if(isValidSquare(targetCol, targetRow)){
                    return true;
                }
            }
        }
        return false;
    }

}
