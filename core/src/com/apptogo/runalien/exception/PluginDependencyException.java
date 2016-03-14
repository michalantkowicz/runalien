package com.apptogo.runalien.exception;

public class PluginDependencyException extends RuntimeException{

	private static final long serialVersionUID = -8150539479242480832L;
	
	public PluginDependencyException(String message){
		super(message);
	}

}
