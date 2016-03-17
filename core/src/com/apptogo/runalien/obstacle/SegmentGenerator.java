package com.apptogo.runalien.obstacle;

import java.util.ArrayList;
import java.util.List;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import static com.apptogo.runalien.obstacle.SegmentDefinitions.*;

public class SegmentGenerator {

	public List<GameActor> get(int[][] content) {
		//TODO add pool support
		List<GameActor> actors = new ArrayList<GameActor>();

		for (int i = 0; i < content.length; i++) {
			for (int j = content.length - 1; j >= 0; j--) {
				//iteration through columns->rows because we need column blocks like bells
				int value = content[j][i];
				float positionX = i * OBSTACLE_SIZE;
				float positionY = (content.length - j - 1) * OBSTACLE_SIZE;

				Body body;

				switch (value) {
				case (EMPTY):
					break;
				case (CRATE):
					body = BodyBuilder.get().type(BodyType.StaticBody).position(-100, GameScreen.getGroundLevel() + 0.2f).addFixture("killingBottom")
							.box(OBSTACLE_SIZE, OBSTACLE_SIZE, positionX, positionY).sensor(true).create();

					GameActor obstacleActor = new GameActor("obstacle");
					obstacleActor.setStaticImage("crate");
					obstacleActor.setName("crate");
					obstacleActor.setBody(body);
					obstacleActor.modifyCustomOffsets(positionX, positionY);

					actors.add(obstacleActor);
					break;
				case (LOG):

					body = BodyBuilder.get().type(BodyType.StaticBody).position(-100, GameScreen.getGroundLevel() + 0.2f).addFixture("killingBottom")
							.box(OBSTACLE_SIZE, OBSTACLE_SIZE, positionX, positionY).sensor(true).create();

					//if previous one also log then join them
					if (j < content.length - 3 && content[j + 3][i] == LOG) {
						addLog(4, positionX, positionY, actors, body);
					} else if (j < content.length - 2 && content[j + 2][i] == LOG) {
						addLog(3, positionX, positionY, actors, body);
					} else if (j < content.length - 1 && content[j + 1][i] == LOG) {
						addLog(2, positionX, positionY, actors, body);
					} else {
						addLog(1, positionX, positionY, actors, body);
					}
					break;
				case (BELL):

					body = BodyBuilder.get().type(BodyType.StaticBody).position(-100, GameScreen.getGroundLevel() + 0.2f).addFixture("killingTop")
							.box(OBSTACLE_SIZE, OBSTACLE_SIZE, positionX, positionY).sensor(true).create();

					if (j >= 3 && content[j - 3][i] == BELL) {
						addBell(4, positionX, positionY, actors, body);
					} else if (j >= 2 && content[j - 2][i] == BELL) {
						addBell(3, positionX, positionY, actors, body);
					} else if (j >= 1 && content[j - 1][i] == BELL) {
						addBell(2, positionX, positionY, actors, body);
					} else {
						addBell(1, positionX, positionY, actors, body);
					}
					break;
				}
			}
		}
		return actors;

	}

	private void addBell(int size, float positionX, float positionY, List<GameActor> actors, Body body) {
		GameActor obstacleActor = new GameActor("upper" + size);
		obstacleActor.setStaticImage("upper" + size);
		obstacleActor.setBody(body);
		obstacleActor.modifyCustomOffsets(positionX, positionY);
		if (!actors.isEmpty() && actors.get(actors.size() - 1).getName().equals("upper" + (size + 1))) {
			obstacleActor.setVisible(false);
		}
		actors.add(obstacleActor);
	}

	private void addLog(int size, float positionX, float positionY, List<GameActor> actors, Body body) {
		GameActor obstacleActor = new GameActor("bottom" + size);
		if (!actors.isEmpty() && size != 1)
			actors.get(actors.size() - (size - 1)).setVisible(false);
		obstacleActor.setStaticImage("bottom" + size);
		obstacleActor.setBody(body);
		obstacleActor.modifyCustomOffsets(positionX, positionY - (size - 1) * OBSTACLE_SIZE);
		actors.add(obstacleActor);
	}
}
