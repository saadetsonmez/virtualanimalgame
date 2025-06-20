package com.virtanimal;

import com.models.UserManager;

public class VirtualPet {
    private UserManager user;

    public VirtualPet(UserManager user) {
        this.user = user;
        this.user.setHunger(Math.max(0, Math.min(user.getHunger(), 100)));
        this.user.setFun(Math.max(0, Math.min(user.getFun(), 100)));
    }

    public void eat(String foodType) {
        switch (foodType.toLowerCase()) {
            case "fish" -> user.setHunger(user.getHunger() + 25);
            case "bone" -> {
                user.setHunger(user.getHunger() + 15);
                user.setFun(user.getFun() + 5);
            }
            case "carrot" -> user.setHunger(user.getHunger() + 10);
            default -> user.setHunger(user.getHunger() + 10);
        }
        saveToJson();
    }

    public void play() {
        user.setFun(user.getFun() + 15);
        user.setHunger(user.getHunger() - 5);
        saveToJson();
    }

    public void sleep() {
        user.setFun(user.getFun() + 10);
        user.setHunger(user.getHunger() - 10);
        saveToJson();
    }

    public void decay() {
        user.setFun(user.getFun() - 1);
        user.setHunger(user.getHunger() - 1);
        saveToJson();
    }

    public void forceSave() {
        saveToJson();
    }

    private void saveToJson() {
        UserManagerService.setFun(user.getFun());
        UserManagerService.setHunger(user.getHunger());
        UserManagerService.setPetName(user.getPetName());
        UserManagerService.saveCurrentUserData();
    }

    public boolean isInDanger() {
        return user.getHunger() < 20 || user.getFun() < 20;
    }

    public String getMood() {
        int fun = user.getFun();
        if (fun > 80) return "Excited 🎉";
        if (fun > 60) return "Happy 😊";
        if (fun > 40) return "Okay 😐";
        if (fun > 20) return "Bored 😕";
        return "Miserable 😭";
    }

    public String getOverallStatus() {
        String mood = getMood();
        String hungerStatus = getHungerStatus();
        return String.format("%s is %s and %s", user.getPetName(), mood.toLowerCase(), hungerStatus);
    }

    private String getHungerStatus() {
        int hunger = user.getHunger();
        if (hunger > 80) return "well-fed 🍽️";
        if (hunger > 60) return "satisfied 😋";
        if (hunger > 40) return "a bit hungry 🤤";
        if (hunger > 20) return "hungry 😋";
        return "starving 😰";
    }

    public String getDetailedStatus() {
        return String.format(
                "Pet Information:\n\n" +
                        "Name: %s\n" +
                        "User ID: %d\n" +
                        "Hunger: %d/100\n" +
                        "Fun: %d/100\n" +
                        "Mood: %s\n\n" +
                        "Overall Status: %s",
                getName(),
                getUserId(),
                getHunger(),
                getFun(),
                getMood(),
                getOverallStatus()
        );
    }

    public int getHunger() { return user.getHunger(); }
    public int getFun() { return user.getFun(); }
    public String getName() { return user.getPetName(); }
    public int getUserId() { return user.getUserId(); }

    public void setHunger(int h) { user.setHunger(h); }
    public void setFun(int f) { user.setFun(f); }
    public void setName(String name) {
        user.setPetName(name);
        saveToJson();
    }

    @Override
    public String toString() {
        return String.format("Pet{name='%s', userId=%d, hunger=%d, fun=%d, mood='%s'}",
                getName(), getUserId(), getHunger(), getFun(), getMood());
    }
}
