package com.apptogo.runalien.level.segment;

import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class SegmentFieldDefinitions {

	//size of one field in segment
	public static final float OBSTACLE_SIZE = UnitConverter.toBox2dUnits(76f);
	
	//names of fields and corresponding images
	public final String CRATE = "crate";
	public final String BIG_CRATE = "bigCrate";
	public final String LOG_1 = "bottom1";
	public final String LOG_2 = "bottom2";
	public final String LOG_3 = "bottom3";
	public final String LOG_4 = "bottom4";
	public final String BELL_1 = "upper1";
	public final String BELL_2 = "upper2";
	public final String BELL_3 = "upper3";
	public final String BELL_4 = "upper4";
	public final String BELL_5 = "upper5";
	public final String BELL_6 = "upper6";
	public final String BELL_7 = "upper7";
	
	public final BodyBuilder CRATE_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, Main.GROUND_LEVEL)
			.addFixture("killingBottom").box(OBSTACLE_SIZE, OBSTACLE_SIZE).sensor(true);
	public final BodyBuilder BIG_CRATE_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, Main.GROUND_LEVEL)
			.addFixture("killingBottom").box(OBSTACLE_SIZE * 2, OBSTACLE_SIZE * 2).sensor(true);
	
	public final BodyBuilder LOG1_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, Main.GROUND_LEVEL)
			.addFixture("killingBottom").box(OBSTACLE_SIZE, OBSTACLE_SIZE).sensor(true);
	public final BodyBuilder LOG2_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, Main.GROUND_LEVEL)
			.addFixture("killingBottom").box(OBSTACLE_SIZE, 2 * OBSTACLE_SIZE).sensor(true);
	public final BodyBuilder LOG3_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, Main.GROUND_LEVEL)
			.addFixture("killingBottom").box(OBSTACLE_SIZE, 3 * OBSTACLE_SIZE).sensor(true);
	public final BodyBuilder LOG4_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, Main.GROUND_LEVEL)
			.addFixture("killingBottom").box(OBSTACLE_SIZE, 4 * OBSTACLE_SIZE).sensor(true);
	
	public final BodyBuilder BELL1_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, Main.GROUND_LEVEL)
			.addFixture("killingTop").box(OBSTACLE_SIZE, OBSTACLE_SIZE).sensor(true);
	public final BodyBuilder BELL2_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, Main.GROUND_LEVEL)
			.addFixture("killingTop").box(OBSTACLE_SIZE, 2 * OBSTACLE_SIZE).sensor(true);
	public final BodyBuilder BELL3_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, Main.GROUND_LEVEL)
			.addFixture("killingTop").box(OBSTACLE_SIZE, 3 * OBSTACLE_SIZE).sensor(true);
	public final BodyBuilder BELL4_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, Main.GROUND_LEVEL)
			.addFixture("killingTop").box(OBSTACLE_SIZE, 4 * OBSTACLE_SIZE).sensor(true);
	public final BodyBuilder BELL5_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, Main.GROUND_LEVEL)
			.addFixture("killingTop").box(OBSTACLE_SIZE, 5 * OBSTACLE_SIZE).sensor(true);
	public final BodyBuilder BELL6_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, Main.GROUND_LEVEL)
			.addFixture("killingTop").box(OBSTACLE_SIZE, 6 * OBSTACLE_SIZE).sensor(true);
	public final BodyBuilder BELL7_BODY = BodyBuilder.get().type(BodyType.StaticBody).position(-100, Main.GROUND_LEVEL)
			.addFixture("killingTop").box(OBSTACLE_SIZE, 7 * OBSTACLE_SIZE).sensor(true);
}
