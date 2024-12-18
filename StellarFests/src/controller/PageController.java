package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import view.AdminPage;
import view.DeleteUserPage;
import view.EditProfilePage;
import view.EventOrganizerPage;
import view.GuestPage;
import view.LoginPage;
import view.VendorPage;
import view.ViewAllEventsPage;
import view.ViewAllUsersPage;
import view.ViewEventDetailsPage;
import view.ViewEventsPage;
import model.Event;
import model.EventOrganizer;

public class PageController {

    private Stage primaryStage;  // Reference to the main stage
    private Scene currentScene;  // Reference to the current scene

    public PageController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Navigate to Login Page
    public void navigateToLogin() {
        UserController userController = new UserController();  // Instantiate UserController
        LoginPage loginPage = new LoginPage(primaryStage, this, userController);  // Pass it to LoginPage constructor
        currentScene = loginPage.getScene();
        primaryStage.setScene(currentScene);
        primaryStage.setTitle("Login Page");
        primaryStage.show();
    }

    
    // Navigate to Admin Page
    public void navigateToAdmin() {
        AdminPage adminPage = new AdminPage(primaryStage, this, null);
        currentScene = adminPage.getScene();  // Assuming AdminPage has a getScene() method
        primaryStage.setScene(currentScene);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.show();
    }

    // Navigate to Guest Page
    public void navigateToGuest() {
        GuestPage guestPage = new GuestPage();
        currentScene = guestPage.getScene();
        primaryStage.setScene(currentScene);
        primaryStage.setTitle("Guest Dashboard");
        primaryStage.show();
    }

    // Navigate to Event Organizer Page
    public void navigateToEventOrganizer() {
    	EventOrganizerController eventController = new EventOrganizerController();
        EventOrganizerPage organizerPage = new EventOrganizerPage(primaryStage, this, eventController);
        currentScene = organizerPage.getScene();
        primaryStage.setScene(currentScene);
        primaryStage.setTitle("Event Organizer Dashboard");
        primaryStage.show();
    }

    // Navigate to Vendor Page
    public void navigateToVendor() {
        VendorPage vendorPage = new VendorPage();
        currentScene = vendorPage.getScene();
        primaryStage.setScene(currentScene);
        primaryStage.setTitle("Vendor Dashboard");
        primaryStage.show();
    }
    
    public void navigateToEventDetails(EventOrganizer selectedEvent) {
        // Ensure the selected event is not null
        if (selectedEvent == null) {
            System.out.println("Error: Selected event is null. Cannot navigate to event details page.");
            return;
        }

        // Create or show the event details page with the given event details
        ViewEventDetailsPage eventDetailsPage = new ViewEventDetailsPage(primaryStage, this, selectedEvent);
        currentScene = eventDetailsPage.getScene();
        primaryStage.setScene(currentScene);
        primaryStage.setTitle("Event Details Page");
        primaryStage.show();
    }
    
    public void navigateToEvents() {
        // Create or show the ViewEventsPage
        ViewEventsPage viewEventsPage = new ViewEventsPage(primaryStage, this);
        currentScene = viewEventsPage.getScene(); // Get the scene from ViewEventsPage
        primaryStage.setScene(currentScene);
        primaryStage.setTitle("Events Page");
        primaryStage.show();
    }
    
    public void navigateToAllEvents() {
    	ViewAllEventsPage allEventsPage = new ViewAllEventsPage(primaryStage, this);
    	currentScene = allEventsPage.getScene(); // Get the scene from ViewEventsPage
        primaryStage.setScene(currentScene);
        primaryStage.setTitle("All Events Page");
        primaryStage.show();
    }
    
    public void navigateToAllUsers() {
    	ViewAllUsersPage allUsersPage = new ViewAllUsersPage(primaryStage, this);
    	currentScene = allUsersPage.getScene();
    	primaryStage.setScene(currentScene);
    	primaryStage.setTitle("All Users Page");
    	primaryStage.show();
    }
    
    public void navigateToDeleteUsers() {
    	DeleteUserPage deleteUserPage = new DeleteUserPage(primaryStage, this);
    	currentScene = deleteUserPage.getScene();
    	primaryStage.setScene(currentScene);
    	primaryStage.setTitle("Delete Users Page");
    	primaryStage.show();
    }
    
    public void navigateToEditProfile() {
        // Initialize the UserController and AdminController
        UserController userController = new UserController();
        AdminController adminController = new AdminController(); // If you need this, initialize it here

        // Pass the initialized controllers to EditProfilePage
        EditProfilePage editProfilePage = new EditProfilePage(primaryStage, this, adminController, userController);

        // Set the scene for EditProfilePage
        currentScene = editProfilePage.getScene();
        primaryStage.setScene(currentScene);
        primaryStage.setTitle("Edit Profile Page");
        primaryStage.show(); 
    }



}
