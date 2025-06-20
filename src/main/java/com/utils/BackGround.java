package com.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;

public class BackGround {
    public static void setBackground(ImageView imageView, Pane container, String path, double blur) {
        try {
            Image image = new Image(BackGround.class.getResourceAsStream(path));
            imageView.setImage(image);
            imageView.setEffect(new GaussianBlur(blur));
            imageView.setPreserveRatio(false);
            imageView.setSmooth(true);
            imageView.fitWidthProperty().bind(container.widthProperty());
            imageView.fitHeightProperty().bind(container.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
