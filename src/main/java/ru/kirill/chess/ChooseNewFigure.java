package ru.kirill.chess;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ChooseNewFigure {

    public static void newWindow(String figColor, FigureChoiceCallback callback) throws FileNotFoundException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                ImageView imageView = (ImageView) e.getSource();
               // imageView.setVisible(false);
                callback.receiveChosenFigure((Class<? extends Figure>) imageView.getUserData(), figColor);
                window.close();
            }
        };
        Pane pane = new Pane();
        setImages(figColor, pane, eventHandler);

        Scene scene = new Scene(pane, 360, 90);
        window.setScene(scene);
        window.setTitle("Выберите фигуру");
        window.showAndWait();
        // window.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    private static void setImages(String color, Pane pane, EventHandler<MouseEvent> eventHandler) throws FileNotFoundException {
        Class[] figures = {Queen.class, Rook.class, Bishop.class, Knight.class};
        int x = 0;
        for (Class figureType : figures) {
            Image image = new Image(new FileInputStream(String.format(
                    "src/main/resources/images/%s-%s.png", color, figureType.getSimpleName().toLowerCase())));
            ImageView imageView = new ImageView();
            imageView.setX(x);
            imageView.setImage(image);
            pane.getChildren().addAll(imageView);
            imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
            System.out.println(imageView.getImage().getUrl());
            imageView.setFitHeight(90);
            imageView.setFitWidth(90);
            imageView.setUserData(figureType);
            x += 90;
        }
    }
}
