package com.apptogo.runalien.obstacle;

import java.util.ArrayList;
import java.util.List;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.math.Vector2;

public class Segment {
	private List<GameActor> obstacles;

	public Segment() {
		obstacles = new ArrayList<GameActor>();
	}

	public Segment(List<GameActor> segment) {
		this.obstacles = segment;
	}

	public void addObstacle(GameActor obstacle) {
		obstacles.add(obstacle);
	}

	public void setPosition(float posX) {
		obstacles.forEach(a -> a.getBody().setTransform(new Vector2(a.getBody().getPosition().x + posX, GameScreen.getGroundLevel() + a.getBody().getPosition().y), 0));
	}

	public List<GameActor> getObstacles() {
		return obstacles;
	}

}
