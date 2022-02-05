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
        int startRow = figureCoordinates.get(0) - 2;
        int startColumn = figureCoordinates.get(1) - 2;
        int[] buffers = {1, 1,-1,-1};
        boolean movePoint = false;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(movePoint){
                    if(startRow >= 0 & startRow <= 7 & startColumn >= 0 & startColumn <= 7){
                        if(board.getFields().get(startRow).get(startColumn) != null) {
                            if(!board.getFields().get(startRow).get(startColumn).color.equals(this.color)) {
                                possibleMoves.add(Arrays.asList(startRow, startColumn));
                            }
                        }
                        else {
                            possibleMoves.add(Arrays.asList(startRow, startColumn));
                        }
                    }
                    movePoint = false;
                }
                else {
                    movePoint = true;
                    }

                if(i % 2 == 0) {
                    startColumn += buffers[i];
                   }
                else {
                    startRow += buffers[i];
                    }
                }
            }
        return possibleMoves;
        }
    }
