package com.apptogo.runalien.level.segment;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.level.Spawnable;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.physics.UserData;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Segment extends GameActor implements Spawnable, Poolable{
	private static final long serialVersionUID = 1122120885547466019L;
	
	private Array<Field> fields;
	
	private Vector2 transform;
	
	private float baseOffset;

	public static int MIN_LEVEL = 0;
	public static int MAX_LEVEL = (int)Main.MAX_SPEED_LEVEL;
	
	public Segment(String name, int minLevel, int maxLevel, float baseOffset) { 
		super(name);
		fields = new Array<Field>();
		
		MIN_LEVEL = minLevel;
		MAX_LEVEL = maxLevel;
		this.baseOffset = baseOffset;
	}

	public void addField(String fieldName, BodyBuilder fieldBodyDef, Vector2 transform) {
		if(this.getBody() == null) {
			this.setBody(fieldBodyDef.create());
			this.setStaticImage(fieldName);
			this.transform = transform;
		}
		else {
			fields.add(new Field(fieldName, fieldBodyDef, transform));
		}
		
		//TODO Implement adding bodies and images
		//if(field != null)
		//	fields.add(field);
	}
/*
	public void setPosition(float posX) {
		for(Field field : fields){
			Vector2 position = new Vector2();
			position.x = field.getBody().getPosition().x + posX + UserData.get(field.getBody()).width/2;
			position.y = Main.GROUND_LEVEL + field.getBody().getPosition().y + UserData.get(field.getBody()).height/2;
			field.getBody().setTransform(position, 0);
		}
	}
*/
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		//TODO change 64f to unitconverter (also in another obstacles)
		for(Field field : fields) {
			batch.draw(field.getFieldRegion(), 
					   field.getFieldBody().getPosition().x - field.getFieldShapeOffset().x, field.getFieldBody().getPosition().y - field.getFieldShapeOffset().y, 
					   getOriginX(), getOriginY(), 
					   field.getFieldRegion().getRegionWidth()/64f, field.getFieldRegion().getRegionHeight()/64f, 
					   getScaleX(), getScaleY(), 
					   getRotation());
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		getBody().setTransform(-1000, Main.GROUND_LEVEL, 0);
	}
	
	@Override
	public void init() {
		super.init();
		
		Vector2 position = getBody().getPosition().cpy();
		getBody().setTransform(position.x + transform.x + UserData.get(getBody()).width/2f,
							   position.y + transform.y + UserData.get(getBody()).height/2f, 0);
		
		for(Field field : fields) {
			field.getFieldBody().setTransform(
				position.x + field.getFieldTransform().x + field.getFieldShapeOffset().x,
				position.y + field.getFieldTransform().y + field.getFieldShapeOffset().y, 
				0
			);
		}
	}
	
	public Array<Field> getFields() {
		return fields;
	}
	
	@Override
	public float getBaseOffset() {
		return baseOffset;
	}

	@Override
	public int getMinLevel() {
		return MIN_LEVEL;
	}

	@Override
	public int getMaxLevel() {
		return MAX_LEVEL;
	}

	int poolIndex;
	
	@Override
	public void setPoolIndex(int poolIndex) {
		this.poolIndex = poolIndex;
	}

	@Override
	public int getPoolIndex() {
		return poolIndex;
	}
}
