package ru.kirill.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notation {
    private String boardNums = "87654321";
    private String boardLetters = "abcdefgh";
    private HashMap<String, String> figureCodes = new HashMap<>();
    private int movesCount = 0;

    public Notation(){
        setMap();
    }

    public String createExpression(int rowTo, int columnTo, String figureBeaten, String figureMoved, String isCheckOrMate, Figure newFigure, String correctiveFactor){
        movesCount += 1;
        return postProcessEx(figureCodes.get(figureMoved) + correctiveFactor + figureBeaten + boardLetters.charAt(columnTo) + boardNums.charAt(rowTo) + newFigCheck(newFigure) + isCheckOrMate);
    }

    public String createExpression(CastlingType castlingType, String isCheckOrMate){
        movesCount += 1;
        if (castlingType.equals(CastlingType.SHORT)){
            return postProcessEx("0-0" + isCheckOrMate);
        }
        else if (castlingType.equals(CastlingType.LONG)){
            return postProcessEx("0-0-0" + isCheckOrMate);
        }
        return "";
    }

    private String postProcessEx(String expression){
        if(movesCount % 2 == 0){
            return expression + "\n";
        }
        else {
            return ((movesCount - 1) / 2 + 1) + "." + expression + "   ";
        }
    }

    private String newFigCheck(Figure newFig){
        if(newFig != null){
            return "=" + figureCodes.get(newFig.getClass().getSimpleName());
        }
        else {
            return "";
        }
    }

    public String getBoardNums(){
        return boardNums;
    }

    public String getBoardLetters(){
        return boardLetters;
    }

    public void reverseNotations(){
        boardNums = new StringBuilder(boardNums).reverse().toString();
        boardLetters = new StringBuilder(boardLetters).reverse().toString();
    }

    private void setMap(){
        figureCodes.put("Pawn", "");
        figureCodes.put("Knight", "N");
        figureCodes.put("Bishop", "B");
        figureCodes.put("Rook", "R");
        figureCodes.put("Queen", "Q");
        figureCodes.put("King", "K");
    }


}
