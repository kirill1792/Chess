package ru.kirill.chess;

import javafx.scene.Group;

import java.io.FileNotFoundException;

public class Game {
    private Player turn = null;
    public Board board;
    public Group root;
    private Player whitePlayer;
    private Player blackPlayer;

    public Game(Group root) {
        this.root = root;
    }
    public void setUpGame() {
        board = new Board();
        board.setFields();
        Player player1 = new Player();
        Player player2 = new Player();
        defineColor(player1, player2);
        turn = whitePlayer;
    }

    private void defineColor (Player player1, Player player2) {
        Player[] players = {player1, player2};
        int rand = (int) (Math.random() * 2 + 1);
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

            Figure currentFigOpposite = whiteFigures[i];
            Pawn whitePawn = new Pawn("white");
            board.setCell(row + 7, column, currentFigOpposite);
            root.getChildren().add(currentFigOpposite.getMyImage());
            board.setCell(row + 6, column, whitePawn);
            root.getChildren().add(whitePawn.getMyImage());

            column++;
        }
        System.out.println(board.getFields());
    }
}