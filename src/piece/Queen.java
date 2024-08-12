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

    //can move both diagonally and horizontally/vertically
    public boolean canMove(int targetCol, int targetRow) {
        if(isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)){
            //checking diagonal
            if(Math.abs(targetCol-preCol) == Math.abs(targetRow-preRow)) {
                if (isValidSquare(targetCol, targetRow) && !isPieceOnDiagonalLine(targetCol, targetRow)) {
                    return true;
                }
            }

            //checking horizontal/vertical
            if(targetCol == col && targetRow == row){
                if(isValidSquare(targetCol, targetRow) || !isPieceOnStraightLine(targetCol, targetRow)){
                    return true;
                }
            }
        }

        return false;
    }
}
