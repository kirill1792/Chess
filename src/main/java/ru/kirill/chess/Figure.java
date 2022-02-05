package ru.kirill.chess;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public abstract class Figure {
    private ImageView myImage;
    public String color;

    public Figure(String color) throws FileNotFoundException {
        this.color = color;
        setUpImage();
    }

    private void setUpImage() throws FileNotFoundException {
         String url = "src/main/resources/images/" + this.color + "-" + this.getClass().getSimpleName().toLowerCase() + ".png";
         Image image = new Image(new FileInputStream(url));
         ImageView imageView = new ImageView();
         imageView.setImage(image);
         imageView.setVisible(false);
         this.myImage = imageView;
    }

    public ImageView getMyImage() {
        return myImage;
    }

    public boolean canMove(List<Integer> coordinatesToMove, List<Integer> selfCoordinates, Board board) {
        List<List<Integer>> result = calculatePossibleMoves(selfCoordinates, board);
        System.out.println(result);
        for (List<Integer> integers : result) {
            if (integers.equals(coordinatesToMove)) {
                return true;
            }
        }
        return false;
    }

    //public boolean canMove(List<Integer> selfCoordinates, Board board) {
        //List<List<Integer>> possibleMoves = calculatePossibleMoves(selfCoordinates, board);

   // }

    public abstract  List<List<Integer>> calculatePossibleMoves(List<Integer> figureCoordinates, Board board);
}
