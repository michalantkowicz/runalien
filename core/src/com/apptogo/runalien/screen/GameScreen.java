package com.apptogo.runalien.screen;

import com.apptogo.runalien.game.GameAnimatedActor;
import com.apptogo.runalien.game.GameStaticActor;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class GameScreen extends BasicScreen {
	private static Box2DDebugRenderer debugRenderer;
	private static World world;
	private static Stage gameworldStage;

	public GameScreen(Main game) {
		super(game);
		debugRenderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0, -5), true);
		createGameWorldStage();
	}

	@Override
	void prepare() {
		GameAnimatedActor player = new GameAnimatedActor("player");
		player.createBoxBody(BodyType.DynamicBody, new Vector2(0.4f, 1));
		player.setTransform(5, 5, 0);
		player.setGraphicOffset(new Vector2(0.4f, 0f));
		gameworldStage.addActor(player);
		
		GameStaticActor ground = new GameStaticActor("ground");
		ground.createBoxBody(BodyType.StaticBody, new Vector2(10f, 1f));
		ground.setTransform(10, 2, 0);
		gameworldStage.addActor(ground);
	}

	@Override
	void step(float delta) {
		world.step(delta, 3, 3);
		
		gameworldStage.act();
		gameworldStage.draw();

		debugRenderer.render(world, gameworldStage.getCamera().combined);
	}

	@Override
	public void dispose() {
		super.dispose();
		debugRenderer = null;
		world = null;
		gameworldStage = null;
	}

	private void createGameWorldStage() {
		gameworldStage = new Stage();
		gameworldStage.setViewport(new FillViewport(UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT)));
		((OrthographicCamera) gameworldStage.getCamera()).setToOrtho(false, UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT));
		((OrthographicCamera) gameworldStage.getCamera()).zoom = 1f;
	}

	// ------ COMMONS ------//
	public static World getWorld() {
		return world;
	}

	public static Stage getGameworldStage() {
		return gameworldStage;
	}

}
