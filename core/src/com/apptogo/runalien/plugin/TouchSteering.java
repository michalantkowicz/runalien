package com.apptogo.runalien.plugin;

import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class TouchSteering extends AbstractPlugin {

	protected boolean jumping = false;
	protected boolean doubleJumping = false;
	
	protected void jump()
	{
		if(!jumping)
		{	
			this.body.setLinearVelocity(new Vector2(this.body.getLinearVelocity().x, 30));
			jumping = true;
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
	}
	
	public void land()
	{System.out.println("LAND");
		if(jumping || doubleJumping)
		{
			jumping = false;
			doubleJumping = false;
		}
	}
	
	@Override
	public void run() {
		if(GameScreen.contactsSnapshot.containsKey("player") && GameScreen.contactsSnapshot.get("player").equals("ground"))
			land();
		
		if(Gdx.input.isKeyJustPressed(Keys.A))
			jump();
		if(Gdx.input.isKeyJustPressed(Keys.S))
			chargeDown();
	}

}
