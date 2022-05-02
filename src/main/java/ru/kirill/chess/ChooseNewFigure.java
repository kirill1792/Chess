package ru.kirill.chess;

import eu.hansolo.tilesfx.tools.Point;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;

public class ChooseNewFigure {

    public static Class<? extends Figure> newWindow(String figColor) throws FileNotFoundException {
        AtomicReference<Class<? extends Figure>> figureType = new AtomicReference<>();
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                ImageView imageView = (ImageView) e.getSource();
                figureType.set((Class<? extends Figure>) imageView.getUserData());
                window.close();
            }
        };
        Pane pane = new Pane();
        setImages(figColor, pane, eventHandler);

        Scene scene = new Scene(pane, 360, 90);
        window.setScene(scene);
        window.setTitle("Выберите фигуру");
        window.showAndWait();
        return figureType.get();
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
