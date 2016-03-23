package com.apptogo.runalien.level.segment;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.physics.UserData;
import com.badlogic.gdx.math.Vector2;

public class SegmentGenerator {
	private final SegmentFieldDefinitions segmentFieldDefinitions = new SegmentFieldDefinitions();
	
	public Segment getSegment(SegmentDefinition segmentDefinition) {
		int definition[][] = segmentDefinition.getDefinition();
		
		Segment segment = new Segment(segmentDefinition.getMinLevel(), segmentDefinition.getMaxLevel(), segmentDefinition.getBaseOffset());

		for (int i = 0; i < definition[0].length; i++) {
			for (int j = definition.length - 1; j >= 0; j--) {
				//iteration through columns(bottom->top) -> rows(left->right) because we need column blocks like bells or logs
				int value = definition[j][i];
				float positionX = i * segmentFieldDefinitions.OBSTACLE_SIZE;
				float positionY = (definition.length - j - 1) * segmentFieldDefinitions.OBSTACLE_SIZE;

				GameActor field = null;

				if(value == SegmentDefinitions.EMPTY) {
					//Do nothing
				}
				else if(value == SegmentDefinitions.CRATE_BOT) {
					field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(segmentFieldDefinitions.CRATE);
					field.getBody().getFixtureList().forEach(f -> UserData.get(f).key = "killingBottom");
					field.getBody().setTransform(new Vector2(positionX, positionY), 0);
					segment.addField(field);
				}

				else if(value == SegmentDefinitions.CRATE_TOP) {
					field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(segmentFieldDefinitions.CRATE);
					field.getBody().getFixtureList().forEach(f -> UserData.get(f).key = "killingTop");
					field.getBody().setTransform(new Vector2(positionX, positionY), 0);
					segment.addField(field);
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
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(segmentFieldDefinitions.LOG_1);
						break;
					case 2:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(segmentFieldDefinitions.LOG_2);
						break;
					case 3:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(segmentFieldDefinitions.LOG_3);
						break;
					case 4:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(segmentFieldDefinitions.LOG_4);
						break;
					}

					field.getBody().setTransform(new Vector2(positionX, positionY + segmentFieldDefinitions.OBSTACLE_SIZE * columnCounter / 2 - segmentFieldDefinitions.OBSTACLE_SIZE / 2), 0);
					segment.addField(field);
				}
				else if(value == SegmentDefinitions.BELL) {
					//check how many in column
					int columnCounter = 1;
					for (int k = j - 1; k >= 0; k--) {
						if (definition[k][i] == SegmentDefinitions.BELL && columnCounter < 4) {
							columnCounter++;
						} else {
							break;
						}
					}
					//modify column index (jump to the top of current obstacle)
					j -= (columnCounter - 1);
					
					switch (columnCounter) {
					case 1:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(segmentFieldDefinitions.BELL_1);
						break;
					case 2:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(segmentFieldDefinitions.BELL_2);
						break;
					case 3:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(segmentFieldDefinitions.BELL_3);
						break;
					case 4:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(segmentFieldDefinitions.BELL_4);
						break;
					}

					field.getBody().setTransform(new Vector2(positionX, positionY + columnCounter * segmentFieldDefinitions.OBSTACLE_SIZE/2), 0);
					segment.addField(field);
				}
			}
		}
		return segment;

	}
}
