package ru.kirill.chess;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Queen extends Figure {
    public Queen(String color) throws FileNotFoundException {
        super(color);
    }

    @Override
    public List<List<Integer>> calculatePossibleMoves(List<Integer> figureCoordinates, Board board) {
        List<List<Integer>> diagonalMoves = calculateDiagonalMoves(figureCoordinates, board);
        List<List<Integer>> straightMoves = calculateStraightMoves(figureCoordinates, board);
        diagonalMoves.addAll(straightMoves);
        return diagonalMoves;
    }

    private List<List<Integer>> calculateDiagonalMoves(List<Integer> figureCoordinates, Board board){
        ArrayList<List<Integer>> possibleMoves = new ArrayList<>();
        int startRow = figureCoordinates.get(0);
        int startColumn = figureCoordinates.get(1);
        int[][] directions = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};

        for (int i = 0; i < 4; i++) {
            int rowBuff = directions[i][0];
            int columnBuff = directions[i][1];
            int currentRow = startRow + rowBuff;
            int currentColumn = startColumn + columnBuff;
            for (int j = 0; j < 8; j++) {
                if (currentRow > 7 | currentRow < 0 | currentColumn > 7 | currentColumn < 0) {
                    break;
                }
                else if (board.getFields().get(currentRow).get(currentColumn) != null) {
                    Figure figure = board.getFields().get(currentRow).get(currentColumn);
                    if (!figure.color.equals(this.color)) {
                        possibleMoves.add(Arrays.asList(currentRow, currentColumn));
                    }
                    break;
                }
                possibleMoves.add(Arrays.asList(currentRow, currentColumn));
                currentRow += rowBuff;
                currentColumn += columnBuff;
            }
        }
        return possibleMoves;
    }

    private List<List<Integer>> calculateStraightMoves(List<Integer> figureCoordinates, Board board) {
        ArrayList<List<Integer>> possibleMoves = new ArrayList<>();
        int startRow = figureCoordinates.get(0);
        int startColumn = figureCoordinates.get(1);
        int[] iterations = {startRow, 7 - startColumn, 7 - startRow, startColumn};
        int[] dirBuffs = {-1, 1, 1,-1};
        String currentDir = "row";

        for (int i = 0; i < 4; i++) {
            int currentRow = startRow;
            int currentColumn = startColumn;
            for (int j = 0; j < iterations[i]; j++) {

                if(currentDir.equals("row")) {
                    currentRow += dirBuffs[i];
                }
                else {
                    currentColumn += dirBuffs[i];
                }

                if (board.getFields().get(currentRow).get(currentColumn) != null) {
                    Figure figure = board.getFields().get(currentRow).get(currentColumn);
                    if (!figure.color.equals(this.color)) {
                        possibleMoves.add(Arrays.asList(currentRow, currentColumn));
                    }
                    break;
                }
                possibleMoves.add(Arrays.asList(currentRow, currentColumn));
            }
            if(currentDir.equals("row")) {
                currentDir = "column";
            }
            else {
                currentDir = "row";
            }
        }
        return possibleMoves;
    }
}
