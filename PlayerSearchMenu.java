package com.example.finalproject12;

import java.util.*;
import java.util.stream.Collectors;
public class PlayerSearchMenu {

    public Player searchByName(List<Player> players, String name) {

        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    public List<Player> searchByClubAndCountry(List<Player> players, String country, String club) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (player.getCountry().equalsIgnoreCase(country)
                    && (club.equalsIgnoreCase("ANY") || player.getClub().equalsIgnoreCase(club))) {
                result.add(player);
            }
        }
        return result;
    }

    public List<Player> searchByPosition(List<Player> players, String position) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (player.getPosition().equalsIgnoreCase(position)) {
                result.add(player);
            }
        }
        return result;
    }

    public List<Player> searchBySalaryRange(List<Player> players, int minSalary, int maxSalary) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (player.getSalary() >= minSalary && player.getSalary() <= maxSalary) {
                result.add(player);
            }
        }
        return result;
    }
    public List<Player>byCountryNameandSalRange(List<Player>players, int minSalary, int maxSalary, String Country){
        List<Player>result = new ArrayList<>();
        for(Player player: players){
            if(player.getSalary()>= minSalary && player.getSalary()<= maxSalary && player.getCountry().equalsIgnoreCase(Country)){
                result.add(player);
            }
        }
        return result;
    }
    public HashMap<String, Long> countryCount(List<Player> players) {
        // Return an empty HashMap if the players list is empty
        if (players.isEmpty()) {
            return new HashMap<>();
        }

        // Create a HashMap with counts of players by country
        HashMap<String, Long> countryCount = players.stream()
                .collect(Collectors.groupingBy(Player::getCountry, HashMap::new, Collectors.counting()));

        return countryCount;
    }

}
