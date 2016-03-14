package com.apptogo.runalien.manager;

import java.util.LinkedList;
import java.util.Queue;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class ObstacleGenerator {

	private final static float GENERATION_OFFSET = 10;
	
	private Queue<GameActor> obstacles = new LinkedList<GameActor>();
	private GameActor player;
	private float nextBlockPosition;
	
	public ObstacleGenerator(GameActor player){
		this.player = player;
		
		for(int i=0; i<10; i++){
			Body obstacleBody = BodyBuilder.get().type(BodyType.StaticBody).position(-100, GameScreen.getGroundLevel() + 0.1f).addFixture("killingBottom").box(0.4f, 0.4f).sensor(true).create();
	        GameActor obstacleActor = new GameActor("bottom1");
	        obstacleActor.setStaticImage("bottom1");
	        obstacleActor.setBody(obstacleBody);
	        
	        obstacles.add(obstacleActor);
		}
		
        calculateNextBlockPosition();
	}
	
	public void generate(){
		if(player.getBody().getPosition().x + GENERATION_OFFSET >= nextBlockPosition){
			calculateNextBlockPosition();
			generateBlock();
		}
	}
	
	private void generateBlock(){
		GameActor obstacle = obstacles.poll();
		if(obstacle != null){
			obstacle.getBody().setTransform(new Vector2(nextBlockPosition, obstacle.getBody().getPosition().y), 0);
			GameScreen.getGameworldStage().addActor(obstacle);
		}
	}
	
	private void calculateNextBlockPosition(){
		this.nextBlockPosition += GENERATION_OFFSET;
	}
	
}
