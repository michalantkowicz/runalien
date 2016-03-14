package com.apptogo.runalien.procedures;

import com.apptogo.runalien.game.ImmaterialGameActor;
import com.badlogic.gdx.utils.Array;


public class RepeatProcedure extends AbstractProcedure {

	String repeatAnimation;
	Array<ImmaterialGameActor> actors = new Array<ImmaterialGameActor>();
	
	public RepeatProcedure(String repeatAnimation){
		super();
		this.repeatAnimation = repeatAnimation;
	}
	
	public void postSetActor() {
		actors.add(actor);
		
		actors.add(new ImmaterialGameActor("1"));
		actors.add(new ImmaterialGameActor("2"));
		
		actors.get(1).setAvailableAnimations(repeatAnimation);
		actors.get(2).setAvailableAnimations(repeatAnimation);
		
		actors.get(1).queueAnimation(repeatAnimation);
		actors.get(2).queueAnimation(repeatAnimation);
		
		actors.get(1).setPosition(actors.get(0).getX() + actors.get(0).getWidth(), actors.get(0).getY());
		actors.get(2).setPosition(actors.get(1).getX() + actors.get(1).getWidth(), actors.get(1).getY());
		
		actor.getStage().addActor(actors.get(1));
		actor.getStage().addActor(actors.get(2));
	}
	
	@Override
	public void run() {
		
	}
}
