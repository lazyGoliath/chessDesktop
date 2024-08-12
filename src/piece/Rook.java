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
        System.out.println("Can Move working");  // DEBUG

        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            // Check if the move is strictly horizontal or vertical
//            if(targetCol>0 && targetCol<8 && (Math.abs(targetRow - preRow) == 0)
//                    || targetRow>0 && targetRow<8 && (Math.abs(targetCol - preCol) == 0)){
            if (targetCol == col || targetRow == row) {
                // Check for obstacles on the path
                if (!isPieceOnStraightLine(targetCol, targetRow)) {
                    // Check if the target square is valid (e.g., not occupied by a friendly piece)
                    if (isValidSquare(targetCol, targetRow)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}


