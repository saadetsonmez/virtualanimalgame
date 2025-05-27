package com.virtanimal;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Controller {

    @FXML private ImageView petImageView;
    @FXML private Pane eyeLayer;
    @FXML private ImageView food1, food2, food3, toy, ball;
    @FXML private Label lblStatus;
    @FXML private Rectangle hungerBar;
    @FXML private Rectangle hungerBarBackground;
    @FXML private Rectangle funBar;
    @FXML private Rectangle funBarBackground;
    @FXML private Label lblMood;
    @FXML private Label petNameLabel;
    @FXML private ImageView logoutImageView;
    @FXML private Button logoutButton;

    private VirtualPet myPet;
    private PetFace_Control petFace;
    private ItemDrag_Manager dragManager;
    private Timeline decayTimer;

    @FXML
    public void initialize() {
        try {
            // JSON'dan mevcut kullanıcının pet'ini yükle
            myPet = VirtualPet.loadCurrentUserPet();
            System.out.println("Pet loaded successfully: " + myPet.toString());

        } catch (Exception e) {
            System.err.println("Error loading pet data: " + e.getMessage());
            e.printStackTrace();

            // Hata durumunda varsayılan pet oluştur
            showAlert(Alert.AlertType.WARNING, "Could not load pet data. Creating default pet.");

            // Geçici çözüm - UserManegerService'den en azından current user ID'yi al
            int userId = UserManegerService.getCurrentUserId();
            if (userId == -1) {
                // Eğer hiç kullanıcı yoksa varsayılan
                myPet = new VirtualPet(1, "Default Pet", 50, 50);
            } else {
                String petName = UserManegerService.getPetName();
                if (petName == null || petName.trim().isEmpty()) {
                    petName = "My Pet";
                }
                myPet = new VirtualPet(userId, petName, 50, 50);
            }
        }

        // Pet ismini ekranda göster
        if (petNameLabel != null) {
            petNameLabel.setText(myPet.getName());
        }

        // Görselleri yükle
        loadImages();

        // Pet yüzü kontrolünü başlat
        setupPetFace();

        // Drag & Drop manager'ı başlat
        setupDragManager();

        // Mouse hareket kontrollerini ayarla - DÜZELTME BURADA!
        setupMouseControlsFixed();

        // Otomatik decay timer'ı başlat (her 30 saniyede bir - çok hızlı olmasın)
        startDecayTimer();

        // İlk durumu güncelle
        updateStatus();

        // BUTON TEST KODU
        if (logoutButton != null) {
            System.out.println("✅ Logout button found and connected!");
            // Extra güvenlik için manuel listener da ekle
            logoutButton.setOnAction(this::handleLogout);
        } else {
            System.out.println("❌ Logout button is NULL!");
        }
    }

    private void loadImages() {
        try {
            petImageView.setImage(new Image(getClass().getResourceAsStream("/com/virtanimal/images/cat.png")));
            food1.setImage(new Image(getClass().getResourceAsStream("/com/virtanimal/images/fish.png")));
            food2.setImage(new Image(getClass().getResourceAsStream("/com/virtanimal/images/bone.png")));
            food3.setImage(new Image(getClass().getResourceAsStream("/com/virtanimal/images/carrot.png")));
            toy.setImage(new Image(getClass().getResourceAsStream("/com/virtanimal/images/toy.png")));
            ball.setImage(new Image(getClass().getResourceAsStream("/com/virtanimal/images/ball.png")));
            logoutImageView.setImage(new Image(getClass().getResourceAsStream("/com/virtanimal/images/exit.png")));

        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
        }
    }

    private void setupPetFace() {
        petFace = new PetFace_Control(eyeLayer);
        eyeLayer.prefWidthProperty().bind(petImageView.fitWidthProperty());
        eyeLayer.prefHeightProperty().bind(petImageView.fitHeightProperty());

        petImageView.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> updateFace());

        petImageView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.widthProperty().addListener((obsW, oldW, newW) -> updateFace());
                newScene.heightProperty().addListener((obsH, oldH, newH) -> updateFace());
            }
        });

        Platform.runLater(() -> {
            updateFace();
            hungerBar.setLayoutX(0);
            funBar.setLayoutX(0);
        });
    }

    private void setupDragManager() {
        // Callback fonksiyonu - pet ile etkileşim sonrası çağrılacak
        Runnable updateCallback = () -> {
            updateStatus();
            // Pet ismini tekrar güncelleyebilir (isim değişirse)
            if (petNameLabel != null) {
                petNameLabel.setText(myPet.getName());
            }
        };

        dragManager = new ItemDrag_Manager(eyeLayer, petImageView, myPet, petFace, updateCallback);
        dragManager.makeFoodDraggable(food1, "fish");
        dragManager.makeFoodDraggable(food2, "bone");
        dragManager.makeFoodDraggable(food3, "carrot");
        dragManager.makeToyDraggable(toy);
        dragManager.makeToyDraggable(ball);
    }

    // ÇÖZELTİ: Mouse kontrollerini düzelt
    private void setupMouseControlsFixed() {
        eyeLayer.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                // SADECE MOUSE_MOVED ve MOUSE_DRAGGED kullan
                // MOUSE_PRESSED'i kaldır çünkü buton click'lerini engelliyor!

                newScene.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
                    // Buton alanında değilse pupil hareketini çalıştır
                    if (!isMouseOnButton(e)) {
                        petFace.movePupils(e.getSceneX(), e.getSceneY());
                    }
                });

                newScene.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
                    if (!isMouseOnButton(e)) {
                        petFace.movePupils(e.getSceneX(), e.getSceneY());
                    }
                });

                // MOUSE_PRESSED'i tamamen kaldırdık!
                // Bu satır buton click'lerini engelliyordu:
                // newScene.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> petFace.movePupils(e.getSceneX(), e.getSceneY()));
            }
        });
    }

    // Yardımcı metod: Mouse buton üzerinde mi?
    private boolean isMouseOnButton(MouseEvent e) {
        if (logoutButton != null && logoutButton.getScene() != null) {
            try {
                Bounds buttonBounds = logoutButton.localToScene(logoutButton.getBoundsInLocal());
                return buttonBounds.contains(e.getSceneX(), e.getSceneY());
            } catch (Exception ex) {
                // Hata durumunda false döndür
                return false;
            }
        }
        return false;
    }

    private void startDecayTimer() {
        // Her 30 saniyede bir decay (eskisinden daha yavaş)
        decayTimer = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            if (myPet != null) {
                myPet.decay();
                updateStatus();

                // Pet tehlikedeyse kullanıcıyı uyar
                if (myPet.isInDanger()) {
                    String warning = myPet.getWarningMessage();
                    if (!warning.isEmpty()) {
                        System.out.println("⚠️ WARNING: " + warning);
                        // İsteğe bağlı: Alert gösterebilirsiniz
                        // showAlert(Alert.AlertType.WARNING, warning);
                    }
                }
            }
        }));
        decayTimer.setCycleCount(Timeline.INDEFINITE);
        decayTimer.play();
    }

    private void updateFace() {
        Platform.runLater(() -> {
            Bounds bounds = petImageView.localToScene(petImageView.getBoundsInLocal());
            petFace.updatePosition(bounds);
        });
    }

    private void updateStatus() {
        if (myPet == null) return;

        // Temel bilgileri güncelle
        lblStatus.setText("Hunger: " + myPet.getHunger() + " | Fun: " + myPet.getFun());
        lblMood.setText("Mood: " + myPet.getMood());

        // Pet ismini güncelle (değişmiş olabilir)
        if (petNameLabel != null) {
            petNameLabel.setText(myPet.getName());
        }

        // Bar'ları güncelle
        updateBarWidth(hungerBar, myPet.getHunger(), true);
        updateBarWidth(funBar, myPet.getFun(), false);

        // Konsola genel durum yazdır
        System.out.println("Status Update: " + myPet.getOverallStatus());

        // Uyarı durumunu kontrol et
        String warning = myPet.getWarningMessage();
        if (!warning.isEmpty()) {
            // Status label'ını uyarı renginde göster
            lblStatus.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            // Normal renk
            lblStatus.setStyle("-fx-text-fill: black; -fx-font-weight: normal;");
        }
    }

    private void updateBarWidth(Rectangle bar, double value, boolean isHunger) {
        double maxWidth = 180;
        double percentage = Math.max(0, Math.min(value, 100)) / 100.0;
        double targetWidth = maxWidth * percentage;

        Timeline smooth = new Timeline(
                new KeyFrame(Duration.millis(200),
                        new javafx.animation.KeyValue(bar.widthProperty(), targetWidth))
        );
        smooth.play();

        if (isHunger) {
            int red = (int)((1 - percentage) * 255);
            int green = (int)(percentage * 255);
            bar.setFill(Color.rgb(red, green, 0));
        } else {
            int blue = (int)(percentage * 255);
            int red = (int)((1 - percentage) * 128);
            bar.setFill(Color.rgb(red, 0, blue));
        }
    }

    public ImageView getPetImageView() {
        return petImageView;
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        System.out.println("🔴 LOGOUT METHOD CALLED!"); // Debug mesajı

        try {
            // Çıkış yapmadan önce pet verilerini kaydet
            if (myPet != null) {
                myPet.forceSave();
                System.out.println("Pet data saved before logout");
            }

            // Timer'ı durdur
            if (decayTimer != null) {
                decayTimer.stop();
                System.out.println("Decay timer stopped");
            }

            // UserManegerService'i temizle
            UserManegerService.setCurrentUser(-1);
            UserManegerService.setPetName("");
            UserManegerService.setHunger(0);
            UserManegerService.setFun(0);
            System.out.println("UserManegerService cleared");

            // Başlangıç sayfasına dön
            Parent root = FXMLLoader.load(getClass().getResource("/com/virtanimal/startpage.fxml"));
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("✅ Logged out successfully");

        } catch (Exception e) {
            System.err.println("❌ Error during logout: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error during logout: " + e.getMessage());
        }
    }

    // Manual save butonu (FXML'de varsa)
    @FXML
    public void handleSavePet(ActionEvent event) {
        if (myPet != null) {
            myPet.forceSave();
            showAlert(Alert.AlertType.INFORMATION, "Pet data saved successfully!");
        }
    }

    // Pet istatistiklerini sıfırla (test için)
    @FXML
    public void handleResetStats(ActionEvent event) {
        if (myPet != null) {
            myPet.setHunger(50);
            myPet.setFun(50);
            updateStatus();
            showAlert(Alert.AlertType.INFORMATION, "Pet stats reset to 50/50!");
        }
    }

    // Pet durumunu göster
    @FXML
    public void handleShowStatus(ActionEvent event) {
        if (myPet != null) {
            String fullStatus = String.format(
                    "Pet Information:\n\n" +
                            "Name: %s\n" +
                            "User ID: %d\n" +
                            "Hunger: %d/100\n" +
                            "Fun: %d/100\n" +
                            "Mood: %s\n\n" +
                            "Overall Status: %s",
                    myPet.getName(),
                    myPet.getUserId(),
                    myPet.getHunger(),
                    myPet.getFun(),
                    myPet.getMood(),
                    myPet.getOverallStatus()
            );

            showAlert(Alert.AlertType.INFORMATION, fullStatus);
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Pet Game");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Uygulama kapanırken çağrılacak (eğer Window close event'i varsa)
    public void onApplicationClose() {
        if (myPet != null) {
            myPet.forceSave();
        }
        if (decayTimer != null) {
            decayTimer.stop();
        }
    }
}