package com.apptogo.runalien.plugin;

import com.apptogo.runalien.exception.PluginDependencyException;
import com.apptogo.runalien.exception.PluginException;
import com.apptogo.runalien.physics.UserData;
import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class TouchSteeringPlugin extends AbstractPlugin {

	protected boolean jumping = false;
	protected boolean doubleJumping = false;
	protected boolean sliding = false;
	
	protected SequenceAction standUpAction = Actions.sequence(); 
	private SoundPlugin soundHandler;
	private RunningPlugin running;
	
	private Fixture defaultFixture, slidingFixture;
	private boolean doStandUp = false;

	protected void jump()
	{
		if(sliding)
			standUp();
		
		if(!jumping)
		{	
			this.body.setLinearVelocity(new Vector2(this.body.getLinearVelocity().x, 30));
			jumping = true;
			actor.changeAnimation("jump");

			soundHandler.pauseSound("scream");
			soundHandler.pauseSound("run");
			soundHandler.playSound("jump");
			
		}
		else
		{
			doubleJump();
		}
	}
	
	public void doubleJump()
	{
		if(!doubleJumping)
		{
			this.body.setLinearVelocity(new Vector2(this.body.getLinearVelocity().x, 25));
			doubleJumping = true;
			
			//50% chance to play scream sound
			if(Math.random() > 0.5f)
				soundHandler.playSound("doubleJump");
			else
				soundHandler.playSound("jump");
		}
	}
	
	public void chargeDown()
	{
		if(jumping)
		{
			this.body.setLinearVelocity(body.getLinearVelocity().x, -35);
			soundHandler.playSound("chargeDown");
		}
		else
		{
			slide();
		}
	}
	
	public void slide()
	{		
		if(!sliding){
			actor.changeAnimation("slide");
			soundHandler.playSound("slide");
			soundHandler.pauseSound("scream");
			soundHandler.pauseSound("run");
		}
		
		sliding = true;
		
		((UserData)slidingFixture.getUserData()).ignore = false;
		slidingFixture.setSensor(false);
		((UserData)defaultFixture.getUserData()).ignore = true;
		defaultFixture.setSensor(true);
		
		actor.removeAction(standUpAction);
		standUpAction = Actions.sequence(Actions.delay(0.4f), Actions.run(new Runnable() {
							@Override
							public void run() {
								if(sliding)
									doStandUp = true;
						}}));
		actor.addAction(standUpAction);
	}
	
	public void standUp()
	{
		sliding = false;
		doStandUp = false;
		
		actor.changeAnimation("standup");
		actor.queueAnimation("run");
		
		soundHandler.resumeSound("scream");
		soundHandler.resumeSound("run");
		
		((UserData)slidingFixture.getUserData()).ignore = true;
		slidingFixture.setSensor(true);
		((UserData)defaultFixture.getUserData()).ignore = false;
		defaultFixture.setSensor(false);
	}
	
	public void land()
	{
		if(jumping || doubleJumping)
		{
			jumping = false;
			doubleJumping = false;
			actor.changeAnimation("land");
			actor.queueAnimation("run");
			
			soundHandler.playSound("land");
			soundHandler.resumeSound("scream");
			soundHandler.resumeSound("run");
		}
	}
	
	public void startRunning(){		
		running.setStarted(true);
		soundHandler.loopSound("scream");
		soundHandler.loopSound("run");
	}
	
	@Override
	public void run() {		
		if(GameScreen.contactsSnapshot.containsKey("player") && GameScreen.contactsSnapshot.get("player").equals("ground"))
			land();
		
		if(doStandUp )
			standUp();
		
		if(running.isStarted()){
			if(Gdx.input.isKeyJustPressed(Keys.A))
				jump();
			if(Gdx.input.isKeyJustPressed(Keys.S))
				chargeDown();
		}
		if(Gdx.input.isKeyJustPressed(Keys.SPACE))
			startRunning();
	}

	@Override
	public void setUpDependencies() {
		try {
			soundHandler = actor.getPlugin(SoundPlugin.class.getSimpleName());
		}
		catch(PluginException e) {
			throw new PluginDependencyException("Actor must have SoundHandler plugin attached!");
		}
		
		try {
			running = actor.getPlugin(RunningPlugin.class.getSimpleName());
		}
		catch(PluginException e) {
			throw new PluginDependencyException("Actor must have Running plugin attached!");
		}
				
		if(body.getFixtureList().size <= 0)
			throw new PluginDependencyException("Actor's body must have at least one (default) fixture!");
		else
			defaultFixture = body.getFixtureList().first();
		
		for(Fixture fixture : body.getFixtureList())
			if(((UserData)fixture.getUserData()).key.contains("sliding")) {
				slidingFixture = fixture;
				break;
			}
		
		if(slidingFixture == null)
			throw new PluginDependencyException("Actor's body must have fixture that contains 'sliding' in userData's key!");
	}
}
