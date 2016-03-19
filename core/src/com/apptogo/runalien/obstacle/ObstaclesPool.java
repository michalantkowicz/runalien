package com.apptogo.runalien.obstacle;

import static com.apptogo.runalien.obstacle.ObstacleDefinitions.CRATE;
import static com.apptogo.runalien.obstacle.ObstacleDefinitions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.physics.BodyBuilder;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;

public class ObstaclesPool {
	private final Logger logger = new Logger(getClass().getName(), Logger.ERROR);

	private final Map<String, Pool<GameActor>> pools = new HashMap<String, Pool<GameActor>>();
	private final Collection<GameActor> activeActors = new ArrayList<GameActor>();

	/**
	 * Constructor creates and fill all pools with obstacles at start
	 */
	public ObstaclesPool() {
		createPool(CRATE, CRATE_BODY, 36);
		
		createPool(LOG_1, LOG1_BODY, 8);
		createPool(LOG_2, LOG2_BODY, 8);
		createPool(LOG_3, LOG3_BODY, 8);
		createPool(LOG_4, LOG4_BODY, 8);
		
		createPool(BELL_1, BELL1_BODY, 8);
		createPool(BELL_2, BELL2_BODY, 8);
		createPool(BELL_3, BELL3_BODY, 8);
		createPool(BELL_4, BELL4_BODY, 8);
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
