package com.example.finalproject12;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketPlaceController {
    @FXML
    private ListView<Player> playerListView; // List of players available for transfer
    @FXML
    private Label playerNameLabel;
    @FXML
    private Label currentClubLabel;
    @FXML
    private Button buyPlayerButton;
    @FXML
    private Button RefreshMarketPlace;
    private Main main;
    private MainMenu mainMenu;
    private Server server = new Server(); // Shared server instance
    private ObservableList<Player> availablePlayers;
    private ObservableList<Player>allplayers;// Players on the transfer list
    private Player selectedPlayer;
    public void setMainMenu(MainMenu mainMenu){
        this.mainMenu = mainMenu;
    }
    public void setMain(Main main) {
        this.main = main;
    }



    @FXML

    private void initialize() {
        if (server == null) {
            throw new IllegalStateException("Server instance is not set. Please initialize the controller with a shared server.");
        }

        // Fetch the shared marketplace list from the server
        try {
            availablePlayers = FXCollections.observableArrayList(server.getMarketplacePlayers());
            allplayers = FXCollections.observableArrayList(server.getAllPlayer());
            playerListView.setItems(availablePlayers);

            // Set the custom cell factory for the ListView to use PlayerListCell
            playerListView.setCellFactory(param -> new PlayerListCell());
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception to see where it fails
            showErrorAlert("Failed to Load Marketplace", "An error occurred while loading the marketplace. Please try again.");
        }

        System.out.println("Available Players: " + availablePlayers.size()); // Debug line to check players

        // Listener for player selection
        playerListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedPlayer = newValue;
            updatePlayerDetails();
        });

        buyPlayerButton.setDisable(true); // Disable button initially
    }

    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public class PlayerListCell extends ListCell<Player> {
        private VBox content;
        private Label nameLabel;
        private Label countryLabel;
        private Label positionLabel;

        public PlayerListCell() {
            nameLabel = new Label();
            countryLabel = new Label();
            positionLabel = new Label();

            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
            countryLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            positionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            content = new VBox(nameLabel, countryLabel, positionLabel);
            content.setSpacing(6);
            content.setStyle("-fx-border-color: #7a8b8f; -fx-border-radius: 10; -fx-padding: 10; -fx-background-radius: 5;");
        }

        @Override
        protected void updateItem(Player player, boolean empty) {
            super.updateItem(player, empty);
            if (empty || player == null) {
                setGraphic(null);
                setStyle(null); // Reset style for empty cells
            } else {
                nameLabel.setText("Name: " + player.getName());
                countryLabel.setText("Country: " + player.getCountry());
                positionLabel.setText("Position: " + player.getPosition());

                if (isSelected()) {
                    // Apply styles for selected state
                    content.setStyle("-fx-background-color: lightgray; -fx-border-color: #7a8b8f; -fx-border-radius: 7; -fx-padding: 10; -fx-background-radius: 5;");
                    nameLabel.setTextFill(Color.BLACK);
                    nameLabel.setFont(new javafx.scene.text.Font("Name", 14));
                    countryLabel.setTextFill(Color.DARKSLATEGRAY);
                    positionLabel.setTextFill(Color.DARKSLATEGRAY);
                } else {
                    // Apply styles for non-selected state with darker border and labels
                    content.setStyle("-fx-background-color: white; -fx-border-color: #4d5b5e; -fx-border-radius: 7; -fx-padding: 10; -fx-background-radius: 5;");
                    nameLabel.setTextFill(Color.BLACK);
                    nameLabel.setFont(new javafx.scene.text.Font("Name", 14));
                    countryLabel.setTextFill(Color.GRAY); // Darkened color for country label
                    positionLabel.setTextFill(Color.GRAY); // Darkened color for position label
                }

                setGraphic(content);
            }
        }
    }


    private void updatePlayerDetails() {
        if (selectedPlayer != null) {
            playerNameLabel.setText("Player: " + selectedPlayer.getName());
            currentClubLabel.setText("Current Club: " + selectedPlayer.getClub());
            buyPlayerButton.setDisable(false); // Enable the button when a player is selected
        } else {
            playerNameLabel.setText("Player: -");
            currentClubLabel.setText("Current Club: -");
            buyPlayerButton.setDisable(true); // Disable the button when no player is selected
        }
    }
    @FXML

    private void handleRefresh() throws Exception {

            try {
                // Clear the server's marketplace player list
                server.clearMarketplacePlayers();

                // Reload players from the file
                server.loadPlayersFromFile("BuyablePlayer.txt");

                // Fetch the updated list of players
                List<Player> players = server.getMarketplacePlayers();

                // Clear and update the observable list
                availablePlayers.clear();
                availablePlayers.addAll(players);

                // Update the ListView with the refreshed data
                playerListView.setItems(availablePlayers);

                System.out.println("Marketplace refreshed successfully. Current players: " + players.size());
            } catch (Exception e) {
                e.printStackTrace();
                showErrorAlert("Failed to Refresh Marketplace", "An error occurred while refreshing the marketplace. Please try again.");
            }

    }


    @FXML
    private void handleBuyPlayerAction() throws Exception {
        if (selectedPlayer != null) {
            String newClub = LoginController.getClubName();

            // Attempt to buy the player from the shared server
            boolean success = server.buyPlayer(newClub, selectedPlayer);

            if (success) {
                showSuccessAlert(newClub);

                // Remove the player from the old club in players_Server.txt (teamMap)
                String oldClub = selectedPlayer.getClub(); // Get the current club before changing

                // Ensure player is removed from the old club

                    // Update allplayers and save data
//                List<Player>All = new ArrayList<>(allplayers);
                   for(Player player: allplayers){
                       if(player.getName().equalsIgnoreCase(selectedPlayer.getName())){
                           player.setClub(newClub);
                           break;
                       }
                   }

                    List<Player> updatedPlayers = new ArrayList<>(availablePlayers);
                    updatedPlayers.removeIf(player -> player.getName().equals(selectedPlayer.getName()));
                try {
                    mainMenu.savePlayersData("players_Server.txt", allplayers);
                    mainMenu.loadDatabase("players_Server.txt");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }



//                server.savePlayersData("players_Server.txt", allplayers);

                // Remove the bought player from the available players list


                // Save the updated available players list to BuyablePlayer.txt
                try {
                    server.saveUpdatedMarketplace("BuyablePlayer.txt", updatedPlayers);
                } catch (IOException e) {
                    System.out.println("Error updating BuyablePlayer.txt: " + e.getMessage());
                    e.printStackTrace();
                }

                // Update the observable list
                availablePlayers.setAll(updatedPlayers);

                // Refresh the ListView to reflect the updated list of available players
                playerListView.setItems(availablePlayers);

                // Prepare the updated list of players for the new club
                List<Player> finalPlayer = new ArrayList<>();
                for (Player player : allplayers) {
                    if (player.getClub().equalsIgnoreCase(newClub)) {
                        finalPlayer.add(player);
                    }
                }

              main.showClubManagerDashboard(newClub, finalPlayer);
            } else {
                showErrorAlert("Player Unavailable", "The selected player is no longer available in the marketplace.");
            }
        } else {
            System.out.println("No player selected for transfer.");
        }
    }


    private void showSuccessAlert(String newClub) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Player Purchased");
        alert.setHeaderText("Transfer Successful");
        alert.setContentText(selectedPlayer.getName() + " has been transferred to " + newClub + ".");
        alert.showAndWait();
    }


    public void refreshAvailablePlayers() {
        Platform.runLater(() -> {
            availablePlayers.clear(); // Clear existing players
            availablePlayers.addAll(server.getMarketplacePlayers()); // Populate from the server
        });
    }

    public void addPlayerToMarket(List<Player> players) {
        Platform.runLater(() -> {
            // Add each player in the list to the shared server marketplace
            for (Player player : players) {
                server.addPlayerToMarketplace(player);

                // Notify all clients about the update
                server.notifyClients("ADD_PLAYER " + player.getName() + "," + player.getClub());
            }

            // Update the ListView with the newly added players
            availablePlayers.addAll(players);  // Add all players in one go for efficiency
            playerListView.setItems(availablePlayers);
        });
    }
}
