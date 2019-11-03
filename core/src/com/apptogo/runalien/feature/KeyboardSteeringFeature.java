package com.apptogo.runalien.feature;

import com.apptogo.runalien.event.GameEventService;
import com.apptogo.runalien.event.implementation.JumpMovementEvent;
import com.apptogo.runalien.event.implementation.SlideMovementEvent;
import com.apptogo.runalien.game.Player;
import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static com.badlogic.gdx.Gdx.input;
import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.L;

@RequiredArgsConstructor
@AllArgsConstructor
public class KeyboardSteeringFeature implements Feature {
    private final GameEventService eventService;
    private final Player player;

    @Setter
    private String topic;

    @Override
    public void run(float delta) {
        if (input.isKeyJustPressed(L))
            eventService.pushEvent(new JumpMovementEvent(getPlayerPosition(), topic));
        if (input.isKeyJustPressed(A)) {
            eventService.pushEvent(new SlideMovementEvent(getPlayerPosition(), topic));
        }
    }

    private Vector2 getPlayerPosition() {
        return new Vector2(player.getX(), player.getY());
    }
}
