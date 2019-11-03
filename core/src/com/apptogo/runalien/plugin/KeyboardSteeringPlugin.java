package com.apptogo.runalien.plugin;

import com.apptogo.runalien.event.GameEventService;
import com.apptogo.runalien.event.implementation.JumpMovementEvent;
import com.apptogo.runalien.event.implementation.SlideMovementEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.TimeUtils;

public class KeyboardSteeringPlugin extends SteeringPlugin {
    private String name;
    private boolean autoStart;
    private long startMillis = TimeUtils.millis();

    public KeyboardSteeringPlugin(GameEventService eventService, String name, boolean autoStart) {
        super(eventService);
        this.name = name;
        this.autoStart = autoStart;
    }

    @Override
    public void run() {
        super.run();

        if (running.isStarted()) {
            if (eventService.getEvent(JumpMovementEvent.class, name).isPresent()/*Gdx.input.isKeyJustPressed(Keys.L)*/) {
                System.out.println("JUMP " + actor.getX() + " " + actor.getY());
                jump();
            }
            if (eventService.getEvent(SlideMovementEvent.class, name).isPresent()/*Gdx.input.isKeyJustPressed(Keys.A)*/) {
                System.out.println("SLIDE " + actor.getX() + " " + actor.getY());
                chargeDown();
            }
        }
        if (!deathPlugin.dead && (Gdx.input.isKeyJustPressed(Input.Keys.J) || (autoStart && TimeUtils.millis() - startMillis > 2000))) {
            autoStart = false;
            if (!running.isStarted()) {
                startRunning();
            }
        }
    }
}
