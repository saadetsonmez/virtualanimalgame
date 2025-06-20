// MouseTracker.java
package com.utils;

import com.utils.PetFace_Control;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MouseTracker {
    public static void setupEyeTracking(Pane eyeLayer, PetFace_Control petFace, Button logoutButton) {
        eyeLayer.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
                    if (!isMouseOnButton(e, logoutButton)) {
                        petFace.movePupils(e.getSceneX(), e.getSceneY());
                    }
                });
                newScene.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
                    if (!isMouseOnButton(e, logoutButton)) {
                        petFace.movePupils(e.getSceneX(), e.getSceneY());
                    }
                });
            }
        });
    }

    private static boolean isMouseOnButton(MouseEvent e, Button button) {
        if (button != null && button.getScene() != null) {
            try {
                Bounds bounds = button.localToScene(button.getBoundsInLocal());
                return bounds.contains(e.getSceneX(), e.getSceneY());
            } catch (Exception ex) {
                return false;
            }
        }
        return false;
    }
} 