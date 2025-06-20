// PetTimer.java
package com.utils;

import com.virtanimal.VirtualPet;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class PetTimer {
    public static Timeline startDecay(VirtualPet pet, Runnable onUpdate) {
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            pet.decay();
            onUpdate.run();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
        return timer;
    }
} 