
package com.virtanimal;

import com.models.UserManager;
import com.utils.*;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Controller {
    @FXML private ImageView petImageView;
    @FXML private Pane eyeLayer;
    @FXML private ImageView food1, food2, food3, toy, ball;
    @FXML private Label lblStatus, lblMood, petNameLabel;
    @FXML private Rectangle hungerBar, funBar;
    @FXML private ImageView logoutImageView;
    @FXML private Button logoutButton;

    private VirtualPet myPet;
    private PetFace_Control petFace;
    private ItemDrag_Manager dragManager;
    private Timeline decayTimer;

    @FXML
    public void initialize() {
        try {
            UserManager currentUser = UserManagerService.loadCurrentUser();
            myPet = new VirtualPet(currentUser);
        } catch (Exception e) {
            int userId = UserManagerService.getCurrentUserId();
            String name = UserManagerService.getPetName();
            if (name == null || name.isEmpty()) name = "My Pet";
            myPet = new VirtualPet(new UserManager(userId == -1 ? 1 : userId, "default", "hash", "mail", name, 50, 50));
        }

        if (petNameLabel != null) petNameLabel.setText(myPet.getName());

        UIInitializer.loadImages(petImageView, food1, food2, food3, toy, ball, logoutImageView);

        petFace = new PetFace_Control(eyeLayer);
        petFace.bindToImageView(petImageView, eyeLayer);

        setupDragManager();
        MouseTracker.setupEyeTracking(eyeLayer, petFace, logoutButton);
        decayTimer = PetTimer.startDecay(myPet, this::updateStatus);

        updateStatus();

        if (logoutButton != null) logoutButton.setOnAction(this::handleLogout);
    }

    private void setupDragManager() {
        Runnable updateCallback = this::updateStatus;
        dragManager = new ItemDrag_Manager(eyeLayer, petImageView, myPet, petFace, updateCallback);
        dragManager.makeFoodDraggable(food1, "fish");
        dragManager.makeFoodDraggable(food2, "bone");
        dragManager.makeFoodDraggable(food3, "carrot");
        dragManager.makeToyDraggable(toy);
        dragManager.makeToyDraggable(ball);
    }

    private void updateStatus() {
        StatusUIUpdater.updateStatusDisplay(myPet, lblStatus, lblMood, petNameLabel, hungerBar, funBar);
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        myPet.forceSave();
        if (decayTimer != null) decayTimer.stop();
        SessionManager.logoutAndRedirect((Node) event.getSource(), "/com/virtanimal/startpage.fxml");
    }

    @FXML
    public void handleSavePet(ActionEvent event) {
        myPet.forceSave();
        AlertHelper.showInfo("Pet data saved successfully!");
    }

    @FXML
    public void handleResetStats(ActionEvent event) {
        myPet.setHunger(50);
        myPet.setFun(50);
        updateStatus();
        AlertHelper.showInfo("Pet stats reset to 50/50!");
    }

    @FXML
    public void handleShowStatus(ActionEvent event) {
        AlertHelper.showInfo(myPet.getDetailedStatus());
    }

    public void onApplicationClose() {
        if (myPet != null) myPet.forceSave();
        if (decayTimer != null) decayTimer.stop();
    }
}
