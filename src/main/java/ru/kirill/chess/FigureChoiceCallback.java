package ru.kirill.chess;

public interface FigureChoiceCallback {
    void receiveChosenFigure(Class<? extends Figure> figureType, String color);
}
