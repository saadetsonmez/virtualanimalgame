package com.models;

public class User {
    public int id;
    public String username;
    public String passwordHash;
    public String email;

    public User(int id, String username, String passwordHash, String email) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }
}
