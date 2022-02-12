package ru.kirill.chess;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rook extends Figure{
    public Rook(String color) throws FileNotFoundException {
        super(color);
    }

    @Override
    public List<List<Integer>> calculatePossibleMoves(List<Integer> figureCoordinates, Board board) {
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
                    possibleMoves.add(Arrays.asList(currentRow, currentColumn));
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
