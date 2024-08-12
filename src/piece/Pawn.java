package piece;

import Main.GamePanel;
import Main.Type;

import java.awt.*;

public class Pawn extends Piece {
    public Pawn(int color, int col, int row) {
        super(color, col, row);

        // assigning ID to each PAWN
        type = Type.PAWN;

        if( color == GamePanel.WHITE){
            image = getImage("res/piece/chess piece images v1.0/w-pawn.png");
        } else {
            image = getImage("res/piece/chess piece images v1.0/b-pawn.png");
        }
    }

    //can move only in a straight line and diagonally to eat pieces; 1st moves 2 spaces when starting out
    //and later moves only 1
    public boolean canMove(int targetCol, int targetRow) {

        if(isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)){

            //define the move type based on the current piece colour
            int moveValue;
            if(color == GamePanel.WHITE){
                moveValue = -1;
            } else {  //row value increase when black pawn moc=ves downward on the board
                moveValue = 1;
            }

            //check the hitting piece
            hittingPiece = isHittingPiece(targetCol, targetRow);

            /// PAWN MOVEMENT
            //1 square movement
            if(targetCol == preCol && targetRow == preRow + moveValue && hittingPiece == null){// && moved){
                return true;
            }
            //2 square movement
            if(targetCol == preCol && targetRow == preRow + moveValue*2 && hittingPiece == null && !moved
                    && !isPieceOnStraightLine(targetCol, targetRow)){
                return true;
            }
            //Diagonal movement & capture(if a piece is on a square diagonally in front of it)
            if(Math.abs(targetCol-preCol) == 1 && targetRow == preRow + moveValue && hittingPiece != null
                    && hittingPiece.color != color){
                return true;
            }

            /// EN PASSANT
            if(Math.abs(targetCol-preCol) == 1 && targetRow == preRow + moveValue){
                for(Piece piece : GamePanel.pieces){
                    if(piece.col == targetCol && piece.row == preRow && piece.twoStepped){
                        hittingPiece = piece;
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
