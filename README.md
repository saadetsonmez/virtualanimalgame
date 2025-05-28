# Virtual Animal Game 🐱

This is a JavaFX-based virtual pet simulation game. Users can register, log in, and interact with an animated cat by feeding and playing with it. The cat's mood and stats update in real-time. User data is stored locally using JSON.

------

## ✨ Features

- 🔐 User Register & Login (JSON-based)
- 🐟 Drag-and-drop food and toys
- 👀 Eyes follow the mouse
- 😺 Mood and stats update dynamically
- 🎨 FXML + CSS UI with responsive design

------

📌 Requirements
Java 17+

JavaFX SDK (configured in your IDE)

Maven (if using pom.xml)

------

## 🗂 Project Structure (Simplified)
src/
├── main/
│ ├── java/
│ │ └── com/
│ │ ├── models/
│ │ │ ├── User.java
│ │ │ └── UserManager.java
│ │ ├── utils/
│ │ │ └── BackGround.java, Center.java
│ │ └── virtanimal/
│ │ ├── Controller.java
│ │ ├── LoginController.java
│ │ ├── RegisterController.java
│ │ ├── UserManagerService.java
│ │ └── HelloApplication.java
│
│ ├── resources/
│ │ └── com/virtanimal/
│ │ ├── images/
│ │ ├── login.fxml
│ │ ├── register.fxml
│ │ ├── startpage.fxml
│ │ └── style.css
│
├── pom.xml

------

## ▶️ How to Run

### Using IntelliJ IDEA (Recommended)
1. Clone this repo:
   ```bash
   git clone https://github.com/saadetsonmez/virtualanimalgame.git
   cd virtualanimalgame
2. Open the project in IntelliJ IDEA.

3. Ensure JavaFX SDK is added and configured in project settings.

4. Run the main class:  com.virtanimal.HelloApplication
   
5. The application should start with the Start Page screen.
ENJOY <3




