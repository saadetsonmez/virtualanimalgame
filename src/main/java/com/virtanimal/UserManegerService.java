package com.virtanimal;
import com.models.UserManeger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class UserManegerService {
    private static final String FILE_PATH = "UserManeger.json";

    // Static variables to hold current user data
    private static int currentUserId = -1;
    private static String currentPetName = "";
    private static int currentHunger = 0;
    private static int currentFun = 0;

    // Getter ve Setter metodları
    public static int getCurrentUserId() { return currentUserId; }
    public static void setCurrentUser(int userId) { currentUserId = userId; }

    public static String getPetName() { return currentPetName; }
    public static void setPetName(String petName) { currentPetName = petName; }

    public static int getHunger() { return currentHunger; }
    public static void setHunger(int hunger) { currentHunger = hunger; }

    public static int getFun() { return currentFun; }
    public static void setFun(int fun) { currentFun = fun; }

    // Mevcut kullanıcının verilerini JSON'a kaydet
    public static void saveCurrentUserData() {
        if (currentUserId == -1) return;

        UserManeger updated = new UserManeger(currentUserId, currentPetName, currentHunger, currentFun);
        addOrUpdate(updated);
    }

    public static List<UserManeger> loadAll() {
        List<UserManeger> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            JSONArray arr = new JSONArray(content);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                UserManeger user = new UserManeger(
                        obj.getInt("userId"),
                        obj.optString("petName", ""),
                        obj.optInt("hunger", 0),
                        obj.optInt("fun", 0)
                );
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static UserManeger getByUserId(int userId) {
        return loadAll().stream()
                .filter(u -> u.userId == userId)
                .findFirst()
                .orElse(null);
    }

    public static void addOrUpdate(UserManeger updated) {
        List<UserManeger> all = loadAll();
        boolean updatedFlag = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).userId == updated.userId) {
                all.set(i, updated);
                updatedFlag = true;
                break;
            }
        }
        if (!updatedFlag) {
            all.add(updated);
        }
        saveAll(all);
    }

    public static void saveAll(List<UserManeger> list) {
        JSONArray arr = new JSONArray();
        for (UserManeger u : list) {
            JSONObject obj = new JSONObject();
            obj.put("userId", u.userId);
            obj.put("petName", u.petName);
            obj.put("hunger", u.hunger);
            obj.put("fun", u.fun);
            arr.put(obj);
        }
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(arr.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Pet name kontrolü ve güncelleme
    public static boolean checkAndUpdatePetName(int userId, String newPetName) {
        if (newPetName == null || newPetName.trim().isEmpty()) {
            return false;
        }

        UserManeger user = getByUserId(userId);
        if (user != null) {
            user.petName = newPetName.trim();
            addOrUpdate(user);

            // Eğer bu current user ise static değişkeni de güncelle
            if (currentUserId == userId) {
                currentPetName = newPetName.trim();
            }
            return true;
        }
        return false;
    }
}