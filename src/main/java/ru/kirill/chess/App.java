package ru.kirill.chess;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class App extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Canvas canvas = new Canvas(1500, 1000);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        stage.setTitle("Chess");
        root.getChildren().add(canvas);
        Game game = new Game(root, gc);
        game.setUpGame();
        //redraw(gc, canvas.getWidth(), canvas.getHeight(), game.board.getFields());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getX() >= 100 & mouseEvent.getX() <= 900 & mouseEvent.getY() >= 100 & mouseEvent.getY() <= 900) {
                    System.out.println("Нажали на доску");
                    try {
                        game.processCoords(mouseEvent.getX(), mouseEvent.getY());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private static void drawBord (GraphicsContext gc) {
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
    private static void drawNotationPlace(GraphicsContext gc){
        gc.strokeRect(1000, 100, 400, 800);
    }
    private static void drawSymbols(GraphicsContext gc){
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

    public static void redraw(GraphicsContext gc, double cvsWidth, double cvsHeight, ArrayList<ArrayList<Figure>> fields) {
        gc.clearRect(0, 0, cvsWidth, cvsHeight);
        drawBord(gc);
        drawSymbols(gc);
        drawFigures(fields);
        drawNotationPlace(gc);
    }

    private static void drawFigures(ArrayList<ArrayList<Figure>> fields){
        for (int i = 0; i < fields.size(); i++) {
            for ( int j = 0; j < fields.get(i).size(); j++) {
                if (fields.get(i).get(j) != null) {
                    Figure figure = fields.get(i).get(j);
                    int xPos = 100 + j * 100 + 6;
                    int yPos = 100 + i * 100 + 6;
                    figure.getMyImage().setX(xPos);
                    figure.getMyImage().setY(yPos);
                    figure.getMyImage().setFitHeight(90);
                    figure.getMyImage().setFitWidth(90);
                    figure.getMyImage().setVisible(true);
                }
            }
        }
    }
}