package ru.kirill.chess;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pawn extends Figure {
    public int direction;
    private Figure chosenFigure;
    public int prevRow;
    public int prevColumn;

    public Pawn(String color, int direction) throws FileNotFoundException {
        super(color);
        this.direction = direction;
    }

    @Override
    public List<List<Integer>> calculatePossibleMoves(List<Integer> figureCoordinates, Board board) {
        ArrayList<List<Integer>> possibleMoves = new ArrayList<>();
        int row = figureCoordinates.get(0);
        int column = figureCoordinates.get(1);
        if (board.checkOutOfBounds(row + direction, column) &&
                board.isEmptyField(row + direction, column)) {
            possibleMoves.add(Arrays.asList(row + direction, column));

            if (board.checkOutOfBounds(row + direction * 2, column) &&
                    board.isEmptyField(row + direction * 2, column) && !this.isMoved) {
                possibleMoves.add(Arrays.asList(row + direction * 2, column));
            }
        }
        if(checkBeat(row + direction, column + 1, board)){
            possibleMoves.add(Arrays.asList(row + direction, column + 1));
        }
        if(checkBeat(row + direction, column - 1, board)){
            possibleMoves.add(Arrays.asList(row + direction, column - 1));
        }
        return possibleMoves;
    }

    public void changeDirection() {
        direction *= -1;
    }

    private boolean checkBeat(int row, int column, Board board) {
        return board.checkOutOfBounds(row, column) && !board.isEmptyField(row, column) && !board.getFields().get(row).get(column).color.equals(this.color);
    }

    public Figure getChosenFigure() {
        return chosenFigure;
    }

    public void setChosenFigure(Figure chosenFigure) {
        this.chosenFigure = chosenFigure;
    }
}
