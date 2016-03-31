package com.apptogo.runalien.level;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.segment.Segment;
import com.apptogo.runalien.level.segment.SegmentDefinition;
import com.apptogo.runalien.level.segment.SegmentDefinitions;
import com.apptogo.runalien.level.segment.SegmentGenerator;
import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;

public class LevelGenerator {
	private final Logger logger = new Logger(getClass().getName(), Logger.DEBUG);

	private final float INITIAL_SPEED = 10f;

	private final float SPAWN_DISTANCE = 15f;
	private final float DISAPPEAR_DISTANCE = 8f;

	private GameActor player;
	private SegmentGenerator segmentGenerator;

	private float nextPosition;
	private float level;

	private List<GameActor> activeObstacles = new ArrayList<GameActor>();
	private List<Segment> activeSegments = new ArrayList<Segment>();

	public LevelGenerator(GameActor player) {

		this.player = player;
		this.segmentGenerator = new SegmentGenerator();
		this.nextPosition = 17f;
	}

	/**
	 * call this method in main loop. It will generate obstacles based on implemented algorithm
	 */
	public void generate() {
		//calculate level
		calculateLevel();

		if (player.getBody().getPosition().x + SPAWN_DISTANCE >= nextPosition) {
			generateRandom();
		}

		//free out of screen obstacles
		freeSegments();
	}

	/* ---------- PRIVATE METHODS ---------- */

	private void freeSegments() {
		for (Segment segment : activeSegments) {
			for (GameActor actor : segment.getFields()) {
				if (actor.getBody().getPosition().x < player.getBody().getPosition().x - DISAPPEAR_DISTANCE)
					actor.setAlive(false);
			}
		}

		for (Iterator<GameActor> iterator = activeObstacles.iterator(); iterator.hasNext();) {
			GameActor obstacle = iterator.next();
			if (obstacle.getBody().getPosition().x < player.getBody().getPosition().x - DISAPPEAR_DISTANCE) {
				obstacle.setAlive(false);
				iterator.remove();
			}
		}
	}

	private void generateRandom() {
		Random random = new Random();
		int randomNumber = random.nextInt(100);

		if (randomNumber > 0)
			generateRandomObstacle();
		else
			generateRandomSegment();
	}

	private void generateRandomObstacle() {

		//iterate through the map and get all obstacles with key matching level.
		List<Pool<GameActor>> possiblePools = new ArrayList<Pool<GameActor>>();
		for (Map.Entry<String, Pool<GameActor>> entry : Main.getInstance().getGameScreen().getObstaclesPool().getObstaclePools().entrySet()) {
			if (Float.valueOf(entry.getKey().split("-")[0]) <= level && Float.valueOf(entry.getKey().split("-")[1]) >= level) {
				possiblePools.add(entry.getValue());
			}
		}

		//get one random obstacle from possible Pools
		if (!possiblePools.isEmpty()) {
			logger.debug("spawning obstacle");

			Random random = new Random();
			GameActor randomObstacle = possiblePools.get(possiblePools.size() > 1 ? random.nextInt( possiblePools.size() - 1) : 0).obtain();
			
			randomObstacle.getBody().setTransform(nextPosition, randomObstacle.getBody().getPosition().y, 0);
			randomObstacle.init();
			activeObstacles.add(randomObstacle);
			this.nextPosition += ((Spawnable) randomObstacle).getBaseOffset() + level;
		}
	}

	private void generateRandomSegment() {
		//get all segmentDefinitions matching current level
		List<SegmentDefinition> segmentDefinitions = new ArrayList<SegmentDefinition>();
		
		for(Field field : SegmentDefinitions.class.getFields()){
			//if it's not segment definition continue
			if(!field.getType().getName().equals(SegmentDefinition.class.getTypeName())){
				continue;
			}
			try {
				SegmentDefinition s = (SegmentDefinition) field.get(null);
				if (s.getMinLevel() <= level && s.getMaxLevel() >= level) {
					segmentDefinitions.add(s);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error("Error during SegmentDefinitions reflection operation: ", e);
			}
		}

		//get random segment def
		if (segmentDefinitions.size() > 0) {
			logger.debug("spawning segment");
			
			Random random = new Random();
			SegmentDefinition segmentToSpawn = segmentDefinitions.get(random.nextInt(segmentDefinitions.size()));

			Segment segment = segmentGenerator.getSegment(segmentToSpawn);
			segment.setPosition(nextPosition);
			activeSegments.add(segment);

			this.nextPosition += segmentToSpawn.getBaseOffset() + level;
		}

	}

	private void calculateLevel() {
		this.level = player.getBody().getLinearVelocity().x - INITIAL_SPEED;
	}

}
