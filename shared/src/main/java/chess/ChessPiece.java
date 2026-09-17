package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public boolean inBounds(int[] move, ChessPosition myPosition){
        int nowR = myPosition.getRow();
        int nowC = myPosition.getColumn();
        if(1 <= (nowR + move[0]) && (nowR + move[0]) <= 8 && 1 <= (nowC + move[1]) && (nowC + move[1]) <= 8){
            return true;
        }
        return false;
    }

    public boolean blocked(ChessPosition myPosition, ChessPosition newPosition, ChessBoard board){
        ChessPiece new_piece = board.getPiece(newPosition);
        ChessPiece piece = board.getPiece(myPosition);
        if(new_piece==null){
            return false;
        }
        else if(new_piece.getTeamColor()==piece.getTeamColor()){
            return true;
        }
        else {
            return false;
        }
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        int [][] moves;
        boolean repeatable;
        if(piece.getPieceType() == PieceType.BISHOP){
            moves = new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}};
            repeatable = true;
        }
        else if(piece.getPieceType() == PieceType.ROOK){
            moves = new int[][]{{0,1},{0,-1},{-1,0},{1,0}};
            repeatable = true;
        }
        else if(piece.getPieceType() == PieceType.KNIGHT){
            moves = new int[][]{{2,1},{1,-2},{-2,1},{-2,-1}};
            repeatable = false;
        }
        else if(piece.getPieceType() == PieceType.KING){
            moves = new int[][]{{1,1},{1,-1},{-1,1},{-1,-1},{0,1},{0,-1},{-1,0},{1,0}};
            repeatable = false;
        }
        else if(piece.getPieceType() == PieceType.QUEEN){
            moves = new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}};
            repeatable = true;
        }
        else {
            moves = new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}};
            repeatable = true;
        }

        Collection<ChessMove> list = new ArrayList<>();
        if(!repeatable) {
            for (int[] move : moves) {
                if(inBounds(move, myPosition)){
                    ChessPosition p = new ChessPosition(myPosition.getRow() + move[0], myPosition.getColumn() + move[1]);
                    if (!blocked(myPosition, p, board)){
                        ChessMove m = new ChessMove(myPosition, p, null);
                        list.add(m);
                    }

                }

            }
        }


        return list;
    }
}
