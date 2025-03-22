package com.example.finalproject12;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class AddPlayersController {
    private Main main;
    private List<Player> players = new ArrayList<>();
    private MainMenu mainMenu;

    public void setMain(Main main, List<Player>players) {
        this.main = main;
        this.players = players;
    }

    @FXML
    private TextField nameField, countryField, ageField, heightField, numberField, weeklySalaryField;

    @FXML
    private ComboBox<String> clubComboBox, positionComboBox;

    // Initialize the controller and populate the combo boxes
    @FXML
    public void initialize() {
        clubComboBox.setItems(FXCollections.observableArrayList(
                "Royal Challengers Bangalore", "Rajasthan Royals", "Gujarat Titans", "Chennai Super Kings",
                "Delhi Capitals", "Mumbai Indians", "Kolkata Knight Riders", "Punjab Kings",
                "Lucknow Super Giants", "Sunrisers Hyderabad"
        ));

        positionComboBox.setItems(FXCollections.observableArrayList(
                "Batsman", "Bowler", "Allrounder", "Wicketkeeper"
        ));
    }

    // Method to handle adding player data
    @FXML
    public void addPlayer(ActionEvent event) {
        try {
            // Retrieve and format input
            String name = formatString(nameField.getText());
            String country = formatString(countryField.getText());
            int age = Integer.parseInt(ageField.getText());
            double height = Double.parseDouble(heightField.getText());
            String club = clubComboBox.getValue();
            String position = positionComboBox.getValue();
            int jerseyNumber = numberField.getText().isEmpty() ? -1 : Integer.parseInt(numberField.getText());
            int weeklySalary = Integer.parseInt(weeklySalaryField.getText());

            // Validate inputs
            if (name.isEmpty() || country.isEmpty() || club == null || position == null) {
                System.out.println("Please fill all mandatory fields.");
                return;
            }

            // Check if player already exists using try-catch block
            try {
                for (Player player : players) {
                    if (player.getName().equalsIgnoreCase(name)) {
                        // Show alert if player exists
                        showAlert("Player already exists", "A player with the name " + name + " is already in the database.");
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error occurred while checking player existence: " + e.getMessage());
                return;
            }

            // Add the new player
            Player newPlayer = new Player(name, country, age, height, club, position, jerseyNumber, weeklySalary);
            players.add(newPlayer);
//            System.out.println("Player added successfully: " + newPlayer);

            // Clear input fields after successful addition
            clearInputFields();
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input. Please check age, height, jersey number, or salary fields.");
        }
    }

    // Method to show an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // Method to format strings (capitalizes the first letter)
    private String formatString(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        return input.trim().substring(0, 1).toUpperCase() + input.trim().substring(1).toLowerCase();
    }

    // Clear all input fields
    private void clearInputFields() {
        nameField.clear();
        countryField.clear();
        ageField.clear();
        heightField.clear();
        numberField.clear();
        weeklySalaryField.clear();
        clubComboBox.getSelectionModel().clearSelection();
        positionComboBox.getSelectionModel().clearSelection();
    }

    // Method to go back to the Home page
    @FXML
    public void backToHome(ActionEvent event) throws Exception {
        main.showHomePage();
    }
}
