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

    public ArrayList<Integer> getElementCoordinates(Figure figure) {
        ArrayList<Integer> coords = new ArrayList<>();

        for (int i = 0; i < fields.size(); i++) {
            if(fields.get(i).contains(figure)) {
                coords.add(i);
                coords.add(fields.get(i).indexOf(figure));
            }
        }
        return coords;
    }
}
