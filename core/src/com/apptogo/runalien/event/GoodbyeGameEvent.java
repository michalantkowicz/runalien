package com.apptogo.runalien.event;

public class GoodbyeGameEvent implements GameEvent {
    @Override
    public GameEventType getGameEventType() {
        return GameEventType.PLAYER_LOGGED_IN;
    }

    @Override
    public String getMessage() {
        return "GOODBYE!";
    }
}
