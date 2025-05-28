package com.models;

public class User {
    public int id;
    public String username;
    public String password;
    public String email;

    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
