package piece;

import Main.Board;
import Main.GamePanel;
import Main.Type;

public class King extends Piece {
    public King(int color, int col, int row) {
        super(color, col, row);

        // assigning ID to each KING
        type = Type.KING;

        if(color == GamePanel.WHITE){
            image = getImage("res/piece/chess piece images v1.0/w-king.png");
        }else{
            image = getImage("res/piece/chess piece images v1.0/b-king.png");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        System.out.println("Can Move working");  //DEBUG

        /// MOVEMENT
        //checking all 8 directions of possible movements
        if(isWithinBoard(targetCol, targetRow)){
            //checking up,down,left and right
            if(Math.abs(targetCol-preCol) + Math.abs(targetRow-preRow) == 1 ||
                    Math.abs(targetCol-preCol) * Math.abs(targetRow-preRow) == 1){   //checking diagonal left and right
                return isValidSquare(targetCol, targetRow);
            }
        }

        /// CASTLING
        //no castling be done if the king has moved
        if(!moved){

            // RIGHT CASTLING
            if(targetCol == preCol+2 && targetRow == preRow && !isPieceOnStraightLine(targetCol, targetRow)){
                for(Piece pieces : GamePanel.pieces){
                    if(pieces.col == preCol+3 && pieces.row == preRow && !pieces.moved){ //obviously, position of a rook
                        GamePanel.castlingPiece = pieces;
                        return true;
                    }
                }
            }

            // LEFT CASTLING
            //only checks for 1st and 2nd squares to the left
            if(targetCol == preCol-2 && targetRow == preRow && !isPieceOnStraightLine(targetCol, targetRow)){
                //need to check if 3rd and 4th squares on king left are vacant
                Piece p[] = new Piece[2];
                for(Piece pieces : GamePanel.pieces){
                    if(pieces.col == preCol-3 && pieces.row == preRow){  //knight piece
                        p[0] = pieces;
                    }
                    if(pieces.col == preCol-4 && pieces.row == preRow){  //rook piece
                        p[1] = pieces;
                    }
                }
                if(p[0] == null && !p[1].moved){
                    GamePanel.castlingPiece = p[1]; //rook will be the castling piece
                    return true;
                }
            }
        }

        return false;
    }
}
