package ru.kirill.chess;

public class MoveResult {
    public Figure movedFig;
    public Figure beatenFigure;
    public Figure newFigure;
    public CastlingType castlingType;
    public String correctiveFactor;

    public MoveResult(Figure movedFig, Figure beatenFigure, String correctiveFactor) {
        this.correctiveFactor = correctiveFactor;
        this.movedFig = movedFig;
        this.beatenFigure = beatenFigure;
    }

    public MoveResult(Figure movedFig, Figure beatenFigure, Figure newFigure, String correctiveFactor){
        this.correctiveFactor = correctiveFactor;
        this.movedFig = movedFig;
        this.beatenFigure = beatenFigure;
        this.newFigure = newFigure;
    }

    public MoveResult(Figure movedFig, Figure beatenFigure, CastlingType castlingType){
        this.movedFig = movedFig;
        this.beatenFigure = beatenFigure;
        this.castlingType = castlingType;
    }
}
