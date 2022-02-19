package ru.kirill.chess;

public class MoveResult {
    public boolean moved;
    public Figure beatenFigure;

    public MoveResult(boolean moved, Figure beatenFigure){
        this.moved = moved;
        this.beatenFigure = beatenFigure;
    }
}
