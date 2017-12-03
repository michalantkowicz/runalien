package com.apptogo.runalien.event;

public class HelloGameEvent implements GameEvent {
    @Override
    public GameEventType getGameEventType() {
        return GameEventType.PLAYER_REGISTERED;
    }

    @Override
    public String getMessage() {
        return "HELLO!";
    }
}