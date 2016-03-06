
package com.apptogo.runalien.screen;

import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.manager.ResourcesManager;
import com.apptogo.runalien.scene2d.Image;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FillViewport;


public class SplashScreen implements Screen
{
	private Main game;
	
	private Stage stage;
	private FillViewport viewport;
	
	Image logo;
	
	private enum SplashPhase
	{
		LOADING,
		END
	}
	
	private SplashPhase currentPhase;
		
	public SplashScreen(Main game) {
		this.game = game;
	}
	
	@Override
	public void show() {
		viewport = new FillViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		stage = new Stage(viewport);
		stage.getCamera().position.set(0f, 0f, 0f);
		
		logo = Image.get(ResourcesManager.getInstance().manager.get("logo.png", Texture.class)).centerX().centerY();
		logo.setColor(logo.getColor().r, logo.getColor().g, logo.getColor().b, 0);
		logo.addAction(Actions.sequence(Actions.alpha(0, 0), Actions.alpha(1, 0)));
		
		stage.addActor(logo);
		
		ResourcesManager.getInstance().loadResources();	
		currentPhase = SplashPhase.LOADING;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		switch(currentPhase)
		{
			case LOADING:
				if(ResourcesManager.getInstance().manager.update() && logo.getActions().size == 0)
				{
					ResourcesManager.getInstance().loadSkin();
					
					logo.addAction(Actions.sequence(Actions.alpha(1, 0), Actions.alpha(0, 0)));
					
					currentPhase = SplashPhase.END;
				}
				break;
				
			case END:
				if(logo.getActions().size == 0)
				{
					game.setScreen(new MenuScreen(game));
				}
				break;
		}
		
		stage.act();
		stage.draw();		
	}

	@Override
	public void resize(int width, int height) {
		this.stage.getViewport().update(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}
}
