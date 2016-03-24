package com.apptogo.runalien.screen;

import java.util.HashMap;
import java.util.Map;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.game.ImmaterialGameActor;
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
import com.apptogo.runalien.plugin.TouchSteeringPlugin;
import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.scene2d.Button;
import com.apptogo.runalien.scene2d.Image;
import com.apptogo.runalien.scene2d.Listener;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class GameScreen extends BasicScreen {
	protected Box2DDebugRenderer debugRenderer;
	protected World world;
	protected Stage gameworldStage;
	protected ObstaclesPool obstaclesPool;
	protected Map<String, String> contactsSnapshot = new HashMap<String, String>();
	protected ContactListener contactListener = new ContactListener();
	protected LevelGenerator levelGenerator;
	protected GameActor player;
	ParallaxActor grass;
	
	protected boolean endGame = false;
	
	public GameScreen(Main game) {
		super(game, "background_game");
	}
	
	@Override
	void prepare() {
		debugRenderer = new Box2DDebugRenderer();

		world = new World(new Vector2(0, -130), true);
		world.setContactListener(contactListener);
		createGameWorldStage();
		stagesToFade.add(gameworldStage);
		
		stage.addActor(Image.get("space").size(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT).position(0, Main.SCREEN_HEIGHT/2f).centerX());
		stage.addActor(Button.get("menu").position(0,  Main.SCREEN_HEIGHT/2f + 300).centerX().setListener(Listener.click(game, new MenuScreen(game))));
		stage.addActor(Button.get("submit").position(0,  Main.SCREEN_HEIGHT/2f + 150).centerX());
		stage.addActor(Button.get("replay").position(0,  Main.SCREEN_HEIGHT/2f).centerX().setListener(Listener.click(game, new GameScreen(game))));
		
		//prepare CustomActionManager
		gameworldStage.addActor(CustomActionManager.getInstance());

		//create player
		player = new GameActor("player");
		player.setAvailableAnimations("diebottom", "dietop", "jump", "land", "slide", "standup", "startrunning");
		player.addAvailableAnimation(Animation.get(0.03f, "run", PlayMode.LOOP));
		player.addAvailableAnimation(Animation.get(0.04f, "idle", PlayMode.LOOP));
		player.queueAnimation("idle");

		player.setBody(BodyBuilder.get().type(BodyType.DynamicBody).position(0, Main.GROUND_LEVEL).fixedRotation(true)
				                        .addFixture("player").box(0.6f, 1.9f).friction(0.1f)
				                        .addFixture("player_sliding").box(1.9f, 0.6f, -0.65f, -0.65f).sensor(true).ignore(true).friction(0.1f)
				                        .create());
		
		player.modifyCustomOffsets(-0.4f, 0f);
		gameworldStage.addActor(player);
		
		//player.addPlugin(new SoundPlugin("scream", "slide", "chargeDown", "land", "jump", "doubleJump", "run"));
		player.addPlugin(new CameraFollowingPlugin());		
		player.addPlugin(new DeathPlugin(this));
		player.addPlugin(new RunningPlugin());
		player.addPlugin(new TouchSteeringPlugin());
		
		//create infinite ground body
		//TODO should be polyline
		BodyBuilder.get().addFixture("ground").box(10000, 0.1f).position(5000 - 5, Main.GROUND_LEVEL - 0.05f).friction(0.1f).create();

		//create obstacle pool
		obstaclesPool = new ObstaclesPool();
		
		//create obstacle generator
		levelGenerator = new LevelGenerator(player);

		//create Parallaxes
		ParallaxActor ground = ParallaxActor.get(gameworldStage.getCamera(), "ground");
		gameworldStage.addActor( ParallaxActor.get(gameworldStage.getCamera(), "clouds").setFixedSpeed(0.004f).moveToY(2) );
		gameworldStage.addActor( ParallaxActor.get(gameworldStage.getCamera(), "wheat").moveToY(Main.GROUND_LEVEL-2.5f).setSpeedModifier(0.5f) );
		gameworldStage.addActor( ground.moveToY(Main.GROUND_LEVEL - ground.getHeight()) );
		grass = ParallaxActor.get(gameworldStage.getCamera(), "grass").moveToY(Main.GROUND_LEVEL);
		gameworldStage.addActor( grass );
		
		ImmaterialGameActor sign = new ImmaterialGameActor("sign");
		sign.setStaticImage("board");
		sign.setPosition(7, Main.GROUND_LEVEL);
		gameworldStage.addActor(sign);
		
//		float x = 10, y = 6, ropeHeight = 7.5f;
//		
//		Body spikeBall = BodyBuilder.get().type(BodyType.DynamicBody).addFixture("SpikeBall").circle(1).friction(0.5f).position(x + ropeHeight, y).create();
//		Body anchor = BodyBuilder.get().type(BodyType.StaticBody).addFixture("AnchorBall").box(0.2f, 0.2f).friction(0.5f).position(x, y).sensor(true).create();
//		Body rope = BodyBuilder.get().type(BodyType.DynamicBody).addFixture("RopeBall").box(ropeHeight, 0.1f).sensor(true).position(x + ropeHeight/2f, y).create();
//		
//		
//		RevoluteJointDef jointDef = new RevoluteJointDef();
//		jointDef.localAnchorB.set(ropeHeight/2f, 0);
//		jointDef.collideConnected = false;
//		jointDef.bodyA = anchor;
//		jointDef.bodyB = rope;
//		
//		world.createJoint(jointDef);
//		
//		jointDef.localAnchorB.set(-ropeHeight/2f, 0);
//		jointDef.collideConnected = false;
//		jointDef.bodyA = spikeBall;
//		jointDef.bodyB = rope;
//		
//		world.createJoint(jointDef);
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
		//levelGenerator.generate();

		//act and draw main stage
		gameworldStage.act(delta);
		gameworldStage.draw();

		//debug renderer
		debugRenderer.render(world, gameworldStage.getCamera().combined);
		
		//make player always on top
		player.toFront();
		grass.toFront();
		
		if(endGame) {
			player.removePlugin("CameraFollowingPlugin");
			
			gameworldStage.addAction(Actions.sequence(Actions.moveBy(0, UnitConverter.toBox2dUnits(-600), 3, Interpolation.pow5),
	                Actions.moveBy(0, UnitConverter.toBox2dUnits(-100), 60)));
			
			stage.addAction(Actions.sequence(Actions.moveBy(0, -600, 3, Interpolation.pow5),
			       Actions.moveBy(0,  -100, 60)));
			
			endGame = false;
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		
		debugRenderer.dispose();
		world.dispose();
		gameworldStage.dispose();
	}

	protected void createGameWorldStage() {
		gameworldStage = new Stage();
		gameworldStage.setViewport(new FillViewport(UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT)));
		((OrthographicCamera) gameworldStage.getCamera()).setToOrtho(false, UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT));
		((OrthographicCamera) gameworldStage.getCamera()).zoom = 1f;
	}

	/**------ GETTERS / SETTERS ------**/
	public void setEndGame(boolean state) {
		endGame = state;
	}
	
	public World getWorld() {
		return world;
	}

	public Stage getGameworldStage() {
		return gameworldStage;
	}

	public ObstaclesPool getObstaclesPool() {
		return obstaclesPool;
	}
	
	public Map<String, String> getContactsSnapshot() {
		return contactsSnapshot;
	}
}
