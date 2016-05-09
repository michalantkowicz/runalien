package com.apptogo.runalien.level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.segment.Segment;
import com.apptogo.runalien.level.segment.SegmentDefinition;
import com.apptogo.runalien.level.segment.SegmentDefinitions;
import com.apptogo.runalien.level.segment.SegmentGenerator;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.plugin.RunningPlugin;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;

public class LevelGenerator {
	private final Logger logger = new Logger(getClass().getName(), Logger.ERROR);

	private final float SPAWN_DISTANCE = 15f;
	private final float DISAPPEAR_DISTANCE = 8f;

	private GameActor player;
	private SegmentGenerator segmentGenerator;

	private float nextPosition;

	private List<GameActor> activeObstacles = new ArrayList<GameActor>();
	private List<Segment> activeSegments = new ArrayList<Segment>();

	private Integer speedLevel;

	public LevelGenerator(GameActor player) {

		this.player = player;
		this.speedLevel = ((RunningPlugin) player.getPlugin(RunningPlugin.class.getSimpleName())).getSpeedLevel();
		this.segmentGenerator = new SegmentGenerator();
		this.nextPosition = 17f;
	}

	/**
	 * call this method in main loop. It will generate obstacles based on implemented algorithm
	 */
	public void generate() {
		updateLevel();

		if (player.getBody().getPosition().x + SPAWN_DISTANCE >= nextPosition) {
			//generateRandom();
			generateRandomObstacle();
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

		if (randomNumber < 50)
			if(!generateRandomObstacle())
				generateRandomSegment();
		else
			generateRandomSegment();
	}

	private boolean generateRandomObstacle() {

		//iterate through the map and get all obstacles with key matching level.
		List<Pool<GameActor>> possiblePools = new ArrayList<Pool<GameActor>>();
		for (Map.Entry<String, Pool<GameActor>> entry : Main.getInstance().getGameScreen().getObstaclesPool().getObstaclePools().entrySet()) {
			if (Float.valueOf(entry.getKey().split("-")[0]) <= speedLevel && Float.valueOf(entry.getKey().split("-")[1]) >= speedLevel) {
				possiblePools.add(entry.getValue());
			}
		}

		//get one random obstacle from possible Pools
		if (!possiblePools.isEmpty()) {
			logger.debug("spawning obstacle. Level: " + speedLevel);

			Random random = new Random();
			GameActor randomObstacle = possiblePools.get(possiblePools.size() > 1 ? random.nextInt(possiblePools.size() - 1) : 0).obtain();

			randomObstacle.getBody().setTransform(nextPosition, randomObstacle.getBody().getPosition().y, 0);
			randomObstacle.init();
			activeObstacles.add(randomObstacle);
			this.nextPosition += ((Spawnable) randomObstacle).getBaseOffset() + speedLevel;
			return true;
		}
		else 
			return false;
	}

	private void generateRandomSegment() {
		//get all segmentDefinitions matching current level
		List<SegmentDefinition> segmentDefinitions = new ArrayList<SegmentDefinition>();

		for (SegmentDefinition sd : SegmentDefinitions.SEGMENT_DEFINITIONS) {
			if (sd.getMinLevel() <= speedLevel && sd.getMaxLevel() >= speedLevel) {
				segmentDefinitions.add(sd);
			}
		}

		//get random segment def
		if (segmentDefinitions.size() > 0) {
			logger.debug("spawning segment. Level: " + speedLevel);

			Random random = new Random();
			SegmentDefinition segmentToSpawn = segmentDefinitions.get(random.nextInt(segmentDefinitions.size()));

			Segment segment = segmentGenerator.getSegment(segmentToSpawn);
			segment.setPosition(nextPosition);
			activeSegments.add(segment);

			this.nextPosition += segment.getBaseOffset() + speedLevel;
		}

	}

	//simple method to avoid huge code
	private void updateLevel() {
		this.speedLevel = ((RunningPlugin) player.getPlugin(RunningPlugin.class.getSimpleName())).getSpeedLevel();
	}
}
