package ru.kirill.chess;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Canvas canvas = new Canvas(1500, 1000);
        Button turnBoardButton = new Button("Повернуть доску");
        ScrollPane scrollPane = new ScrollPane();
        TextArea textArea = new TextArea();
        turnBoardButton.setLayoutX(500);
        turnBoardButton.setLayoutY(25);
        scrollPane.setPrefViewportHeight(800);
        scrollPane.setPrefViewportWidth(400);
        textArea.setPrefHeight(812);
        textArea.setPrefWidth(412);
        textArea.setLayoutX(1000);
        textArea.setStyle("-fx-font-weight:bold");
        textArea.setStyle("-fx-font-size: 30");
        textArea.setLayoutY(100);
        scrollPane.setLayoutX(1000);
        scrollPane.setLayoutY(100);
        //scrollPane.setContent(new Text("ewrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr"));
        root.getChildren().add(canvas);
        root.getChildren().add(scrollPane);
        textArea.setEditable(false);
        scrollPane.setContent(textArea);
        root.getChildren().add(turnBoardButton);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        stage.setTitle("Chess");
        Game game = new Game(root, gc, textArea);
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
        turnBoardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                game.turnMyBoard();
                redraw(gc, 1000, 1000, game.board.getFields(), game.notation.getBoardNums(), game.notation.getBoardLetters());
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
    private static void drawSymbols(GraphicsContext gc, String numbers, String letters){
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

    public static void redraw(GraphicsContext gc, double cvsWidth, double cvsHeight, ArrayList<ArrayList<Figure>> fields, String numbers, String letters) {
        gc.clearRect(0, 0, cvsWidth, cvsHeight);
        drawBord(gc);
        drawSymbols(gc, numbers, letters);
        drawFigures(fields);
        //drawNotation(expressions, gc);
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

    private static void drawNotation(List<String> expressions, GraphicsContext gc){
        gc.strokeRect(1000, 100, 400, 800);
        int startX = 1050;
        int currentX = startX;
        int currentY = 125;
        int count = 1;
        for (int i = 0; i < expressions.size(); i++) {
            String expression = expressions.get(i);
            if(i % 2 == 0){
                expression = count + "." + expression;
                currentY += 50;
                currentX = startX;
                count++;
            }
            else {
                currentX += 100;
            }
            gc.fillText(expression, currentX, currentY);
            System.out.println(expression);
        }
    }
}

