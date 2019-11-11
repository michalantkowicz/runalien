package com.apptogo.runalien.feature;

import com.badlogic.gdx.math.Vector2;
import lombok.Value;

@Value
public class MoveRecord {
    private final int type;
    private final Vector2 position;
}
