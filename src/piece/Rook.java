package piece;

import Main.GamePanel;

public class Rook extends Piece {
    public Rook(int color, int col, int row) {
        super(color, col, row);

        if(color == GamePanel.WHITE) {
            image = getImage("res/piece/chess piece images v1.0/w-rook.png");
        }else{
            image = getImage("res/piece/chess piece images v1.0/b-rook.png");
        }
    }

    //can move only horizontally or vertically
    public boolean canMove(int targetCol, int targetRow) {
        if(isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)){
//            if(targetCol>0 && targetCol<8 && (Math.abs(targetRow - preRow) == 0)
//                    || targetRow>0 && targetRow<8 && (Math.abs(targetCol - preCol) == 0)){
            if(targetCol == col && targetRow == row){
                if(isValidSquare(targetCol, targetRow) && !isPieceOnStraightLine(targetCol, targetRow)){
                    return true;
                }
            }
        }
        return false;
    }
}
