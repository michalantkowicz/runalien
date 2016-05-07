package com.apptogo.runalien.screen;

import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.plugin.SoundPlugin;
import com.apptogo.runalien.scene2d.Button;
import com.apptogo.runalien.scene2d.Listener;
import com.apptogo.runalien.scene2d.TextButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen extends BasicScreen {
	
	Music music;
	
    public MenuScreen(Main game) {
        super(game, "background_menu");
    }

    @Override
    protected void prepare() {
//    	SoundPlugin.loopSingleSound("runalienMusic");
    	//TODO move this code to soundmanager
    	music = Gdx.audio.newMusic(Gdx.files.internal("runalienMusic.ogg"));
    	music.setLooping(true);
    	music.play();
    	
    	//TODO move to scene2d class. click listeners
        ClickListener showAchievementsListener = new ClickListener() {
        	@Override
        	public void clicked (InputEvent event, float x, float y) {
				Main.gameCallback.showAchievements();
        	}
        };
        ClickListener showLeaderboardListener = new ClickListener() {
        	@Override
        	public void clicked (InputEvent event, float x, float y) {
				Main.gameCallback.showLeaderboard();
        	}
        };
        
        ClickListener shareOnGooglePlusListener = new ClickListener() {
        	@Override
        	public void clicked (InputEvent event, float x, float y) {
				Main.gameCallback.shareOnGooglePlus();
        	}
        };
        
        Group group = new Group();
                
        //TODO do not refresh screen after change vibration mode
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
        
        if(true || Gdx.app.getPreferences("SETTINGS").getBoolean("TUTORIAL")) {
        	tables.get(0).add(TextButton.get("PLAY", "play").setListener(Listener.click(game, new GameScreen(game)))).expandX().row();
        }
        else {
        	tables.get(0).add(TextButton.get("PLAY", "play").setListener(Listener.click(game, new TutorialScreen(game)))).expandX().row();
        }
        
        
        tables.get(0).add(TextButton.get("RANK", "rank").setListener(showLeaderboardListener)).expandX().row();

        Group share = new Group();
        
        share.addActor(Button.get("achievement").setListener(showAchievementsListener).position(-0, 0));
        share.addActor(Button.get("gplus").setListener(shareOnGooglePlusListener).position(120, 0));

        tables.get(0).add(share).left().pad(140, 0, 0, 0).row();
    }

    @Override
    protected void step(float delta) {
        // TODO Auto-generated method stub

    }
    
    @Override
	protected void handleInput() {
    	if(Gdx.input.isKeyJustPressed(Keys.BACK)) {
    		Gdx.app.exit();
    	}
    }

	@Override
	public void dispose() {
		super.dispose();
		//SoundPlugin.stopSingleSound("runalienMusic");
		music.stop();
		music.dispose();
	}

}
