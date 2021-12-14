package ru.kirill.chess;

public class Game {
    private String turn = "white";
    private Board board;

    public void setUpGame() {
        board = new Board();
        board.setField();
    }

    private void defineColor () {

    }

    private void changeTurn () {
        if(turn.equals("white")) {
            turn = "black";
        }
        else {
            turn = "white";
        }
    }
}
