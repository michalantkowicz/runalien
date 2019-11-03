package com.apptogo.runalien.feature;

import com.apptogo.runalien.event.GameEventService;
import com.apptogo.runalien.event.implementation.JumpMovementEvent;
import com.apptogo.runalien.event.implementation.SlideMovementEvent;
import com.apptogo.runalien.game.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static com.badlogic.gdx.Gdx.input;

@RequiredArgsConstructor
@AllArgsConstructor
public class TouchScreenSteeringFeature implements Feature {
    private static final float CENTER = Gdx.app.getGraphics().getWidth() / 2f;

    private final GameEventService eventService;
    private final Player player;

    @Setter
    private String topic;

    @Override
    public void run(float delta) {

        if (input.getInputProcessor() != null) {
            if (rightSideOfScreenTouched())
                eventService.pushEvent(new JumpMovementEvent(getPlayerPosition(), topic));
            if (leftSideOfScreenTouched()) {
                eventService.pushEvent(new SlideMovementEvent(getPlayerPosition(), topic));
            }
        }
    }

    private boolean leftSideOfScreenTouched() {
        return input.justTouched() && input.getX() <= CENTER;
    }

    private boolean rightSideOfScreenTouched() {
        return input.justTouched() && input.getX() > CENTER;
    }

    private Vector2 getPlayerPosition() {
        return new Vector2(player.getX(), player.getY());
    }
}
