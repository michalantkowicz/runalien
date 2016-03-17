package com.apptogo.runalien.screen;

import java.util.HashMap;
import java.util.Map;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.game.ParallaxActor;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.manager.CustomActionManager;
import com.apptogo.runalien.obstacle.ObstacleGenerator;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.physics.ContactListener;
import com.apptogo.runalien.plugin.CameraFollowingPlugin;
import com.apptogo.runalien.plugin.DeathPlugin;
import com.apptogo.runalien.plugin.RunningPlugin;
import com.apptogo.runalien.plugin.SoundPlugin;
import com.apptogo.runalien.plugin.TouchSteeringPlugin;
import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;

//TODO implement SINGLETON pattern!
public class GameScreen extends BasicScreen {
	private final static float GROUND_LEVEL = -3.2f;

	private static Box2DDebugRenderer debugRenderer;
	private static World world;
	private static Stage gameworldStage;

	private ContactListener contactListener = new ContactListener();
	private ObstacleGenerator obstacleGenerator;
	public static Map<String, String> contactsSnapshot = new HashMap<String, String>();

	public GameScreen(Main game) {
		super(game);
		debugRenderer = new Box2DDebugRenderer();
		debugRenderer.SHAPE_AWAKE.set(Color.PINK);

		world = new World(new Vector2(0, -100), true);
		world.setContactListener(contactListener);
		createGameWorldStage();
	}

	@Override
	void prepare() {

		//prepare CustomActionManager
		gameworldStage.addActor(CustomActionManager.getInstance());

		//create player
		GameActor player = new GameActor("player");
		player.setAvailableAnimations("diebottom", "dietop", "jump", "land", "slide", "standup", "startrunning");
		player.addAvailableAnimation(Animation.get(0.03f, "run", PlayMode.LOOP));
		player.addAvailableAnimation(Animation.get(0.04f, "idle", PlayMode.LOOP));
		player.queueAnimation("idle");

		player.setBody(BodyBuilder.get().type(BodyType.DynamicBody).position(0, getGroundLevel() + 1)
				                        .addFixture("player").box(0.6f, 1.9f).friction(0)
				                        .addFixture("player_sliding").box(1.9f, 0.6f, -0.65f, -0.65f).sensor(true).ignore(true).friction(0)
				                        .create());
		
		player.modifyCustomOffsets(-0.4f, 0f);
		gameworldStage.addActor(player);

		player.addPlugin(new SoundPlugin("scream", "slide", "chargeDown", "land", "jump", "doubleJump", "run"));
		player.addPlugin(new DeathPlugin());
		player.addPlugin(new RunningPlugin());
		player.addPlugin(new CameraFollowingPlugin());
		player.addPlugin(new TouchSteeringPlugin());

		//create infinite ground body
		//TODO should be polyline
		BodyBuilder.get().addFixture("ground").box(10000, 0.1f).position(5000 - 5, getGroundLevel() - 0.2f).create();

		//create obstacle generator
		obstacleGenerator = new ObstacleGenerator(player);

		//create clouds
		ParallaxActor clouds = new ParallaxActor(gameworldStage.getCamera(), "clouds");
		clouds.debug();
		//clouds.setSize(1280 / UnitConverter.PPM, 200 / UnitConverter.PPM);
		//clouds.setPosition(-640 / UnitConverter.PPM, 100 / UnitConverter.PPM);
		gameworldStage.addActor(clouds);
	}

	@Override
	void step(float delta) {

		//simulate physics and handle body contacts
		contactListener.contacts.clear();
		world.step(delta, 3, 3);
		contactsSnapshot = contactListener.contacts;

		//generate obstacles
		obstacleGenerator.generate();

		//act and draw main stage
		gameworldStage.act();
		gameworldStage.draw();

		//debug renderer
		debugRenderer.render(world, gameworldStage.getCamera().combined);
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
	}

	private void createGameWorldStage() {
		gameworldStage = new Stage();
		gameworldStage.setViewport(new FillViewport(UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT)));
		((OrthographicCamera) gameworldStage.getCamera()).setToOrtho(false, UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT));
		((OrthographicCamera) gameworldStage.getCamera()).zoom = 1f;
	}

	/** ------ COMMONS ------ **/
	public static World getWorld() {
		return world;
	}

	public static Stage getGameworldStage() {
		return gameworldStage;
	}

	public static float getGroundLevel() {
		return GROUND_LEVEL;
	}
}
