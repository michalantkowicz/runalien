package com.apptogo.runalien.screen;

import com.apptogo.runalien.game.GameActor;
import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.manager.ResourcesManager;
import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

        GameActor player = new GameActor("player");
        player.setAnimations(Animation.getAnimations(0.9f, -0.23f, "run", "jump"));
        player.setCurrentAnimation("jump");
        player.createBoxBody(BodyType.DynamicBody, new Vector2(0.3f, 0.8f));
        player.getBody().setTransform(new Vector2(15, 2), 0);
        player.modifyCustomOffsets(-0.4f, 0.2f);
        gameworldStage.addActor(player);

        GameActor ground = new GameActor("ground");
        player.setAnimations(Animation.getAnimations(0, 0, "ground"));
        player.setCurrentAnimation("ground");
        ground.createBoxBody(BodyType.StaticBody, new Vector2(5f, 0.3f));
        ground.getBody().setTransform(new Vector2(5, 2), 0);
        ground.modifyCustomOffsets(-0.4f, 0.2f);
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
