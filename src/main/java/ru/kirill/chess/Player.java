package ru.kirill.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    public Figure selectedFigure = null;
    public ArrayList<Figure> myFigures = null;
    public King myKing;
    public String name;

    public Player(String name) {
        this.name = name;
    }


    //public boolean isMyFigure(Figure figure) {
        //return myFigures.contains(figure);
   //}


    public void setMyKing() {
        for (Figure figure: myFigures) {
            if(figure instanceof King) {
                myKing = (King) figure;
                break;
            }
        }
    }

    public List<Integer> getKingCoords(Board board) {
        return board.getElementCoordinates(this.myKing);
    }

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
                 ArrayList<Integer> coordinates = board.getElementCoordinates(selectedFigure);
                 if (selectedFigure.canMove(Arrays.asList(row, column), coordinates, board)) {
                     System.out.println("Выполняю ход фигурой: " + selectedFigure + " " + "На поле:" + row + " " + column);
                     board.setCell(coordinates.get(0), coordinates.get(1), null);
                     board.setCell(row, column, selectedFigure);
                     selectedFigure = null;
                     return true;
                 }
                 else {
                     System.out.println("Так нельзя ходить");
                     return false;
                 }
             }
             else {
                 System.out.println("Нельзя походить: нет выбранной фигуры");
                 return false;
             }
         }
         else {
             if (checkSelectedFigure()) {
                 System.out.println("Пробую побить фигуру");
                 ArrayList<Integer> coordinates = board.getElementCoordinates(selectedFigure);
                 if (selectedFigure.canMove(Arrays.asList(row, column), coordinates, board)) {
                     System.out.println("Выполняю ход фигурой: " + selectedFigure + " " + "На поле:" + row + " " + column);
                     board.setCell(coordinates.get(0), coordinates.get(1), null);
                     board.getFields().get(row).get(column).getMyImage().setVisible(false);
                     board.setCell(row, column, selectedFigure);
                     selectedFigure = null;
                     return true;
                 }
                 else {
                     System.out.println("Так нельзя ходить");
                     return false;
                 }
             }
             else {
                 System.out.println("Нельзя побить фигуру: нет выбранной фигуры");
                 return false;
             }
         }
    }

    public boolean checkForCheck(List<Integer> figureCoordinates, Board board) {
        List<List<Integer>> totalEnemyMoves = new ArrayList<>();
        for(Figure enemyFigure : getEnemyFigures(board)) {
            List<List<Integer>> moves = enemyFigure.calculatePossibleMoves(board.getElementCoordinates(enemyFigure), board);
            totalEnemyMoves.addAll(moves);
        }
        if(findMatch(figureCoordinates, totalEnemyMoves)){
            System.out.println("Поле бьётся");
            return true;
        }
        else {
            return false;
        }
    }

    private boolean findMatch(List<Integer> kingCoords, List<List<Integer>> enemyFiguresMoves) {
        for (List<Integer> move: enemyFiguresMoves) {
            if(move.equals(kingCoords)) {
                return true;
            }
        }
        return false;
    }

    private List<Figure> getEnemyFigures(Board board){
        String needColor;
        if(this.myKing.color.equals("white")){
            needColor = "black";
        }
        else {
            needColor = "white";
        }
        return board.getFiguresByColor(needColor);
    }
}

/*System.out.println("Выполняю ход фигурой: " + selectedFigure + " " + "На поле:" + row + " " + column);
        ArrayList<Integer> coordinates = board.getElementCoordinates(selectedFigure);
        board.setCell(coordinates.get(0), coordinates.get(1), null);
        board.setCell(row, column, selectedFigure);
        selectedFigure = null;
        return true;*/