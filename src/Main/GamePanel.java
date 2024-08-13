package Main;

import piece.*;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;

import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    public static int HEIGHT = 800;
    public static int WIDTH = 1100;
    final int FPS = 60;
    Thread gameThread = null;
    Mouse mouse = new Mouse();

    //instantiate board class
    Board board = new Board();

    //PIECES
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();  //to revert the changes
    Piece activePiece = null;  //current piece held by the user to be updated
    public static Piece castlingPiece = null;
    ArrayList<Piece> promotionPieces = new ArrayList<>();
    Piece checkPiece = null;

    //COLOR
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;   //game starts with white

    //BOOLEANS
    boolean canMove;
    boolean validSquare;
    boolean promotion;
    boolean gameOver = false;

    //constructor to initialize the panel specs
    public GamePanel(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        setPieces();
        //testPromotion();
        copyPieces(pieces, simPieces);

        //read mouse motion
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
    }

    //instantiate the thread
    public void launchGame(){
        gameThread = new Thread(this);
        gameThread.start();   //calls the run method
    }

    //add pieces to the list
    public void setPieces(){

        //White Team
        pieces.add(new Pawn(WHITE, 0, 6));
        pieces.add(new Pawn(WHITE, 1, 6));
        pieces.add(new Pawn(WHITE, 2, 6));
        pieces.add(new Pawn(WHITE, 3, 6));
        pieces.add(new Pawn(WHITE, 4, 6));
        pieces.add(new Pawn(WHITE, 5, 6));
        pieces.add(new Pawn(WHITE, 6, 6));
        pieces.add(new Pawn(WHITE, 7, 6));
        pieces.add(new Rook(WHITE, 0, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        pieces.add(new Knight(WHITE, 1, 7));
        pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new Bishop(WHITE, 2, 7));
        pieces.add(new Bishop(WHITE, 5, 7));
        pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new King(WHITE, 4, 7));

        //Black Team
        pieces.add(new Pawn(BLACK, 0, 1));
        pieces.add(new Pawn(BLACK, 1, 1));
        pieces.add(new Pawn(BLACK, 2, 1));
        pieces.add(new Pawn(BLACK, 3, 1));
        pieces.add(new Pawn(BLACK, 4, 1));
        pieces.add(new Pawn(BLACK, 5, 1));
        pieces.add(new Pawn(BLACK, 6, 1));
        pieces.add(new Pawn(BLACK, 7, 1));
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        pieces.add(new Knight(BLACK, 1, 0));
        pieces.add(new Knight(BLACK, 6, 0));
        pieces.add(new Bishop(BLACK, 2, 0));
        pieces.add(new Bishop(BLACK, 5, 0));
        pieces.add(new Queen(BLACK, 3, 0));
        pieces.add(new King(BLACK, 4, 0));
    }

