package com.apptogo.runalien.app42;


import com.badlogic.gdx.utils.Json;
import com.shephertz.app42.paas.sdk.java.App42API;
import com.shephertz.app42.paas.sdk.java.App42CallBack;
import com.shephertz.app42.paas.sdk.java.ServiceAPI;
import com.shephertz.app42.paas.sdk.java.storage.StorageService;
import com.shephertz.app42.paas.sdk.java.user.User;
import com.shephertz.app42.paas.sdk.java.user.UserService;

import java.util.Locale;

public class CloudHandler {
    ServiceAPI serviceAPI;

    UserService userService;
    StorageService storageService = null;
    User user;

    public CloudHandler(String apikKey, String secretKey) {
        App42API.initialize(apikKey, secretKey);
        userService = App42API.buildUserService();
        storageService = App42API.buildStorageService();
    }

    public void register(String username, String password, Callback callBack) {
        //TODO to provide country region in email address in a proper way
        String email = username + "@" + Locale.getDefault().getCountry() + ".runalien.com";
        userService.createUser(username, password, email, retrieveApp42Callback(callBack));
    }

    public void login() {
        user = userService.authenticate("user", "password");
    }

    public void saveGameResult(GameResult gameResult) {
        Json json = new Json();
        storageService.insertJSONDocument("RUNALIEN", "DUEL", json.toJson(gameResult));
    }

    public String getUsername() {
        return user.getUserName();
    }

    private App42CallBack retrieveApp42Callback(Callback callBack) {
        return new App42CallBack() {
            @Override
            public void onSuccess(Object o) {
                callBack.success();
            }

            @Override
            public void onException(Exception e) {
                // process some default way of error
                callBack.failure(e);
            }
        };
    }
}
