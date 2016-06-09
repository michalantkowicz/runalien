package com.apptogo.runalien.screen;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.game.ImmaterialGameActor;
import com.apptogo.runalien.game.ParallaxActor;
import com.apptogo.runalien.game.ParticleEffectActor;
import com.apptogo.runalien.level.LevelGenerator;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.manager.CustomAction;
import com.apptogo.runalien.manager.CustomActionManager;
import com.apptogo.runalien.manager.ResourcesManager;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.physics.ContactListener;
import com.apptogo.runalien.plugin.CameraFollowingPlugin;
import com.apptogo.runalien.plugin.DeathPlugin;
import com.apptogo.runalien.plugin.RunningPlugin;
import com.apptogo.runalien.plugin.SoundPlugin;
import com.apptogo.runalien.plugin.TouchSteeringPlugin;
import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.scene2d.Button;
import com.apptogo.runalien.scene2d.Image;
import com.apptogo.runalien.scene2d.Label;
import com.apptogo.runalien.scene2d.Listener;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class GameScreen extends BasicScreen {	
	protected Box2DDebugRenderer debugRenderer;
	protected World world;
	protected Stage middleBackgroundStage;
	protected Stage gameworldStage;
	protected ContactListener contactListener = new ContactListener();
	protected LevelGenerator levelGenerator;
	protected GameActor player;
	protected ParallaxActor grass;
	protected Group finalScore;
	protected FPSLogger logger = new FPSLogger();
	protected int score = 0;
	protected Image sky_day, sky_night, sun, moon;
	protected Label scoreLabel;
	public Button tutorialButton, submitButton;
	
	protected ShaderProgram shaderProgram;
	protected float shaderBrightness, shaderSaturation;
	
	protected boolean endGame = false, gameFinished = false;
	public boolean LEFT = false, RIGHT = false;
	
	public GameScreen(Main game) {
		super(game);
		//TODO steps not disposed after going to menu 
		//TODO check the daytime changing
	}
	
	@Override
	protected void prepare() {		
		debugRenderer = new Box2DDebugRenderer();
		
		world = new World(new Vector2(0, -145), true);
		world.setContactListener(contactListener);
		
		boolean isDay = Gdx.app.getPreferences("SETTINGS").getBoolean("DAYTIME", false);
		
		createBackgroundStage(isDay);
		createMiddleBackgroundStage();
		createGameWorldStage();
		createStage();
		
		//prepare CustomActionManager
		gameworldStage.addActor(CustomActionManager.getInstance());
		
		//Shaders
		//TODO to check performance (and improve it)
		shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/vert.shd"), Gdx.files.internal("shaders/frag.shd"));
		
		shaderSaturation = isDay ? 1f : 0.5f;
		shaderBrightness = isDay ? 1f : 0.75f;
		
		shaderProgram.begin();
		shaderProgram.setUniformf("saturation", shaderSaturation);
		shaderProgram.setUniformf("brightness", shaderBrightness);
		shaderProgram.end();
		
		middleBackgroundStage.getBatch().setShader(shaderProgram);
		gameworldStage.getBatch().setShader(shaderProgram);
		
		//get isDay flag [t:day f:night] from Preferences and check whether it is time to change daytime
		if(Gdx.app.getPreferences("SETTINGS").getInteger("LASTDAYCHANGE", 0) + Main.DAYTIME_CHANGE_INTERVAL < TimeUtils.millis()/1000 ) {
			Gdx.app.getPreferences("SETTINGS").putBoolean("DAYTIME", !isDay).flush();
			Gdx.app.getPreferences("SETTINGS").putInteger("LASTDAYCHANGE", (int) (TimeUtils.millis()/1000)).flush();
		
			CustomActionManager.getInstance().registerAction(new CustomAction(0.5f){
				@Override
				public void perform() {
					toggleDaytime(Gdx.app.getPreferences("SETTINGS").getBoolean("DAYTIME", true));
				}
			});
		}
		
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
		
		player.addPlugin(new SoundPlugin("scream", "slide", "chargeDown", "land", "jump", "doubleJump", "run", "bell", "die"));
		player.addPlugin(new CameraFollowingPlugin());		
		player.addPlugin(new DeathPlugin());
		player.addPlugin(new RunningPlugin());
		player.addPlugin(new TouchSteeringPlugin());
		
		//create infinite ground body
		BodyBuilder.get().addFixture("ground").box(10000, 0.1f).position(5000 - 5, Main.GROUND_LEVEL - 0.05f).friction(0.1f).categoryBits(Main.GROUND_BITS).create();

		//create Parallaxes
		gameworldStage.addActor( ParallaxActor.get(gameworldStage.getCamera(), "spaceRubbish").setFixedSpeed(0.002f).moveToY(UnitConverter.toBox2dUnits(1090)) );
		gameworldStage.addActor( ParallaxActor.get(gameworldStage.getCamera(), "clouds").setFixedSpeed(0.004f).moveToY(2) );
		gameworldStage.addActor( ParallaxActor.get(gameworldStage.getCamera(), "wheat").moveToY(Main.GROUND_LEVEL-2.5f).setSpeedModifier(0.5f) );
		ParallaxActor ground = ParallaxActor.get(gameworldStage.getCamera(), "ground");
		gameworldStage.addActor( ground.moveToY(Main.GROUND_LEVEL - ground.getHeight()) );
		grass = ParallaxActor.get(gameworldStage.getCamera(), "grass").moveToY(Main.GROUND_LEVEL - 0.01f);
		gameworldStage.addActor( grass );
		
		tutorialButton = Button.get("tutorial").position(-600, -350).setListener(Listener.click(game, new TutorialScreen(game)));
		
		//Sign and tutorial button if not TutorialScreen instance
		if(!(this instanceof TutorialScreen)) {		
			stage.addActor(tutorialButton);
			
			ImmaterialGameActor sign = new ImmaterialGameActor("sign");
			sign.setStaticImage("board");
			sign.setPosition(7, Main.GROUND_LEVEL);
			gameworldStage.addActor(sign);
			
			Group topScore = createTopScore(String.valueOf(Gdx.app.getPreferences("SETTINGS").getLong("TOPSCORE")), 1/UnitConverter.PPM/1.5f);
			topScore.setPosition(sign.getX() + 1.6f - topScore.getWidth()/2f,  sign.getY() + 1.5f);
			gameworldStage.addActor(topScore);
		}
		
		//create obstacle generator
		levelGenerator = new LevelGenerator(player);
	}
	
	@Override
	protected void step(float delta) {
		//simulate physics and handle body contacts
		ContactListener.SNAPSHOT.clear();
		world.step(delta, 3, 3);
		
		//generate obstacles
		levelGenerator.generate();

		//update shader if need to
		if(shaderSaturation < 1 && shaderSaturation > 0.5f) {
			shaderProgram.begin();
			shaderProgram.setUniformf("saturation", shaderSaturation);
			shaderProgram.setUniformf("brightness", shaderBrightness);
			shaderProgram.end();
		}
		
		//act and draw main stage
		middleBackgroundStage.getViewport().apply();
		middleBackgroundStage.act(delta);
		middleBackgroundStage.draw();
		
		//act and draw main stage
		gameworldStage.getViewport().apply();
		gameworldStage.act(delta);
		gameworldStage.draw();
		
		//debug renderer
//		debugRenderer.render(world, gameworldStage.getCamera().combined);
		
		//make player always on top
		player.toFront();
		grass.toFront();
		
		if(endGame) {
			Main.gameCallback.setBannerVisible(true);
			
			//TODO Once I had PluginException here caused by ball hit - WHY?
			player.removePlugin("CameraFollowingPlugin");
			
			finalScore.setVisible(true);
			
			SoundPlugin.playSingleSound("whoosh");
			gameworldStage.addAction(Actions.sequence(Actions.moveBy(0, UnitConverter.toBox2dUnits(-850), 2.5f, Interpolation.pow5),
	                Actions.moveBy(0, UnitConverter.toBox2dUnits(-30), 60)));
			
			backgroundStage.addAction(Actions.sequence(Actions.moveBy(0, -850, 2.5f, Interpolation.pow5),
				       Actions.moveBy(0,  -30, 60)));
			
			middleBackgroundStage.addAction(Actions.sequence(Actions.moveBy(0, -850, 2.5f, Interpolation.pow5),
				       Actions.moveBy(0,  -30, 60)));
			
			stage.addAction(Actions.sequence(Actions.moveBy(0, -850, 2.5f, Interpolation.pow5),
			       Actions.moveBy(0,  -30, 60)));
			
			this.scoreLabel.addAction(Actions.fadeOut(1, Interpolation.pow5Out));
			
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
				
				submitButton.addAction(Actions.forever(Actions.sequence(
						                                           Actions.moveBy(0, 15, 0.05f),
						                                           Actions.moveBy(0, -30, 0.05f),
						                                           Actions.moveBy(0, 15, 0.5f, Interpolation.elasticOut),
						                                           Actions.delay(1))));
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
		this.middleBackgroundStage.getViewport().update(width, height);
    }

	@Override
	public void dispose() {
		super.dispose();
		debugRenderer.dispose();
		world.dispose();
		gameworldStage.dispose();
		Main.gameCallback.setBannerVisible(false);
		player.getPlugin(SoundPlugin.class).stopAllSounds();
	}
	
	void toggleDaytime(boolean isDay)
	{ 
		//setting up values for animation
		float sunXPos = -sun.getWidth()/2f;
		float moonXPos = -moon.getWidth()/2f;
		//Vector3 is more convenient than declaring 3 variables here
		Vector3 sunDelay = isDay ? new Vector3(0.35f, 0.25f, 0.1f) : new Vector3(0, 0.1f, 0.15f);
		Vector3 moonDelay = isDay ? new Vector3(0, 0.1f, 0.15f) : new Vector3(0.35f, 0.25f, 0.1f);
		float middlePos = 160;
		float sunFinalPos = isDay ? 140 : -400;
		float moonFinalPos = isDay ? -400 : 140;
		float skyDayAlpha = isDay ? 1 : 0;
		final float saturationStep = isDay ? 0.01f : -0.01f;
		final float brightnessStep = isDay ? 0.005f : -0.005f;
		final float saturationDest = isDay ? 1f : 0.5f;
		final float brightnessDest = isDay ? 1f : 0.75f;
		
		//applying animation
		sky_day.addAction(Actions.alpha(skyDayAlpha, 0.5f));
		sun.addAction(Actions.sequence(Actions.delay(sunDelay.x), Actions.moveTo(sunXPos, middlePos, sunDelay.y), Actions.moveTo(sunXPos, sunFinalPos, sunDelay.z)));
		moon.addAction(Actions.sequence(Actions.delay(moonDelay.x), Actions.moveTo(moonXPos, middlePos, moonDelay.y), Actions.moveTo(moonXPos, moonFinalPos, moonDelay.z)));
		CustomActionManager.getInstance().registerAction(new CustomAction(0, 50) {
			@Override
			public void perform() {
				shaderSaturation += saturationStep;
				shaderBrightness += brightnessStep;
				
				if(getLoopCount() == 50) {
					shaderSaturation = saturationDest;
					shaderBrightness = brightnessDest;
				};
			}
		});
	}

	protected void createBackgroundStage(boolean isDay) { 
		float sunPosition = isDay ? 140 : -400;
		float moonPosition = isDay ? -400 : 140;
		
		sun = Image.get("sun").position(0, sunPosition).centerX();
		backgroundStage.addActor(sun);
		sun.toBack();
		
		moon = Image.get("moon").position(0, moonPosition).centerX();
		backgroundStage.addActor(moon);
		moon.toBack();
		
		sky_day = Image.get("sky_day").width(Main.SCREEN_WIDTH).position(0, -Main.SCREEN_HEIGHT/2f).centerX();
		if(!isDay){
			sky_day.addAction(Actions.alpha(0));
		}
		backgroundStage.addActor(sky_day);
		sky_day.toBack();
		
		sky_night = Image.get("sky_night").width(Main.SCREEN_WIDTH).position(0, -Main.SCREEN_HEIGHT/2f).centerX();
		backgroundStage.addActor(sky_night);
		sky_night.toBack();
	}
	
	protected void createMiddleBackgroundStage() {
		middleBackgroundStage = new Stage(new FillViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT));
		middleBackgroundStage.getCamera().position.set(0, 0, 0);
		
		middleBackgroundStage.addActor(Image.get("background_game").centerX().centerY());
	}
	
	protected void createGameWorldStage() {
		gameworldStage = new Stage();
		gameworldStage.setViewport(new FillViewport(UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT)));
		//((OrthographicCamera) gameworldStage.getCamera()).setToOrtho(false, UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT));
		//((OrthographicCamera) gameworldStage.getCamera()).zoom = 1f;
	}
	
	protected void createStage() {
		stage.addActor(Button.get("menu").position(0,  Main.SCREEN_HEIGHT/2f + 540).centerX().setListener(Listener.click(game, new MenuScreen(game))));
		
		submitButton = Button.get("submit").position(0,  Main.SCREEN_HEIGHT/2f + 390).centerX()
				             .setListener(new ClickListener(){
			                     @Override
									 public void clicked(InputEvent event, float x, float y) {
										 Main.gameCallback.submitScore(score);
									 }
				             });
		
		stage.addActor(submitButton);
		
		stage.addActor(Button.get("replay").position(0,  Main.SCREEN_HEIGHT/2f + 240).centerX().setListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Main.gameCallback.showFullscreenAd();
			}
		}));
		
		finalScore = new Group();
		finalScore.setOrigin(Align.center);
		finalScore.addAction(Actions.rotateBy(-1));
		finalScore.addAction(Actions.forever(Actions.sequence(Actions.moveBy(0, 40, 4), Actions.moveBy(0, -40, 6))));
		finalScore.addAction(Actions.forever(Actions.sequence(Actions.rotateBy(2, 2), Actions.rotateBy(-2, 2))));
		finalScore.setPosition(-420,  Main.SCREEN_HEIGHT);
		
		finalScore.addActor(Image.get("balloon").centerX().centerY());
		finalScore.setVisible(false);
		stage.addActor(finalScore);
		
		scoreLabel = Label.get("0", "tutorial").position(-600, 320);
		stage.addActor(scoreLabel);
		
		stage.addListener(new ClickListener(){ 
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(x <= 0)
					LEFT = true;
				else
					RIGHT = true;
				return super.touchDown(event, x, y, pointer, button);
			}
		});
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

	public void removeTutorialButton() {
		if(this.tutorialButton != null) {
			this.tutorialButton.clearListeners();
			this.tutorialButton.addAction(Actions.fadeOut(1, Interpolation.pow5Out));
		}
	}
}
