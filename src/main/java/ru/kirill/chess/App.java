package ru.kirill.chess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Canvas canvas = new Canvas(1000, 1000);
        Board board = new Board();
        board.setFields();
        Image image = new Image(new FileInputStream("src/main/resources/images/black-bishop.png"));
        Bishop bishop = new Bishop(6, 6, new ImageView());
        bishop.myImage.setImage(image);
        bishop.myImage.setFitWidth(90);
        bishop.myImage.setFitHeight(90);
        board.setCell(bishop.getRow(), bishop.getColumn(), bishop);
        System.out.println(board.getFields());
       //FileInputStream inputstream = new FileInputStream("src/main/resources/images/black-queen.png");
       //Image image = new Image(inputstream);
       //Image image = new Image("C:\\Users\\groho\\Downloads\\white-pawn.png");
       //ImageView iv = new ImageView();
       //iv.setFitHeight(90);
       //iv.setFitWidth(90);
       //iv.setX(206);
       //iv.setY(206);
       //iv.setImage(image);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        redraw(gc, canvas.getWidth(), canvas.getHeight(), board.getFields());
        stage.setTitle("Chess");
        root.getChildren().add(canvas);
        root.getChildren().add(bishop.myImage);
        //root.getChildren().remove(iv);
        //redraw(gc, canvas.getWidth(), canvas.getHeight());
        //gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void drawBord (GraphicsContext gc) {
          int squareWidth = 100;
          int squareHeight = 100;
          int baseX = 100;
          int baseY = 100;
          int currentX = baseX;
          int currentY = baseY;
          String whiteColor = "FFE4B5";
          String brownColor = "#8B4513";
          String currentColor = whiteColor;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gc.setFill(Color.web(currentColor));
                gc.fillRect(currentX, currentY, squareWidth,squareHeight);
                currentX += 100;
                if(j < 7){
                    if (currentColor.equals(whiteColor)){
                        currentColor = brownColor;
                    }
                    else {
                        currentColor = whiteColor;
                    }
                }
            }
            currentX = baseX;
            currentY += 100;


        }
        gc.strokeRect(100, 100, 800,800);
    }

    private void drawSymbols(GraphicsContext gc){
        String numbers = "87654321";
        String letters = "abcdefgh";

        int numsX = 75;
        int numsY = 150;

        for (int i = 0; i < 8; i++) {
             gc.setFont(new Font(30));
             gc.setFill(Color.BLACK);
             gc.fillText(Character.toString(numbers.charAt(i)), numsX, numsY);
             numsY += 100;
        }

        int lettersX = 150;
        int lettersY = 940;

        for (int j = 0; j < 8; j++) {
            gc.setFont(new Font(30));
            gc.setFill(Color.BLACK);
            gc.fillText(Character.toString(letters.charAt(j)), lettersX, lettersY);
            lettersX += 100;
        }
    }
    private void redraw(GraphicsContext gc, double cvsWidth, double cvsHeight, ArrayList<ArrayList<Figure>> fields) {
        gc.clearRect(0, 0, cvsWidth, cvsHeight);
        drawBord(gc);
        drawSymbols(gc);
        drawFigures(fields);
    }

    private void drawFigures(ArrayList<ArrayList<Figure>> fields){
        for (ArrayList<Figure> field : fields) {
            for (Figure value : field) {
                if (value != null) {
                    int xPos = 100 + value.getColumn() * 100 + 6;
                    int yPos = 100 + value.getRow() * 100 + 6;
                    value.myImage.setX(xPos);
                    value.myImage.setY(yPos);
                }
            }
        }
    }
}