package ru.kirill.chess;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Game {
    private Player turn = null;
    public Board board;
    public Group root;
    private GraphicsContext gc;
    private Player whitePlayer;
    private Player blackPlayer;

    public Game(Group root, GraphicsContext gc) {
        this.root = root;
        this.gc = gc;
    }
    public void setUpGame() throws FileNotFoundException {
        board = new Board();
        board.setFields();
        Player player1 = new Player("Вася");
        Player player2 = new Player("Петя");
        defineColor(player1, player2);
        turn = whitePlayer;
        createFigures();
        App.redraw(gc, 1000, 1000, board.getFields());
    }

    private void defineColor (Player player1, Player player2) {
        Player[] players = {player1, player2};
        int rand = (int) (Math.random() * 2);
        System.out.println(rand);
        whitePlayer = players[rand];
        if(players[0].equals(whitePlayer)) {
            blackPlayer = players[1];
        }
        else  {
            blackPlayer = players[0];
        }
    }

    private void changeTurn () {
        if(turn.equals(whitePlayer)) {
            turn = blackPlayer;
        }
        else {
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
                 new Bishop( "white"),
                 new Knight("white"),
                 new Rook("white")};

         Figure[] blackFigures = {new Rook("black"),
                 new Knight("black"),
                 new Bishop("black"),
                 new Queen("black"),
                 new King("black"),
                 new Bishop("black"),
                 new Knight("black"),
                 new Rook( "black")};

        int row = 0;
        int column = 0;

        for (int i = 0; i < board.getFields().size(); i++) {
            Figure currentFig = blackFigures[i];
            Pawn blackPawn = new Pawn("black");
            board.setCell(row, column, currentFig);
            root.getChildren().add(currentFig.getMyImage());

            board.setCell(row + 1, column, blackPawn);
            root.getChildren().add(blackPawn.getMyImage());
            blackPlayerFigures.add(currentFig);
            blackPlayerFigures.add(blackPawn);

            Figure currentFigOpposite = whiteFigures[i];
            Pawn whitePawn = new Pawn("white");
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

    public void processCoords(double x, double y) {
          int row = (int) Math.floor((y - 100) / 100);
          int column = (int) Math.floor((x - 100) / 100);
          boolean result = turn.makeMove(row, column, board);
          if (result) {
              changeTurn();
              System.out.println("Смена хода");
              if(this.turn.checkForCheck(turn.getKingCoords(board), board)) {
                  System.out.println("Объявлен шах королю игрока: " + turn.name);
                  turn.myKing.isChecked = true;
                  boolean result1 = turn.checkmate(board);
                  if(result1){
                      System.out.println("Мат!");
                  }
                  else {
                      System.out.println("Мата нет");
                  }
              }
          }
          App.redraw(gc, 1000, 1000, board.getFields());
          System.out.println(board.getFields());
          System.out.println("Перерисовка");
          System.out.println("Ходит:" + turn.name);
    }
}