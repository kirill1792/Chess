package ru.kirill.chess;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private Player turn = null;
    public Board board;
    public Group root;
    private GraphicsContext gc;
    private Player whitePlayer;
    private Player blackPlayer;
    public Notation notation;
    private TextArea textArea;

    public Game(Group root, GraphicsContext gc, TextArea textArea) {
        this.root = root;
        this.gc = gc;
        this.textArea = textArea;
    }

    public void setUpGame() throws FileNotFoundException {
        board = new Board();
        board.setFields();
        notation = new Notation();
        Player player1 = new Player("Вася");
        Player player2 = new Player("Петя");
        defineColor(player1, player2);
        turn = whitePlayer;
        createFigures();
        whitePlayer.shortCastingRook = (Rook) board.getFields().get(7).get(7);
        whitePlayer.longCastlingRook = (Rook) board.getFields().get(7).get(0);
        blackPlayer.shortCastingRook = (Rook) board.getFields().get(0).get(7);
        blackPlayer.longCastlingRook = (Rook) board.getFields().get(0).get(0);
        App.redraw(gc, 1000, 1000, board.getFields(), notation.getBoardNums(), notation.getBoardLetters());
    }

    private void defineColor(Player player1, Player player2) {
        Player[] players = {player1, player2};
        int rand = (int) (Math.random() * 2);
        System.out.println(rand);
        whitePlayer = players[rand];
        if (players[0].equals(whitePlayer)) {
            blackPlayer = players[1];
        } else {
            blackPlayer = players[0];
        }
    }

    private void changeTurn() {
        if (turn.equals(whitePlayer)) {
            turn = blackPlayer;
        } else {
            turn = whitePlayer;
        }
    }

    public void createFigures() throws FileNotFoundException {
        ArrayList<Figure> whitePlayerFigures = new ArrayList<>();
        ArrayList<Figure> blackPlayerFigures = new ArrayList<>();

        Figure[] whiteFigures = {new Rook("white"),
                new Knight("white"),
                new Bishop("white"),
                new Queen("white"),
                new King("white"),
                new Bishop("white"),
                new Knight("white"),
                new Rook("white")};

        Figure[] blackFigures = {new Rook("black"),
                new Knight("black"),
                new Bishop("black"),
                new Queen("black"),
                new King("black"),
                new Bishop("black"),
                new Knight("black"),
                new Rook("black")};

        int row = 0;
        int column = 0;

        for (int i = 0; i < board.getFields().size(); i++) {
            Figure currentFig = blackFigures[i];
            Pawn blackPawn = new Pawn("black", 1);
            board.setCell(row, column, currentFig);
            root.getChildren().add(currentFig.getMyImage());

            board.setCell(row + 1, column, blackPawn);
            root.getChildren().add(blackPawn.getMyImage());
            blackPlayerFigures.add(currentFig);
            blackPlayerFigures.add(blackPawn);

            Figure currentFigOpposite = whiteFigures[i];
            Pawn whitePawn = new Pawn("white", -1);
            board.setCell(row + 7, column, currentFigOpposite);
            root.getChildren().add(currentFigOpposite.getMyImage());
            board.setCell(row + 6, column, whitePawn);
            root.getChildren().add(whitePawn.getMyImage());
            whitePlayerFigures.add(currentFigOpposite);
            whitePlayerFigures.add(whitePawn);

            column++;
        }

        whitePlayer.myFigures = whitePlayerFigures;
        blackPlayer.myFigures = blackPlayerFigures;
        whitePlayer.setMyKing();
        blackPlayer.setMyKing();


        System.out.println(board.getFields());
    }

    public void processCoords(double x, double y) throws FileNotFoundException {
        int row = (int) Math.floor((y - 100) / 100);
        int column = (int) Math.floor((x - 100) / 100);
        MoveResult result = turn.makeMove(row, column, board, notation.getBoardNums(), notation.getBoardLetters());
        if (result.movedFig != null) {
            String checkmateMarker = "";
            String figureBeaten = "";
            if(result.newFigure != null){
                root.getChildren().add(result.newFigure.getMyImage());
            }
            //if (result.beatenFigure != null) {
               // result.beatenFigure.getMyImage().setVisible(false);
           // }
            changeTurn();
            if (result.beatenFigure != null) {
                result.beatenFigure.getMyImage().setVisible(false);
                turn.myFigures.remove(result.beatenFigure);
                figureBeaten = "x";
            }
            System.out.println("Смена хода");
            if (turn.checkForCheck(turn.getKingCoords(board), board)) {
                System.out.println("Объявлен шах королю игрока: " + turn.name);
                turn.myKing.isChecked = true;
                boolean result1 = turn.checkmate(board);
                if (result1) {
                    System.out.println("Мат!");
                    checkmateMarker = "#";
                } else {
                    System.out.println("Мата нет");
                    checkmateMarker = "+";
                }
            } else {
                boolean result1 = turn.checkmate(board);
                if (result1) {
                    System.out.println("Пат!");
                } else {
                    System.out.println("Пата нет");
                }
            }
            if(result.castlingType != null){
                textArea.appendText(notation.createExpression(result.castlingType, checkmateMarker));
            }
            else {
                System.out.println(result.movedFig.getClass().getSimpleName());
                textArea.appendText(notation.createExpression(row, column, figureBeaten, result.movedFig.getClass().getSimpleName(), checkmateMarker, result.newFigure, result.correctiveFactor));
            }
        }
        App.redraw(gc, 1000, 1000, board.getFields(), notation.getBoardNums(), notation.getBoardLetters());
        for (ArrayList<Figure> element : board.getFields()) {
            System.out.println(element);
        }
        System.out.println("Перерисовка");
        System.out.println("Ходит:" + turn.name);
    }

    private void changePawnDirection(List<Figure> figures) {
        for (Figure figure : figures) {
            if (figure instanceof Pawn) {
                ((Pawn) figure).changeDirection();
            }
        }
    }
    public void turnMyBoard(){
        board.turnBoard();
        changePawnDirection(whitePlayer.myFigures);
        changePawnDirection(blackPlayer.myFigures);
        notation.reverseNotations();
        whitePlayer.reverseBoost();
        blackPlayer.reverseBoost();
    }

