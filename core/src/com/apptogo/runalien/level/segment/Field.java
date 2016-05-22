package com.apptogo.runalien.level.segment;

import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.manager.ResourcesManager;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.physics.UserData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Field {
	private String fieldName;
	private Body fieldBody;
	private Vector2 fieldTransform;
	private Vector2 fieldShapeOffset;
	private AtlasRegion fieldRegion;
	
	public Field() {
	}
	
	public Field(String fieldName, BodyBuilder fieldBodyDef, Vector2 fieldTransform) {
		this.fieldName = fieldName;
		this.fieldBody = fieldBodyDef.create();
		this.fieldTransform = fieldTransform;
		this.fieldShapeOffset = new Vector2(UserData.get(getFieldBody()).width/2f, UserData.get(getFieldBody()).height/2f);
		this.fieldRegion = ResourcesManager.getInstance().getAtlasRegion(fieldName);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Body getFieldBody() {
		return fieldBody;
	}

	public void setFieldBody(Body fieldBody) {
		this.fieldBody = fieldBody;
	}

	public Vector2 getFieldTransform() {
		return fieldTransform;
	}

	public void setFieldTransform(Vector2 fieldTransform) {
		this.fieldTransform = fieldTransform;
	}
	
	public Vector2 getFieldShapeOffset() {
		return fieldShapeOffset;
	}

	public void setFieldShapeOffset(Vector2 fieldShapeOffset) {
		this.fieldShapeOffset = fieldShapeOffset;
	}

	public AtlasRegion getFieldRegion() {
		return fieldRegion;
	}

	public void setFieldRegion(AtlasRegion fieldSprite) {
		this.fieldRegion = fieldSprite;
	}
}
