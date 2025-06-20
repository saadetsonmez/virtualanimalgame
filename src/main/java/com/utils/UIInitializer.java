package com.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UIInitializer {

    public static void loadImages(
            ImageView petImage,
            ImageView food1,
            ImageView food2,
            ImageView food3,
            ImageView toy,
            ImageView ball,
            ImageView logoutImage
    ) {
        try {
            petImage.setImage(new Image(UIInitializer.class.getResourceAsStream("/com/virtanimal/images/cat.png")));
            food1.setImage(new Image(UIInitializer.class.getResourceAsStream("/com/virtanimal/images/fish.png")));
            food2.setImage(new Image(UIInitializer.class.getResourceAsStream("/com/virtanimal/images/bone.png")));
            food3.setImage(new Image(UIInitializer.class.getResourceAsStream("/com/virtanimal/images/carrot.png")));
            toy.setImage(new Image(UIInitializer.class.getResourceAsStream("/com/virtanimal/images/toy.png")));
            ball.setImage(new Image(UIInitializer.class.getResourceAsStream("/com/virtanimal/images/ball.png")));
            logoutImage.setImage(new Image(UIInitializer.class.getResourceAsStream("/com/virtanimal/images/exit.png")));
        } catch (Exception e) {
            System.err.println("❌ Image load failed: " + e.getMessage());
        }
    }
}
