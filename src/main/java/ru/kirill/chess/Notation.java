package ru.kirill.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notation {
    private List<String> expressions = new ArrayList<>();
    private String boardNums = "87654321";
    private String boardLetters = "abcdefgh";
    private HashMap<String, String> figureCodes = new HashMap<>();

    public Notation(){
        setMap();
    }

    public List<String> getExpressions() {
        return expressions;
    }

    public void createExpression(int rowTo, int columnTo, String figureBeaten, String figureMoved, String isCheckOrMate, Figure newFigure, String correctiveFactor){
    String finalNotation = figureCodes.get(figureMoved) + correctiveFactor + figureBeaten + boardLetters.charAt(columnTo) + boardNums.charAt(rowTo) + newFigCheck(newFigure) + isCheckOrMate;
    expressions.add(finalNotation);
    }

    public void createExpression(CastlingType castlingType, String isCheckOrMate){
        if (castlingType.equals(CastlingType.SHORT)){
            expressions.add("0-0" + isCheckOrMate);
        }
        else if (castlingType.equals(CastlingType.LONG)){
            expressions.add("0-0-0" + isCheckOrMate);
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
