package com.apptogo.runalien.screen;

import java.util.HashMap;
import java.util.Map;

import com.apptogo.runalien.exception.ScreenException;
import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.game.ParallaxActor;
import com.apptogo.runalien.level.LevelGenerator;
import com.apptogo.runalien.level.ObstaclesPool;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.manager.CustomActionManager;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.physics.ContactListener;
import com.apptogo.runalien.plugin.CameraFollowingPlugin;
import com.apptogo.runalien.plugin.DeathPlugin;
import com.apptogo.runalien.plugin.RunningPlugin;
import com.apptogo.runalien.plugin.SoundPlugin;
import com.apptogo.runalien.plugin.TouchSteeringPlugin;
import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.scene2d.Image;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FillViewport;

//TODO implement SINGLETON pattern!
public class GameScreen extends BasicScreen {
	
	private static GameScreen INSTANCE = null;
	public static GameScreen getInstance() {
		if(INSTANCE == null)
			throw new ScreenException("GameScreen hasn't been instantiated yet!");
		else
			return INSTANCE;
	}
		
	private static Box2DDebugRenderer debugRenderer;
	private static World world;
	private static Stage gameworldStage;
	private static ObstaclesPool obstaclesPool;
	public static Map<String, String> contactsSnapshot = new HashMap<String, String>();
	
	private ContactListener contactListener = new ContactListener();
	private LevelGenerator levelGenerator;
	private GameActor player;
	
	public GameScreen(Main game) {
		super(game, "background_game");
		debugRenderer = new Box2DDebugRenderer();
		debugRenderer.SHAPE_AWAKE.set(Color.PINK);

		world = new World(new Vector2(0, -130), true);
		world.setContactListener(contactListener);
		createGameWorldStage();
		
		INSTANCE = this;
	}
	
	@Override
	void prepare() {
		stage.addActor(Image.get("space").size(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT).position(0, Main.SCREEN_HEIGHT/2f).centerX());
		stage.addActor(Image.get("menu").position(0,  Main.SCREEN_HEIGHT/2f + 300).centerX());
		stage.addActor(Image.get("submit").position(0,  Main.SCREEN_HEIGHT/2f + 150).centerX());
		stage.addActor(Image.get("replay").position(0,  Main.SCREEN_HEIGHT/2f).centerX());
		
		//prepare CustomActionManager
		gameworldStage.addActor(CustomActionManager.getInstance());

		//create player
		player = new GameActor("player");
		player.setAvailableAnimations("diebottom", "dietop", "jump", "land", "slide", "standup", "startrunning");
		player.addAvailableAnimation(Animation.get(0.03f, "run", PlayMode.LOOP));
		player.addAvailableAnimation(Animation.get(0.04f, "idle", PlayMode.LOOP));
		player.queueAnimation("idle");

		player.setBody(BodyBuilder.get().type(BodyType.DynamicBody).position(0, Main.GROUND_LEVEL)
				                        .addFixture("player").box(0.6f, 1.9f).friction(0.1f)
				                        .addFixture("player_sliding").box(1.9f, 0.6f, -0.65f, -0.65f).sensor(true).ignore(true).friction(0.1f)
				                        .create());
		
		player.modifyCustomOffsets(-0.4f, 0f);
		gameworldStage.addActor(player);
		
		player.addPlugin(new SoundPlugin("scream", "slide", "chargeDown", "land", "jump", "doubleJump", "run"));
		player.addPlugin(new CameraFollowingPlugin());		
		player.addPlugin(new DeathPlugin(this));
		player.addPlugin(new RunningPlugin());
		player.addPlugin(new TouchSteeringPlugin());
		
		//create infinite ground body
		//TODO should be polyline
		BodyBuilder.get().addFixture("ground").box(10000, 0.1f).position(5000 - 5, Main.GROUND_LEVEL - 0.4f).friction(0.1f).create();

		//create obstacle pool
		obstaclesPool = new ObstaclesPool();
		
		//create obstacle generator
		levelGenerator = new LevelGenerator(player);

		//create Parallaxes
		gameworldStage.addActor( ParallaxActor.get(gameworldStage.getCamera(), "clouds").setFixedSpeed(0.001f).moveToY(2) );
		gameworldStage.addActor( ParallaxActor.get(gameworldStage.getCamera(), "wheat").moveToY(Main.GROUND_LEVEL-0.5f).setSpeedModifier(0.5f) );
		gameworldStage.addActor( ParallaxActor.get(gameworldStage.getCamera(), "wheat").moveToY(Main.GROUND_LEVEL-0.5f).setSpeedModifier(0.5f) );
	}
	
	@Override
	void step(float delta) {
		//simulate physics and handle body contacts
		contactListener.contacts.clear();
		world.step(delta, 3, 3);
		contactsSnapshot = contactListener.contacts;

		//update pools
		obstaclesPool.freePools();
		
		//generate obstacles
		levelGenerator.generate();

		//act and draw main stage
		gameworldStage.act(delta);
		gameworldStage.draw();

		//debug renderer
		debugRenderer.render(world, gameworldStage.getCamera().combined);
		
		//make player always on top
		player.toFront();
	}

	@Override
	public void dispose() {
		super.dispose();

		debugRenderer.dispose();
		debugRenderer = null;

		world.dispose();
		world = null;

		gameworldStage.dispose();
		gameworldStage = null;
		
		obstaclesPool = null;
	}

	private void createGameWorldStage() {
		gameworldStage = new Stage();
		gameworldStage.setViewport(new FillViewport(UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT)));
		((OrthographicCamera) gameworldStage.getCamera()).setToOrtho(false, UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT));
		((OrthographicCamera) gameworldStage.getCamera()).zoom = 1f;
	}

	/** ------ FOR INSTANCE ------**/
	public void endGame() {
		player.removePlugin("CameraFollowingPlugin");
		
		gameworldStage.addAction(Actions.sequence(Actions.moveBy(0, UnitConverter.toBox2dUnits(-600), 3, Interpolation.pow5),
                Actions.moveBy(0, UnitConverter.toBox2dUnits(-100), 60)));
		
		stage.addAction(Actions.sequence(Actions.moveBy(0, -600, 3, Interpolation.pow5),
		       Actions.moveBy(0,  -100, 60)));
	}
	
	/** ------ COMMONS ------ **/
	public static World getWorld() {
		return world;
	}

	public static Stage getGameworldStage() {
		return gameworldStage;
	}

	public static ObstaclesPool getObstaclesPool() {
		return obstaclesPool;
	}
}
