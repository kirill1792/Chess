package ru.kirill.chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public void setFields(ArrayList<ArrayList<Figure>> fieldsToCopy) {
        ArrayList<ArrayList<Figure>> arr = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            arr.add(new ArrayList<>(fieldsToCopy.get(i)));
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

    public List<Figure> getFiguresByColor(String color) {
        List<Figure> figures = new ArrayList<>();
        for (ArrayList<Figure> field : fields) {
            for (Figure figure : field) {
                if (figure != null && figure.color.equals(color)) {
                    figures.add(figure);
                }
            }
        }
        return figures;
    }

    public boolean isEmptyField(int row, int column) {
        return getFields().get(row).get(column) == null;
    }

    public boolean checkOutOfBounds(int row, int column) {
        return row >= 0 && row <= 7 && column >= 0 && column <= 7;
    }

    public void turnBoard(){
        ArrayList<ArrayList<Figure>> newFields = new ArrayList<>();
        for (int i = fields.size() - 1; i > -1 ; i--) {
            ArrayList<Figure> res = fields.get(i);
            Collections.reverse(res);
            newFields.add(res);
        }
        fields = newFields;
    }
}
