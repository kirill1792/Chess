package ru.kirill.chess;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Knight extends Figure{
    public Knight(String color) throws FileNotFoundException {
        super(color);
    }

    @Override
    public List<List<Integer>> calculatePossibleMoves(List<Integer> figureCoordinates, Board board) {
        ArrayList<List<Integer>> possibleMoves = new ArrayList<>();
        int startRow = figureCoordinates.get(0);
        int startColumn = figureCoordinates.get(1);
        int[] directions = {-2, 2, 2,-2};
        int valueToBuff = startRow;
        String dirToBuf = "column";

        for (int i = 0; i < 4; i++) {
            valueToBuff += directions[i];
            if(dirToBuf.equals("column")){
                possibleMoves.add(Arrays.asList(valueToBuff, startColumn - 1));
                possibleMoves.add(Arrays.asList(valueToBuff, startColumn + 1));

                dirToBuf = "row";
                valueToBuff = startColumn;
            }
            else {
                possibleMoves.add(Arrays.asList(startRow - 1, valueToBuff));
                possibleMoves.add(Arrays.asList(startRow + 1, valueToBuff));

                dirToBuf = "column";
                valueToBuff = startRow;
            }
        }
        return possibleMoves;
    }
}
