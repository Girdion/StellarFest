package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import view.LoginPage;

public class PageController {

    private Stage primaryStage;  // Reference to the main stage
    private Scene currentScene;  // Reference to the current scene

    public PageController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Navigate to Login Page
    public void navigateToLogin() {
        LoginPage loginPage = new LoginPage();
        currentScene = loginPage.getScene();  // Assuming getScene() returns the scene for LoginPage
        primaryStage.setScene(currentScene);
        primaryStage.setTitle("Login Page");
        primaryStage.show();
    }
   
}
