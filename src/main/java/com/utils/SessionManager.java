// SessionManager.java
package com.utils;

import com.virtanimal.UserManagerService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SessionManager {
    public static void logoutAndRedirect(Node source, String fxmlPath) {
        try {
            UserManagerService.setCurrentUser(-1);
            UserManagerService.setPetName("");
            UserManagerService.setHunger(0);
            UserManagerService.setFun(0);

            Parent root = FXMLLoader.load(SessionManager.class.getResource(fxmlPath));
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 