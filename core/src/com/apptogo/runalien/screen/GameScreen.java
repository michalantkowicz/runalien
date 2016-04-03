package com.apptogo.runalien.screen;

import java.util.HashMap;
import java.util.Map;

import javax.swing.GroupLayout.Alignment;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.game.ImmaterialGameActor;
import com.apptogo.runalien.game.ParallaxActor;
import com.apptogo.runalien.game.ParticleEffectActor;
import com.apptogo.runalien.level.LevelGenerator;
import com.apptogo.runalien.level.ObstaclesPool;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.manager.CustomActionManager;
import com.apptogo.runalien.manager.ResourcesManager;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.physics.ContactListener;
import com.apptogo.runalien.plugin.CameraFollowingPlugin;
import com.apptogo.runalien.plugin.DeathPlugin;
import com.apptogo.runalien.plugin.RunningPlugin;
import com.apptogo.runalien.plugin.TouchSteeringPlugin;
import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.scene2d.Button;
import com.apptogo.runalien.scene2d.Image;
import com.apptogo.runalien.scene2d.Label;
import com.apptogo.runalien.scene2d.Listener;
import com.apptogo.runalien.scene2d.TextButton;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

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
	Group finalScore;
	FPSLogger logger = new FPSLogger();
	int score = 0;
	protected Label scoreLabel;
	
	protected TextButton tutorialButton;
	
	protected boolean endGame = false, gameFinished = false;
	
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
		
		stage.addActor(Image.get("space").width(Main.SCREEN_WIDTH).position(0, Main.SCREEN_HEIGHT/2f).centerX());
		stage.addActor(Button.get("menu").position(0,  Main.SCREEN_HEIGHT/2f + 500).centerX().setListener(Listener.click(game, new MenuScreen(game))));
		stage.addActor(Button.get("submit").position(0,  Main.SCREEN_HEIGHT/2f + 350).centerX());
		stage.addActor(Button.get("replay").position(0,  Main.SCREEN_HEIGHT/2f + 200).centerX().setListener(Listener.click(game, new GameScreen(game))));
		
		finalScore = new Group();
		finalScore.setOrigin(Align.center);
		finalScore.addAction(Actions.rotateBy(-1));
		finalScore.addAction(Actions.forever(Actions.sequence(Actions.moveBy(0, 40, 4), Actions.moveBy(0, -40, 6))));
		finalScore.addAction(Actions.forever(Actions.sequence(Actions.rotateBy(2, 2), Actions.rotateBy(-2, 2))));
		finalScore.setPosition(-420,  Main.SCREEN_HEIGHT);
		
		finalScore.addActor(Image.get("balloon").centerX().centerY());
		finalScore.setVisible(false);
		stage.addActor(finalScore);		
		
		//prepare CustomActionManager
		gameworldStage.addActor(CustomActionManager.getInstance());

		//create player
		player = new GameActor("player");
		player.setAvailableAnimations("diebottom", "dietop", "jump", "land", "slide", "standup", "startrunning");
		player.addAvailableAnimation(Animation.get(0.03f, "run", PlayMode.LOOP));
		player.addAvailableAnimation(Animation.get(0.04f, "idle", PlayMode.LOOP));
		player.queueAnimation("idle");

		player.setBody(BodyBuilder.get().type(BodyType.DynamicBody).position(0, Main.GROUND_LEVEL).fixedRotation(true)
				                        .addFixture("player").box(0.6f, 1.9f).friction(0.5f)
				                        .addFixture("player", "sliding").box(1.9f, 0.6f, -0.65f, -0.65f).sensor(true).ignore(true).friction(0.5f)
				                        .create());
		
		player.modifyCustomOffsets(-0.4f, 0f);
		gameworldStage.addActor(player);
		
		//player.addPlugin(new SoundPlugin("scream", "slide", "chargeDown", "land", "jump", "doubleJump", "run"));
		player.addPlugin(new CameraFollowingPlugin());		
		player.addPlugin(new DeathPlugin());
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
		gameworldStage.addActor( ParallaxActor.get(gameworldStage.getCamera(), "spaceRubbish").setFixedSpeed(0.002f).moveToY(UnitConverter.toBox2dUnits(1050)) );
		gameworldStage.addActor( ParallaxActor.get(gameworldStage.getCamera(), "clouds").setFixedSpeed(0.004f).moveToY(2) );
		gameworldStage.addActor( ParallaxActor.get(gameworldStage.getCamera(), "wheat").moveToY(Main.GROUND_LEVEL-2.5f).setSpeedModifier(0.5f) );
		ParallaxActor ground = ParallaxActor.get(gameworldStage.getCamera(), "ground");
		gameworldStage.addActor( ground.moveToY(Main.GROUND_LEVEL - ground.getHeight()) );
		grass = ParallaxActor.get(gameworldStage.getCamera(), "grass").moveToY(Main.GROUND_LEVEL);
		gameworldStage.addActor( grass );
		
		//Sign and tutorial button if not TutorialScreen instance
		if(!(this instanceof TutorialScreen)) {
			tutorialButton = TextButton.get("TUTORIAL").position(-620, -390).setListener(Listener.click(game, new TutorialScreen(game)));
			stage.addActor(tutorialButton);
			
			gameworldStage.addActor(Image.get("buttonTutorial").scale(1/UnitConverter.PPM).position(-4.2f, Main.GROUND_LEVEL - 2));
							
			ImmaterialGameActor sign = new ImmaterialGameActor("sign");
			sign.setStaticImage("board");
			sign.setPosition(7, Main.GROUND_LEVEL);
			gameworldStage.addActor(sign);
			
			Group topScore = createTopScore(String.valueOf(Gdx.app.getPreferences("SETTINGS").getLong("TOPSCORE")), 1/UnitConverter.PPM/1.5f);
			topScore.setPosition(sign.getX() + 1.6f - topScore.getWidth()/2f,  sign.getY() + 1.5f);
			gameworldStage.addActor(topScore);
		}
		
		scoreLabel = Label.get("0", "tutorial").position(-600, 320);
		stage.addActor(scoreLabel);
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
		grass.toFront();
		
		if(endGame) {
			player.removePlugin("CameraFollowingPlugin");
			
			finalScore.setVisible(true);
			
			gameworldStage.addAction(Actions.sequence(Actions.moveBy(0, UnitConverter.toBox2dUnits(-850), 2.5f, Interpolation.pow5),
	                Actions.moveBy(0, UnitConverter.toBox2dUnits(-30), 60)));
			
			stage.addAction(Actions.sequence(Actions.moveBy(0, -850, 2.5f, Interpolation.pow5),
			       Actions.moveBy(0,  -30, 60)));
			
			
			Group finalScoreLabel = createTopScore(String.valueOf(score), 1);
			finalScoreLabel.setPosition(-finalScoreLabel.getWidth()/2f, 10);
			finalScore.addActor(finalScoreLabel);
			
			if(score > Gdx.app.getPreferences("SETTINGS").getLong("TOPSCORE")){
				Gdx.app.getPreferences("SETTINGS").putLong("TOPSCORE", score).flush();
			
				//add extras to balloon
				finalScore.addActor(Image.get("newTop").position(0, -260).centerX());
				
				ParticleEffectActor stars = new ParticleEffectActor("losecoins.p", 1, 1, 1, 1, (TextureAtlas)ResourcesManager.getInstance().get("menu_atlas.pack"));
				stars.obtainAndStart(0, -80, 0);
				finalScore.addActor(stars);
				stars.toBack();
			}
			
			endGame = false;
			gameFinished = true;
		}
		else if(!gameFinished){
			score = (int) (player.getBody().getPosition().x / 10);
			scoreLabel.setText("score: " + String.valueOf(score));
		}
	}
	
	@Override
    public void resize(int width, int height) {
		super.resize(width, height);
        this.gameworldStage.getViewport().update(width, height);
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
		gameworldStage.setViewport(new FitViewport(UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT)));
		((OrthographicCamera) gameworldStage.getCamera()).setToOrtho(false, UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT));
		((OrthographicCamera) gameworldStage.getCamera()).zoom = 1f;
	}
	
	protected Group createTopScore(String score, float scale) {
		Group scoreGroup = new Group();
		scoreGroup.setTransform(false);
		
		for(String digit : score.split("")) {
			if(!digit.isEmpty()){
				Image digitImage = Image.get(digit).position(scoreGroup.getWidth(), 0).scale(scale);
				scoreGroup.addActor(digitImage);
				scoreGroup.setWidth(scoreGroup.getWidth() + digitImage.getWidth() + 2 * scale);
				scoreGroup.setHeight(Math.max(scoreGroup.getHeight(), digitImage.getHeight()));
			}
		}
		
		return scoreGroup;
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

	public void removeTutorialButton() {
		if(this.tutorialButton != null) {
			this.tutorialButton.remove();
		}
	}
}
