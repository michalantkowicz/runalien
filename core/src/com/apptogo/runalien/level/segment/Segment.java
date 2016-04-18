package com.apptogo.runalien.level.segment;

import java.util.ArrayList;
import java.util.List;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.physics.UserData;
import com.badlogic.gdx.math.Vector2;

public class Segment {
	private List<GameActor> fields;
	private int minLevel;
	private int maxLevel;
	private float baseOffset;

	public Segment(int minLevel, int maxLevel, float baseOffset) {
		fields = new ArrayList<GameActor>();
		this.maxLevel = maxLevel;
		this.minLevel = minLevel;
		this.baseOffset = baseOffset;
	}

	public void addField(GameActor field) {
		fields.add(field);
	}

	public void setPosition(float posX) {
		for(GameActor field : fields){
			Vector2 position = new Vector2();
			position.x = field.getBody().getPosition().x + posX + UserData.get(field.getBody()).width/2;
			position.y = Main.GROUND_LEVEL + field.getBody().getPosition().y + UserData.get(field.getBody()).height/2;
			field.getBody().setTransform(position, 0);
		}
	}

	public List<GameActor> getFields() {
		return fields;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public float getBaseOffset() {
		return baseOffset;
	}

	public void setBaseOffset(float baseOffset) {
		this.baseOffset = baseOffset;
	}
}
