package ru.kirill.chess;

import java.util.ArrayList;

public class Board {
    private ArrayList<ArrayList<Figure>> fields = new ArrayList<>();

    public void setFields() {
        ArrayList<ArrayList<Figure>> arr = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            arr.add(new ArrayList<>());
            for (int j = 0; j < 8; j++) {
                arr.get(i).add(null);
            }
        }
        fields = arr;
    }

    public ArrayList<ArrayList<Figure>> getFields() {
        return fields;
    }

    public void setCell(int row, int column, Figure figure) {
        fields.get(row).set(column, figure);
    }
}
