// StatusUIUpdater.java
package com.utils;

import com.virtanimal.VirtualPet;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StatusUIUpdater {
    public static void updateStatusDisplay(VirtualPet pet, Label lblStatus, Label lblMood, Label nameLabel, Rectangle hungerBar, Rectangle funBar) {
        lblStatus.setText("Hunger: " + pet.getHunger() + " | Fun: " + pet.getFun());
        lblMood.setText("Mood: " + pet.getMood());
        nameLabel.setText(pet.getName());

        updateBar(hungerBar, pet.getHunger(), true);
        updateBar(funBar, pet.getFun(), false);

    }

    private static void updateBar(Rectangle bar, int value, boolean isHunger) {
        double maxWidth = 180;
        double percent = Math.max(0, Math.min(value, 100)) / 100.0;
        bar.setWidth(maxWidth * percent);

        if (isHunger) {
            int red = (int)((1 - percent) * 255);
            int green = (int)(percent * 255);
            bar.setFill(Color.rgb(red, green, 0));
        } else {
            int blue = (int)(percent * 255);
            int red = (int)((1 - percent) * 128);
            bar.setFill(Color.rgb(red, 0, blue));
        }
    }
} 