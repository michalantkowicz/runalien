package com.apptogo.runalien.level;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
			//TODO check what's better
			//			for(GameActor actor : segment.getObstacles()){
			// 				if(actor.getBody().getPosition().x < player.getBody().getPosition().x + 1)
			//					actor.setAlive(false);
			//			}
			segment.getFields().stream().filter(o -> o.getBody().getPosition().x < player.getBody().getPosition().x - DISAPPEAR_DISTANCE).forEach(o -> o.setAlive(false));
		}
		
		for(GameActor obstacle : activeObstacles){
			if(obstacle.getBody().getPosition().x < player.getBody().getPosition().x - DISAPPEAR_DISTANCE){
				obstacle.setAlive(false);
			}
		}
		activeObstacles.removeIf(o -> o.getBody().getPosition().x < player.getBody().getPosition().x - DISAPPEAR_DISTANCE);
	}

	
	private void generateRandom(){
		Random random = new Random();
		int randomNumber = random.nextInt(100);
		
		if(randomNumber > 0)
			generateRandomObstacle();
		else
			generateRandomSegment();
	}
	
	private void generateRandomObstacle() {
		List<Map.Entry<String,Pool<GameActor>>> allPools = new ArrayList<Map.Entry<String,Pool<GameActor>>>(Main.getInstance().getGameScreen().getObstaclesPool().getObstaclePools().entrySet());
		Collections.shuffle(allPools);
		
		Optional<Entry<String, Pool<GameActor>>> optional = allPools.stream().filter(k -> Float.valueOf(k.getKey().split("-")[0]) <= level && Float.valueOf(k.getKey().split("-")[1]) >= level).findAny();
		
		if(optional.isPresent()){
			logger.debug("spawning obstacle");
			GameActor obstacle = optional.get().getValue().obtain();
			obstacle.getBody().setTransform(nextPosition,  obstacle.getBody().getPosition().y, 0);
			obstacle.init();
			activeObstacles.add(obstacle);
			this.nextPosition += ((Spawnable)obstacle).getBaseOffset() + level;
		}
	}
	
	private void generateRandomSegment() {
		Field[] segmentDefinitionFields = Arrays.asList(SegmentDefinitions.class.getFields()).stream().filter(f -> f.getType().getName().equals(SegmentDefinition.class.getTypeName())).toArray(Field[]::new);

		//get all segmentDefinitions matching current level
		List<SegmentDefinition> segmentDefinitions = new ArrayList<SegmentDefinition>();
		for (int i = 0; i < segmentDefinitionFields.length; i++) {
			try {
				SegmentDefinition s = (SegmentDefinition)segmentDefinitionFields[i].get(null);
				if(s.getMinLevel() <= level && s.getMaxLevel() >= level){
					segmentDefinitions.add(s);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error("Error during SegmentDefinitions reflection operation: ", e);
			}
		}
		
		if(segmentDefinitions.size() > 0){
			logger.debug("spawning segment");
			//get random segment def
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
