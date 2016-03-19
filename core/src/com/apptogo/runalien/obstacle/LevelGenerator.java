package com.apptogo.runalien.obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.apptogo.runalien.game.GameActor;
import com.badlogic.gdx.utils.Logger;

public class LevelGenerator {
	private final Logger logger = new Logger(getClass().getName(), Logger.DEBUG);
	
	private final static float GENERATION_OFFSET = 10;

	private GameActor player;
	private SegmentGenerator segmentGenerator;
	
	private float nextBlockPosition;
	private List<Segment> segments = new ArrayList<Segment>();
	
	public LevelGenerator(GameActor player) {

		this.player = player;
		this.segmentGenerator = new SegmentGenerator();

		calculateNextBlockPosition();
	}

	/**
	 * call this method in main loop. It will generate obstacles based on implemented algorithm
	 */
	public void generate() {
		//TODO here implement algorithm
		if (player.getBody().getPosition().x + GENERATION_OFFSET >= nextBlockPosition) {
//			generateSegment(SegmentDefi?nitions.BIG_BELL_PYRAMID);
			generateRandomBlock();
		}
		
		//free out of screen obstacles
		freeSegments();
	}

	
	/* ---------- PRIVATE METHODS ---------- */
	
	private void generateSegment(int[][] segmentDef) {
		Segment segment = segmentGenerator.getSegment(segmentDef);
		segment.setPosition(nextBlockPosition);
		segments.add(segment);
		
		calculateNextBlockPosition();
	}

	private void calculateNextBlockPosition() {
		this.nextBlockPosition += GENERATION_OFFSET;
	}
	
	private void freeSegments(){
		for(Segment segment : segments){
			//TODO check what's better
//			for(GameActor actor : segment.getObstacles()){
// 				if(actor.getBody().getPosition().x < player.getBody().getPosition().x + 1)
//					actor.setAlive(false);
//			}
			segment.getObstacles().stream().filter(o -> o.getBody().getPosition().x < player.getBody().getPosition().x + 1).forEach(o -> o.setAlive(false));
		}
	}

	/**
	 * generates all blocks from available definitions randomly
	 */
	private void generateRandomBlock() {
		//int[][] is defined like this
		String typeName = "[[I";
		
		//there are some different fields in SegmentDefinitions. We need to find index of first definition
		int firstSegmentIndex = 0;
		for (int i = 0; i < SegmentDefinitions.class.getFields().length; i++) {
			if (SegmentDefinitions.class.getFields()[i].getType().getName().equals(typeName)) {
				firstSegmentIndex = i;
				break;
			}
		}

		//randomize
		Random generator = new Random();
		int random = generator.nextInt(SegmentDefinitions.class.getFields().length - firstSegmentIndex) + firstSegmentIndex;

		//get all segment definitions using reflection
		try {
			generateSegment((int[][]) SegmentDefinitions.class.getFields()[random].get(null));
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			logger.error("Error during SegmentDefinitions reflection operation: ", e);
		}
	}

}
