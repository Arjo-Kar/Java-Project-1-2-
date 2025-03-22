package com.example.finalproject12;

import java.io.*;
import java.util.*;

public class MainMenu {
    private List<Player> players;

    // Constructor or method to initialize playerList
    public MainMenu() {
        players = new ArrayList<>();  // Initialize the player list
        // Add players to the list here, or load from a file/database
    }

    // Getter for the player list


    Scanner scanner = new Scanner(System.in);

    private String Stringformatting(String input) {
        String[] words = input.trim().replaceAll("\\s+", " ").split(" ");

        StringBuilder formattedString = new StringBuilder();
        for (String word : words) {
            formattedString.append(Character.toUpperCase(word.charAt(0))) 
                    .append(word.substring(1).toLowerCase()) 
                    .append(" ");
        }

        String result = formattedString.toString().trim();
        return result;
    }

    public void loadDatabase(String INPUT_FILE_NAME) {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME))) {
            while (true) {
                String line = br.readLine();
                if (line == null) break;

                if (line.trim().isEmpty()) continue; // Skip empty lines

                String[] parts = line.split(",");
                if (parts.length != 8) {
                    System.err.println("Malformed line: " + line);
                    continue; // Skip malformed lines
                }

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim(); // Trim whitespace
                }

                try {
                    String name = parts[0];
                    String country = parts[1];
                    int age = Integer.parseInt(parts[2]);
                    double height = Double.parseDouble(parts[3]);
                    String club = parts[4];
                    String position = parts[5];
                    int jerseyNumber = parts[6].isEmpty() ? -1 : Integer.parseInt(parts[6]);
                    int weeklySalary = Integer.parseInt(parts[7]);

                    Player newPlayer = new Player(name, country, age, height, club, position, jerseyNumber, weeklySalary);
                    players.add(newPlayer);

//                    System.out.println("Added player: " + newPlayer);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format in line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void savePlayersData(String filename, List<Player>All) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (Player player : All) {
                String playerData = String.join(",",
                        player.getName(),
                        player.getCountry(),
                        String.valueOf(player.getAge()),
                        String.valueOf(player.getHeight()),
                        player.getClub(),
                        player.getPosition(),
                        String.valueOf(player.getJerseyNumber()),
                        String.valueOf(player.getSalary())
                );
                writer.write(playerData);
                writer.newLine();

            }
        } catch (IOException e) {
            System.out.println("Error writing player data: " + e.getMessage());
        }
    }
    public List<Player> getPlayerList() {
        return players;
    }
    public void loadMenu(String OUTPUT_FILE_NAME) {
        System.out.println("\nWelcome to the Cricket Player Database System!\n");
        int choice = -1;
        while (choice != 4) {
            System.out.println("Main Menu:\n(1) Search Players\n(2) Search Clubs\n(3) Add Player\n(4) Exit System");

            while (choice < 1 || choice > 4) {
                System.out.print("Enter your choice (1-4): ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a valid integer for Menu category.");
                    scanner.nextLine();
                    continue;
                }
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < 1 || choice > 4) {
                    System.out.println("Invalid choice.");
                } else {
                    break;
                }
            }

            switch (choice) {
                case 1:
                    displayplayerSearchMethod(players);
                    break;

                case 2:
                    displayclubSearchMethod(players);
                    break;

                case 3:
                    addPlayerMethod(players);
                    break;

                case 4:
                    System.out.println("Exiting the system........");
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME))) {
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
                    return;
            }
            choice = -1;
        }
    }

    private void displayplayerSearchMethod(List<Player> players) {

        int playerSearchOption = -1;
        PlayerSearchMenu newPlayerSearch = new PlayerSearchMenu();

        while (playerSearchOption != 7) {
            
            System.out.print("\nPlayer Searching Options:\n(1) By Player Name\n(2) By Club and Country\n(3) By Position\n"
                    + "(4) By Salary Range\n(5) Country-wise Player Count\n(6) Back to Main Menu\n(7) By Country and Salary Range\n");

            while (playerSearchOption < 1 || playerSearchOption > 7) {
                System.out.print("\nEnter your choice (1-7): ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a valid integer for player search category.");
                    scanner.nextLine();
                    continue;
                }
                playerSearchOption = scanner.nextInt();
                scanner.nextLine();
                if (playerSearchOption < 1 || playerSearchOption > 7) {
                    System.out.println("\nInvalid choice. Please enter a number between 1 and 6.");
                }
            }

            if (playerSearchOption == 6) { 
                System.out.println("\nReturning back to MainMenu........");
            }

            switch (playerSearchOption) {
                case 1:
                    System.out.print("Enter player name: ");
                    String name = scanner.nextLine();
                    name = Stringformatting(name);

                    Player playerByName = newPlayerSearch.searchByName(players, name);
                    if (playerByName != null) {
                        System.out.println(playerByName.toString());
                    } else {
                        System.out.println("No such player with this name.");
                    }
                    break;

                case 2:
                    System.out.print("Enter country name: ");
                    String countryName = scanner.nextLine();
                    countryName = Stringformatting(countryName);

                    System.out.print("Enter club name (or 'ANY' for any club): ");
                    String clubName = scanner.nextLine();
                    clubName = Stringformatting(clubName);

                    List<Player> playersByCountryClub = newPlayerSearch.searchByClubAndCountry(players, countryName, clubName);
                    if (playersByCountryClub.isEmpty()) {
                        System.out.println("No such player with this country and club.");
                    } else {
                       

                        for (Player player : playersByCountryClub) {
                            System.out.println(player.toString());
                        }
                    }
                    break;

                case 3:
                    System.out.print("Enter position: ");
                    String position = scanner.nextLine();
                    position = Stringformatting(position);

                    List<Player> playersByPosition = newPlayerSearch.searchByPosition(players, position);
                    if (playersByPosition.isEmpty()) {
                        System.out.println("No such player with this position.");
                    } else {
                        
                        for (Player player : playersByPosition) {
                            System.out.println(player.toString());
                        }
                    }
                    break;

                case 4:
                    System.out.print("Enter minimum salary: ");
                    int minSalary = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter maximum salary: ");
                    int maxSalary = scanner.nextInt();
                    List<Player> playersInRange = newPlayerSearch.searchBySalaryRange(players, minSalary, maxSalary);
                    if (playersInRange.isEmpty()) {
                        System.out.println("No players found in the given salary range.");
                    } else {
                        System.out.println("Players found: ");
                        for (Player p : playersInRange) {
                            System.out.println(p.toString());
                        }
                    }
                    break;

                case 5:
                   
                    HashMap<String, Long> countryCounts = new HashMap<>();
                    for (Player player : players) {
                        countryCounts.put(player.getCountry(),
                                countryCounts.getOrDefault(player.getCountry(), 0L) + 1);
                    }
                    printCountryCounts(countryCounts);
                    break;

                case 6:
                    return;
                case 7:
                    System.out.print("Enter Country Name: ");
                    String country = scanner.nextLine();
                    country = Stringformatting(country);
                    System.out.print("Enter minimum salary: ");
                    int minSal = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter maximum salary: ");
                    int maxSal = scanner.nextInt();

                    List<Player>result = newPlayerSearch.byCountryNameandSalRange(players, minSal, maxSal, country);
                    if(result.isEmpty()){
                        System.out.println("No player is found with this country and this salary range");
    
                    }
                    else{
                        
                        for (Player p : result) {
                            System.out.println(p.toString());
                        }
                    }
                    break;
            }
            playerSearchOption = -1; 
        }
    }
   public static void printCountryCounts(HashMap<String, Long> countryCounts) {
    if (countryCounts == null || countryCounts.isEmpty()) {
        System.out.println("No players available to display country counts.");
        return;
    }

    countryCounts.forEach((country, count) -> 
        System.out.println("Number of players from " + country + ": " + count)
    );
}


    private void displayclubSearchMethod(List<Player> players) {

        int clubSearchOption = -1;
        ClubSearchMenu newClubSearch = new ClubSearchMenu();
        while (clubSearchOption != 5) {
            System.out.print("Club Searching Options:\n"
                    + "(1) Player(s) with the maximum salary of a club\n"
                    + "(2) Player(s) with the maximum age of a club\n"
                    + "(3) Player(s) with the maximum height of a club\n"
                    + "(4) Total yearly salary of a club\n"
                    + "(5) Back to Main Menu\n");

            while (clubSearchOption < 1 || clubSearchOption > 5) {
                System.out.print("Enter your choice (1-5): ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a valid integer for club search category.");
                    scanner.nextLine();
                    continue;
                }
                clubSearchOption = scanner.nextInt();
                scanner.nextLine();
                if (clubSearchOption < 1 || clubSearchOption > 5) {
                    System.out.println("Invalid choice.");
                }
            }

            if (clubSearchOption == 5) {
                System.out.println("Returning back to MainMenu........");
                return;
            }

            System.out.print("Enter the club name: ");
            String clubName = scanner.nextLine();
            clubName = Stringformatting(clubName);

            switch (clubSearchOption) {
                case 1:
                   
                    List<Player> maxSalaryPlayers = newClubSearch.maxSalaryOfClub(players, clubName);
                    if (maxSalaryPlayers.isEmpty()) {
                        System.out.println("No players found in the club.");
                    } else {
                        System.out.println("Player(s) with the maximum salary:");
                        for (Player player : maxSalaryPlayers) {
                            System.out.println(player.toString());
                        }
                    }
                    break;

                case 2: 
                    List<Player> maxAgePlayers = newClubSearch.maxAgeOfClub(players, clubName);
                    if (maxAgePlayers.isEmpty()) {
                        System.out.println("No players found in the club.");
                    } else {
                        System.out.println("Player(s) with the maximum age:");
                        for (Player player : maxAgePlayers) {
                            System.out.println(player.toString());
                        }
                    }
                    break;

                case 3: 
                    List<Player> maxHeightPlayers = newClubSearch.maxHeightOfClub(players, clubName);
                    if (maxHeightPlayers.isEmpty()) {
                        System.out.println("No players found in the club.");
                    } else {
                        System.out.println("Player(s) with the maximum height:");
                        for (Player player : maxHeightPlayers) {
                            System.out.println(player.toString());
                        }
                    }
                    break;

                case 4: 
                    long totalSalary = newClubSearch.calculateTotalYearlySalary(players, clubName);
                    System.out.println("The total yearly salary of the club is: " + totalSalary);
                    break;
            }

            clubSearchOption = -1;
        }
    }

    private void addPlayerMethod(List<Player> players) {
        System.out.print("Enter Player Name: ");
        String name = scanner.nextLine();
        name = Stringformatting(name);

        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name)) {

                System.out.println("Player with name " + name + " is already in the Database");
                return;
                
            }
        }

        System.out.print("Enter Player Country: ");
        String country = scanner.nextLine();
        country = Stringformatting(country);

        System.out.print("Enter Player Age: ");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Player Height: ");
        double height = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter Player Club: ");
        String club = scanner.nextLine();
        club = Stringformatting(club);

        System.out.print("Enter Player Position: ");
        String position = scanner.nextLine();
        position = Stringformatting(position);

        System.out.print("Enter Player Jersey Number: ");
        String s = scanner.nextLine();
        int jerseyNumber;
        if(s.equalsIgnoreCase(""))jerseyNumber = -1;
        else jerseyNumber = Integer.parseInt(s);

        System.out.print("Enter Player Weekly Salary: ");
        int weeklySalary = Integer.parseInt(scanner.nextLine());

        Player newPlayer = new Player(name, country, age, height, club, position, jerseyNumber, weeklySalary);

        players.add(newPlayer);

    }
}
