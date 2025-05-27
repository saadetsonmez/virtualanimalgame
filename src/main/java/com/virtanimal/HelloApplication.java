package com.virtanimal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.utils.BackGround;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("startpage.fxml"));
        AnchorPane root = fxmlLoader.load();

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Virtual Pet");
        stage.setScene(scene);
        stage.show();


        /*Controller controller = fxmlLoader.getController();
        ImageView petImageView = controller.getPetImageView();

        petImageView.fitWidthProperty().bind(scene.widthProperty());
        petImageView.fitHeightProperty().bind(scene.heightProperty());*/
    }

    public static void main(String[] args) {
        launch();
    }
}
