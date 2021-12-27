package ru.kirill.chess;

import java.io.FileNotFoundException;

public class Pawn extends Figure{
    private boolean isMoved = false;

    public Pawn(String color) throws FileNotFoundException {
        super(color);
    }

    @Override
    public void calculatePossibleMoves() {

    }
}
