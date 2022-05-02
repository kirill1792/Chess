package ru.kirill.chess;

public class Castling1 {
    public Board board;
    public King king;
    public Rook rook;

    public Castling1(Board board, King king, Rook rook){
        this.board  = board;
        this.king = king;
        this.rook = rook;
    }

    public boolean castle(Coordinates newCoords, Player player){
        Coordinates newRookCoords = calculateNewRookCoords(newCoords);
        Coordinates kingCoords = board.getElementCoordinates(king);
        Coordinates rookCoords = board.getElementCoordinates(rook);
        if(newRookCoords != null && checkKing(newCoords, player)){
            board.setCell(kingCoords.getRow(), kingCoords.getColumn(), null);
            board.setCell(newCoords.getRow(), newCoords.getColumn(), king);
            board.setCell(rookCoords.getRow(), rookCoords.getColumn(), null);
            board.setCell(newRookCoords.getRow(), newRookCoords.getColumn(), rook);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkKing(Coordinates newCoords, Player player){
        Coordinates startKingCoords = board.getElementCoordinates(king);
        Board copyBoard = new Board();
        copyBoard.setFields(board.getFields());
        int buff = getBuffer(newCoords.getColumn() - startKingCoords.getColumn());
        for (int i = startKingCoords.getColumn() + buff; i < newCoords.getColumn() + buff; i += buff){
            if(!copyBoard.isEmptyField(startKingCoords.getRow(), i)){
                return false;
            }
            copyBoard.setCell(startKingCoords.getRow(), i, king);
            if (player.checkForCheck(new Coordinates(startKingCoords.getRow(), i), copyBoard)){
                return false;
            }
        }
        return true;
    }

    private int getBuffer(int variance){
        if(variance > 0){
        return 1;
        }
        else {
            return -1;
        }
    }
    private Coordinates calculateNewRookCoords(Coordinates newCoords){
        Coordinates oldRookCoords = board.getElementCoordinates(rook);
        if (oldRookCoords.getRow() == -1 && oldRookCoords.getColumn() == -1){
            return null;
        }
        Coordinates newRookCoords = new Coordinates(newCoords.getRow(), newCoords.getColumn() + getBuffer(newCoords.getColumn() - oldRookCoords.getColumn()));
        if(rook.calculatePossibleMoves(oldRookCoords, board).contains(newRookCoords)){
            return newRookCoords;
        }
        return null;
    }

    public static boolean kingCondition(boolean isChecked, boolean isMoved){
        return !isChecked && !isMoved;
    }
}
