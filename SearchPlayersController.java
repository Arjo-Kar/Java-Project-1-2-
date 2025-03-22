package com.example.finalproject12;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public class SearchPlayersController {

    @FXML
    private VBox resultVBox;

    @FXML
    private TextField nameField, countryField, minSalaryField, maxSalaryField;

    @FXML
    private ComboBox<String> clubComboBox, positionComboBox;

    @FXML
    private ListView<String> resultListView; // No longer used after incorporating TableView

    private Main main;
    private PlayerSearchMenu playerSearchMenu;
    private List<Player> players; // List of players, assumed to be populated elsewhere

    // Initialize the controller with the main reference and the player list
    public void setMain(Main main, List<Player> players) {
        this.main = main;
        this.players = players;
        this.playerSearchMenu = new PlayerSearchMenu();

        // Set ComboBox options
        clubComboBox.setItems(FXCollections.observableArrayList(
                "Royal Challengers Bangalore", "Rajasthan Royals", "Gujarat Titans", "Chennai Super Kings",
                "Delhi Capitals", "Mumbai Indians", "Kolkata Knight Riders", "Punjab Kings",
                "Lucknow Super Giants", "Sunrisers Hyderabad", "ANY"
        ));

        positionComboBox.setItems(FXCollections.observableArrayList(
                "Batsman", "Bowler", "Allrounder", "Wicketkeeper"
        ));
    }

    @FXML
    public void searchByPlayerName(ActionEvent event) {
        clearAllFieldsExcept("name");
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showAlert("Invalid Input", "Please enter a player name.");
            return;
        }

        Player player = playerSearchMenu.searchByName(players, name);
        if (player != null) {
            displaySearchResults(List.of(player));
        } else {
            showAlert("Player Not Found", "No player found with the name: " + name);
        }
    }

    @FXML
    public void searchByClubAndCountry(ActionEvent event) {
        clearAllFieldsExcept("clubAndCountry");
        String country = countryField.getText().trim();
        String club = clubComboBox.getValue() != null ? clubComboBox.getValue().trim() : "ANY";

        List<Player> result = playerSearchMenu.searchByClubAndCountry(players, country, club);
        displaySearchResults(result);
    }

    @FXML
    public void searchByPosition(ActionEvent event) {
        clearAllFieldsExcept("position");
        String position = positionComboBox.getValue();
        if (position == null || position.trim().isEmpty()) {
            showAlert("Invalid Input", "Please select a position.");
            return;
        }

        List<Player> result = playerSearchMenu.searchByPosition(players, position);
        displaySearchResults(result);
    }

    @FXML
    public void searchBySalaryRange(ActionEvent event) {
        clearAllFieldsExcept("salaryRange");
        try {
            String minSalaryText = minSalaryField.getText().trim();
            String maxSalaryText = maxSalaryField.getText().trim();

            if (minSalaryText.isEmpty() || maxSalaryText.isEmpty()) {
                showAlert("Invalid Salary Range", "Please enter both minimum and maximum salary.");
                return;
            }

            int minSalary = Integer.parseInt(minSalaryText);
            int maxSalary = Integer.parseInt(maxSalaryText);

            if (minSalary > maxSalary) {
                showAlert("Invalid Salary Range", "Minimum salary cannot be greater than maximum salary.");
                return;
            }

            List<Player> result = playerSearchMenu.searchBySalaryRange(players, minSalary, maxSalary);
            displaySearchResults(result);
        } catch (NumberFormatException e) {
            showAlert("Invalid Salary Input", "Please enter valid salary numbers.");
        }
    }

    @FXML
    public void countryWisePlayerCount(ActionEvent event) {
        clearAllFieldsExcept("countryWiseCount");
        Map<String, Long> count = playerSearchMenu.countryCount(players);

        if (count.isEmpty()) {
            showAlert("No Data", "No country-wise player data available.");
            return;
        }

        displayCountryPlayerCount(count);
    }

    private void displayCountryPlayerCount(Map<String, Long> count) {
        TableView<Map.Entry<String, Long>> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create columns for Country and Player Count
        TableColumn<Map.Entry<String, Long>, String> countryColumn = new TableColumn<>("Country");
        countryColumn.setCellValueFactory(entry -> new SimpleStringProperty(entry.getValue().getKey()));

        TableColumn<Map.Entry<String, Long>, Long> playerCountColumn = new TableColumn<>("Player Count");
        playerCountColumn.setCellValueFactory(entry -> new SimpleLongProperty(entry.getValue().getValue()).asObject());

        // Add columns to the TableView
        tableView.getColumns().addAll(countryColumn, playerCountColumn);

        if (count == null || count.isEmpty()) {
            showAlert("No Results", "No players found for the given search criteria.");
        } else {
            // Convert the map entries directly into the TableView
            ObservableList<Map.Entry<String, Long>> observableResults = FXCollections.observableArrayList(count.entrySet());
            tableView.setItems(observableResults);
        }

        resultVBox.getChildren().clear(); // Clear previous results
        resultVBox.getChildren().add(tableView); // Add the TableView to the VBox
    }


    // Helper method to clear all fields except the one relevant to the current search
    private void clearAllFieldsExcept(String context) {
        if (!context.equals("name")) nameField.clear();
        if (!context.equals("clubAndCountry")) {
            countryField.clear();
            clubComboBox.setValue(null);
        }
        if (!context.equals("position")) positionComboBox.setValue(null);
        if (!context.equals("salaryRange")) {
            minSalaryField.clear();
            maxSalaryField.clear();
        }
    }

    @FXML
    public void backToHome(ActionEvent event) throws Exception {
        main.showHomePage();
    }

    // Display search results using a TableView
    private void displaySearchResults(List<Player> results) {
        TableView<Player> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create columns for Player attributes
        TableColumn<Player, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Player, String> countryColumn = new TableColumn<>("Country");
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

        TableColumn<Player, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Player, Double> heightColumn = new TableColumn<>("Height (m)");
        heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));

        TableColumn<Player, String> clubColumn = new TableColumn<>("Club");
        clubColumn.setCellValueFactory(new PropertyValueFactory<>("club"));

        TableColumn<Player, String> positionColumn = new TableColumn<>("Position");
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        TableColumn<Player, String> jerseyNumberColumn = new TableColumn<>("Jersey Number");
        jerseyNumberColumn.setCellValueFactory(cellData -> {
            // Get the jersey number from the Player object
            Integer jerseyNumber = cellData.getValue().getJerseyNumber();
            // Return "N/A" if the jersey number is negative, else return the number as a string
            return new SimpleStringProperty(jerseyNumber < 0 ? "N/A" : String.valueOf(jerseyNumber));
        });

        TableColumn<Player, Integer> salaryColumn = new TableColumn<>("Weekly Salary ($)");
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));

        // Add all columns to the TableView
        tableView.getColumns().addAll(nameColumn, countryColumn, ageColumn, heightColumn, clubColumn, positionColumn, jerseyNumberColumn, salaryColumn);

        if (results == null || results.isEmpty()) {
            showAlert("No Results", "No players found for the given search criteria.");
        } else {
            ObservableList<Player> observableResults = FXCollections.observableArrayList(results);
            tableView.setItems(observableResults);
        }

        resultVBox.getChildren().clear(); // Clear previous results
        resultVBox.getChildren().add(tableView); // Add the TableView to the VBox
    }

    // Display an alert with a message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
