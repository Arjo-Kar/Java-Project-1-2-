package com.example.finalproject12;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchClubsController {

    public VBox resultVBox;
    private Main main;
    private ClubSearchMenu clubSearchMenu;
    private List<Player> players; // List of players, assumed to be populated elsewhere

    @FXML
    private ComboBox<String> clubComboBox;

    @FXML
    private ListView<String> resultListView;

    // Initialize the controller with the main reference and the player list
    public void setMain(Main main, List<Player> players) {
        this.main = main;
        this.players = players;
        this.clubSearchMenu = new ClubSearchMenu();

        // Set ComboBox options for clubs
        clubComboBox.setItems(FXCollections.observableArrayList(
                "Royal Challengers Bangalore", "Rajasthan Royals", "Gujarat Titans", "Chennai Super Kings",
                "Delhi Capitals", "Mumbai Indians", "Kolkata Knight Riders", "Punjab Kings",
                "Lucknow Super Giants", "Sunrisers Hyderabad"
        ));
    }

    @FXML
    public void searchByMaxSalary(ActionEvent event) {
        resultListView.getItems().clear(); // Clear previous results
        String club = clubComboBox.getValue();
        if (club == null || club.trim().isEmpty()) {
            showAlert("Invalid Input", "Please select a club.");
            return;
        }

        List<Player> result = clubSearchMenu.maxSalaryOfClub(players, club);
        displaySearchResults(result);
    }

    @FXML
    public void searchByMaxAge(ActionEvent event) {

        resultListView.getItems().clear(); // Clear previous results
        String club = clubComboBox.getValue();
        if (club == null || club.trim().isEmpty()) {
            showAlert("Invalid Input", "Please select a club.");
            return;
        }

        List<Player> result = clubSearchMenu.maxAgeOfClub(players, club);
         displaySearchResults(result);
    }

    @FXML
    public void searchByMaxHeight(ActionEvent event) {

        resultListView.getItems().clear(); // Clear previous results
        String club = clubComboBox.getValue();
        if (club == null || club.trim().isEmpty()) {
            showAlert("Invalid Input", "Please select a club.");
            return;
        }

        List<Player> result = clubSearchMenu.maxHeightOfClub(players, club);
        displaySearchResults(result);
    }

    @FXML
    public void searchByTotalYearlySalary(ActionEvent event) {

        resultListView.getItems().clear(); // Clear previous results
        String club = clubComboBox.getValue();
        List<Player>result = new ArrayList<>();
        for(Player player: players){
            if(player.getClub().equalsIgnoreCase(club)){
                result.add(player);
            }
        }
        if (club == null || club.trim().isEmpty()) {
            showAlert("Invalid Input", "Please select a club.");
            return;
        }

        Long totalSalary = clubSearchMenu.calculateTotalYearlySalary(players, club);
        HashMap<String, Long>res = new HashMap<>();
        res.put(club, totalSalary);
        displaySearchResultsTotalSalary(res);
    }

    // Helper method to clear all fields except the one relevant to the current search
    private void displaySearchResultsTotalSalary(Map<String, Long> count) {
        TableView<Map.Entry<String, Long>> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create columns for Country and Player Count
        TableColumn<Map.Entry<String, Long>, String> countryColumn = new TableColumn<>("Club");
        countryColumn.setCellValueFactory(entry -> new SimpleStringProperty(entry.getValue().getKey()));

        TableColumn<Map.Entry<String, Long>, Long> playerCountColumn = new TableColumn<>("Total Yearly Salary");
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


    // Navigate back to Main Menu
    @FXML
    public void backToMainMenu(ActionEvent event) throws Exception {
        main.showHomePage();
    }

    // Display search results in the ListView
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

    public void setClubComboBox(ComboBox<String> clubComboBox) {
        this.clubComboBox = clubComboBox;
    }
}
