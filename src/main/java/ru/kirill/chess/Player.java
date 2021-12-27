package ru.kirill.chess;

import java.util.ArrayList;

public class Player {
    public Figure selectedFigure = null;
    public ArrayList<Figure> myFigures = null;
    public String name;

    public Player(String name) {
        this.name = name;
    }


    //public boolean isMyFigure(Figure figure) {
        //return myFigures.contains(figure);
   // }

    private boolean checkSelectedFigure() {
        return !(selectedFigure == null);
    }

    private void setSelectedFigure(Figure figure) {
        selectedFigure = figure;
    }

    public boolean makeMove(int row, int column, Board board) {
         if (myFigures.contains(board.getFields().get(row).get(column))) {
             setSelectedFigure(board.getFields().get(row).get(column));
             System.out.println("Установлена фигура");
             return false;
         }
         else if (board.getFields().get(row).get(column) == null) {
             if (checkSelectedFigure()) {
                 System.out.println("Выполняю ход фигурой: " + selectedFigure + " " + "На поле:" + row + " " + column);
                 ArrayList<Integer> coordinates = board.getElementCoordinates(selectedFigure);
                 board.setCell(coordinates.get(0), coordinates.get(1), null);
                 board.setCell(row, column, selectedFigure);
                 selectedFigure = null;
                 return true;
             }
             else {
                 System.out.println("Нельзя походить: нет выбранной фигуры");
                 return false;
             }
         }
         else {
             if (checkSelectedFigure()) {
                 System.out.println("Пробую побить фигуру");
                 return false;
             }
             else {
                 System.out.println("Нельзя побить фигуру: нет выбранной фигуры");
                 return false;
             }
         }
    }
}
