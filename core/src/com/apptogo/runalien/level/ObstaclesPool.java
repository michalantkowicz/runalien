package com.apptogo.runalien.level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.segment.SegmentFieldDefinitions;
import com.apptogo.runalien.physics.BodyBuilder;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;

public class ObstaclesPool {
	private final Logger logger = new Logger(getClass().getName(), Logger.ERROR);

	private final Map<String, Pool<GameActor>> pools = new HashMap<String, Pool<GameActor>>();
	private final Collection<GameActor> activeActors = new ArrayList<GameActor>();

	private final SegmentFieldDefinitions segmentFieldDefinitions = new SegmentFieldDefinitions();
	
	/**
	 * Constructor creates and fill all pools with obstacles at start
	 */
	public ObstaclesPool() {
		createPool(segmentFieldDefinitions.CRATE, segmentFieldDefinitions.CRATE_BODY, 36);
		createPool(segmentFieldDefinitions.BIG_CRATE, segmentFieldDefinitions.BIG_CRATE_BODY, 8);
		
		createPool(segmentFieldDefinitions.LOG_1, segmentFieldDefinitions.LOG1_BODY, 8);
		createPool(segmentFieldDefinitions.LOG_2, segmentFieldDefinitions.LOG2_BODY, 8);
		createPool(segmentFieldDefinitions.LOG_3, segmentFieldDefinitions.LOG3_BODY, 8);
		createPool(segmentFieldDefinitions.LOG_4, segmentFieldDefinitions.LOG4_BODY, 8);
		
		createPool(segmentFieldDefinitions.BELL_1, segmentFieldDefinitions.BELL1_BODY, 8);
		createPool(segmentFieldDefinitions.BELL_2, segmentFieldDefinitions.BELL2_BODY, 8);
		createPool(segmentFieldDefinitions.BELL_3, segmentFieldDefinitions.BELL3_BODY, 8);
		createPool(segmentFieldDefinitions.BELL_4, segmentFieldDefinitions.BELL4_BODY, 8);
		createPool(segmentFieldDefinitions.BELL_5, segmentFieldDefinitions.BELL5_BODY, 8);
		createPool(segmentFieldDefinitions.BELL_6, segmentFieldDefinitions.BELL6_BODY, 8);
		createPool(segmentFieldDefinitions.BELL_7, segmentFieldDefinitions.BELL7_BODY, 8);
	}

	/**
	 * call it in main loop. It will handle pools and will free every not alive obstacle.
	 * All you need to take care of is setting alive in proper moment.
	 * 
	 */
	public void freePools() {
		activeActors.stream().filter(a -> !a.isAlive()).forEach(a -> pools.get(a.getName()).free(a));
		
		boolean somethingWasRemoved = activeActors.removeIf(a -> !a.isAlive());
		if (somethingWasRemoved)
			logger.debug("Freeing pools. " + activeActors.size() + " Active actors left.");
	}

	/**
	 * Gives obstacle actor from pool. Remember about setting alive to false when you don't need it anymore
	 * Obstacle will be automatically returned to pool
	 * 
	 * @param actorName
	 * @return actor which is pooled
	 */
	public GameActor getObstacle(String obstacleName) {
		GameActor obstacle = pools.get(obstacleName).obtain();
		obstacle.init();
		activeActors.add(obstacle);
		logger.debug("Getting " + obstacleName + " from pool. " + pools.get(obstacleName).getFree() + " left in pool");
		return obstacle;
	}
	
	private void createPool(String actorName, BodyBuilder actorBodyDef, int capacity) {
		//TODO consider filling pools with objects at start
		logger.debug("Creating pool of: " + actorName);
		pools.put(actorName, new Pool<GameActor>(capacity) {
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
}
