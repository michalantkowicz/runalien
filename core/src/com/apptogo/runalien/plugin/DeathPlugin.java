package com.apptogo.runalien.plugin;

import com.apptogo.runalien.exception.PluginDependencyException;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.physics.ContactListener;
import com.apptogo.runalien.physics.UserData;
import com.badlogic.gdx.physics.box2d.Fixture;

public class DeathPlugin extends AbstractPlugin {

    public boolean dead;

    private final boolean DEBUG_IMMORTAL = false;

    private SoundPlugin soundHandler;
    private boolean shouldFinishGame;

    public DeathPlugin(boolean shouldFinishGame) {
        super();
        this.shouldFinishGame = shouldFinishGame;
        dead = false;
    }

    @Override
    public void run() {
        for (Fixture bodyFixture : actor.getBody().getFixtureList())
            if (!DEBUG_IMMORTAL && ContactListener.SNAPSHOT.collide(UserData.get(bodyFixture), "killing")) {
                dead = true;
                String obstacleType = ContactListener.SNAPSHOT.getCollision(UserData.get(bodyFixture), "killing").getValue().type;

                soundHandler.stopSound("runscream");

                if ("bell".equals(obstacleType))
                    soundHandler.playSound("bell");
                else if ("log".equals(obstacleType))
                    soundHandler.playSound("die");
                else if ("crate".equals(obstacleType))
                    soundHandler.playSound("die");
                else if ("cutBottom".equals(obstacleType)) {
                    soundHandler.playSound("die");
                } else if ("rocket".equals(obstacleType)) {
                    soundHandler.playSound("explosion");
                } else if ("ball".equals(obstacleType)) {
                    soundHandler.playSound("ballHit");
                } else if ("weasel".equals(obstacleType)) {
                    soundHandler.playSound("weaselHit");
                }


                for (Fixture fixture : body.getFixtureList())
                    UserData.get(fixture).ignore = true;

                if (ContactListener.SNAPSHOT.collide(UserData.get(actor.getBody()), "killingTop"))
                    actor.changeAnimation("dietop");
                else
                    actor.changeAnimation("diebottom");

                if (shouldFinishGame) {
                    Main.getInstance().getGameScreen().setEndGame(true);
                }

                break;
            }
    }

    @Override
    public void setUpDependencies() {
        soundHandler = actor.getPlugin(SoundPlugin.class);

        if (body.getFixtureList().size <= 0)
            throw new PluginDependencyException("Actor's body must have at least one (default) fixture!");
    }
}
