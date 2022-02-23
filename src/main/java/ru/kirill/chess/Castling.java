package ru.kirill.chess;

import java.util.Arrays;
import java.util.List;

public class Castling {
    private Player player;
    private List<Integer> castlingPoint;

    public Castling(Player player, List<Integer> castlingPoint){
        this.player = player;
        this.castlingPoint = castlingPoint;
    }

    public boolean castle(Board board){
        Figure rook = defineRook(board);
        if(!(rook instanceof Rook) || !checkKing()){
            return false;
        }
        Board copyBoard = new Board();
        copyBoard.setFields(board.getFields());
        int variance = castlingPoint.get(1) - board.getElementCoordinates(player.myKing).get(1);
        List<Integer> rookCoords = processNewRookCoords(getBuffer(variance), (Rook) rook, board);
        if(processNewRookCoords(getBuffer(variance), (Rook) rook, board) != null &&
                canKingCastle(board.getElementCoordinates(player.myKing).get(1) + 1, castlingPoint.get(1) + 1, board)) {
            board.setCell(board.getElementCoordinates(player.myKing).get(0), board.getElementCoordinates(player.myKing).get(1), null);
            board.setCell(castlingPoint.get(0),castlingPoint.get(1), player.myKing);

            board.setCell(board.getElementCoordinates(rook).get(0), board.getElementCoordinates(rook).get(1), null);
            board.setCell(rookCoords.get(0), rookCoords.get(1), rook);
            return true;
        }
        else {
            return false;
        }
    }

    private Figure defineRook(Board board){
        return player.getRook(castlingPoint, board);
    }

    private boolean checkKing(){
        return !player.myKing.isMoved && !player.myKing.isChecked;
    }

    private int getBuffer(int variance){
        if(variance > 0){
            return -1;
        }
        else{
            return 1;
        }
    }
    private List<Integer> processNewRookCoords(int buffer, Rook rook, Board board){
        List<List<Integer>> moves = rook.calculatePossibleMoves(board.getElementCoordinates(rook), board);
        List<Integer> newRookCoords = Arrays.asList(castlingPoint.get(0), castlingPoint.get(1) + buffer);
        if (moves.contains(newRookCoords)){
            return newRookCoords;
        }
        else {
            return null;
        }
    }

    private boolean canKingCastle(int startIndex, int finalIndex, Board board){
        for (int i = startIndex; i < finalIndex; i++) {
            if (board.getFields().get(castlingPoint.get(0)).get(i) != null || player.checkForCheck(Arrays.asList(castlingPoint.get(0), i), board)){
                return false;
            }
        }
        return true;
    }
}
