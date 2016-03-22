package com.apptogo.runalien.level.segment;

import static com.apptogo.runalien.level.segment.SegmentDefinitions.BELL;
import static com.apptogo.runalien.level.segment.SegmentDefinitions.CRATE_BOT;
import static com.apptogo.runalien.level.segment.SegmentDefinitions.CRATE_TOP;
import static com.apptogo.runalien.level.segment.SegmentDefinitions.EMPTY;
import static com.apptogo.runalien.level.segment.SegmentDefinitions.LOG;
import static com.apptogo.runalien.level.segment.SegmentFieldDefinitions.BELL_1;
import static com.apptogo.runalien.level.segment.SegmentFieldDefinitions.BELL_2;
import static com.apptogo.runalien.level.segment.SegmentFieldDefinitions.BELL_3;
import static com.apptogo.runalien.level.segment.SegmentFieldDefinitions.BELL_4;
import static com.apptogo.runalien.level.segment.SegmentFieldDefinitions.CRATE;
import static com.apptogo.runalien.level.segment.SegmentFieldDefinitions.LOG_1;
import static com.apptogo.runalien.level.segment.SegmentFieldDefinitions.LOG_2;
import static com.apptogo.runalien.level.segment.SegmentFieldDefinitions.LOG_3;
import static com.apptogo.runalien.level.segment.SegmentFieldDefinitions.LOG_4;
import static com.apptogo.runalien.level.segment.SegmentFieldDefinitions.OBSTACLE_SIZE;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.physics.UserData;
import com.badlogic.gdx.math.Vector2;

public class SegmentGenerator {

	public Segment getSegment(SegmentDefinition segmentDefinition) {
		int definition[][] = segmentDefinition.getDefinition();
		
		Segment segment = new Segment(segmentDefinition.getMinLevel(), segmentDefinition.getMaxLevel(), segmentDefinition.getBaseOffset());

		for (int i = 0; i < definition[0].length; i++) {
			for (int j = definition.length - 1; j >= 0; j--) {
				//iteration through columns(bottom->top) -> rows(left->right) because we need column blocks like bells or logs
				int value = definition[j][i];
				float positionX = i * OBSTACLE_SIZE;
				float positionY = (definition.length - j - 1) * OBSTACLE_SIZE;

				GameActor field = null;

				switch (value) {
				case (EMPTY):
					break;

				case (CRATE_BOT):
					field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(CRATE);
					field.getBody().getFixtureList().forEach(f -> UserData.get(f).key = "killingBottom");
					field.getBody().setTransform(new Vector2(positionX, positionY), 0);
					segment.addField(field);
					break;

				case (CRATE_TOP):
					field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(CRATE);
					field.getBody().getFixtureList().forEach(f -> UserData.get(f).key = "killingTop");
					field.getBody().setTransform(new Vector2(positionX, positionY), 0);
					segment.addField(field);
					break;

				case (LOG):
					//check how many in column
					int columnCounter = 1;
					for (int k = j - 1; k >= 0; k--) {
						if (definition[k][i] == LOG && columnCounter < 4) {
							columnCounter++;
						} else {
							break;
						}
					}
					//modify column index (jump to the top of current obstacle)
					j -= (columnCounter - 1);
					
					switch (columnCounter) {
					case 1:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(LOG_1);
						break;
					case 2:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(LOG_2);
						break;
					case 3:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(LOG_3);
						break;
					case 4:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(LOG_4);
						break;
					}

					field.getBody().setTransform(new Vector2(positionX, positionY + OBSTACLE_SIZE * columnCounter / 2 - OBSTACLE_SIZE / 2), 0);
					segment.addField(field);
					break;
				case (BELL):
					//check how many in column
					columnCounter = 1;
					for (int k = j - 1; k >= 0; k--) {
						if (definition[k][i] == BELL && columnCounter < 4) {
							columnCounter++;
						} else {
							break;
						}
					}
					//modify column index (jump to the top of current obstacle)
					j -= (columnCounter - 1);
					
					switch (columnCounter) {
					case 1:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(BELL_1);
						break;
					case 2:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(BELL_2);
						break;
					case 3:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(BELL_3);
						break;
					case 4:
						field = Main.getInstance().getGameScreen().getObstaclesPool().getObstacle(BELL_4);
						break;
					}

					field.getBody().setTransform(new Vector2(positionX, positionY + columnCounter * OBSTACLE_SIZE/2), 0);
					segment.addField(field);
					break;
				}
			}
		}
		return segment;

	}
}
