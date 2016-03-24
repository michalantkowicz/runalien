package com.apptogo.runalien.level.segment;

import java.util.ArrayList;
import java.util.List;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.Spawnable;
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
	}

	public void addField(GameActor field) {
		fields.add(field);
	}

	public void setPosition(float posX) {
		fields.forEach(a -> a.getBody().setTransform(new Vector2(a.getBody().getPosition().x + posX + UserData.get(a.getBody()).width/2, Main.GROUND_LEVEL + a.getBody().getPosition().y + UserData.get(a.getBody()).height/2), 0));
	}

	public List<GameActor> getFields() {
		return fields;
	}
}
