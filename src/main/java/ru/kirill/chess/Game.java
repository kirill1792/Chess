package ru.kirill.chess;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;

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

    public Game(Group root, GraphicsContext gc) {
        this.root = root;
        this.gc = gc;
    }

    public void setUpGame() throws FileNotFoundException {
        board = new Board();
        board.setFields();
        notation = new Notation();
        Player player1 = new Player("Вася");
        Player player2 = new Player("Петя");
        defineColor(player1, player2);
        turn = whitePlayer;
        whitePlayer.shortCastlingPoint = new ArrayList<>(Arrays.asList(7, 6));
        whitePlayer.longCastlingPoint = new ArrayList<>(Arrays.asList(7, 2));
        blackPlayer.shortCastlingPoint = new ArrayList<>(Arrays.asList(7, 1));
        blackPlayer.longCastlingPoint = new ArrayList<>(Arrays.asList(7, 5));
        //whitePlayer.setCastlingRooks(board);
        //blackPlayer.setCastlingRooks(board);
        createFigures();
        //board.turnBoard();
        App.redraw(gc, 1000, 1000, board.getFields());
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
        MoveResult result = turn.makeMove(row, column, board);
        if (result.moved) {
            if(!transformPawn()){
                return;
            }
            if (result.beatenFigure != null) {
                result.beatenFigure.getMyImage().setVisible(false);
            }
            board.turnBoard();
            notation.reverseNotations();
            changePawnDirection(turn.myFigures);
            changeTurn();
            changePawnDirection(turn.myFigures);
            if (result.beatenFigure != null) {
                turn.myFigures.remove(result.beatenFigure);
            }
            System.out.println("Смена хода");
            if (turn.checkForCheck(turn.getKingCoords(board), board)) {
                System.out.println("Объявлен шах королю игрока: " + turn.name);
                turn.myKing.isChecked = true;
                boolean result1 = turn.checkmate(board);
                if (result1) {
                    System.out.println("Мат!");
                } else {
                    System.out.println("Мата нет");
                }
            } else {
                boolean result1 = turn.checkmate(board);
                if (result1) {
                    System.out.println("Пат!");
                } else {
                    System.out.println("Пата нет");
                }
            }
        }
        App.redraw(gc, 1000, 1000, board.getFields());
        for (ArrayList<Figure> element : board.getFields()) {
            System.out.println(element);
        }
        System.out.println("Перерисовка");
        System.out.println("Ходит:" + turn.name);
        System.out.println(turn.shortCastlingPoint);
        System.out.println(turn.longCastlingPoint);
    }

    private void changePawnDirection(List<Figure> figures) {
        for (Figure figure : figures) {
            if (figure instanceof Pawn pawn) {
                pawn.changeDirection();
            }
        }
    }

    private boolean transformPawn() throws FileNotFoundException {
        for (Figure figure : board.getFields().get(0)) {
            if (figure instanceof Pawn pawn) {
                List<Integer> coords = board.getElementCoordinates(pawn);
                ChooseNewFigure.newWindow(figure.color, new FigureChoiceCallbackImpl(pawn));
                Figure chosenFigure = pawn.getChosenFigure();
                if (chosenFigure != null) {
                    turn.myFigures.remove(pawn);
                    pawn.getMyImage().setVisible(false);
                    turn.myFigures.add(chosenFigure);
                    board.setCell(coords.get(0), coords.get(1), chosenFigure);
                    return true;
                }
                else {
                    board.setCell(coords.get(0), coords.get(1), null);
                    board.setCell(pawn.prevRow, pawn.prevColumn, pawn);
                    return false;
                }
            }
        }
        return true;
    }

    private class FigureChoiceCallbackImpl implements FigureChoiceCallback {
        private final Pawn pawn;

        public FigureChoiceCallbackImpl(Pawn pawn) {
            this.pawn = pawn;
        }

        @Override
        public void receiveChosenFigure(Class<? extends Figure> figureType, String color) {
            try {
                Figure figure = figureType.getDeclaredConstructor(String.class).newInstance(color);
                root.getChildren().add(figure.getMyImage());
                // установка параметров выбранной фигуры
                System.out.println("Chosen figure: " + figure);
                pawn.setChosenFigure(figure);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}