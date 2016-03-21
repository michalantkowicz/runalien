package com.apptogo.runalien.level;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.segment.Segment;
import com.apptogo.runalien.level.segment.SegmentDefinition;
import com.apptogo.runalien.level.segment.SegmentDefinitions;
import com.apptogo.runalien.level.segment.SegmentGenerator;
import com.apptogo.runalien.plugin.RunningPlugin;
import com.badlogic.gdx.utils.Logger;

public class LevelGenerator {
	private final Logger logger = new Logger(getClass().getName(), Logger.DEBUG);

	private final float INITIAL_SPEED = 10f;
	
	private final float SPAWN_DISTANCE = 15f;
	private final float DISAPPEAR_DISTANCE = 5f;
	
	private GameActor player;
	private SegmentGenerator segmentGenerator;

	private float nextPosition;
	private int level;

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
			generateRandomSegmentMeetingLevelRequirements();
		}

		//free out of screen obstacles
		freeSegments();
	}

	/* ---------- PRIVATE METHODS ---------- */

	private void generateSegment(SegmentDefinition segmentDefinition) {
		Segment segment = segmentGenerator.getSegment(segmentDefinition);
		segment.setPosition(nextPosition);
		activeSegments.add(segment);

		this.nextPosition += segmentDefinition.getBaseOffset() + level;
	}

	private void freeSegments() {
		for (Segment segment : activeSegments) {
			//TODO check what's better
			//			for(GameActor actor : segment.getObstacles()){
			// 				if(actor.getBody().getPosition().x < player.getBody().getPosition().x + 1)
			//					actor.setAlive(false);
			//			}
			segment.getFields().stream().filter(o -> o.getBody().getPosition().x < player.getBody().getPosition().x - DISAPPEAR_DISTANCE).forEach(o -> o.setAlive(false));
		}
	}

	private void generateRandomSegmentMeetingLevelRequirements() {
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
		
		//get random segment def
		Random random = new Random();
		SegmentDefinition segmentToSpawn = segmentDefinitions.get(random.nextInt(segmentDefinitions.size()));
		
		generateSegment(segmentToSpawn);
	}

	/**
	 * generates all blocks from available definitions randomly
	 */
	@SuppressWarnings("unused")
	private void generateRandomSegment() {

		//get all segment definitions using reflection and streams
		try {
			List<Field> fields = Arrays.asList(SegmentDefinitions.class.getFields());
			Collections.shuffle(fields);

			Field field = fields.stream().filter(f -> f.getType().getName().equals(SegmentDefinition.class.getTypeName())).findAny().get();

			generateSegment((SegmentDefinition) field.get(null));
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			logger.error("Error during SegmentDefinitions reflection operation: ", e);
		}
	}

	private void calculateLevel() {
		this.level = (int) (((RunningPlugin) player.getPlugin(RunningPlugin.class.getSimpleName())).getRunningSpeed() - INITIAL_SPEED);
	}

}
