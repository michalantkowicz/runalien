package com.apptogo.runalien.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.segment.SegmentDefinition;
import com.apptogo.runalien.level.segment.SegmentDefinitions;
import com.apptogo.runalien.level.segment.SegmentGenerator;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.plugin.RunningPlugin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;

public class LevelGenerator {
	private final Logger logger = new Logger(getClass().getName(), Logger.ERROR);

	Array<Pool<GameActor>> pools = new Array<Pool<GameActor>>();
	Array<GameActor> activeActors = new Array<GameActor>();
	
	Map<Integer, IntArray> poolsLevel = new HashMap<Integer, IntArray>();  
	
	Random random = new Random();
	
	private SegmentGenerator segmentGenerator;
	
	private final float SPAWN_DISTANCE = 15f;
	private final float DISAPPEAR_DISTANCE = 8f;

	private GameActor player;

	private float nextPosition;

	private List<GameActor> activeObstacles = new ArrayList<GameActor>();

	private Integer speedLevel;

	public LevelGenerator(GameActor player) {

		this.player = player;
		this.speedLevel = ((RunningPlugin) player.getPlugin(RunningPlugin.class.getSimpleName())).getSpeedLevel();
		this.segmentGenerator = new SegmentGenerator();
		this.nextPosition = 17f;
		
		//creating slots for every speed level
		for(int i = 0; i <= Main.MAX_SPEED_LEVEL; i++)
			poolsLevel.put(i, new IntArray());
		
		createObstaclePools();
	}
	
	/**
	 * call this method in main loop. It will generate obstacles based on implemented algorithm
	 */
	public void generate() {
		//updateing player's speedLevel
		this.speedLevel = ((RunningPlugin) player.getPlugin(RunningPlugin.class.getSimpleName())).getSpeedLevel();

		if (player.getBody().getPosition().x + SPAWN_DISTANCE >= nextPosition) {
			generateRandomObstacle();
		}

		//free out of screen obstacles
		freePools();
		
		for(Pool<GameActor> p : pools) {
			System.out.print(p.getFree()+", ");
		}
		System.out.println();
	}
	
	private void generateRandomObstacle() {
		int possiblePoolsIndex = speedLevel > 0 ? random.nextInt(speedLevel + 1) : 0;
		IntArray possiblePools = poolsLevel.get(possiblePoolsIndex);
		
		if (possiblePools.size > 0) {
			int drawnPoolIndex = random.nextInt(possiblePools.size);
			Pool<GameActor> drawnPool = pools.get(possiblePools.get(drawnPoolIndex));
						
			GameActor randomObstacle = drawnPool.obtain();

			randomObstacle.getBody().setTransform(nextPosition, randomObstacle.getBody().getPosition().y, 0);
			randomObstacle.init(speedLevel);
			activeObstacles.add(randomObstacle);
			this.nextPosition += ((Spawnable) randomObstacle).getBaseOffset() + speedLevel;
		}
	}
	
	private void freePools() {
		for (Iterator<GameActor> iterator = activeObstacles.iterator(); iterator.hasNext();) {
			GameActor obstacle = iterator.next();
			if (obstacle.getBody().getPosition().x < player.getBody().getPosition().x - DISAPPEAR_DISTANCE) {
				obstacle.reset();
				iterator.remove();
				//TODO needs to implement reset() method due to proper position reset
				//pools.get(((Spawnable)obstacle).getPoolIndex()).free(obstacle);
			}
		}
	}
	
	//Creating obstacle pools
	private void createObstaclePools() {
		//TODO consider filling pools with objects at start
		logger.debug("Creating obstacles pools: ");
				
		//create segments
		for (final SegmentDefinition definition : SegmentDefinitions.SEGMENT_DEFINITIONS) {
			setAvailablePoolLevels(pools.size, definition.getMinLevel(), definition.getMaxLevel());
			final int poolIndex = pools.size;
			pools.add(new Pool<GameActor>(4) {
				@Override
				protected GameActor newObject() {
					GameActor obstacleActor = segmentGenerator.getSegment(definition);
					((Spawnable)obstacleActor).setPoolIndex(poolIndex);
					return obstacleActor;
				}
			});
		}
		
//		//create spheres
//		setAvailablePoolLevels(pools.size, Sphere.MIN_LEVEL, Sphere.MAX_LEVEL);
//		pools.add(new Pool<GameActor>(4) {
//			@Override
//			protected GameActor newObject() {
//				GameActor obstacleActor = new Sphere("sphere");
//				obstacleActor.setAlive(false);
//
//				return obstacleActor;
//			}
//		});
//		
//		//create weasles
//		setAvailablePoolLevels(pools.size, Rocket.MIN_LEVEL, Rocket.MAX_LEVEL);
//		pools.add(new Pool<GameActor>(4) {
//			@Override
//			protected GameActor newObject() {
//				GameActor obstacleActor = new Rocket("rocket");
//				obstacleActor.setAlive(false);
//
//				return obstacleActor;
//			}
//		});
	}
	
	private void setAvailablePoolLevels(int poolIndex, int min, int max) {
		for(int i = min; i <= max; i++)
			if(!poolsLevel.get(i).contains(poolIndex))
				poolsLevel.get(i).add(poolIndex);
	}
}
