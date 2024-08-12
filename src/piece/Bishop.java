package piece;

import Main.GamePanel;
import Main.Type;

import java.awt.*;

public class Bishop extends Piece {
    public Bishop(int color, int col, int row) {
        super(color, col, row);

        // assigning ID to each BISHOP
        type = Type.BISHOP;

        if(color == GamePanel.WHITE){
            image = getImage("res/piece/chess piece images v1.0/w-bishop.png");
        }else{
            image = getImage("res/piece/chess piece images v1.0/b-bishop.png");
        }
    }

    //ratio of right/left to up/down movent should be 1
    public boolean canMove(int targetCol, int targetRow) {
        if(isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)){
            if(Math.abs(targetCol-col) == Math.abs(targetRow-row)){
            //if(Math.abs(targetCol-preCol) / Math.abs(targetRow-preRow) == 1){
                if(isValidSquare(targetCol, targetRow) && !isPieceOnDiagonalLine(targetCol, targetRow)){
                    return true;
                }
            }
        }

        return false;
    }
}
