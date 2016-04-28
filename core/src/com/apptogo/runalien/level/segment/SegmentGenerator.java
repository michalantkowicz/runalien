package com.apptogo.runalien.level.segment;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.math.Vector2;

public class SegmentGenerator {
	private final SegmentFieldDefinitions segmentFieldDefinitions = new SegmentFieldDefinitions();
	
	public Segment getSegment(SegmentDefinition segmentDefinition) {
		int length = segmentDefinition.getDefinition().length;
		int[][] definition = new int[length][segmentDefinition.getDefinition()[0].length];
	    for (int i = 0; i < length; i++) {
	        System.arraycopy(segmentDefinition.getDefinition()[i], 0, definition[i], 0, segmentDefinition.getDefinition()[i].length);
	    }
		
		//TODO exception for maximum size
		Segment segment = new Segment(segmentDefinition.getMinLevel(), segmentDefinition.getMaxLevel(), segmentDefinition.getBaseOffset() + definition[0].length);

		for (int i = 0; i < definition[0].length; i++) {
			for (int j = definition.length - 1; j >= 0; j--) {
				//iteration through columns(bottom->top) -> rows(left->right) because we need column blocks like bells or logs
				int value = definition[j][i];
				float positionX = i * SegmentFieldDefinitions.OBSTACLE_SIZE;
				float positionY = (definition.length - j - 1) * SegmentFieldDefinitions.OBSTACLE_SIZE;

				GameActor field = null;

				if(value == SegmentDefinitions.EMPTY) {
					//Do nothing
				}
				else if(value == SegmentDefinitions.CRATE_BOT) {
					field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.CRATE);
					field.getBody().setTransform(new Vector2(positionX, positionY), 0);
					segment.addField(field);
				}
				else if(value == SegmentDefinitions.CRATE_TOP) {
					field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.CRATE);
					field.getBody().setTransform(new Vector2(positionX, positionY), 0);
					segment.addField(field);
				}
				else if(value == SegmentDefinitions.BIG_CRATE_BOT){
					if(j-1>=0 && i+1<definition[0].length && definition[j-1][i] == SegmentDefinitions.BIG_CRATE_BOT
							&& definition[j][i+1] == SegmentDefinitions.BIG_CRATE_BOT
							&& definition[j-1][i+1] == SegmentDefinitions.BIG_CRATE_BOT){
						
						//clear fields when big crate already used it
						definition[j-1][i] = SegmentDefinitions.EMPTY;
						definition[j][i+1] = SegmentDefinitions.EMPTY;
						definition[j-1][i+1] = SegmentDefinitions.EMPTY;
						
						field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.BIG_CRATE);
						
						field.getBody().setTransform(new Vector2(positionX, positionY), 0);
						segment.addField(field);	
					}
				}
				else if(value == SegmentDefinitions.BIG_CRATE_TOP){
					if(j-1>=0 && i+1<definition[0].length && definition[j-1][i] == SegmentDefinitions.BIG_CRATE_TOP
							&& definition[j][i+1] == SegmentDefinitions.BIG_CRATE_TOP
							&& definition[j-1][i+1] == SegmentDefinitions.BIG_CRATE_TOP){
						
						definition[j-1][i] = SegmentDefinitions.EMPTY;
						definition[j][i+1] = SegmentDefinitions.EMPTY;
						definition[j-1][i+1] = SegmentDefinitions.EMPTY;
						
						field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.BIG_CRATE);

						field.getBody().setTransform(new Vector2(positionX, positionY), 0);
						segment.addField(field);	
					}
				}
				else if(value == SegmentDefinitions.LOG) {
					//check how many in column
					int columnCounter = 1;
					for (int k = j - 1; k >= 0; k--) {
						if (definition[k][i] == SegmentDefinitions.LOG && columnCounter < 4) {
							columnCounter++;
						} else {
							break;
						}
					}
					//modify column index (jump to the top of current obstacle)
					j -= (columnCounter - 1);
					
					switch (columnCounter) {
					case 1:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.LOG_1);
						break;
					case 2:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.LOG_2);
						break;
					case 3:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.LOG_3);
						break;
					case 4:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.LOG_4);
						break;
					}

					field.getBody().setTransform(new Vector2(positionX, positionY), 0);
					segment.addField(field);
				}
				else if(value == SegmentDefinitions.BELL) {
					//check how many in column
					int columnCounter = 1;
					for (int k = j - 1; k >= 0; k--) {
						if (definition[k][i] == SegmentDefinitions.BELL && columnCounter < 7) {
							columnCounter++;
						} else {
							break;
						}
					}
					//modify column index (jump to the top of current obstacle)
					j -= (columnCounter - 1);
					
					switch (columnCounter) {
					case 1:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.BELL_1);
						break;
					case 2:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.BELL_2);
						break;
					case 3:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.BELL_3);
						break;
					case 4:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.BELL_4);
						break;
					case 5:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.BELL_5);
						break;
					case 6:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.BELL_6);
						break;
					case 7:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getSegmentField(segmentFieldDefinitions.BELL_7);
						break;
					}

					field.getBody().setTransform(new Vector2(positionX, positionY), 0);
					segment.addField(field);
				}
			}
		}
		return segment;

	}
}
