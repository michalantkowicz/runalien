package com.apptogo.runalien.event.implementation;

import com.apptogo.runalien.event.GameEvent;
import com.badlogic.gdx.math.Vector2;

import static com.apptogo.runalien.event.GameEventType.MOVEMENT;

public class JumpMovementEvent extends GameEvent<Vector2> {
    public JumpMovementEvent(Vector2 eventObject, String topic) {
        super(MOVEMENT, eventObject, topic);
    }
}
