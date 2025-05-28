package com.models;

public class UserManager {
    public int userId;
    public String petName;
    public int hunger;
    public int fun;

    public UserManager(int userId, String petName, int hunger, int fun) {
        this.userId = userId;
        this.petName = petName;
        this.hunger = hunger;
        this.fun = fun;
    }
}
