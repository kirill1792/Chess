package ru.kirill.chess;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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

    public boolean checkForFriendlyFig(int row, int column, ArrayList<ArrayList<Figure>> fields) {
        return !fields.get(row).get(column).color.equals(this.color);
    }

    public abstract void calculatePossibleMoves();
}
