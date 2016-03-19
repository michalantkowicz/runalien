package com.apptogo.runalien.game;

import com.apptogo.runalien.manager.CustomAction;
import com.apptogo.runalien.manager.CustomActionManager;
import com.apptogo.runalien.manager.ResourcesManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParallaxActor extends Actor {

	private OrthographicCamera camera;
	private TextureRegion tr;
		
	float swidth, width, height, uu, w2u, speed = 0.0001f;
	//TODO refactor + uzaleznienie predkosci od szybkosci playera
	public ParallaxActor(Camera camera, String textureRegionName) {
		this.camera = (OrthographicCamera)camera;
		this.tr = ResourcesManager.getInstance().getAtlasRegion(textureRegionName);
		setSize(0.01f,0.01f);
		
		width = 1280/64f;
		swidth = width;
		
		height = tr.getRegionHeight()/64f;
		uu = tr.getU();
		w2u = width / tr.getU2();
		
		startMoving();
	}
	
	
	
	
	private void startMoving() {

		CustomAction cloudMovingAction = new CustomAction(0, 0) {

			@Override
			public void perform() {
				uu += speed;
				width -= speed * w2u;
				
				if( uu >= tr.getU2())
				{
					uu = uu - tr.getU2();
					width = swidth - (uu * w2u);
				}
			}
		};

		CustomActionManager.getInstance().registerAction(cloudMovingAction);
	}
	
	
	
	@Override
	public void act(float delta) {
		setPosition(camera.position.x, camera.position.y + 2);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		tr.setU(uu);
		batch.draw(tr, getX() -640/64f, getY(), width, height);
		
		TextureRegion r = new TextureRegion(tr);
		r.setU(0);
		r.setU2(uu);
		batch.draw(r, getX() -640/64f + width, getY(), swidth - width, height);
	}
}