//    private void testPromotion(){
//        pieces.add(new Pawn(WHITE, 0, 5));
//        pieces.add(new Pawn(BLACK, 7, 3));
//    }

    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target){
        target.clear();
        target.addAll(source);
        //System.out.println(target.size());
    }

    //run the game loop - core of the GAME
    public void run(){

        //GAME loop - (1) Sleep method
//        double drawInterval = (double) 1000000000 / FPS;   // in nanoseconds - 0.01667 secs
//        double nextDrawTime = System.nanoTime() + drawInterval;  //current system program running time - System.nanoTime()
//
//        while(gameThread == null){
//            update();
//
//            repaint();  //calls paintComponent() method
//
//            //now, find the remaining time left after running each iteration
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime = remainingTime / 1000000;   //nano -> milli secs
//
//                if(remainingTime < 0){
//                    remainingTime = 0;
//                }
//
//                Thread.sleep((long) remainingTime);
//
//                nextDrawTime += drawInterval;  //update time stamp of next draw
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        // (2) Delta/ Accumulator method
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while( gameThread != null ){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1){
                update();
                repaint();  // calls paint component method
                delta--;
            }
        }

    }

    //updates player position and make changes
    private void update() {

        //game temporarily stopped until promoting piece selected
        if(promotion){
            promoting();
        } else if(!gameOver){
            // Handle Mouse Pressed
            if (mouse.pressed) {
                if (activePiece == null) {
                    // Check if we can pick up a new piece
                    for (Piece piece : simPieces) {
                        if (piece.color == currentColor && piece.col == mouse.x / Board.SQUARE_SIZE
                                && piece.row == mouse.y / Board.SQUARE_SIZE) {
                            // Pick up the piece if the color and position match
                            activePiece = piece;
                            System.out.println("Picked up piece: " + activePiece); // Debug statement
                            break; // Exit the loop once the piece is found
                        }
                    }
                } else {
                    // Simulate the move if a piece is already being held
                    simulate();
                }
            } else {
                // Handle Mouse Released
                if (activePiece != null) {
                    // Adjust the position of the piece
                    if(validSquare) {

                        // MOVE CONFIRMED
                        //Update the piece list in case a piece has been captured and removed
                        copyPieces(simPieces, pieces);
                        activePiece.updatePosition();
                        System.out.println("Moved piece to: " + activePiece); // Debug statement

                        //update rook's position
                        if(castlingPiece != null){
                            castlingPiece.updatePosition();
                        }

                        if(isKingInCheck() && isCheckMate()){
                            gameOver = true;
                            } else {
                                if(canPromote()){
                                    promotion = true;
                                } else {
                                    changePlayer();  //switch to opponent
                                }
                        }
                    } else{
                        //invalid move, reset everything
                        copyPieces(pieces, simPieces);
                        // Reset activePiece to allow picking up another piece
                        activePiece.resetPosition();
                        activePiece = null;
                    }
                }
            }
        }

    }

    //thinking phase
    private void simulate(){

        // Reset the piece list in every loop
        //This is basically for restoring the removed piece during the simulation
        copyPieces(pieces, simPieces);

        // Reset the castling piece position if move unconfirmed
        if(castlingPiece != null){
            castlingPiece.col = castlingPiece.preCol;
            castlingPiece.x = castlingPiece.getX(castlingPiece.preCol);
            castlingPiece = null;
        }
        //if a piece is being held, simulate a move
        activePiece.x = mouse.x - Board.HALF_SQUARE_SIZE;//subtracted so that mouse is at the centre of piece
        activePiece.y = mouse.y - Board.HALF_SQUARE_SIZE;

        //update activeP row n col to the centre
        activePiece.row = activePiece.getRow(activePiece.y);
        activePiece.col = activePiece.getCol(activePiece.x);

        //check if move is valid
        canMove = false;
        validSquare = false;
        //Checking if the piece is hovering over a reachable square
        if(activePiece.canMove(activePiece.col, activePiece.row)){
            canMove = true;
            validSquare = true;

            if(activePiece.hittingPiece != null){  //hitting opponent piece
                simPieces.remove(activePiece.hittingPiece.getIndex());  //still thinking phase
            }

            checkCastling();

            if(!isIllegal(activePiece) || !opponentCanCaptureKing()){
                validSquare = true;
            }
        }
    }

    private boolean isKingInCheck(){
        Piece opponentKing = getKing(true);

        //checks if active piece can move to the square where opponent king is, hence checked
        if(activePiece.canMove(opponentKing.col, opponentKing.row)){
            checkPiece = activePiece;
            return true;
        }

        checkPiece = null;
        return false;
    }

    //if parameter true, method returns opponent king
    private Piece getKing(boolean opponent){
        Piece king = null;

        for(Piece piece : simPieces){
            if(opponent){
                if(piece.type == Type.KING && piece.color != currentColor){
                    king = piece;
                }
            } else {
                if(piece.type == Type.KING && piece.color == currentColor){
                    king = piece;
                }
            }
        }

        return king;
    }

    /// PROTECTING KING FROM A CHECKMATE POSSIBILITIES

    //capture the checking piece

    //1.) if king can move to a square not under attack
    private boolean isCheckMate(){
        Piece opponentKing = getKing(true);

        if(kingCanMove(opponentKing)){
            return false;
        } else {
            //2.) if another king's ally piece blocks the check
            //find difference in position between the checking piece and king
            int colDiff = Math.abs(opponentKing.col - checkPiece.col);
            int rowDiff = Math.abs(opponentKing.row - checkPiece.row);

            //estimating the type of attacking piece based on difference
            if(colDiff == 0){
                //checking piece attacking vertically

                //checking if checkingP attacking from above or below the king
                if(checkPiece.row < opponentKing.row){
                    //checkingP attacking from above
                    for(int row = checkPiece.row; row < opponentKing.row; row++){
                        for(Piece piece : simPieces){
                            if(piece != opponentKing && piece.color != currentColor && piece.canMove(checkPiece.col, row)){
                                return false;  //checkmate saved!!
                            }
                        }
                    }
                }
                if(checkPiece.row > opponentKing.row){
                    //checkingP attacking from below
                    for(int row = checkPiece.row; row > opponentKing.row; row--){
                        for(Piece piece : simPieces){
                            if(piece != opponentKing && piece.color != currentColor && piece.canMove(checkPiece.col, row)){
                                return false;  //checkmate saved!!
                            }
                        }
                    }
                }
            } else if(rowDiff == 0){
                //checking piece attacking horizontally

                //checking if checkingP attacking from left or right side of the king
                if(checkPiece.col < opponentKing.col){
                    //checkingP attacking from left side
                    for(int col = checkPiece.col; col < opponentKing.col; col++){
                        for(Piece piece : simPieces){
                            if(piece != opponentKing && piece.color != currentColor && piece.canMove(col, checkPiece.row)){
                                return false;  //checkmate saved!!
                            }
                        }
                    }
                }
                if(checkPiece.col > opponentKing.col){
                    //checkingP attacking from right side
                    for(int col = checkPiece.row; col > opponentKing.row; col--){
                        for(Piece piece : simPieces){
                            if(piece != opponentKing && piece.color != currentColor && piece.canMove(col, checkPiece.row)){
                                return false;  //checkmate saved!!
                            }
                        }
                    }
                }
            } else if(colDiff == rowDiff){
                //checking piece attacking diagonally

                //checking if checkingP attacking diagonally from above or below the king
                if(checkPiece.row < opponentKing.row){
                    //checkingP attacking from above
                    if(checkPiece.col < opponentKing.col){
                        //checkingP attacking from upper-left
                        for(int col = checkPiece.col, row = checkPiece.row; col < opponentKing.col; col++, row++){
                            for(Piece piece : simPieces){
                                if(piece != opponentKing && piece.color != currentColor && piece.canMove(col, row)){
                                    return false;  //check saved
                                }
                            }
                        }
                    }
                    if(checkPiece.col > opponentKing.col){
                        //checkingP attacking from upper-right
                        for(int col = checkPiece.col, row = checkPiece.row; col > opponentKing.col; col--, row++){
                            for(Piece piece : simPieces){
                                if(piece != opponentKing && piece.color != currentColor && piece.canMove(col, row)){
                                    return false;  //check saved
                                }
                            }
                        }
                    }
                }
                if(checkPiece.row > opponentKing.row){
                    //checkingP attacking from below
                    if(checkPiece.col < opponentKing.col){
                        //checkingP attacking from lower-left
                        for(int col = checkPiece.col, row = checkPiece.row; col < opponentKing.col; col++, row--){
                            for(Piece piece : simPieces){
                                if(piece != opponentKing && piece.color != currentColor && piece.canMove(col, row)){
                                    return false;  //check saved
                                }
                            }
                        }
                    }
                    if(checkPiece.col > opponentKing.col){
                        //checkingP attacking from lower-right
                        for(int col = checkPiece.col, row = checkPiece.row; col > opponentKing.col; col--, row--){
                            for(Piece piece : simPieces){
                                if(piece != opponentKing && piece.color != currentColor && piece.canMove(col, row)){
                                    return false;  //check saved
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
    private boolean kingCanMove(Piece king){

        //simulate if there is any square where the king can move to
        if(isValidMove(king, -1, -1)){ return true; }  //top-left
        if(isValidMove(king, -1, 0)){ return true; }   //top-up
        if(isValidMove(king, -1, 1)){ return true; }   //top-right
        if(isValidMove(king, 0, -1)){ return true; }   //side-left
        if(isValidMove(king, 0, 1)){ return true; }    //side-right
        if(isValidMove(king, 1, -1)){ return true; }   //bottom-left
        if(isValidMove(king, 1, 0)){ return true; }    //bottom-down
        if(isValidMove(king, 1, 1)){ return true; }    //bottom-right

        return false;  //if no possible king move
    }
    private boolean isValidMove(Piece king, int rowPlus, int colPlus){

        boolean isValidMove = false;

        // update the king's position for now
        king.col += colPlus;
        king.row += rowPlus;

        //check if new square is safe or not
        if(king.canMove(king.col, king.row)){
            //remove if king is hitting any piece(opponent)
            if(king.hittingPiece != null){
                simPieces.remove(king.hittingPiece.getIndex());
            }
            //if move is not illegal
            if(!isIllegal(king)){
                isValidMove = true;
            }
        }

        //Reset the king's position and remove the removed piece
        king.resetPosition();
        copyPieces(pieces, simPieces);

        return isValidMove;
    }

    private void checkCastling() {
        if(castlingPiece != null ){
            if(castlingPiece.col == 0){  //left castling piece
                castlingPiece.col += 3;
            }
            if(castlingPiece.col == 7){  //right castling piece
                 castlingPiece.col -= 2;
            }
            castlingPiece.x = castlingPiece.getX(castlingPiece.col);
        }
    }

    //switch turns
    private void changePlayer(){
        if(currentColor == BLACK){
            currentColor = WHITE;
            // Reset white's two stepped status
            for(Piece piece : pieces){
                if(piece.color == WHITE){
                    piece.twoStepped = false;
                }
            }
        } else {
            currentColor = BLACK;
            // Reset black's two stepped status
            for(Piece piece : pieces){
                if(piece.color == BLACK){
                    piece.twoStepped = false;
                }
            }
        }
        activePiece = null;
    }

    private boolean canPromote(){
        if(activePiece.type == Type.PAWN){
            if(activePiece.color == BLACK && activePiece.row == 7 || activePiece.color == WHITE && activePiece.row == 0){
                promotionPieces.clear();
                promotionPieces.add(new Rook(currentColor, 9, 2));
                promotionPieces.add(new Knight(currentColor, 9, 3));
                promotionPieces.add(new Bishop(currentColor, 9, 4));
                promotionPieces.add(new Queen(currentColor, 9, 5));
                return true;
            }
        }

        return false;
    }

    //checking if the move is safe for the king
    private boolean isIllegal(Piece king){
        if(king.type == Type.KING){
            for(Piece piece : simPieces){
                if(piece != king && piece.color != king.color && piece.canMove(king.col, king.row)){
                    return true;
                }
            }
        }

        return false;
    }

    //checks if move is safe for king in check condition
    private boolean opponentCanCaptureKing(){

        Piece king = getKing(true);  //current colour's king

        for(Piece piece : simPieces){
            if(piece.color != king.color && piece.canMove(king.col, king.row)){
                return true;
            }
        }

        return false;
    }

    private void promoting(){
        if(mouse.pressed){
            for(Piece piece : promotionPieces){
                if(piece.col == mouse.x / Board.SQUARE_SIZE && piece.row == mouse.y / Board.SQUARE_SIZE){
                    switch(piece.type){
                        case ROOK : simPieces.add(new Rook(currentColor, activePiece.col, activePiece.row));break;
                        case KNIGHT : simPieces.add(new Knight(currentColor, activePiece.col, activePiece.row));break;
                        case BISHOP : simPieces.add(new Bishop(currentColor, activePiece.col, activePiece.row));break;
                        case QUEEN : simPieces.add(new Queen(currentColor, activePiece.col, activePiece.row));break;
                        default: break;
                    }
                    simPieces.remove(activePiece.getIndex());
                    activePiece = null;
                    promotion = false;
                    copyPieces(simPieces, pieces);
                    changePlayer();
                }
            }
        }
    }

    //draw those changes graphically on the board
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        //BOARD
        Graphics2D g2d = (Graphics2D) g;
        board.draw(g2d);

        //PIECES
        for(Piece piece : pieces){
            piece.draw(g2d);
        }

        //adding an effect when a piece is selected
        if(activePiece != null) {

            if(canMove){
                //checks if move is safe for the king or not
                if(isIllegal(activePiece) || opponentCanCaptureKing()){
                    //set background color of current piece box to white
                    g2d.setColor(Color.GRAY);
                    //change opacity
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    //apply changes to the box
                    g2d.fillRect(activePiece.col * Board.SQUARE_SIZE, activePiece.row * Board.SQUARE_SIZE,
                            Board.SQUARE_SIZE, Board.SQUARE_SIZE);

                    //reset opacity when piece is removed from a box
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                } else {
                    //set background color of current piece box to white
                    g2d.setColor(Color.WHITE);
                    //change opacity
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    //apply changes to the box
                    g2d.fillRect(activePiece.col * Board.SQUARE_SIZE, activePiece.row * Board.SQUARE_SIZE,
                            Board.SQUARE_SIZE, Board.SQUARE_SIZE);

                    //reset opacity when piece is removed from a box
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }

            }

            //draw the active piece in the end, so it won't be hidden by the board or the colored square
            activePiece.draw(g2d);
        }

        //STATUS MESSAGES
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        g2d.setColor(Color.WHITE);

        // displaying promotion pieces
        if(promotion){
            g2d.drawString("Promoting to :", 815, 150);
            for(Piece piece : promotionPieces){
                g2d.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row),
                        Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
            }
        } else {
            if(currentColor == WHITE){
                g2d.drawString("White's turn", 840, 550);
                if(checkPiece != null && checkPiece.color == BLACK){
                    g2d.setColor(Color.RED);
                    g2d.drawString("The King", 840, 650);
                    g2d.drawString("is in Check!", 840, 700);
                }
            } else {
                g2d.drawString("Black's turn", 840, 250);
                if(checkPiece != null && checkPiece.color == WHITE){
                    g2d.setColor(Color.RED);
                    g2d.drawString("The King", 840, 100);
                    g2d.drawString("is in Check!", 840, 150);
                }
            }
            if(gameOver){
                String s ="";
                if(currentColor == WHITE){
                    s = "WHITE WINS";
                } else {
                    s = "BLACK WINS";
                }
                g2d.setFont(new Font("Arial", Font.PLAIN, 40));
                g2d.setColor(Color.RED);
                g2d.drawString("Game", 840, 300);
                g2d.drawString("Over !!", 840, 350);
                g2d.setFont(new Font("Arial", Font.BOLD, 40));
                g2d.setColor(Color.GREEN);
                g2d.drawString(s, 200, 420);
            }
        }
    }
}
