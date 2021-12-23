package ru.kirill.chess;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;

public class Queen extends Figure {
    public Queen(String color) throws FileNotFoundException {
        super(color);
    }

    @Override
    public void calculatePossibleMoves() {

    }
}
