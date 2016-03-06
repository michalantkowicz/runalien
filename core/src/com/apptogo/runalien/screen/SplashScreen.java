
package com.apptogo.runalien.screen;

import java.util.Random;

import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.manager.ResourcesManager;
import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.scene2d.Image;
import com.apptogo.runalien.tools.Movement;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.FillViewport;


public class SplashScreen implements Screen
{
	private Main game;
	
	private Stage stage;
	private FillViewport viewport;
	
	private Movement movement;
	
	private Image logo, background;
	private Animation letterAnimation;
	
	private enum SplashPhase
	{
		APPTOGO_LOGO_IN,
		APPTOGO_LOGO_OUT,
		DASHANDSMASH_LOGO_IN,
		SCREEN_SHAKEING,
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
		
		movement = new Movement(Interpolation.exp10, 0.015f);
		
		logo = Image.get(ResourcesManager.getInstance().manager.get("logo.png", Texture.class)).centerX().centerY();
		logo.addAction(Actions.sequence(Actions.alpha(0, 0), Actions.alpha(1, 1)));
		stage.addActor(logo);
		
		ResourcesManager.getInstance().manager.load("splash.pack", TextureAtlas.class);	
		currentPhase = SplashPhase.APPTOGO_LOGO_IN;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		switch(currentPhase)
		{
			case APPTOGO_LOGO_IN:
				if(logo.getActions().size == 0 && ResourcesManager.getInstance().manager.update())
				{
					logo.addAction(Actions.sequence(Actions.alpha(1, 2), Actions.alpha(0, 1)));		
					currentPhase = SplashPhase.APPTOGO_LOGO_OUT;
				}
				break;
				
			case APPTOGO_LOGO_OUT:
				if(logo.getActions().size == 0)
				{
					stage.addActor(Image.get("logoSplash").centerX().centerY(Main.SCREEN_HEIGHT));
					
					letterAnimation = Animation.get(0.03f, "letter").centerX(-355f).centerY(Main.SCREEN_HEIGHT - 160).stop();
					stage.addActor(letterAnimation);
					
					stage.addAction(Actions.sequence(Actions.moveTo(0, -Main.SCREEN_HEIGHT - 80, 0.8f, Interpolation.pow5In)));
															
					currentPhase = SplashPhase.DASHANDSMASH_LOGO_IN;
				}
				break;
				
			case DASHANDSMASH_LOGO_IN:
				if(stage.getRoot().getActions().size == 0)
				{
					letterAnimation.start();
					stage.addActor(Animation.get(0.035f, "dust").scaleFrames(2f).centerX(-150).centerY(Main.SCREEN_HEIGHT - 170).action(Actions.alpha(0, 1)));
					
					background = Image.get("splashbackground").centerX().centerY(Main.SCREEN_HEIGHT + 40);
					background.addAction(Actions.sequence(Actions.alpha(0,0), Actions.alpha(1, 1)));
					stage.addActor(background);
					background.toBack();
					
					currentPhase = SplashPhase.SCREEN_SHAKEING;
				}
				break;
				
			case SCREEN_SHAKEING:
				if(stage.getRoot().getActions().size == 0)
				{					
					ResourcesManager.getInstance().loadResources();
					
					//minimal time for waiting
					SequenceAction sequence = new SequenceAction();
					for(float i = 1; i < 50; i++)
					{
						float randomX = stage.getRoot().getX() + 100/i * (( new Random() ).nextFloat() - 0.5f);
						float randomY = stage.getRoot().getY() + 100/i * (( new Random() ).nextFloat() - 0.5f);
						
						sequence.addAction(Actions.moveTo(randomX, randomY, 0.005f));
					}
					
					stage.addAction(Actions.sequence(sequence, Actions.alpha(1, 3f)));
					
					currentPhase = SplashPhase.LOADING;
				}
				break;
				
			case LOADING:
				if(stage.getRoot().getActions().size == 0 && ResourcesManager.getInstance().manager.update())
				{
					ResourcesManager.getInstance().loadSkin();
					
					movement.set(stage.getCamera().position, stage.getCamera().position.cpy().add(0, 0, 2));
					stage.addAction(Actions.sequence(Actions.alpha(1, 0.5f), Actions.alpha(0, 0.5f)));
					
					currentPhase = SplashPhase.END;
				}
				break;
				
			case END:
				if(movement.isRun())
				{
					((OrthographicCamera)stage.getCamera()).zoom += movement.getCurrent().z;
					((OrthographicCamera)stage.getCamera()).update();
				}
				if(movement.complete())
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
