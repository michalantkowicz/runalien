package com.apptogo.runalien.screen;

import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.scene2d.Button;
import com.apptogo.runalien.scene2d.Listener;
import com.apptogo.runalien.scene2d.TextButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;

public class MenuScreen extends BasicScreen {

    public MenuScreen(Main game) {
        super(game, "background_menu");
    }

    @Override
    void prepare() {
        Group group = new Group();
                
        if(Gdx.app.getPreferences("SETTINGS").getBoolean("VIBRATIONS"))
        	group.addActor(Button.get("vibration").position(-120, -90)
        										  .setListener(Listener.preferences("SETTINGS", "VIBRATIONS", false))
        										  .setListener(Listener.click(game, new MenuScreen(game))));
        else
        	group.addActor(Button.get("vibrationOff").position(-120, -90)
        											 .setListener(Listener.preferences("SETTINGS", "VIBRATIONS", true))
        											 .setListener(Listener.click(game, new MenuScreen(game))));

        tables.get(0).add(group).right().row();
        tables.get(0).add().height(300).row();
        
        if(Gdx.app.getPreferences("SETTINGS").getBoolean("TUTORIAL")) {
        	tables.get(0).add(TextButton.get("PLAY", "play").setListener(Listener.click(game, new GameScreen(game)))).expandX().row();
        }
        else {
        	tables.get(0).add(TextButton.get("PLAY", "play").setListener(Listener.click(game, new TutorialScreen(game)))).expandX().row();
        }
        
        
        tables.get(0).add(TextButton.get("RANK", "rank").setListener(Listener.click(game, new TutorialScreen(game)))).expandX().row();

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
