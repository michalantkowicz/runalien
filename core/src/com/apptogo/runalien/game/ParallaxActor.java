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
	private TextureRegion textureRegion;

	//TODO DODAC MOZLIWOSC USTAWIANIA PREDKOSCI W ZALEZNOSCI OD PREDKOSCI KAMERY!
	private float speedX = 0.0001f, speedY;

	/**
	 * Backuped start value of U2 coordinate (hold for reset feature)
	 */
	private float startU2;

	/**
	 * U and U2 coordinates for parallaxed part of region
	 */
	private float parallaxU, parallaxU2;

	public ParallaxActor(Camera camera, String textureRegionName) {

		this.camera = (OrthographicCamera) camera;

		this.textureRegion = ResourcesManager.getInstance().getAtlasRegion(textureRegionName);
		startU2 = textureRegion.getU2();
		parallaxU = -startU2;

		startMoving();
	}

	/**
	 * If current region's U2 coordinate is geq to start U2 value that means that 
	 * one full loop has passed and we need to reset coordinates to start 
	 * scrolling from beginning. Other way we increment coordinates
	 * to scroll region with proper speed 
	 */
	private void startMoving() {

		CustomAction cloudMovingAction = new CustomAction(0.05f, 0) {

			@Override
			public void perform() {
				if (textureRegion.getU2() >= 2 * startU2) {

					textureRegion.setU(0);
					textureRegion.setU2(startU2);

					parallaxU = -startU2;
					parallaxU2 = 0;
				} else {
					textureRegion.setU(textureRegion.getU() + speedX);
					textureRegion.setU2(textureRegion.getU2() + speedX);

					parallaxU += speedX;
					parallaxU2 += speedX;
				}
			}
		};

		CustomActionManager.getInstance().registerAction(cloudMovingAction);
	}

	@Override
	public void act(float delta) {
		setPosition(camera.position.x - getWidth() / 2f, getY());
	}

	/**
	 * We are drawing the region with set up U and U2 coordinates which renders
	 * left side of the region. Then we are changing temporarily coordinates
	 * to render right side of looped graphic (and not to create next region).
	 * At the and we are setting again current U and U2 coordinates to region.
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());

		float tempU = textureRegion.getU();
		float tempU2 = textureRegion.getU2();

		textureRegion.setU(parallaxU);
		textureRegion.setU2(parallaxU2);

		batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());

		textureRegion.setU(tempU);
		textureRegion.setU2(tempU2);
	}
}
