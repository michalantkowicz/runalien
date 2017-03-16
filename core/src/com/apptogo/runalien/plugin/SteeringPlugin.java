package com.apptogo.runalien.plugin;

import com.apptogo.runalien.exception.PluginDependencyException;
import com.apptogo.runalien.manager.CustomAction;
import com.apptogo.runalien.manager.CustomActionManager;
import com.apptogo.runalien.physics.ContactListener;
import com.apptogo.runalien.physics.UserData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Logger;

public abstract class SteeringPlugin extends AbstractPlugin {

	private final Logger logger = new Logger(getClass().getName(), Logger.DEBUG);
	
	protected RunningPlugin running;
	protected DeathPlugin deathPlugin;
	private SoundPlugin soundHandler;
	private AchievementPlugin achievementPlugin;	
	
	protected Fixture defaultFixture, slidingFixture;
	
	protected boolean jumping = false;
	protected boolean doubleJumping = false;
	protected boolean sliding = false;
	protected boolean doStandUp = false;
	
	protected CustomAction standUpAction = new CustomAction(0.4f) {
		@Override
		public void perform() {
			if(sliding)
				doStandUp = true;
		}
	};
	
	protected void jump()
	{
		if(sliding)
			standUp();
		
		if(!jumping)
		{	
			this.body.setLinearVelocity(new Vector2(this.body.getLinearVelocity().x, 37));
			jumping = true;
			actor.changeAnimation("jump");

			soundHandler.stopSound("runscream");
			soundHandler.playSound("jump");
			
			achievementPlugin.handleJump();
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
			if(Math.random() > 0.5f)
				soundHandler.playSound("doubleJump");
			else
				soundHandler.playSound("jump");
		}
	}
	
	public void chargeDown()
	{
		if(jumping && body.getPosition().y > 0.5f)
		{
			System.out.println("charge on: " + body.getPosition().y);
			this.body.setLinearVelocity(body.getLinearVelocity().x, -45);
			soundHandler.playSound("chargeDown");
		}
		else
		{
			System.out.println("land and slide on: " + body.getPosition().y);
			jumping = false;
			doubleJumping = false;
			slide();
		}
	}
	
	public void slide()
	{		
		if(!sliding){
			actor.changeAnimation("slide");
			soundHandler.stopSound("runscream");
			soundHandler.playSound("slide");
			
			achievementPlugin.fire(AchievementPlugin.SLIDER);
		}
		
		sliding = true;
		
		UserData.get(slidingFixture).ignore = false;
		slidingFixture.setSensor(false);
		UserData.get(defaultFixture).ignore = true;
		defaultFixture.setSensor(true);
		
		CustomActionManager.getInstance().unregisterAction(standUpAction);
		CustomActionManager.getInstance().registerAction(standUpAction);
	}
	
	public void standUp()
	{
		sliding = false;
		doStandUp = false;
		
		actor.changeAnimation("standup");
		actor.queueAnimation("run");
		
		soundHandler.loopSound("runscream");
		
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
			
			soundHandler.playSound("land");
			soundHandler.loopSound("runscream");
		}
	}
	
	public void startRunning(){		
		running.setStarted(true);
		soundHandler.loopSound("runscream");
	}
	
	public void stopRunning(){		
		running.setStarted(false);
		soundHandler.stopSound("runscream");
	}

	@Override
	public void run() {
		if(running.isStarted()) {
			if(ContactListener.SNAPSHOT.collide(UserData.get(actor.getBody()), "ground"))
				land();
			
			if(doStandUp)
				standUp();
		}
	}
	
	@Override
	public void setUpDependencies() {
		soundHandler = actor.getPlugin(SoundPlugin.class);
		running = actor.getPlugin(RunningPlugin.class);
		deathPlugin = actor.getPlugin(DeathPlugin.class);
		achievementPlugin = actor.getPlugin(AchievementPlugin.class);
						
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
