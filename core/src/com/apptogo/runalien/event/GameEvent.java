package com.apptogo.runalien.event;

public interface GameEvent {
    GameEventType getGameEventType();
    String getMessage();
}
