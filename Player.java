package com.example.finalproject12;

public class Player {

    private String name;
    private String country;
    private int age;
    private double height;
    private String club;
    private String position;
    private int jerseyNumber; 
    private int salary;

    public Player(String name, String country, int age, double height, String club, String position, int jerseyNumber, int salary) {
        this.name = name;
        this.country = country;
        this.age = age;
        this.height = height;
        this.club = club;
        this.position = position;
        this.jerseyNumber = jerseyNumber; 
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public int getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public String getClub() {
        return club;
    }

    public String getPosition() {
        return position;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public int getSalary() {
        return salary;
    }
    public void setClub(String clubname){
        this.club = clubname;
    }
     @Override
     public String toString() {
         StringBuilder sb = new StringBuilder();
         sb.append("Name: ").append(name).append("\n")
                 .append("Country: ").append(country).append("\n")
                 .append("Age: ").append(age).append("\n")
                 .append("Height: ").append(height).append("\n")
                 .append("Club: ").append(club).append("\n")
                 .append("Position: ").append(position).append("\n");

         if (jerseyNumber != -1) {
             sb.append("Jersey Number: ").append(jerseyNumber).append("\n");
         }

         sb.append("Weekly Salary: ").append(salary).append("\n");

         return sb.toString();
     }
}
