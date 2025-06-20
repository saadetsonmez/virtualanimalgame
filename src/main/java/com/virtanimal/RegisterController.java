package com.virtanimal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.utils.BackGround;
import com.utils.PasswordUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class RegisterController {

    @FXML private AnchorPane registerPage;
    @FXML private ImageView backgroundImage;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField emailField;
    @FXML private VBox registerForm;

    private static final String FILE_PATH = "User.json";
    private Stage stage;
    private Scene scene;

    @FXML
    public void initialize() {
        BackGround.setBackground(backgroundImage, registerPage, "/com/virtanimal/images/background.png", 10);
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
                StringBuilder sb = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                }
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

            JSONObject newUser = new JSONObject();
            newUser.put("id", newUserId);
            newUser.put("username", username);
            newUser.put("passwordHash", PasswordUtil.hashPassword(password));
            newUser.put("email", email);
            userArray.put(newUser);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(userArray.toString(2));
            }

            File petFile = new File("UserManager.json");
            JSONArray petArray = new JSONArray();

            if (petFile.exists()) {
                String content = new String(java.nio.file.Files.readAllBytes(petFile.toPath()));
                petArray = new JSONArray(content);
            }

            JSONObject newPet = new JSONObject();
            newPet.put("userId", newUserId);
            newPet.put("petName", "");
            newPet.put("hunger", 0);
            newPet.put("fun", 0);
            petArray.put(newPet);

            try (BufferedWriter petWriter = new BufferedWriter(new FileWriter(petFile))) {
                petWriter.write(petArray.toString(2));
            }

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
