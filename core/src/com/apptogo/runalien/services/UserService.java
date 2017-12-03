package com.apptogo.runalien.services;

import com.apptogo.runalien.app42.CloudHandler;
import com.apptogo.runalien.app42.entity.User;
import com.apptogo.runalien.event.PlayerLoggedInGameEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class UserService {
    private static UserService instance = null;

    public static UserService getInstance() {
        return instance;
    }

    public static void create(CloudHandler cloudHandler) {
        instance = new UserService(cloudHandler);
    }

    private CloudHandler cloudHandler = null;
    Preferences userPreferences;
    private User user;

    private UserService(CloudHandler cloudHandler) {
        this.cloudHandler = cloudHandler;
        this.userPreferences = Gdx.app.getPreferences("user");
        //TODO support scenario when preferences are empty and there's need to create new one (maybe some popup with proposal of creating)
//        userPreferences.putString("user.username", "user").flush();
//        userPreferences.putString("user.email", "user@password.com").flush();
//        userPreferences.putString("user.password", "qwerty").flush();
    }

    public boolean isUserCreated() {
        if(userPreferences.contains("user.username") && userPreferences.contains("user.email")) {
            return true;
        }
        return false;
    }


    public void loginUser() {
        String username = userPreferences.getString("user.username");
        String password = userPreferences.getString("user.password");
        String email = userPreferences.getString("user.email");

        user = new User(username, password, email);
        cloudHandler.login(user, new PlayerLoggedInGameEvent());
        System.out.println("LOGGING IN");
    }

}
