package com.apptogo.runalien.plugin;

import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class TouchSteering extends AbstractPlugin {

	protected boolean jumping = false;
	protected boolean doubleJumping = false;
	protected boolean sliding = false;
	
	protected SequenceAction standUpAction = Actions.sequence(); 
	
	protected void jump()
	{
		sliding = false;
		
		if(!jumping)
		{	
			this.body.setLinearVelocity(new Vector2(this.body.getLinearVelocity().x, 30));
			jumping = true;
			actor.changeAnimation("jump");
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
		}
	}
	
	public void chargeDown()
	{
		if(jumping)
		{
			this.body.setLinearVelocity(body.getLinearVelocity().x, -35);
		}
		else
		{
			slide();
		}
	}
	
	public void slide()
	{		
		if(!sliding)
			actor.changeAnimation("slide");
		
		sliding = true;
		
		actor.removeAction(standUpAction);
		
		standUpAction = Actions.sequence(Actions.delay(0.4f), Actions.run(new Runnable() {
							@Override
							public void run() {
								if(sliding)
									standUp();
						}}));
		
		actor.addAction(standUpAction);
	}
	
	public void standUp()
	{
		sliding = false;
		actor.changeAnimation("standup");
		actor.queueAnimation("run");
	}
	
	public void land()
	{
		if(jumping || doubleJumping)
		{
			jumping = false;
			doubleJumping = false;
			actor.changeAnimation("land");
			actor.queueAnimation("run");
		}
	}
	
	public void startRunning(){
		Running running = actor.getPlugin(Running.class.getSimpleName());
		running.setStarted(true);
	}
	
	@Override
	public void run() {
		if(GameScreen.contactsSnapshot.containsKey("player") && GameScreen.contactsSnapshot.get("player").equals("ground"))
			land();
		
		if(Gdx.input.isKeyJustPressed(Keys.A))
			jump();
		if(Gdx.input.isKeyJustPressed(Keys.S))
			chargeDown();
		if(Gdx.input.isKeyJustPressed(Keys.SPACE))
			startRunning();
			
	}

}
