package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    public void removePiece(ChessPosition position) {
        board[position.getRow()-1][position.getColumn()-1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "board=" + Arrays.toString(board) +
                '}';
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece bp = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece wp = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece br = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece wr = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece bb = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece wb = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece bkn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece wkn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece bq = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece wq = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece bk = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece wk = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        for(int i = 1; i < 9; i++){
            ChessPosition c = new ChessPosition(7,i);
            addPiece(c,bp);
        }
        for(int i = 1; i < 9; i++){
            ChessPosition c = new ChessPosition(2,i);
            addPiece(c,wp);
        }
        ChessPosition cr = new ChessPosition(1,1);
        addPiece(cr,wr);
        ChessPosition cr2 = new ChessPosition(1,8);
        addPiece(cr2,wr);
        ChessPosition wkn1 = new ChessPosition(1,2);
        addPiece(wkn1,wkn);
        ChessPosition wkn2 = new ChessPosition(1,7);
        addPiece(wkn2,wkn);
        ChessPosition wr1 = new ChessPosition(1,3);
        addPiece(wr1,wb);
        ChessPosition wr2 = new ChessPosition(1,6);
        addPiece(wr2,wb);
        ChessPosition wq1 = new ChessPosition(1,4);
        addPiece(wq1,wq);
        ChessPosition wk1 = new ChessPosition(1,5);
        addPiece(wk1,wk);

        ChessPosition br1 = new ChessPosition(8,1);
        addPiece(br1,br);
        ChessPosition br2 = new ChessPosition(8,8);
        addPiece(br2,br);
        ChessPosition bkn1 = new ChessPosition(8,2);
        addPiece(bkn1,bkn);
        ChessPosition bkn2 = new ChessPosition(8,7);
        addPiece(bkn2,bkn);
        ChessPosition bb1 = new ChessPosition(8,3);
        addPiece(bb1,bb);
        ChessPosition bb2 = new ChessPosition(8,6);
        addPiece(bb2,bb);
        ChessPosition bq1 = new ChessPosition(8,4);
        addPiece(bq1,bq);
        ChessPosition bk1 = new ChessPosition(8,5);
        addPiece(bk1,bk);
    }
}
