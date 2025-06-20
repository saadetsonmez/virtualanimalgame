package com.models;

public class UserManager {
    private int userId;
    private String username;
    private String passwordHash;
    private String email;

    private String petName;
    private int hunger;
    private int fun;

    public UserManager(int userId, String username, String passwordHash, String email,
                       String petName, int hunger, int fun) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.petName = petName;
        this.hunger = hunger;
        this.fun = fun;
    }

    // Getter & Setter'lar
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public int getHunger() { return hunger; }
    public void setHunger(int hunger) { this.hunger = hunger; }

    public int getFun() { return fun; }
    public void setFun(int fun) { this.fun = fun; }
}
