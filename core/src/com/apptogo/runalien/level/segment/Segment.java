package com.apptogo.runalien.level.segment;

import java.util.ArrayList;
import java.util.List;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.Spawnable;
import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.math.Vector2;

public class Segment implements Spawnable {
	private List<GameActor> fields;
	private int minLevel;
	private int maxLevel;
	private float baseOffset;

	public Segment(int minLevel, int maxLevel, float baseOffset) {
		fields = new ArrayList<GameActor>();
	}

	public void addField(GameActor field) {
		fields.add(field);
	}

	public void setPosition(float posX) {
		fields.forEach(a -> a.getBody().setTransform(new Vector2(a.getBody().getPosition().x + posX, Main.GROUND_LEVEL + a.getBody().getPosition().y), 0));
	}

	public List<GameActor> getFields() {
		return fields;
	}

	@Override
	public float getBaseOffset() {
		return baseOffset;
	}

	@Override
	public float getMinLevel() {
		return minLevel;
	}

	@Override
	public float getMaxLevel() {
		return maxLevel;
	}

}
