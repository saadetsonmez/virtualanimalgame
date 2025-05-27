package com.virtanimal;

import com.models.UserManeger;

public class VirtualPet {
    private int userId;
    private String name;
    private int hunger;
    private int fun;

    // Constructor - JSON'dan yüklenen verilerle
    public VirtualPet(int userId, String name, int hunger, int fun) {
        this.userId = userId;
        this.name = name;
        this.hunger = Math.max(0, Math.min(hunger, 100)); // 0-100 arası sınırla
        this.fun = Math.max(0, Math.min(fun, 100));
    }

    // Constructor - Yeni pet için (varsayılan değerlerle)
    public VirtualPet(int userId, String name) {
        this.userId = userId;
        this.name = name;
        this.hunger = 50; // Başlangıç değeri
        this.fun = 50;
    }

    // UserManegerService'den mevcut kullanıcının pet'ini yükle
    public static VirtualPet loadCurrentUserPet() {
        int currentUserId = UserManegerService.getCurrentUserId();
        if (currentUserId == -1) {
            throw new IllegalStateException("No user is currently logged in");
        }

        String petName = UserManegerService.getPetName();
        int hunger = UserManegerService.getHunger();
        int fun = UserManegerService.getFun();

        return new VirtualPet(currentUserId, petName, hunger, fun);
    }

    public void eat(String foodType) {
        switch (foodType.toLowerCase()) {
            case "fish":
                hunger = Math.min(hunger + 25, 100);
                break;
            case "bone":
                hunger = Math.min(hunger + 15, 100);
                fun = Math.min(fun + 5, 100);
                break;
            case "carrot":
                hunger = Math.min(hunger + 10, 100);
                break;
            default:
                hunger = Math.min(hunger + 10, 100);
        }

        // Değişiklikleri JSON'a kaydet
        saveToJson();

        System.out.println(name + " ate " + foodType + ". Hunger: " + hunger + ", Fun: " + fun);
    }

    public void play() {
        fun = Math.min(fun + 15, 100);
        hunger = Math.max(hunger - 5, 0);

        // Değişiklikleri JSON'a kaydet
        saveToJson();

        System.out.println(name + " played! Fun: " + fun + ", Hunger: " + hunger);
    }

    public void sleep() {
        hunger = Math.max(hunger - 10, 0);
        fun = Math.min(fun + 10, 100);

        // Değişiklikleri JSON'a kaydet
        saveToJson();

        System.out.println(name + " slept. Hunger: " + hunger + ", Fun: " + fun);
    }

    // Zaman geçtikçe otomatik azalma
    public void decay() {
        hunger = Math.max(hunger - 1, 0);
        fun = Math.max(fun - 1, 0);

        // Değişiklikleri JSON'a kaydet
        saveToJson();

        System.out.println("Time passed... " + name + "'s stats decreased. Hunger: " + hunger + ", Fun: " + fun);
    }

    // JSON'a kaydetme işlemi
    private void saveToJson() {
        try {
            // UserManegerService'deki static değişkenleri güncelle
            UserManegerService.setHunger(this.hunger);
            UserManegerService.setFun(this.fun);

            // JSON dosyasına kaydet
            UserManegerService.saveCurrentUserData();

        } catch (Exception e) {
            System.err.println("Error saving pet data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Pet'i manuel olarak JSON'a kaydet (örneğin oyun kapanırken)
    public void forceSave() {
        saveToJson();
        System.out.println("Pet data saved to JSON!");
    }

    public String getMood() {
        if (fun > 80) return "Excited 🎉";
        if (fun > 60) return "Happy 😊";
        if (fun > 40) return "Okay 😐";
        if (fun > 20) return "Bored 😕";
        return "Miserable 😭";
    }

    // Pet'in genel durumunu döndür
    public String getOverallStatus() {
        String mood = getMood();
        String hungerStatus = getHungerStatus();
        return String.format("%s is %s and %s", name, mood.toLowerCase(), hungerStatus);
    }

    private String getHungerStatus() {
        if (hunger > 80) return "well-fed 🍽️";
        if (hunger > 60) return "satisfied 😋";
        if (hunger > 40) return "a bit hungry 🤤";
        if (hunger > 20) return "hungry 😋";
        return "starving 😰";
    }

    // Pet'in tehlikede olup olmadığını kontrol et
    public boolean isInDanger() {
        return hunger < 20 || fun < 20;
    }

    // Pet'in uyarı mesajını döndür
    public String getWarningMessage() {
        if (hunger < 10 && fun < 10) {
            return name + " is both starving and very unhappy! Please take care of them! 🚨";
        } else if (hunger < 10) {
            return name + " is starving! Please feed them! 🍽️";
        } else if (fun < 10) {
            return name + " is very sad! Please play with them! 🎮";
        } else if (hunger < 20) {
            return name + " is getting hungry 🤤";
        } else if (fun < 20) {
            return name + " is getting bored 😕";
        }
        return "";
    }

    // Getters
    public int getHunger() { return hunger; }
    public int getFun() { return fun; }
    public String getName() { return name; }
    public int getUserId() { return userId; }

    // Setters (JSON'dan yüklerken kullanılabilir)
    public void setHunger(int hunger) {
        this.hunger = Math.max(0, Math.min(hunger, 100));
    }

    public void setFun(int fun) {
        this.fun = Math.max(0, Math.min(fun, 100));
    }

    public void setName(String name) {
        this.name = name;
        // İsim değişikliğini de kaydet
        UserManegerService.setPetName(name);
        UserManegerService.saveCurrentUserData();
    }

    @Override
    public String toString() {
        return String.format("Pet{name='%s', userId=%d, hunger=%d, fun=%d, mood='%s'}",
                name, userId, hunger, fun, getMood());
    }
}