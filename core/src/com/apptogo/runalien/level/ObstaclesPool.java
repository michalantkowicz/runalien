package com.apptogo.runalien.level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.obstacle.FallingSphere;
import com.apptogo.runalien.level.obstacle.Sphere;
import com.apptogo.runalien.level.segment.SegmentFieldDefinitions;
import com.apptogo.runalien.physics.BodyBuilder;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;

public class ObstaclesPool {
	private final Logger logger = new Logger(getClass().getName(), Logger.ERROR);

	private final Map<String, Pool<GameActor>> segmentPools = new HashMap<String, Pool<GameActor>>();
	private final Map<String, Pool<GameActor>> obstaclePools = new HashMap<String, Pool<GameActor>>();
	private final Collection<GameActor> activeActors = new ArrayList<GameActor>();

	private final SegmentFieldDefinitions segmentFieldDefinitions = new SegmentFieldDefinitions();
	
	/**
	 * Constructor creates and fill all pools with obstacles at start
	 */
	public ObstaclesPool() {
		createSegmentPool(segmentFieldDefinitions.CRATE, segmentFieldDefinitions.CRATE_BODY, 36);
		createSegmentPool(segmentFieldDefinitions.BIG_CRATE, segmentFieldDefinitions.BIG_CRATE_BODY, 8);
		
		createSegmentPool(segmentFieldDefinitions.LOG_1, segmentFieldDefinitions.LOG1_BODY, 8);
		createSegmentPool(segmentFieldDefinitions.LOG_2, segmentFieldDefinitions.LOG2_BODY, 8);
		createSegmentPool(segmentFieldDefinitions.LOG_3, segmentFieldDefinitions.LOG3_BODY, 8);
		createSegmentPool(segmentFieldDefinitions.LOG_4, segmentFieldDefinitions.LOG4_BODY, 8);
		
		createSegmentPool(segmentFieldDefinitions.BELL_1, segmentFieldDefinitions.BELL1_BODY, 8);
		createSegmentPool(segmentFieldDefinitions.BELL_2, segmentFieldDefinitions.BELL2_BODY, 8);
		createSegmentPool(segmentFieldDefinitions.BELL_3, segmentFieldDefinitions.BELL3_BODY, 8);
		createSegmentPool(segmentFieldDefinitions.BELL_4, segmentFieldDefinitions.BELL4_BODY, 8);
		createSegmentPool(segmentFieldDefinitions.BELL_5, segmentFieldDefinitions.BELL5_BODY, 8);
		createSegmentPool(segmentFieldDefinitions.BELL_6, segmentFieldDefinitions.BELL6_BODY, 8);
		createSegmentPool(segmentFieldDefinitions.BELL_7, segmentFieldDefinitions.BELL7_BODY, 8);
		
		createObstaclePools();
	}

	/**
	 * call it in main loop. It will handle pools and will free every not alive obstacle.
	 * All you need to take care of is setting alive in proper moment.
	 * 
	 */
	public void freePools() {
		for(Iterator<GameActor> iterator = activeActors.iterator(); iterator.hasNext();){
			GameActor actor = iterator.next();
			if(!actor.isAlive()){
				segmentPools.get(actor.getName()).free(actor);
				iterator.remove();
				logger.debug("Freeing pools. " + activeActors.size() + " Active actors left.");
			}
		}
	}

	/**
	 * Gives obstacle actor from pool. Remember about setting alive to false when you don't need it anymore
	 * Obstacle will be automatically returned to pool
	 * 
	 * @param actorName
	 * @return actor which is pooled
	 */
	public GameActor getSegmentField(String segmentName) {
		GameActor segmentField = segmentPools.get(segmentName).obtain();
		segmentField.init();
		activeActors.add(segmentField);
		logger.debug("Getting " + segmentName + " from pool. " + segmentPools.get(segmentName).getFree() + " left in pool");
		return segmentField;
	}
	
	private void createSegmentPool(final String actorName, final BodyBuilder actorBodyDef, int capacity) {
		//TODO consider filling pools with objects at start
		logger.debug("Creating pool of: " + actorName);
		segmentPools.put(actorName, new Pool<GameActor>(capacity) {
			@Override
			protected GameActor newObject() {
				GameActor obstacleActor = new GameActor(actorName);
				obstacleActor.setStaticImage(actorName);
				obstacleActor.setBody(actorBodyDef.create());
				obstacleActor.setAlive(false);

				return obstacleActor;
			}
		});
	}
	private void createObstaclePools() {
		//TODO consider filling pools with objects at start
		logger.debug("Creating obstacles pools: ");
		
		//create spheres
		obstaclePools.put("5-10", new Pool<GameActor>(4) {
			@Override
			protected GameActor newObject() {
				GameActor obstacleActor = new Sphere("sphere");
				obstacleActor.setAlive(false);

				return obstacleActor;
			}
		});
		
		//create weasles
		obstaclePools.put("0-10", new Pool<GameActor>(4) {
			@Override
			protected GameActor newObject() {
				GameActor obstacleActor = new FallingSphere("fallingSphere");
				obstacleActor.setAlive(false);

				return obstacleActor;
			}
		});
	}

	public Map<String, Pool<GameActor>> getObstaclePools() {
		return obstaclePools;
	}
}
