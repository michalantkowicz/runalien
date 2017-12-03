package com.apptogo.runalien.event;

import com.apptogo.runalien.event.meta.GameEventStatus;
import com.apptogo.runalien.event.meta.GameEventType;

public abstract class GameEvent {
    private com.apptogo.runalien.event.meta.GameEventType gameEventType;
    private GameEventStatus eventStatus;
    private String message;
    private Exception exception;

    public com.apptogo.runalien.event.meta.GameEventType getGameEventType() {
        return gameEventType;
    }

    public void setGameEventType(GameEventType gameEventType) {
        this.gameEventType = gameEventType;
    }

    public GameEventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(GameEventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public boolean isSuccess() {
        return GameEventStatus.SUCCESS.equals(this.eventStatus);
    }
}
