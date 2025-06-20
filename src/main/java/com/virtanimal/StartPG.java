package com.virtanimal;

import com.utils.BackGround;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartPG extends Application {

    @FXML private ImageView bgImage;
    @FXML private Pane container;

    private static Stage mainStage;
    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/virtanimal/startpage.fxml"));
        Parent root = loader.load();
        mainScene = new Scene(root);
        mainStage = stage;
        stage.setScene(mainScene);
        stage.setTitle("Virtual Pet Game");
        stage.show();
    }

    @FXML
    public void initialize() {
        BackGround.setBackground(bgImage, container, "/com/virtanimal/images/background.png", 10);
    }

    @FXML
    public void goToRegister() {
        loadPage("Register.fxml");
    }

    @FXML
    public void goToLogin() {
        loadPage("Login.fxml");
    }

    //upload new page for reg&log pages
    public static void loadPage(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(StartPG.class.getResource("/com/virtanimal/" + fxmlFileName));
            Parent page = loader.load();
            Scene scene = new Scene(page);
            mainStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void goBackToStartPage() {
        if (mainStage != null && mainScene != null) {
            mainStage.setScene(mainScene);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
