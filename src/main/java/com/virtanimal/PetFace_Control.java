package com.virtanimal;

import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;

public class PetFace_Control {
    private Ellipse leftEye, rightEye;
    private Circle leftPupil, rightPupil;
    private Ellipse mouth;

    public PetFace_Control(Pane layer) {
        // Create eyes and mouth and add them to the layer
        leftEye = new Ellipse(25, 20);
        leftEye.setFill(Color.WHITE);
        leftPupil = new Circle(14, Color.BLACK);

        rightEye = new Ellipse(25, 20);
        rightEye.setFill(Color.WHITE);
        rightPupil = new Circle(14, Color.BLACK);

        mouth = new Ellipse();
        mouth.setFill(Color.RED);
        mouth.setVisible(false);

        layer.getChildren().addAll(leftEye, rightEye, leftPupil, rightPupil, mouth);
    }

    public void updatePosition(Bounds bounds) {
        double x = bounds.getMinX();
        double y = bounds.getMinY();
        double w = bounds.getWidth();
        double h = bounds.getHeight();

        double lx = x + w * 0.46, rx = x + w * 0.54;
        double ly = y + h * 0.48, ry = y + h * 0.47;
        double eyeRX = w * 0.03, eyeRY = w * 0.04, pupilR = w * 0.020;

        leftEye.setCenterX(lx);
        leftEye.setCenterY(ly);
        leftEye.setRadiusX(eyeRX);
        leftEye.setRadiusY(eyeRY);
        leftPupil.setCenterX(lx);
        leftPupil.setCenterY(ly);
        leftPupil.setRadius(pupilR);

        rightEye.setCenterX(rx);
        rightEye.setCenterY(ry);
        rightEye.setRadiusX(eyeRX);
        rightEye.setRadiusY(eyeRY);
        rightPupil.setCenterX(rx);
        rightPupil.setCenterY(ry);
        rightPupil.setRadius(pupilR);

        double mouthX = x + w * 0.5;
        double mouthY = y + h * 0.55;
        double mouthRX = w * 0.035;
        double mouthRY = h * 0.025;

        mouth.setCenterX(mouthX);
        mouth.setCenterY(mouthY);
        mouth.setRadiusX(mouthRX);
        mouth.setRadiusY(mouthRY);
    }

    public void movePupils(double mouseX, double mouseY) {
        movePupilForEye(mouseX, mouseY, leftEye, leftPupil);
        movePupilForEye(mouseX, mouseY, rightEye, rightPupil);
    }

    private void movePupilForEye(double mouseX, double mouseY, Ellipse eye, Circle pupil) {
        double dx = mouseX - eye.getCenterX();
        double dy = mouseY - eye.getCenterY();
        double distance = Math.min(eye.getRadiusX() * 0.4, Math.sqrt(dx * dx + dy * dy));
        double angle = Math.atan2(dy, dx);
        pupil.setCenterX(eye.getCenterX() + distance * Math.cos(angle));
        pupil.setCenterY(eye.getCenterY() + distance * Math.sin(angle));
    }

    public void setMouthVisible(boolean visible) {
        mouth.setVisible(visible);
    }
}
