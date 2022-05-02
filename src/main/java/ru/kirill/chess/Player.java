package ru.kirill.chess;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    public Figure selectedFigure = null;
    public ArrayList<Figure> myFigures = null;
    public King myKing;
    public String name;
    private int castlingBoost = 2;
    public Rook shortCastingRook;
    public Rook longCastlingRook;

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

    public Coordinates getKingCoords(Board board) {
        return board.getElementCoordinates(this.myKing);
    }

    private boolean checkSelectedFigure() {
        return !(selectedFigure == null);
    }

    private void setSelectedFigure(Figure figure) {
        selectedFigure = figure;
    }

    public void reverseBoost(){
        castlingBoost *= -1;
    }

    private Rook defineRook(CastlingType castlingType){
        if (castlingType.equals(CastlingType.SHORT)){
            return shortCastingRook;
        }
        else {
            return longCastlingRook;
        }
    }

    public MoveResult makeMove(int row, int column, Board board, String boardNums, String boardLetters) throws FileNotFoundException {
        Figure movedFigure = null;
        if (myFigures.contains(board.getFields().get(row).get(column))) {
            setSelectedFigure(board.getFields().get(row).get(column));
            System.out.println("Установлена фигура");
            return new MoveResult(null, null, "");
        }
        else {
            if (checkSelectedFigure()) {
                Coordinates coordinates = board.getElementCoordinates(selectedFigure);
                CastlingType castlingType = checkTryingCasting(column, coordinates.getColumn());
                if (castlingType != CastlingType.NONE){
                    System.out.println("Пытаются сделать рокировку");
                    Castling1 castling1 = new Castling1(board, myKing, defineRook(castlingType));
                    boolean castlingRes = castling1.castle(new Coordinates(row, column), this);
                    if(castlingRes){
                        selectedFigure.isMoved = true;
                        movedFigure = selectedFigure;
                        selectedFigure = null;
                        return new MoveResult(movedFigure, null, castlingType);
                    }
                }
                if(canMove(new Coordinates(row, column), board, selectedFigure)){
                    Figure nextCell = board.getFields().get(row).get(column);
                    if(checkForPawn(row)){
                        try {
                            Class<? extends Figure> newFigureType = ChooseNewFigure.newWindow(this.myKing.color);
                            if(newFigureType == null){
                                return new MoveResult(null, null, "");
                            }
                            String correction = "";
                            if(nextCell != null){
                                correction = Character.toString(boardLetters.charAt(coordinates.getColumn()));
                            }
                            Figure newFigure = newFigureType.getDeclaredConstructor(String.class).newInstance(this.myKing.color);
                            myFigures.remove(selectedFigure);
                            selectedFigure.getMyImage().setVisible(false);
                            myFigures.add(newFigure);
                            board.setCell(coordinates.getRow(), coordinates.getColumn(), null);
                            board.setCell(row, column, newFigure);
                            selectedFigure.isMoved = true;
                            movedFigure = selectedFigure;
                            selectedFigure = null;
                            myKing.isChecked = false;
                            return new MoveResult(movedFigure, nextCell, newFigure, correction);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                    String correction;
                    if(selectedFigure instanceof Pawn && nextCell != null){
                        correction = Character.toString(boardLetters.charAt(coordinates.getColumn()));
                    }
                    else {
                        correction = checkForDoubleStrike(new Coordinates(row, column), selectedFigure, board, boardNums, boardLetters);
                    }

                    board.setCell(coordinates.getRow(), coordinates.getColumn(), null);
                    board.setCell(row, column, selectedFigure);
                    selectedFigure.isMoved = true;
                    movedFigure = selectedFigure;
                    selectedFigure = null;
                    myKing.isChecked = false;
                    return new MoveResult(movedFigure, nextCell, correction);
                }
                else {
                    System.out.println("Так нельзя ходить");
                    return new MoveResult(null, null, "");
                }
            }
            else {
                System.out.println("Нельзя походить: нет выбранной фигуры");
                return new MoveResult(null, null, "");
            }
        }
    }

    public boolean checkForCheck(Coordinates figureCoordinates, Board board) {
        List<Coordinates> totalEnemyMoves = new ArrayList<>();
        for (Figure enemyFigure : getEnemyFigures(board)) {
            List<Coordinates> moves = enemyFigure.calculatePossibleMoves(board.getElementCoordinates(enemyFigure), board);
            totalEnemyMoves.addAll(moves);
        }
        if (findMatch(figureCoordinates, totalEnemyMoves)) {
            System.out.println("Поле бьётся");
            return true;
        }
        else {
            return false;
        }
    }

    private boolean findMatch(Coordinates kingCoords, List<Coordinates> enemyFiguresMoves) {
        for (Coordinates move : enemyFiguresMoves) {
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

    public boolean canMove(Coordinates coordinatesToMove, Board board, Figure figure) {
        Coordinates selfCoordinates = board.getElementCoordinates(figure);
        List<Coordinates> result = figure.calculatePossibleMoves(selfCoordinates, board);
        System.out.println(result);
        for (Coordinates coordinates : result) {
            if (coordinates.equals(coordinatesToMove)) {
                Board copyBoard = new Board();
                copyBoard.setFields(board.getFields());
                copyBoard.setCell(selfCoordinates.getRow(), selfCoordinates.getColumn(), null);
                copyBoard.setCell(coordinatesToMove.getRow(), coordinatesToMove.getColumn(), figure);
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
            for (Coordinates coords : myFigure.calculatePossibleMoves(board.getElementCoordinates(myFigure), board)) {
                boolean result = canMove(coords, board, myFigure);
                if (result) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkForPawn(int row){
        if (selectedFigure instanceof Pawn){
            return row == 0 || row == 7;
        }
        return false;
    }
    private CastlingType checkTryingCasting(int column, int figureColumn){
        if(selectedFigure instanceof King && Castling1.kingCondition(((King) selectedFigure).isChecked, selectedFigure.isMoved)){
            if(figureColumn + castlingBoost == column){
                return CastlingType.SHORT;
            }
            else if (figureColumn - castlingBoost == column) {
                return CastlingType.LONG;
            }
        }
        return CastlingType.NONE;
    }

  private String checkForDoubleStrike(Coordinates position, Figure figure, Board board, String boardNums, String boardLetters) {
      List<Figure> anotherFiguresTargeting = new ArrayList<>();
      for (Figure figureToCheck : myFigures) {
          if (figure != figureToCheck && figureToCheck.getClass().equals(figure.getClass())) {
              List<Coordinates> coordinates = figureToCheck.calculatePossibleMoves(board.getElementCoordinates(figureToCheck), board);

              for (Coordinates move: coordinates){
                  System.out.println("ВОЗМОЖНЫЙ ХОД" + move.getRow() + move.getColumn());
              }
              if (coordinates.contains(position)) {
                  anotherFiguresTargeting.add(figureToCheck);
              }
          }
      }
      System.out.println(anotherFiguresTargeting);
      if (anotherFiguresTargeting.size() != 0) {
          boolean rowMatch = false;
          boolean columnMatch = false;
          Coordinates figureCoordinates = board.getElementCoordinates(figure);
          for (Figure fig : anotherFiguresTargeting) {
              Coordinates figCoordinates = board.getElementCoordinates(fig);
              if (figCoordinates.getRow() == figureCoordinates.getRow()) {
                  rowMatch = true;
              }
              if (figCoordinates.getColumn() == figureCoordinates.getColumn()) {
                  columnMatch = true;
              }
          }
          return matchCheck(rowMatch, columnMatch, figureCoordinates.getRow(), figureCoordinates.getColumn(), boardNums, boardLetters);
      }
      else {
          return "";
      }
  }

  private String matchCheck(boolean rowMatch, boolean columnMatch, int row, int column, String boardNums, String boardLetters){
      if ((rowMatch && !columnMatch) || (!rowMatch && !columnMatch)){
          return Character.toString(boardLetters.charAt(column));
      }
      else if(!rowMatch){
          return Character.toString(boardNums.charAt(row));
      }
      else {
          return boardLetters.charAt(column) + Character.toString(boardNums.charAt(row));
      }
  }



}
//    private class FigureChoiceCallbackImpl implements FigureChoiceCallback {
//        private final Pawn pawn;
//        public FigureChoiceCallbackImpl(Pawn pawn) {
//            this.pawn = pawn;
//        }
//        @Override
//        public void receiveChosenFigure(Class<? extends Figure> figureType, String color) {
//            try {
//                Figure figure = figureType.getDeclaredConstructor(String.class).newInstance(color);
//                // root.getChildren().add(figure.getMyImage());
//                // установка параметров выбранной фигуры
//                System.out.println("Chosen figure: " + figure);
//                pawn.setChosenFigure(figure);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
/*    public MoveResult makeMove(int row, int column, Board board) {
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
    }*/