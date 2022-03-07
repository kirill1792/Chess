package ru.kirill.chess;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bishop extends Figure{
    public Bishop(String color) throws FileNotFoundException {
        super(color);
    }

    @Override
    public List<List<Integer>> calculatePossibleMoves(List<Integer> figureCoordinates, Board board) {
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
                if (!board.checkOutOfBounds(currentRow, currentColumn)) {
                    break;
                }
                else if (!board.isEmptyField(currentRow, currentColumn)) {
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
}