//    private boolean transformPawn() throws FileNotFoundException {
//        for (Figure figure : board.getFields().get(0)) {
//            if (figure instanceof Pawn pawn) {
//                Coordinates coords = board.getElementCoordinates(pawn);
//                ChooseNewFigure.newWindow(figure.color, new FigureChoiceCallbackImpl(pawn));
//                Figure chosenFigure = pawn.getChosenFigure();
//                if (chosenFigure != null) {
//                    turn.myFigures.remove(pawn);
//                    pawn.getMyImage().setVisible(false);
//                    turn.myFigures.add(chosenFigure);
//                    board.setCell(coords.getRow(), coords.getColumn(), chosenFigure);
//                    return true;
//                }
//                else {
//                    board.setCell(coords.getRow(), coords.getColumn(), null);
//                    board.setCell(pawn.prevRow, pawn.prevColumn, pawn);
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

//    private class FigureChoiceCallbackImpl implements FigureChoiceCallback {
//        private final Pawn pawn;
//
//        public FigureChoiceCallbackImpl(Pawn pawn) {
//            this.pawn = pawn;
//        }
//
//        @Override
//        public void receiveChosenFigure(Class<? extends Figure> figureType, String color) {
//            try {
//                Figure figure = figureType.getDeclaredConstructor(String.class).newInstance(color);
//                root.getChildren().add(figure.getMyImage());
//                // установка параметров выбранной фигуры
//                System.out.println("Chosen figure: " + figure);
//                pawn.setChosenFigure(figure);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}