package com.virtanimal;

import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ItemDrag_Manager {

    private Pane eyeLayer;
    private ImageView petImageView;
    private VirtualPet pet;
    private PetFace_Control faceControl;
    private Runnable updateCallback;

    public ItemDrag_Manager(Pane eyeLayer, ImageView petImageView, VirtualPet pet, PetFace_Control faceControl, Runnable updateCallback) {
        this.eyeLayer = eyeLayer;
        this.petImageView = petImageView;
        this.pet = pet;
        this.faceControl = faceControl;
        this.updateCallback = updateCallback;
    }

    public ItemDrag_Manager(Pane eyeLayer, ImageView petImageView, VirtualPet myPet, PetFace_Control petFace, Object updateStatus) {
    }

    public void makeFoodDraggable(ImageView source, String foodType) {
        source.setOnMousePressed(event -> {
            ImageView dragged = createDraggedImage(source, event);
            Scene scene = source.getScene();
            if (scene == null) return;

            scene.setOnMouseDragged(e -> {
                dragged.setLayoutX(e.getSceneX() - dragged.getFitWidth() / 2);
                dragged.setLayoutY(e.getSceneY() - dragged.getFitHeight() / 2);
                Bounds petBounds = petImageView.localToScene(petImageView.getBoundsInLocal());
                faceControl.setMouthVisible(petBounds.contains(e.getSceneX(), e.getSceneY()));
            });

            scene.setOnMouseReleased(e -> {
                Bounds petBounds = petImageView.localToScene(petImageView.getBoundsInLocal());
                if (petBounds.contains(e.getSceneX(), e.getSceneY())) {
                    pet.eat(foodType);

                    //updt bar value!!
                    updateCallback.run();
                }
                faceControl.setMouthVisible(false);
                eyeLayer.getChildren().remove(dragged);
                scene.setOnMouseDragged(null);
                scene.setOnMouseReleased(null);
            });

            event.consume();
        });
    }

    public void makeToyDraggable(ImageView source) {
        source.setOnMousePressed(event -> {
            ImageView dragged = createDraggedImage(source, event);
            Scene scene = source.getScene();
            if (scene == null) return;

            scene.setOnMouseDragged(e -> {
                dragged.setLayoutX(e.getSceneX() - dragged.getFitWidth() / 2);
                dragged.setLayoutY(e.getSceneY() - dragged.getFitHeight() / 2);
            });

            scene.setOnMouseReleased(e -> {
                pet.play();
                updateCallback.run();
                eyeLayer.getChildren().remove(dragged);
                scene.setOnMouseDragged(null);
                scene.setOnMouseReleased(null);
            });

            event.consume();
        });
    }

    private ImageView createDraggedImage(ImageView source, MouseEvent event) {
        ImageView dragged = new ImageView(source.getImage());
        dragged.setFitWidth(source.getFitWidth());
        dragged.setFitHeight(source.getFitHeight());
        dragged.setPreserveRatio(true);
        dragged.setLayoutX(event.getSceneX() - dragged.getFitWidth() / 2);
        dragged.setLayoutY(event.getSceneY() - dragged.getFitHeight() / 2);
        eyeLayer.getChildren().add(dragged);
        return dragged;
    }
}
