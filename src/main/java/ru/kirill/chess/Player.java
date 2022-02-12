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
                 List<Integer> coordinates = board.getElementCoordinates(selectedFigure);
                 if (canMove(Arrays.asList(row, column), board, selectedFigure)) {
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
                 if (canMove(Arrays.asList(row, column), board, selectedFigure)) {
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

    public boolean canMove(List<Integer> coordinatesToMove, Board board, Figure figure) {
        List<Integer> selfCoordinates = board.getElementCoordinates(figure);
        List<List<Integer>> result = figure.calculatePossibleMoves(selfCoordinates, board);
        System.out.println(result);
        for (List<Integer> integers : result) {
            if (integers.equals(coordinatesToMove)) {
                Board copyBoard = new Board();
                copyBoard.setFields(board.getFields());
                copyBoard.setCell(selfCoordinates.get(0), selfCoordinates.get(1), null);
                copyBoard.setCell(coordinatesToMove.get(0), coordinatesToMove.get(1), selectedFigure);
                if(checkForCheck(getKingCoords(copyBoard), copyBoard)){
                    System.out.println("Тут будет шах!");
                    return false;
                }
                else {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkmate(Board board){
        for (Figure myFigure: myFigures) {
            for (List<Integer> coords: myFigure.calculatePossibleMoves(board.getElementCoordinates(myFigure), board)){
                if (!(board.getFields().get(coords.get(0)).get(coords.get(1)) != null && board.getFields().get(coords.get(0)).get(coords.get(1)).color.equals(this.myKing.color))){
                    boolean result = canMove(coords, board, myFigure);
                    if (result){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

/*System.out.println("Выполняю ход фигурой: " + selectedFigure + " " + "На поле:" + row + " " + column);
        ArrayList<Integer> coordinates = board.getElementCoordinates(selectedFigure);
        board.setCell(coordinates.get(0), coordinates.get(1), null);
        board.setCell(row, column, selectedFigure);
        selectedFigure = null;
        return true;*/