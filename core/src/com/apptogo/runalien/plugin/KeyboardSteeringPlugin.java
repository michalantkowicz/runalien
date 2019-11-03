package com.apptogo.runalien.plugin;

import com.apptogo.runalien.event.GameEventService;
import com.apptogo.runalien.event.implementation.JumpMovementEvent;
import com.apptogo.runalien.event.implementation.SlideMovementEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class KeyboardSteeringPlugin extends SteeringPlugin {
    private String name;

    public KeyboardSteeringPlugin(GameEventService eventService, String name) {
        super(eventService);
        this.name = name;
    }

    @Override
    public void run() {
        super.run();

        if (running.isStarted()) {
            if (eventService.getEvent(JumpMovementEvent.class, name).isPresent()/*Gdx.input.isKeyJustPressed(Keys.L)*/)
                jump();
            if (eventService.getEvent(SlideMovementEvent.class, name).isPresent()/*Gdx.input.isKeyJustPressed(Keys.A)*/)
                chargeDown();
        }
        if (!deathPlugin.dead && Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            if (!running.isStarted())
                startRunning();
            else {
                stopRunning();
            }
        }
    }
}
