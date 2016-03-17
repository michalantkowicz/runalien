package com.apptogo.runalien.obstacle;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Logger;

public class ObstacleGenerator {
	private final Logger logger = new Logger(getClass().getName(), Logger.DEBUG);
	
	private final static float GENERATION_OFFSET = 10;

	private Queue<GameActor> obstacles = new LinkedList<GameActor>();
	private GameActor player;
	private float nextBlockPosition;
	private SegmentGenerator segmentGenerator;

	public ObstacleGenerator(GameActor player) {

		this.player = player;
		this.segmentGenerator = new SegmentGenerator();

		for (int i = 0; i < 10; i++) {
			Body obstacleBody = BodyBuilder.get().type(BodyType.StaticBody).position(-100, GameScreen.getGroundLevel() + 0.1f).addFixture("killingBottom").box(0.4f, 0.4f).sensor(true).create();
			GameActor obstacleActor = new GameActor("bottom1");
			obstacleActor.setStaticImage("bottom1");
			obstacleActor.setBody(obstacleBody);

			obstacles.add(obstacleActor);
		}

		calculateNextBlockPosition();
	}

	public void generate() {
		if (player.getBody().getPosition().x + GENERATION_OFFSET >= nextBlockPosition) {
			calculateNextBlockPosition();
			generateAllBlockRandomly();
		}
	}

	private void generateSegment(int[][] segmentDef) {
		List<GameActor> segment = segmentGenerator.get(segmentDef);
		for (GameActor obstacle : segment) {
			obstacle.getBody().setTransform(new Vector2(nextBlockPosition, obstacle.getBody().getPosition().y), 0);
			GameScreen.getGameworldStage().addActor(obstacle);
		}

	}

	private void calculateNextBlockPosition() {
		this.nextBlockPosition += GENERATION_OFFSET;
	}

	/**
	 * generates all blocks from available definitions randomly
	 */
	private void generateAllBlockRandomly() {
		//int[][] is defined like this
		String typeName = "[[I";
		
		//there are some different fields in SegmentDefinitions. We need to find index of first definition
		int firstSegmentIndex = 0;
		for (int i = 0; i < SegmentDefinitions.class.getFields().length; i++) {
			if (SegmentDefinitions.class.getFields()[i].getType().getName().equals(typeName)) {
				firstSegmentIndex = i;
				break;
			}
		}

		//randomize
		Random generator = new Random();
		int random = generator.nextInt(SegmentDefinitions.class.getFields().length - firstSegmentIndex) + firstSegmentIndex;

		//get all segment definitions using reflection
		try {
			generateSegment((int[][]) SegmentDefinitions.class.getFields()[random].get(null));
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			logger.error("Error during SegmentDefinitions reflection operation: ", e);
		}
	}

}
