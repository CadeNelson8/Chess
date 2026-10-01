package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import chess.ChessPiece;

import chess.ChessBoard;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessGame.TeamColor teamTurn = TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //ChessBoard boardcopy = new ChessBoard(board);

        ChessPiece m = board.getPiece(startPosition);
        Collection<ChessMove> list = m.pieceMoves(board, startPosition);
        TeamColor color = m.getTeamColor();
        board.removePiece(startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move : list){
            //if move would leave king in check, remove from list
            board.addPiece(move.getEndPosition(), m);
            if(!isInCheckHelper(color,board)){
                validMoves.add(move);
            }
            board.removePiece(move.getEndPosition());
        }
        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

    }

    public ChessPosition getKingPosition(TeamColor teamColor){
        for(int i = 1; i < 9; i++){
            for(int k = 1; k < 9; k++){
                ChessPosition c = new ChessPosition(i, k);
                if(board.getPiece(c)!=null && board.getPiece(c).getPieceType()== ChessPiece.PieceType.KING && board.getPiece(c).getTeamColor() == teamColor){
                    return c;
                }
            }
        }
        return null;
    }

    public Collection<ChessPosition> getEnemyPlaces(TeamColor teamColor){
        Collection<ChessPosition> list = new ArrayList<>();
        for(int i = 1; i < 9; i++){
            for(int k = 1; k < 9; k++){
                ChessPosition c = new ChessPosition(i, k);
                if(board.getPiece(c)!=null && board.getPiece(c).getTeamColor() != teamColor){
                    list.add(c);
                }
            }
        }
        return list;
    }

    /*
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheckHelper(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingpos = getKingPosition(teamColor);
        //iterate through all enemy chess pieces. If one of them has kingpos in their move list, return true. False otherwise
        Collection <ChessPosition> enemyPlaces = getEnemyPlaces(teamColor);
        for (ChessPosition place : enemyPlaces){
            Collection<ChessMove> moves = board.getPiece(place).pieceMoves(board, place);
            for(ChessMove move : moves){
                if(move.getEndPosition().equals(kingpos)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingpos = getKingPosition(teamColor);
        //iterate through all enemy chess pieces. If one of them has kingpos in their move list, return true. False otherwise
        Collection <ChessPosition> enemyPlaces = getEnemyPlaces(teamColor);
        for (ChessPosition place : enemyPlaces){
            Collection<ChessMove> moves = board.getPiece(place).pieceMoves(board, place);
            for(ChessMove move : moves){
                if(move.getEndPosition().equals(kingpos)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //if isInCheck is true and all other moves are blocked or would be in check, return true
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //if isInCheck is false and all other moves are blocked or would be in check, return true
        return true;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return board.equals(chessGame.board) && teamTurn.equals(chessGame.getTeamTurn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                '}';
    }
}
