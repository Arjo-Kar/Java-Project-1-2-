package com.example.finalproject12;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.List;

public class LoginController {


    @FXML
    private Button visitorButton;

    @FXML
    private Button clubManagerButton;

    @FXML
    private TextField clubNameTextField; // TextField for club name

    @FXML
    private PasswordField passwordField; // PasswordField for password

    private Main main;
    private static String clubName;

    public static String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        LoginController.clubName = clubName;
    }

    public void setMain(Main main) {
        this.main = main;
    }
    // Reference to the main application

    /**
     * Handles the "Login as Visitor" button action.
     */
    @FXML
    private void visitorLoginAction() {
        try {
            if (main != null) {
                main.showHomePage();
            } else {
                showAlert(AlertType.ERROR, "Navigation Error", "Main application reference is missing.");
            }
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Navigation Error", "Failed to navigate to the home page.");
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Login as Club Manager" button action.
     */
    @FXML
    private void clubManagerLoginAction() {
        Server server = new Server();
        try {
            String clubName = clubNameTextField.getText();
            String password = passwordField.getText();
            LoginController.clubName = clubNameTextField.getText();
            // Validate input fields
            if (clubName.isEmpty() || password.isEmpty()) {
                showAlert(AlertType.ERROR, "Input Error", "Please enter both Club Name and Password.");
                return;
            }

            // Validate credentials
            String storedPassword = server.userMap.get(clubName);
            if (storedPassword == null) {
                showAlert(AlertType.ERROR, "Login Failed", "Invalid Club Name or Password.");
                return;
            }

            if (!storedPassword.equals(password)) {
                showAlert(AlertType.ERROR, "Login Failed", "Incorrect password.");
                return;
            }

            // Retrieve players
            List<Player> players = server.teamMap.get(clubName);

            if (players == null || players.isEmpty()) {
                showAlert(AlertType.ERROR, "No Players Found", "No players found for the selected club.");
                return;
            }

            // Navigate to Club Manager Dashboard
            if (main != null) {
                main.showClubManagerDashboard(clubName, players);
            } else {
                showAlert(AlertType.ERROR, "Navigation Error", "Main application reference is missing.");
            }

        } catch (NullPointerException e) {
            showAlert(AlertType.ERROR, "Null Pointer Exception", "A required object was not found. Please check the inputs and try again.");
            e.printStackTrace();  // Log full stack trace for debugging
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Unexpected Error", "An unexpected error occurred during login: " + e.getMessage());
            e.printStackTrace();  // Log full stack trace for debugging
        }
    }

    /**
     * Displays an alert dialog with the specified parameters.
     *
     * @param alertType The type of the alert.
     * @param title     The title of the alert.
     * @param message   The message to display.
     */

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets the reference to the main application.
     *
     * @param main The main application instance.
     */

}
