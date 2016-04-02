package com.apptogo.runalien.plugin;

import com.apptogo.runalien.exception.PluginDependencyException;
import com.apptogo.runalien.exception.PluginException;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.physics.UserData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public abstract class SteeringPlugin extends AbstractPlugin {

	protected RunningPlugin running;
	protected DeathPlugin deathPlugin;
	//private SoundPlugin soundHandler;
	
	protected Fixture defaultFixture, slidingFixture;
	
	protected boolean jumping = false;
	protected boolean doubleJumping = false;
	protected boolean sliding = false;
	protected boolean doStandUp = false;
	
	protected SequenceAction standUpAction = Actions.sequence(); 
	
	protected void jump()
	{
		if(sliding)
			standUp();
		
		if(!jumping)
		{	
			this.body.setLinearVelocity(new Vector2(this.body.getLinearVelocity().x, 35));
			jumping = true;
			actor.changeAnimation("jump");

//			soundHandler.pauseSound("scream");
//			soundHandler.pauseSound("run");
//			soundHandler.playSound("jump");
			
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
			this.body.setLinearVelocity(new Vector2(this.body.getLinearVelocity().x, 30));
			doubleJumping = true;
			
			//50% chance to play scream sound
//			if(Math.random() > 0.5f)
//				soundHandler.playSound("doubleJump");
//			else
//				soundHandler.playSound("jump");
		}
	}
	
	public void chargeDown()
	{
		if(jumping)
		{
			this.body.setLinearVelocity(body.getLinearVelocity().x, -35);
//			soundHandler.playSound("chargeDown");
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
//			soundHandler.playSound("slide");
//			soundHandler.pauseSound("scream");
//			soundHandler.pauseSound("run");
		}
		
		sliding = true;
		
		UserData.get(slidingFixture).ignore = false;
		slidingFixture.setSensor(false);
		UserData.get(defaultFixture).ignore = true;
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
		
//		soundHandler.resumeSound("scream");
//		soundHandler.resumeSound("run");
		
		UserData.get(slidingFixture).ignore = true;
		slidingFixture.setSensor(true);
		UserData.get(defaultFixture).ignore = false;
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
			
//			soundHandler.playSound("land");
//			soundHandler.resumeSound("scream");
//			soundHandler.resumeSound("run");
		}
	}
	
	public void startRunning(){		
		running.setStarted(true);
//		soundHandler.loopSound("scream");
//		soundHandler.loopSound("run");
	}
	
	public void stopRunning(){		
		running.setStarted(false);
//		soundHandler.pauseSound("scream");
//		soundHandler.pauseSound("run");
	}

	@Override
	public void run() {
		if(running.isStarted()) {
			if(Main.getInstance().getGameScreen().getContactsSnapshot().containsKey("player") && Main.getInstance().getGameScreen().getContactsSnapshot().get("player").equals("ground"))
				land();
			
			if(doStandUp )
				standUp();
		}
	}
	
	@Override
	public void setUpDependencies() {
//		try {
//			soundHandler = actor.getPlugin(SoundPlugin.class.getSimpleName());
//		}
//		catch(PluginException e) {
//			throw new PluginDependencyException("Actor must have SoundHandler plugin attached!");
//		}
		//Running plugin check
		try {
			running = actor.getPlugin(RunningPlugin.class.getSimpleName());
		}
		catch(PluginException e) {
			throw new PluginDependencyException("Actor must have Running plugin attached!");
		}
		
		//DeathPlugin check
		try {
			deathPlugin = actor.getPlugin(DeathPlugin.class.getSimpleName());
		}
		catch(PluginException e) {
			throw new PluginDependencyException("Actor must have Death plugin attached!");
		}
		
						
		//Default fixture check
		for(Fixture fixture : body.getFixtureList())
			if(((UserData)fixture.getUserData()).type == "default") {
				defaultFixture = fixture;
				break;
			}
		
		if(defaultFixture == null)
			throw new PluginDependencyException("Actor's body must have at least one (default) fixture!");
		
		//Sliding fixture check
		for(Fixture fixture : body.getFixtureList())
			if(((UserData)fixture.getUserData()).type == "sliding") {
				slidingFixture = fixture;
				break;
			}
		
		if(slidingFixture == null)
			throw new PluginDependencyException("Actor's body must have fixture that is of 'sliding' type!");
	}

	public boolean isSliding() {
		return sliding;
	}
	
	public boolean isJumping() {
		return jumping;
	}
	
	public boolean isDoubleJumping() {
		return doubleJumping;
	}
}
