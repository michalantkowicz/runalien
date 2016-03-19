package com.apptogo.runalien.obstacle;

import static com.apptogo.runalien.obstacle.ObstacleDefinitions.*;
import static com.apptogo.runalien.obstacle.ObstacleDefinitions.OBSTACLE_SIZE;
import static com.apptogo.runalien.obstacle.SegmentDefinitions.*;
import static com.apptogo.runalien.obstacle.SegmentDefinitions.EMPTY;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.physics.UserData;
import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.math.Vector2;

public class SegmentGenerator {

	public Segment getSegment(int[][] content) {
		Segment segment = new Segment();

		for (int i = 0; i < content[0].length; i++) {
			for (int j = content.length - 1; j >= 0; j--) {
				//iteration through columns(bottom->top) -> rows(left->right) because we need column blocks like bells or logs
				int value = content[j][i];
				float positionX = i * OBSTACLE_SIZE;
				float positionY = (content.length - j - 1) * OBSTACLE_SIZE;

				GameActor obstacle = null;

				switch (value) {
				case (EMPTY):
					break;

				case (CRATE_BOT):
					obstacle = GameScreen.getObstaclesPool().getObstacle(CRATE);
					obstacle.getBody().getFixtureList().forEach(f -> UserData.get(f).key = "killingBottom");
					obstacle.getBody().setTransform(new Vector2(positionX, positionY), 0);
					segment.addObstacle(obstacle);
					break;

				case (CRATE_TOP):
					obstacle = GameScreen.getObstaclesPool().getObstacle(CRATE);
					obstacle.getBody().getFixtureList().forEach(f -> UserData.get(f).key = "killingTop");
					obstacle.getBody().setTransform(new Vector2(positionX, positionY), 0);
					segment.addObstacle(obstacle);
					break;

				case (LOG):
					//check how many in column
					int columnCounter = 1;
					for (int k = j - 1; k >= 0; k--) {
						if (content[k][i] == LOG && columnCounter < 4) {
							columnCounter++;
						} else {
							break;
						}
					}
					//modify column index (jump to the top of current obstacle)
					j -= (columnCounter - 1);
					
					switch (columnCounter) {
					case 1:
						obstacle = GameScreen.getObstaclesPool().getObstacle(LOG_1);
						break;
					case 2:
						obstacle = GameScreen.getObstaclesPool().getObstacle(LOG_2);
						break;
					case 3:
						obstacle = GameScreen.getObstaclesPool().getObstacle(LOG_3);
						break;
					case 4:
						obstacle = GameScreen.getObstaclesPool().getObstacle(LOG_4);
						break;
					}

					obstacle.getBody().setTransform(new Vector2(positionX, positionY + OBSTACLE_SIZE * columnCounter / 2 - OBSTACLE_SIZE / 2), 0);
					segment.addObstacle(obstacle);
					break;
				case (BELL):
					//check how many in column
					columnCounter = 1;
					for (int k = j - 1; k >= 0; k--) {
						if (content[k][i] == BELL && columnCounter < 4) {
							columnCounter++;
						} else {
							break;
						}
					}
					//modify column index (jump to the top of current obstacle)
					j -= (columnCounter - 1);
					
					switch (columnCounter) {
					case 1:
						obstacle = GameScreen.getObstaclesPool().getObstacle(BELL_1);
						break;
					case 2:
						obstacle = GameScreen.getObstaclesPool().getObstacle(BELL_2);
						break;
					case 3:
						obstacle = GameScreen.getObstaclesPool().getObstacle(BELL_3);
						break;
					case 4:
						obstacle = GameScreen.getObstaclesPool().getObstacle(BELL_4);
						break;
					}

					obstacle.getBody().setTransform(new Vector2(positionX, positionY + columnCounter * OBSTACLE_SIZE/2), 0);
					segment.addObstacle(obstacle);
					break;
				}
			}
		}
		return segment;

	}
}
