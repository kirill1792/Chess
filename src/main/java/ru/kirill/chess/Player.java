package ru.kirill.chess;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    public Figure selectedFigure = null;
    public ArrayList<Figure> myFigures = null;
    public King myKing;
    public String name;
    public List<Integer> shortCastlingPoint;
    public List<Integer> longCastlingPoint;

    public Player(String name) {
        this.name = name;
    }

    public void setMyKing() {
        for (Figure figure : myFigures) {
            if (figure instanceof King) {
                myKing = (King) figure;
                break;
            }
        }
    }

    public Figure getRook(List<Integer> castlingPoint, Board board) {
        if (this.myKing.color.equals("white")) {
            if (castlingPoint.equals(shortCastlingPoint)) {
                return board.getFields().get(7).get(7);
            } else {
                return board.getFields().get(7).get(0);
            }
        } else {
            if (castlingPoint.equals(shortCastlingPoint)) {
                return board.getFields().get(7).get(0);
            } else {
                return board.getFields().get(7).get(7);
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

    public MoveResult makeMove(int row, int column, Board board) {
        if (myFigures.contains(board.getFields().get(row).get(column))) {
            setSelectedFigure(board.getFields().get(row).get(column));
            System.out.println("Установлена фигура");
            return new MoveResult(false, null);
        } else if (board.isEmptyField(row, column)) {
            if (checkSelectedFigure()) {
                List<Integer> coordinates = board.getElementCoordinates(selectedFigure);
                if (selectedFigure instanceof King && Arrays.asList(row, column).equals(shortCastlingPoint) | Arrays.asList(row, column).equals(longCastlingPoint) && !myKing.isMoved) {
                    System.out.println("Пробуем сделать рокировку");
                    List<Integer> castlingPoint;
                    if (Arrays.asList(row, column).equals(shortCastlingPoint)) {
                        castlingPoint = shortCastlingPoint;
                    } else {
                        castlingPoint = longCastlingPoint;
                    }
                    Castling castling = new Castling(this, castlingPoint);
                    boolean result = castling.castle(board);
                    if (result) {
                        System.out.println("Рокируемся!");
                        myKing.isMoved = true;
                        return new MoveResult(true, null);
                    } else {
                        System.out.println("Рокировка невозможна");
                        return new MoveResult(false, null);
                    }
                }
                if (canMove(Arrays.asList(row, column), board, selectedFigure)) {
                    System.out.println("Выполняю ход фигурой: " + selectedFigure + " " + "На поле:" + row + " " + column);
                    board.setCell(coordinates.get(0), coordinates.get(1), null);
                    board.setCell(row, column, selectedFigure);
                    if(selectedFigure instanceof Pawn pawn){
                        pawn.prevRow = coordinates.get(0);
                        pawn.prevColumn = coordinates.get(1);
                    }
                    selectedFigure.isMoved = true;
                    selectedFigure = null;
                    myKing.isChecked = false;
                    return new MoveResult(true, null);
                } else {
                    System.out.println("Так нельзя ходить");
                    return new MoveResult(false, null);
                }
            } else {
                System.out.println("Нельзя походить: нет выбранной фигуры");
                return new MoveResult(false, null);
            }
        } else {
            if (checkSelectedFigure()) {
                System.out.println("Пробую побить фигуру");
                ArrayList<Integer> coordinates = board.getElementCoordinates(selectedFigure);
                if (canMove(Arrays.asList(row, column), board, selectedFigure)) {
                    System.out.println("Выполняю ход фигурой: " + selectedFigure + " " + "На поле:" + row + " " + column);
                    Figure oldFig = board.getFields().get(row).get(column);
                    board.setCell(coordinates.get(0), coordinates.get(1), null);
                    board.setCell(row, column, selectedFigure);
                    if(selectedFigure instanceof Pawn pawn){
                        pawn.prevRow = coordinates.get(0);
                        pawn.prevColumn = coordinates.get(1);
                    }
                    selectedFigure.isMoved = true;
                    selectedFigure = null;
                    myKing.isChecked = false;
                    return new MoveResult(true, oldFig);
                } else {
                    System.out.println("Так нельзя ходить");
                    return new MoveResult(false, null);
                }
            } else {
                System.out.println("Нельзя побить фигуру: нет выбранной фигуры");
                return new MoveResult(false, null);
            }
        }
    }

    public boolean checkForCheck(List<Integer> figureCoordinates, Board board) {
        List<List<Integer>> totalEnemyMoves = new ArrayList<>();
        for (Figure enemyFigure : getEnemyFigures(board)) {
            List<List<Integer>> moves = enemyFigure.calculatePossibleMoves(board.getElementCoordinates(enemyFigure), board);
            totalEnemyMoves.addAll(moves);
        }
        if (findMatch(figureCoordinates, totalEnemyMoves)) {
            System.out.println("Поле бьётся");
            return true;
        } else {
            return false;
        }
    }

    private boolean findMatch(List<Integer> kingCoords, List<List<Integer>> enemyFiguresMoves) {
        for (List<Integer> move : enemyFiguresMoves) {
            if (move.equals(kingCoords)) {
                return true;
            }
        }
        return false;
    }

    private List<Figure> getEnemyFigures(Board board) {
        String needColor;
        if (this.myKing.color.equals("white")) {
            needColor = "black";
        } else {
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
                copyBoard.setCell(coordinatesToMove.get(0), coordinatesToMove.get(1), figure);
                if (checkForCheck(getKingCoords(copyBoard), copyBoard)) {
                    System.out.println("Тут будет шах!");
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkmate(Board board) {
        for (Figure myFigure : myFigures) {
            for (List<Integer> coords : myFigure.calculatePossibleMoves(board.getElementCoordinates(myFigure), board)) {
                boolean result = canMove(coords, board, myFigure);
                if (result) {
                    return false;
                }
            }
        }
        return true;
    }

    //private boolean checkForPawn(Figure selectedFigure, int newRow) {
        //return selectedFigure instanceof Pawn && newRow == 0;
    //}
}