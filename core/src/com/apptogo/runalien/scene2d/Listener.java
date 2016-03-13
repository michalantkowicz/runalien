package com.apptogo.runalien.scene2d;

import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Listener {

	public static ClickListener click(final Main game, final Screen screen)
    {
        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(screen);
            }
        };

        return listener;
    }
	
	public static ClickListener preferences(String preferencesName, String valueKey, boolean value)
    {
        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
            	Gdx.app.getPreferences(preferencesName).putBoolean(valueKey, value);
            	Gdx.app.getPreferences(preferencesName).flush();
            }
        };

        return listener;
    }

}
