package com.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;

public class BackGround {

    public void backGround(ImageView backgroundImage, AnchorPane loginPage, int blurDeger) {
        try {
            Image image = new Image(getClass().getResource("/com/virtanimal/images/background.png").toExternalForm());
            backgroundImage.setImage(image);
            backgroundImage.setEffect(new GaussianBlur(blurDeger));
            backgroundImage.fitHeightProperty().bind(loginPage.heightProperty());
            backgroundImage.fitWidthProperty().bind(loginPage.widthProperty());
            backgroundImage.setPreserveRatio(false);
        } catch (Exception e) {
            System.out.println("Error!" + e.getMessage());
            e.printStackTrace();
        }
    }
}
