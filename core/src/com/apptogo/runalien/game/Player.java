package com.apptogo.runalien.game;

import com.apptogo.runalien.event.GameEventService;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.plugin.AchievementPlugin;
import com.apptogo.runalien.plugin.CameraFollowingPlugin;
import com.apptogo.runalien.plugin.DeathPlugin;
import com.apptogo.runalien.plugin.KeyboardSteeringPlugin;
import com.apptogo.runalien.plugin.RunningPlugin;
import com.apptogo.runalien.plugin.SoundPlugin;
import com.apptogo.runalien.plugin.TouchSteeringPlugin;
import com.apptogo.runalien.scene2d.Animation;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Player extends GameActor {
    public Player(Stage gameworldStage, GameEventService eventService) {
        super("player");
        setAvailableAnimations("diebottom", "dietop", "jump", "land", "slide", "standup", "startrunning");
        addAvailableAnimation(Animation.get(0.03f, "run", com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP));
        addAvailableAnimation(Animation.get(0.04f, "idle", com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP));
        queueAnimation("idle");

        setBody(BodyBuilder.get().type(BodyDef.BodyType.DynamicBody).position(0, Main.GROUND_LEVEL).fixedRotation(true).addFixture("player").box(0.6f, 1.9f).friction(0.5f)
                .addFixture("player", "sliding").box(1.9f, 0.6f, -0.65f, -0.65f).sensor(true).ignore(true).friction(0.5f).create());

        modifyCustomOffsets(-0.4f, 0f);

        gameworldStage.addActor(this);

        addPlugin(new AchievementPlugin());
        addPlugin(new SoundPlugin("runscream", "slide", "chargeDown", "land", "jump", "doubleJump", "bell", "die", "explosion", "ballHit", "weaselHit"));
        addPlugin(new CameraFollowingPlugin());
        addPlugin(new DeathPlugin());
        addPlugin(new RunningPlugin());
        addPlugin(new TouchSteeringPlugin(eventService));
        addPlugin(new KeyboardSteeringPlugin(eventService));
    }
}
