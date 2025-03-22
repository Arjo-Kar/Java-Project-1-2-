package com.example.finalproject12;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;

public class MarketPlaceDTO implements Serializable {
    private List<Player> players; // List of players in the marketplace
    private boolean status;       // Connection status

    private transient Socket socket; // The client's socket connection
    private transient ObjectOutputStream outputStream; // Stream to send messages

    // Constructor with players and client socket
    public MarketPlaceDTO(List<Player> players, Socket socket) {
        this.players = players;
        this.socket = socket;
        this.status = true; // Assume connection is active

        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error initializing output stream for client: " + e.getMessage());
            this.status = false;
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    public void addPlayer(Player player){
        players.add(player);
    }

    // Send a message to the client
    public void sendMessage(String message) {
        try {
            if (outputStream != null) {
                outputStream.writeObject(message);
                outputStream.flush();
            }
        } catch (IOException e) {
            System.out.println("Error sending message to client: " + e.getMessage());
            this.status = false; // Update connection status if there's an issue
        }
    }

    // Send an updated players list to the client
    public void sendPlayersList() {
        try {
            if (outputStream != null) {
                outputStream.writeObject(players);
                outputStream.flush();
            }
        } catch (IOException e) {
            System.out.println("Error sending players list to client: " + e.getMessage());
            this.status = false; // Update connection status if there's an issue
        }
    }

    // Close the client's connection
    public void closeConnection() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing client connection: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "MarketPlaceDTO{" +
                "players=" + players +
                ", status=" + status +
                '}';
    }
}
