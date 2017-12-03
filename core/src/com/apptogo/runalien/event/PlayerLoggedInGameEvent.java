package com.apptogo.runalien.event;

import com.apptogo.runalien.event.meta.GameEventType;

import static com.apptogo.runalien.event.meta.GameEventType.PLAYER_REGISTERED;

public class PlayerLoggedInGameEvent extends GameEvent {
    @Override
    public GameEventType getGameEventType() {
        return PLAYER_REGISTERED;
    }
}