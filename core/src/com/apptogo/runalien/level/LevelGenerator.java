package com.apptogo.runalien.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.apptogo.runalien.exception.LevelGeneratorException;
import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.segment.SegmentDefinition;
import com.apptogo.runalien.level.segment.SegmentDefinitions;
import com.apptogo.runalien.level.segment.SegmentGenerator;
import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;

public class LevelGenerator {
	class QueuedObstacle {
		public float position;
		public int poolIndex;
		public int speedLevel;
		public QueuedObstacle(float position, int poolIndex, int speedLevel) {
			this.position = position;
			this.poolIndex = poolIndex;
			this.speedLevel = speedLevel;
		}
	}
	
	private final Logger logger = new Logger(getClass().getName(), Logger.ERROR);

	Array<Pool<GameActor>> pools = new Array<Pool<GameActor>>();
	Array<GameActor> activeActors = new Array<GameActor>();
	
	Map<Integer, IntArray> poolsLevel = new HashMap<Integer, IntArray>();  
	
	Random random = new Random();
	
	private SegmentGenerator segmentGenerator;
	
	private final float SPAWN_DISTANCE = 15f;
	private final float DISAPPEAR_DISTANCE = 8f;

	private GameActor player;

	private QueuedObstacle obstacleToSet;
	
	private List<GameActor> activeObstacles = new ArrayList<GameActor>();

	private LinkedList<QueuedObstacle> obstaclesQueue = new LinkedList<QueuedObstacle>();

	public LevelGenerator(GameActor player) {

		this.player = player;
		this.segmentGenerator = new SegmentGenerator();
		
		//creating slots for every speed level
		for(int i = 0; i <= Main.MAX_SPEED_LEVEL; i++)
			poolsLevel.put(i, new IntArray());
		
		createObstaclePools();
		
		fulfillObstaclesQueue(10000);
		obstacleToSet = obstaclesQueue.pollFirst();
	}
	
	public void fulfillObstaclesQueue(float maxObstaclePosition) {
		for(IntArray possiblePools : poolsLevel.values())
			if(possiblePools.size < 2)
				throw new LevelGeneratorException("Every speedLevel (from 0 - "+Main.MAX_SPEED_LEVEL+") needs to have at least two possible obstacles!");
		
		float nextPosition = 17f;
		Integer speedLevel;
		
		//Last generated value to avoid repeating - initialized with whatever value (it could be sth else)
		int lastPoolIndex = 0; 
		
		while(nextPosition < maxObstaclePosition) {
			speedLevel = (int)(nextPosition/Main.SPEEDUP_INTERVAL);
			speedLevel = (speedLevel > Main.MAX_SPEED_LEVEL) ? Main.MAX_SPEED_LEVEL : speedLevel;
			
			IntArray possiblePools = poolsLevel.get(speedLevel);
			
			if (possiblePools.size > 0) {
				possiblePools.shuffle();
				
				int drawnPoolIndex = possiblePools.first();
				
				//Avoid repating
				//TODO make sure that every array has more than one element (as assertion)
				if(drawnPoolIndex == lastPoolIndex)
					drawnPoolIndex = possiblePools.peek();
				lastPoolIndex = drawnPoolIndex;	
				
				obstaclesQueue.add(new QueuedObstacle(nextPosition, drawnPoolIndex, speedLevel));
				
				//Now obtain obstacle just for a while to get it's BaseOffset
				Pool<GameActor> drawnPool = pools.get(drawnPoolIndex);
				GameActor obstacle = drawnPool.obtain();
				
				nextPosition += ((Spawnable) obstacle).getBaseOffset() + speedLevel;
				
				//Free the obstacle - it is not necessary any longer now
				drawnPool.free(obstacle);
			}
		}
	}
	
	/**
	 * call this method in main loop. It will generate obstacles based on implemented algorithm
	 */
	public void generate() {
		//set the obstacle if it is time to
		if(player.getBody().getPosition().x + SPAWN_DISTANCE >= obstacleToSet.position) {
			Pool<GameActor> drawnPool = pools.get(obstacleToSet.poolIndex);
				
			GameActor randomObstacle = drawnPool.obtain();
			randomObstacle.getBody().setTransform(obstacleToSet.position, randomObstacle.getBody().getPosition().y, 0);
			randomObstacle.init(obstacleToSet.speedLevel);
			activeObstacles.add(randomObstacle);
			
			obstacleToSet = obstaclesQueue.pollFirst();
			System.out.println(obstacleToSet.poolIndex);
		}

		//free out of screen obstacles
		freePools();
		
//		for(Pool<GameActor> p : pools) {
//			System.out.print(p.getFree()+", ");
//		}
//		System.out.println();
	}
	
	private void freePools() {
		for (Iterator<GameActor> iterator = activeObstacles.iterator(); iterator.hasNext();) {
			GameActor obstacle = iterator.next();
			if (obstacle.getBody().getPosition().x + ((Spawnable)obstacle).getBaseOffset() < player.getBody().getPosition().x - DISAPPEAR_DISTANCE) {
				obstacle.reset();
				iterator.remove();
				
				pools.get(((Spawnable)obstacle).getPoolIndex()).free(obstacle);
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
			Pool<GameActor> pool = new Pool<GameActor>(4) {
				@Override
				protected GameActor newObject() {
					GameActor obstacleActor = segmentGenerator.getSegment(definition);
					((Spawnable)obstacleActor).setPoolIndex(poolIndex);
					return obstacleActor;
				}
			};
			
			pools.add(pool);
			fulfillPool(pool, 1);
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
	
	private void fulfillPool(Pool<GameActor> pool, int objectsCount) {
		Array<GameActor> gameActors = new Array<GameActor>();
		
		for(int i = 0; i < objectsCount; i++)
			gameActors.add(pool.obtain());
		
		for(GameActor gameActor : gameActors)
			pool.free(gameActor);
	}
	
	private void setAvailablePoolLevels(int poolIndex, int min, int max) {
		for(int i = min; i <= max; i++)
			if(!poolsLevel.get(i).contains(poolIndex))
				poolsLevel.get(i).add(poolIndex);
	}
}
