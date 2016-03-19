package com.apptogo.runalien.obstacle;

import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class ObstacleDefinitions {

	//size of one block in segment
	public static final float OBSTACLE_SIZE = 0.7f;
	
	//names of actors and corresponding images
	public static final String CRATE = "crate";
	public static final String LOG_1 = "bottom1";
	public static final String LOG_2 = "bottom2";
	public static final String LOG_3 = "bottom3";
	public static final String LOG_4 = "bottom4";
	public static final String BELL_1 = "upper1";
	public static final String BELL_2 = "upper2";
	public static final String BELL_3 = "upper3";
	public static final String BELL_4 = "upper4";
	
	public final static BodyBuilder CRATE_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, GameScreen.getGroundLevel())
			.addFixture("killingBottom").box(OBSTACLE_SIZE, OBSTACLE_SIZE).sensor(true);
	
	public final static BodyBuilder LOG1_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, GameScreen.getGroundLevel())
			.addFixture("killingBottom").box(OBSTACLE_SIZE, OBSTACLE_SIZE).sensor(true);
	public final static BodyBuilder LOG2_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, GameScreen.getGroundLevel())
			.addFixture("killingBottom").box(OBSTACLE_SIZE, 2 * OBSTACLE_SIZE).sensor(true);
	public final static BodyBuilder LOG3_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, GameScreen.getGroundLevel())
			.addFixture("killingBottom").box(OBSTACLE_SIZE, 3 * OBSTACLE_SIZE).sensor(true);
	public final static BodyBuilder LOG4_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, GameScreen.getGroundLevel())
			.addFixture("killingBottom").box(OBSTACLE_SIZE, 4 * OBSTACLE_SIZE).sensor(true);
	
	public final static BodyBuilder BELL1_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, GameScreen.getGroundLevel())
			.addFixture("killingTop").box(OBSTACLE_SIZE, OBSTACLE_SIZE).sensor(true);
	public final static BodyBuilder BELL2_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, GameScreen.getGroundLevel())
			.addFixture("killingTop").box(OBSTACLE_SIZE, 2 * OBSTACLE_SIZE).sensor(true);
	public final static BodyBuilder BELL3_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, GameScreen.getGroundLevel())
			.addFixture("killingTop").box(OBSTACLE_SIZE, 3 * OBSTACLE_SIZE).sensor(true);
	public final static BodyBuilder BELL4_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, GameScreen.getGroundLevel())
			.addFixture("killingTop").box(OBSTACLE_SIZE, 4 * OBSTACLE_SIZE).sensor(true);
}
