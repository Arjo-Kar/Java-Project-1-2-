package com.example.finalproject12;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HomeController {
    private static final String INPUT_FILE_NAME = "players.txt";
    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    public void navigateToSearchPlayers(ActionEvent event) throws Exception {
        // Ensure 'main' is initialized and has a valid reference to MainMenu
        if (main.getMainMenu() == null) {
            main.setMainMenu(new MainMenu());  // Initialize mainMenu if it's null
        }

        // Get the list of players from mainMenu
        List<Player> players = main.getMainMenu().getPlayerList();

        // Call the method to show the Search Players page, passing the list of players
        main.showSearchPlayersPage(players);
    }

    @FXML
    public void navigateToSearchClubs(ActionEvent event) throws Exception {
        // Ensure 'main' is initialized and has a valid reference to MainMenu
        if (main.getMainMenu() == null) {
            main.setMainMenu(new MainMenu());  // Initialize mainMenu if it's null
        }

        // Get the list of players from mainMenu
        List<Player> players = main.getMainMenu().getPlayerList();
        main.showSearchClubsPage(players);
    }

    @FXML
    public void navigateToAddPlayer(ActionEvent event) throws Exception {
        if (main.getMainMenu() == null) {
            main.setMainMenu(new MainMenu());  // Initialize mainMenu if it's null
        }

        // Get the list of players from mainMenu
        List<Player> players = main.getMainMenu().getPlayerList();
        main.showAddPlayerPage(players);
    }

    @FXML
     public void BackToMain(ActionEvent event)throws Exception {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(INPUT_FILE_NAME))) {
            List<Player> players = main.getMainMenu().getPlayerList();
            for (Player player : players) {

                bw.write(player.getName() + "," + player.getCountry() + "," + player.getAge() + ","
                        + player.getHeight() + "," + player.getClub() + "," + player.getPosition() + ","
                        + (player.getJerseyNumber() == -1 ? -1 : player.getJerseyNumber()) + ","
                        + player.getSalary());

                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
        main.showLoginPage();
       // Close the application
    }
}
