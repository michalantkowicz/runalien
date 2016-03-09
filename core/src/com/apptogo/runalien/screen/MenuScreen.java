package com.apptogo.runalien.screen;

import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.scene2d.Button;
import com.apptogo.runalien.scene2d.Listener;
import com.apptogo.runalien.scene2d.TextButton;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.Group;

public class MenuScreen extends BasicScreen {

    public MenuScreen(Main game) {
        super(game, "background_menu");
    }

    @Override
    void prepare() {
        Group group = new Group();
        group.addActor(Button.get("sound").position(-130, -50));
        group.addActor(Button.get("vibration").position(-50, -50));

        tables.get(0).add(group).right().row();
        tables.get(0).add().height(300).row();
        tables.get(0).add(TextButton.get("PLAY", "play").setListener(Listener.click(game, new GameScreen(game)))).expandX().row();
        tables.get(0).add(TextButton.get("RANK", "rank")).expandX().row();

        Group share = new Group();
        share.addActor(Button.get("achievement").position(-0, 0));
        share.addActor(Button.get("gplus").position(120, 0));

        tables.get(0).add(share).left().pad(140, 0, 0, 0).row();
    }

    @Override
    void step(float delta) {
        // TODO Auto-generated method stub

    }

}
