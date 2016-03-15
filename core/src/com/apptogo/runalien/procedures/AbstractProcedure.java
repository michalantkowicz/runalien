package com.apptogo.runalien.procedures;

import com.apptogo.runalien.game.ImmaterialGameActor;

public abstract class AbstractProcedure {
	protected ImmaterialGameActor actor;

	abstract public void run();
	
	public void postSetActor() {	
	}
	
	public ImmaterialGameActor getActor() {
		return actor;
	}

	public void setActor(ImmaterialGameActor actor) {
		this.actor = actor;		
		postSetActor();
	}
}
