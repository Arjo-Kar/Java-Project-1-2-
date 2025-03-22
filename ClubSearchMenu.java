package com.example.finalproject12;

import java.util.*;

public class ClubSearchMenu {

    public List<Player> maxSalaryOfClub(List<Player>players, String clubname) {
        
        if (players == null || players.isEmpty()) {
            System.out.println("No players found in this club.");
            return new ArrayList<>();
        }
        int maxSalary = 0;
        List<Player> maxSalaryPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getSalary() > maxSalary && player.getClub().equalsIgnoreCase(clubname)) {
                maxSalaryPlayers.clear();
                maxSalaryPlayers.add(player);
                maxSalary = player.getSalary();
            }
            else if(player.getClub().equalsIgnoreCase(clubname) && player.getSalary() == maxSalary){
                maxSalaryPlayers.add(player);
            }
        }
        return maxSalaryPlayers;
    }

  public List<Player> maxAgeOfClub(List<Player> players, String clubName) {
    if (players == null || players.isEmpty()) {
        System.out.println("No players found in this club.");
        return new ArrayList<>();
    }
    int maxAge = 0;
    List<Player> maxAgePlayers = new ArrayList<>();
    for (Player player : players) {
        if (player.getAge() > maxAge && player.getClub().equalsIgnoreCase(clubName)) {
            maxAgePlayers.clear();
            maxAgePlayers.add(player);
            maxAge = player.getAge(); // Update maxAge
        } else if (player.getClub().equalsIgnoreCase(clubName) && player.getAge() == maxAge) {
            maxAgePlayers.add(player);
        }
    }
    return maxAgePlayers;
}

public List<Player> maxHeightOfClub(List<Player> players, String clubName) {
    if (players == null || players.isEmpty()) {
        System.out.println("No players found in this club.");
        return new ArrayList<>();
    }
    double maxHeight = 0;
    List<Player> maxHeightPlayers = new ArrayList<>();
    for (Player player : players) {
        if (player.getHeight() > maxHeight && player.getClub().equalsIgnoreCase(clubName)) {
            maxHeightPlayers.clear();
            maxHeightPlayers.add(player);
            maxHeight = player.getHeight(); // Update maxHeight
        } else if (player.getClub().equalsIgnoreCase(clubName) && player.getHeight() == maxHeight) {
            maxHeightPlayers.add(player);
        }
    }
    return maxHeightPlayers;
}

    public long calculateTotalYearlySalary(List<Player>players , String clubName) {
        long totalSalary = 0L;
       
        for (Player player : players) {
            if(player.getClub().equalsIgnoreCase(clubName))
            totalSalary += ((long) player.getSalary());
        }

        return totalSalary * 52; 
    }
}
