package ru.kirill.chess;

public class Notation {
    private String boardNums = "87654321";
    private String boardLetters = "abcdefgh";

    public void reverseNotations(){
        boardNums = new StringBuilder(boardNums).reverse().toString();
        boardLetters = new StringBuilder(boardLetters).reverse().toString();
    }
}
