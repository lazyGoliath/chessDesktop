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

    //can move only in a straight line and diagonally to eat pieces; 1st moves 2 spaces when starting out
    //and later moves only 1
    public boolean canMove(int targetCol, int targetRow) {
        //define the move type based on the current piece colour
        int moveValue;
        if(color == GamePanel.WHITE){
            moveValue = -1;
        } else {  //row value increase when black pawn moc=ves downward on the board
            moveValue = 1;
        }

        //check the hitting piece
        hittingPiece = isHittingPiece(targetCol, targetRow);

        //checking if pawn has moved or not
        if(moved){  //already displaced from original position
            //pawn will move only 1 square
            if(targetCol == preCol && targetRow == preRow + moveValue && hittingPiece == null){
                return true;
            }
        } else {
            if(targetCol == preCol && targetRow == preRow + moveValue*2 && hittingPiece == null){
                return true;
            }
        }

        if(isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)){
            if(Math.abs(targetRow-row) == 2){
                if(isValidSquare(targetCol, targetRow)){
                    return true;
                }
            }
        }

        return false;
    }
}
