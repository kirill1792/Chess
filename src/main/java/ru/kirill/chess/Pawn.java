package ru.kirill.chess;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pawn extends Figure{
    private boolean isMoved = false;

    public Pawn(String color) throws FileNotFoundException {
        super(color);
    }

    @Override
    public List<List<Integer>> calculatePossibleMoves(List<Integer> figureCoordinates, Board board) {
        ArrayList<List<Integer>> possibleMoves = new ArrayList<>();
        if(color.equals("white")) {
            if(board.getFields().get(figureCoordinates.get(0) - 1).get(figureCoordinates.get(1)) == null) {
                possibleMoves.add(Arrays.asList(figureCoordinates.get(0) - 1, figureCoordinates.get(1)));
            }
            if (!isMoved) {
                if(board.getFields().get(figureCoordinates.get(0) - 2).get(figureCoordinates.get(1)) == null) {
                    possibleMoves.add(Arrays.asList(figureCoordinates.get(0) - 2, figureCoordinates.get(1)));
                }
            }
            possibleMoves.addAll(checkBeating(figureCoordinates, -1, board));
        }
        else {
            if(board.getFields().get(figureCoordinates.get(0) + 1).get(figureCoordinates.get(1)) == null) {
                possibleMoves.add(Arrays.asList(figureCoordinates.get(0) + 1, figureCoordinates.get(1)));
            }
            if (!isMoved) {
                if(board.getFields().get(figureCoordinates.get(0) + 2).get(figureCoordinates.get(1)) == null) {
                    possibleMoves.add(Arrays.asList(figureCoordinates.get(0) + 2, figureCoordinates.get(1)));
                }
            }
            possibleMoves.addAll(checkBeating(figureCoordinates, 1, board));
        }
        System.out.println("Pawn possible moves:" + possibleMoves);
        return possibleMoves;
    }

    //@Override
    public boolean canMove(List<Integer> coordinatesToMove, List<Integer> selfCoordinates, Board board) {
        List<List<Integer>> result = calculatePossibleMoves(selfCoordinates, board);
        for (List<Integer> integers : result) {
            if (integers.equals(coordinatesToMove)) {
                isMoved = true;
                return true;
            }
        }
        return false;
    }

    private ArrayList<List<Integer>> checkBeating(List<Integer> figureCoordinates, int buffer, Board board) {
        ArrayList<List<Integer>> possibleBeats = new ArrayList<>();
        if (figureCoordinates.get(1) - 1 >= 0 && board.getFields().get(figureCoordinates.get(0) + buffer).get(figureCoordinates.get(1) - 1) != null) {
            if(!board.getFields().get(figureCoordinates.get(0) + buffer).get(figureCoordinates.get(1) - 1).color.equals(this.color)) {
                possibleBeats.add(Arrays.asList(figureCoordinates.get(0) + buffer, figureCoordinates.get(1) - 1));
            }

        }
        if (figureCoordinates.get(1) + 1 <= 7 && board.getFields().get(figureCoordinates.get(0) + buffer).get(figureCoordinates.get(1) + 1) != null) {
            if (!board.getFields().get(figureCoordinates.get(0) + buffer).get(figureCoordinates.get(1) + 1).color.equals(this.color)) {
                possibleBeats.add(Arrays.asList(figureCoordinates.get(0) + buffer, figureCoordinates.get(1) + 1));
            }
        }
        System.out.println("Pawn beating" + possibleBeats);
        return possibleBeats;
    }
}
