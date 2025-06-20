package com.virtanimal;

import com.models.UserManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class UserManagerService {
    private static final String FILE_PATH = "UserManager.json";

    private static int currentUserId = -1;
    private static String currentPetName = "";
    private static int currentHunger = 0;
    private static int currentFun = 0;

    public static int getCurrentUserId() { return currentUserId; }
    public static void setCurrentUser(int userId) { currentUserId = userId; }

    public static String getPetName() { return currentPetName; }
    public static void setPetName(String petName) { currentPetName = petName; }

    public static int getHunger() { return currentHunger; }
    public static void setHunger(int hunger) { currentHunger = hunger; }

    public static int getFun() { return currentFun; }
    public static void setFun(int fun) { currentFun = fun; }

    public static void saveCurrentUserData() {
        if (currentUserId == -1) return;

        UserManager updated = new UserManager(
                currentUserId,
                "default",
                "hash",
                "default@example.com",
                currentPetName,
                currentHunger,
                currentFun
        );
        addOrUpdate(updated);
    }

    public static UserManager loadCurrentUser() {
        if (currentUserId == -1) throw new IllegalStateException("No user is currently logged in");

        return new UserManager(
                currentUserId,
                "default",
                "hash",
                "default@example.com",
                currentPetName,
                currentHunger,
                currentFun
        );
    }

    public static List<UserManager> loadAll() {
        List<UserManager> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            JSONArray arr = new JSONArray(content);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                UserManager user = new UserManager(
                        obj.getInt("userId"),
                        "default",
                        "hash",
                        "default@example.com",
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

    public static UserManager getByUserId(int userId) {
        return loadAll().stream()
                .filter(u -> u.getUserId() == userId)
                .findFirst()
                .orElse(null);
    }

    public static void addOrUpdate(UserManager updated) {
        List<UserManager> all = loadAll();
        boolean updatedFlag = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getUserId() == updated.getUserId()) {
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

    public static void saveAll(List<UserManager> list) {
        JSONArray arr = new JSONArray();
        for (UserManager u : list) {
            JSONObject obj = new JSONObject();
            obj.put("userId", u.getUserId());
            obj.put("petName", u.getPetName());
            obj.put("hunger", u.getHunger());
            obj.put("fun", u.getFun());
            arr.put(obj);
        }
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(arr.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkAndUpdatePetName(int userId, String newPetName) {
        if (newPetName == null || newPetName.trim().isEmpty()) {
            return false;
        }

        UserManager user = getByUserId(userId);
        if (user != null) {
            user.setPetName(newPetName.trim());
            addOrUpdate(user);

            if (currentUserId == userId) {
                currentPetName = newPetName.trim();
            }
            return true;
        }
        return false;
    }
}
