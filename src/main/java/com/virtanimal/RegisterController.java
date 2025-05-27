package com.virtanimal;

import com.utils.Center;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

import com.utils.BackGround;

// Java IO
import java.io.IOException;


public class RegisterController {
    private Stage stage;
    private Scene scene;

    public final BackGround backGround = new BackGround();

    @FXML
    private AnchorPane registerPage;
    @FXML private ImageView backgroundImage;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField emailField;

    @FXML private VBox registerForm;

    private static final String FILE_PATH = "users.json";

    @FXML
    public void initialize() {
        backGround.backGround(backgroundImage, registerPage, 15);
        Platform.runLater(() -> {
            Center.centerNode(registerForm, registerPage);
        });
    }

    @FXML
    public void backToStartPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/virtanimal/startpage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Passwords do not match.");
            return;
        }

        if (password.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Password must be at least 6 characters.");
            return;
        }

        try {
            File file = new File(FILE_PATH);
            JSONArray userArray = new JSONArray();
            int maxId = 0;

            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                userArray = new JSONArray(sb.toString());

                for (int i = 0; i < userArray.length(); i++) {
                    JSONObject user = userArray.getJSONObject(i);
                    if (user.getString("username").equalsIgnoreCase(username)) {
                        showAlert(Alert.AlertType.ERROR, "Username already exists.");
                        return;
                    }
                    if (user.getString("email").equalsIgnoreCase(email)) {
                        showAlert(Alert.AlertType.ERROR, "Email already registered.");
                        return;
                    }
                    maxId = Math.max(maxId, user.getInt("id"));
                }
            }

            int newUserId = maxId + 1;

            // Yeni kullanıcı JSON objesi
            JSONObject newUser = new JSONObject();
            newUser.put("id", newUserId);
            newUser.put("username", username);
            newUser.put("password", password);
            newUser.put("email", email);

            userArray.put(newUser);

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(userArray.toString(2));
            writer.close();

            // ✨ Ayrıca userPets.json'a kayıt
            File petFile = new File("UserManeger.json");
            JSONArray petArray = new JSONArray();

            if (petFile.exists()) {
                String content = new String(java.nio.file.Files.readAllBytes(petFile.toPath()));
                petArray = new JSONArray(content);
            }

            JSONObject newPet = new JSONObject();
            newPet.put("userId", newUserId);
            newPet.put("petName", "");     // ilk girişte login ekranı bunu kontrol edecek
            newPet.put("hunger", 0);
            newPet.put("fun", 0);

            petArray.put(newPet);

            BufferedWriter petWriter = new BufferedWriter(new FileWriter(petFile));
            petWriter.write(petArray.toString(2));
            petWriter.close();

            showAlert(Alert.AlertType.INFORMATION, "Registration successful!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error saving user.");
        }
    }


    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Register");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
