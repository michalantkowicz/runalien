package com.apptogo.runalien.screen;

import java.util.HashMap;
import java.util.Map;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.game.ImmaterialGameActor;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.physics.ContactListener;
import com.apptogo.runalien.plugin.CameraFollowing;
import com.apptogo.runalien.plugin.DeathPlugin;
import com.apptogo.runalien.plugin.Running;
import com.apptogo.runalien.plugin.SoundHandler;
import com.apptogo.runalien.plugin.TouchSteering;
import com.apptogo.runalien.procedures.RepeatProcedure;
import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;


//implement SINGLETON pattern!
public class GameScreen extends BasicScreen {
    private static Box2DDebugRenderer debugRenderer;
    private static World world;
    private static Stage gameworldStage;
    private final static float GROUND_LEVEL = -3.2f;
    
    public static Map<String, String> contactsSnapshot = new HashMap<String, String>();
    
	ContactListener contactListener = new ContactListener();

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

        GameActor player = new GameActor("player");
        player.setAvailableAnimations("diebottom", "dietop", "jump", "land", "slide", "standup", "startrunning");
        player.addAvailableAnimation(Animation.get(0.03f, "run", PlayMode.LOOP));
        player.addAvailableAnimation(Animation.get(0.04f, "idle", PlayMode.LOOP));
        player.queueAnimation("idle");
        
        player.setBody(BodyBuilder.get().type(BodyType.DynamicBody).position(0, getGroundLevel() + 1)
        		.addFixture("player").box(0.6f,  1.9f).friction(0)
        		.addFixture("player_sliding").box(1.9f,  0.6f, -0.65f, -0.65f).sensor(true).ignore(true).friction(0).create());
        
        player.modifyCustomOffsets(-0.4f, 0f);
        gameworldStage.addActor(player);
        
        player.addPlugin(new SoundHandler("scream", "slide", "chargeDown", "land", "jump", "doubleJump", "run"));
        player.addPlugin(new DeathPlugin());
        player.addPlugin(new Running());
        player.addPlugin(new CameraFollowing());
        //player.addPlugin(new GroundRepeating());
        player.addPlugin(new TouchSteering());
        
        BodyBuilder.get().addFixture("ground").box(10000, 0.1f).position(5000 - 5, getGroundLevel() - 0.2f).create();
        
        ImmaterialGameActor clouds = new ImmaterialGameActor("clouds");
        gameworldStage.addActor(clouds);
        clouds.setAvailableAnimations("clouds");
        clouds.queueAnimation("clouds");
        clouds.setPosition(UnitConverter.toBox2dUnits(-600), UnitConverter.toBox2dUnits(200));
        clouds.setSize(UnitConverter.toBox2dUnits(1280), UnitConverter.toBox2dUnits(200));
        clouds.addProcedure(new RepeatProcedure("clouds"));
        
        
        //TEMPORARY CREATED OBSTACLES
        for(int i = 0; i < 10; i++)
        	BodyBuilder.get().type(BodyType.StaticBody).position(23 + 23*i, getGroundLevel() + 0.2f).addFixture("killingBottom").box(0.4f, 0.4f).create();
        
        for(int i = 0; i < 10; i++)
        	BodyBuilder.get().type(BodyType.StaticBody).position(11 + 23*i, getGroundLevel()  + 1.8f).addFixture("killingTop").box(0.4f, 0.4f).create();
        //--END OF TEMP
    }

    @Override
    void step(float delta) {
    	contactListener.contacts.clear();
    	world.step(delta, 3, 3);

        contactsSnapshot = contactListener.contacts;

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
