package com.virtanimal;

import com.utils.BackGround;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class StartPage {

    private Stage stage;
    private Scene scene;

    @FXML
    private AnchorPane startPage;


    @FXML
    private ImageView backgroundImage;
    private final BackGround backGround = new BackGround();


    @FXML
    public void initialize() {
        backGround.backGround(backgroundImage, startPage, 0);
    }

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    public void goToRegister(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/virtanimal/register.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/virtanimal/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
