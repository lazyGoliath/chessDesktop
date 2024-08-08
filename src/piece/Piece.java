package piece;

import Main.Board;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class Piece {

    public BufferedImage image;
    public int x, y;
    public int row, col, preRow, preCol;
    public int color;
    public Piece hittingPiece = null;
    public boolean moved;

    public Piece(int color, int col, int row){
        this.color = color;
        this.row = row;
        this.col = col;
        x = getX(col);
        y = getY(row);
        preRow = row;
        preCol = col;
    }

    public BufferedImage getImage(String imagePath) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new FileInputStream(imagePath));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public int getX(int col) {
        return col* Board.SQUARE_SIZE;
    }
    public int getY(int row) {
        return row * Board.SQUARE_SIZE;
    }

    //managing positioning of the moved piece
    public int getRow(int y) {
        return (y + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;  //manipulate  current position to the centre of piece icon
    }
    public int getCol(int x) {
        return (x + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }
    public int getIndex() {
        for(int index = 0; index < GamePanel.simPieces.size(); index++){
            if(GamePanel.simPieces.get(index) == this) {
                return index;
            }
        }
        return 0;  //piece is missing
    }

    public void updatePosition() {
        x = getX(col);
        y = getY(row);
        //update previous position since move has been confirmed
        preRow = getRow(y);
        preCol = getCol(x);
        moved = true;
    }

    public void resetPosition() {
        col = preCol;
        row = preRow;
        x = getX(col);
        y = getY(row);
    }

    public boolean canMove(int targetCol, int targetRow) {
        return false;
    }
    public boolean isWithinBoard(int targetCol, int targetRow) {
//        if(targetCol<=8 && targetCol>0 && targetRow<=8 && targetRow>0) {
//            return true;
//        }
//        return false;  ,,OR,
        return targetCol < 8 && targetCol >= 0 && targetRow < 8 && targetRow >= 0;
    }

    public Piece isHittingPiece(int targetCol, int targetRow){
        for(Piece piece : GamePanel.simPieces) {
            if (piece.col == targetCol && piece.row == targetRow && piece != this) {
                return piece;
            }
        }

        return null;
    }
    public boolean isValidSquare(int targetCol, int targetRow) {
        hittingPiece = isHittingPiece(targetCol, targetRow);
        if(hittingPiece == null) {  //vacant square
            return true;
        } else {  //occupied square
            if(hittingPiece.color != this.color) { //opponent can be captured
                return true;
            } else{
                hittingPiece = null;  //reset to null
                return false;
            }
        }
    }

    //piece related specific properties
    public boolean isSameSquare(int targetCol, int targetRow) {
        return targetCol == col && targetRow == row;
    }
    public boolean isPieceOnStraightLine(int targetCol, int targetRow) {
        //when piece moving to the left
        for(int c = preCol-1; c>targetCol; c--){
            for(Piece piece : GamePanel.simPieces){
                if(c == piece.col && targetRow == piece.row){
                    hittingPiece = piece;
                    return true;
                }
            }
        }

        //when piece moving to the right
        for(int c = preCol+1; c<targetCol; c++){
            for(Piece piece : GamePanel.simPieces){
                if(c == piece.col && targetRow == piece.row){
                    hittingPiece = piece;
                    return true;
                }
            }
        }

        //when piece moving up
        for(int r = preRow-1; r>targetRow; r--){
            for(Piece piece : GamePanel.simPieces){
                if(r == piece.row && targetCol == piece.col){
                    hittingPiece = piece;
                    return true;
                }
            }
        }

        //when piece moving down
        for(int r = preRow+1; r<targetRow; r++){
            for(Piece piece : GamePanel.simPieces){
                if(r == piece.row && targetCol == piece.col){
                    hittingPiece = piece;
                    return true;
                }
            }
        }

        return false;
    }
    public boolean isPieceOnDiagonalLine(int targetCol, int targetRow) {
        if(targetRow < preRow) {
            if(targetCol < preCol) {
                //when piece moving left-up
                for(int c = preCol-1; c>targetCol; c--){
                    int diff = Math.abs(c - preCol);
                    for(Piece piece : GamePanel.simPieces){
                        if(c == piece.col && (preRow - diff) == piece.row){
                            hittingPiece = piece;
                            return true;
                        }
                    }
                }
            } else {
                //when piece moving right-up
                for(int c = preCol+1; c<targetCol; c++){
                    int diff = Math.abs(c - preCol);
                    for(Piece piece : GamePanel.simPieces){
                        if(c == piece.col && (preRow + diff) == piece.row){
                            hittingPiece = piece;
                            return true;
                        }
                    }
                }
            }
        }

        if(targetRow > preRow) {
            if(targetCol > preCol) {
                //when piece moving right-down
                for(int c = preCol+1; c<targetCol; c++){
                    int diff = Math.abs(c - preCol);
                    for(Piece piece : GamePanel.simPieces){
                        if(c == piece.col && (preRow + diff) == piece.row){
                            hittingPiece = piece;
                            return true;
                        }
                    }
                }
            } else {
                //when piece moving left-down
                for(int c = preCol-1; c>targetCol; c--){
                    int diff = Math.abs(c - preCol);
                    for(Piece piece : GamePanel.simPieces){
                        if(c == piece.col && (preRow - diff) == piece.row){}
                    }
                }
            }
        }

        return false;

    }

    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
    }
}
