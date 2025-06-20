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

import org.json.JSONArray;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.util.Optional;

public class LoginController {

    @FXML private AnchorPane loginPage;
    @FXML private ImageView backgroundImage;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button logbackbtn;
    @FXML private VBox loginForm;

    private Stage stage;
    private Scene scene;
    private static final String FILE_PATH = "User.json";
    private static final String PET_FILE_PATH = "UserManager.json";
    @FXML
    public void initialize() {
        BackGround.setBackground(backgroundImage, loginPage, "/com/virtanimal/images/background.png", 10);
    }

    @FXML
    public void backToStartPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/virtanimal/startpage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter username and password.");
            return;
        }

        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                showAlert(Alert.AlertType.ERROR, "No registered users found.");
                return;
            }

            StringBuilder jsonBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }

            JSONArray userArray = new JSONArray(jsonBuilder.toString());

            for (int i = 0; i < userArray.length(); i++) {
                JSONObject user = userArray.getJSONObject(i);
                if (user.getString("username").equalsIgnoreCase(username) &&
                        BCrypt.checkpw(password, user.getString("passwordHash"))){

                    int userId = user.getInt("id");
                    checkAndSetUserManager(userId);

                    JSONObject pet = getUserManagerById(userId);
                    if (pet == null) {
                        showAlert(Alert.AlertType.ERROR, "Pet data not found.");
                        return;
                    }

                    UserManagerService.setCurrentUser(userId);
                    UserManagerService.setPetName(pet.getString("petName"));
                    UserManagerService.setHunger(pet.getInt("hunger"));
                    UserManagerService.setFun(pet.getInt("fun"));

                    Parent root = FXMLLoader.load(getClass().getResource("/com/virtanimal/hello-view.fxml"));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    return;
                }
            }

            showAlert(Alert.AlertType.ERROR, "Invalid username or password.");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error while logging in.");
        }
    }

    private JSONObject getUserManagerById(int userId) throws IOException {
        File file = new File(PET_FILE_PATH);
        if (!file.exists()) return null;

        String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        JSONArray array = new JSONArray(content);

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if (obj.getInt("userId") == userId) {
                return obj;
            }
        }
        return null;
    }

    private void checkAndSetUserManager(int userId) throws IOException {
        File file = new File(PET_FILE_PATH);
        JSONArray petArray = new JSONArray();

        if (file.exists()) {
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            petArray = new JSONArray(content);

            for (int i = 0; i < petArray.length(); i++) {
                JSONObject obj = petArray.getJSONObject(i);
                if (obj.getInt("userId") == userId) {
                    String petName = obj.optString("petName", "").trim();
                    if (petName.isEmpty()) {
                        String newName = promptForPetName();
                        if (newName != null) {
                            obj.put("petName", newName);
                            savePetArray(petArray);
                        }
                    }
                    return;
                }
            }
        }

        String name = promptForPetName();
        if (name != null) {
            JSONObject newPet = new JSONObject();
            newPet.put("userId", userId);
            newPet.put("petName", name);
            newPet.put("hunger", 0);
            newPet.put("fun", 0);
            petArray.put(newPet);
            savePetArray(petArray);
        }
    }

    private String promptForPetName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Name Your Pet");
        dialog.setHeaderText("Please give your pet a name:");
        dialog.setContentText("Pet name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            return result.get().trim();
        }
        showAlert(Alert.AlertType.WARNING, "Pet name cannot be empty.");
        return null;
    }

    private void savePetArray(JSONArray array) throws IOException {
        try (FileWriter writer = new FileWriter(PET_FILE_PATH)) {
            writer.write(array.toString(2));
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Login");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
