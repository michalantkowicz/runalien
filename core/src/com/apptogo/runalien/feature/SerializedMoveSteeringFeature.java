package com.apptogo.runalien.feature;

import com.apptogo.runalien.event.GameEventService;
import com.apptogo.runalien.event.implementation.JumpMovementEvent;
import com.apptogo.runalien.event.implementation.SlideMovementEvent;
import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.game.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SerializedMoveSteeringFeature implements Feature {
    private Gson gson = new Gson();
    private List<MoveRecord> moves;

    private GameEventService eventService;
    private final GameActor player;
    private MoveRecord currentMove;

    public SerializedMoveSteeringFeature(GameEventService eventService, GameActor player) {
        this.eventService = eventService;
        this.player = player;

        Type listType = new TypeToken<ArrayList<MoveRecord>>() {
        }.getType();
        Preferences preferences = Gdx.app.getPreferences("moveRecording");
        String recording = preferences.getString("recording");
        moves = gson.fromJson(recording, listType);

        if(moves.size() > 0) {
            currentMove = moves.remove(0);
        }
    }

    @Override
    public void run(float delta) {
        if (currentMove != null && player.getBody().getPosition().x >= currentMove.getPosition().x) {
            if (currentMove.getType() == 1)
                eventService.pushEvent(new JumpMovementEvent(getPlayerPosition(), player.getName()));
            if (currentMove.getType() == 2) {
                eventService.pushEvent(new SlideMovementEvent(getPlayerPosition(), player.getName()));
            }

            if (moves.size() > 0) {
                currentMove = moves.remove(0);
            } else {
                currentMove = null;
            }
        }
    }

    private Vector2 getPlayerPosition() {
        return new Vector2(player.getX(), player.getY());
    }
}
