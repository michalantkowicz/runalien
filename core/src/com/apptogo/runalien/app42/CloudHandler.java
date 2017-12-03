package com.apptogo.runalien.app42;

import com.apptogo.runalien.app42.entity.User;
import com.apptogo.runalien.event.GameEvent;
import com.apptogo.runalien.event.queue.GameEventQueue;
import com.apptogo.runalien.event.meta.GameEventStatus;
import com.badlogic.gdx.utils.Json;
import com.shephertz.app42.paas.sdk.java.App42API;
import com.shephertz.app42.paas.sdk.java.App42CallBack;
import com.shephertz.app42.paas.sdk.java.ServiceAPI;
import com.shephertz.app42.paas.sdk.java.storage.StorageService;
import com.shephertz.app42.paas.sdk.java.user.UserService;

import java.util.Locale;

public class CloudHandler {
    ServiceAPI serviceAPI;

    UserService userService;
    StorageService storageService = null;

    public CloudHandler(String apikKey, String secretKey) {
        App42API.initialize(apikKey, secretKey);
        userService = App42API.buildUserService();
        storageService = App42API.buildStorageService();
    }

    public void register(String username, String password, GameEvent event) {
        //TODO to provide country region in email address in a proper way
        String email = username + "@" + Locale.getDefault().getCountry() + ".runalien.com";
        userService.createUser(username, password, email, retrieveApp42Callback(event));
    }

    public void login(User user, GameEvent event) {
        userService.authenticate(user.getUsername(), user.getPassword(), retrieveApp42Callback(event));
    }

    public void saveGameResult(GameResult gameResult) {
        Json json = new Json();
        storageService.insertJSONDocument("RUNALIEN", "DUEL", json.toJson(gameResult));
    }

    private App42CallBack retrieveApp42Callback(GameEvent event) {
        return new App42CallBack() {
            @Override
            public void onSuccess(Object o) {
                event.setEventStatus(GameEventStatus.SUCCESS);
                GameEventQueue.getInstance().put(event);
            }

            @Override
            public void onException(Exception e) {
                event.setEventStatus(GameEventStatus.FAILED);
                event.setException(e);
                GameEventQueue.getInstance().put(event);
            }
        };
    }
}
