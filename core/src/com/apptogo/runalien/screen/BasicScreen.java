package com.apptogo.runalien.screen;

import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.scene2d.Image;
import com.apptogo.runalien.tools.Movement;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class BasicScreen implements Screen {
    protected Main game;

    private Stage fadingStage;
    
    protected Stage backgroundStage;
    protected FillViewport backgroundViewport;
    
    protected FitViewport viewport;
    protected Stage stage;
    protected OrthographicCamera camera;

    protected String[] backgroundTextureNames = new String[] {};

    protected Array<Table> tables = new Array<Table>(new Table[] { new Table(), new Table(), new Table() });
    
    Movement movement = new Movement();

	private boolean FADING_OUT = false;
	private Screen screenToChange;

    abstract protected void prepare();

    abstract protected void step(float delta);

    public BasicScreen(Main game)
    {
        this.game = game;
    }

    public BasicScreen(Main game, String backgroundTextureName) {
        this.game = game;
        this.backgroundTextureNames = new String[] { backgroundTextureName };
    }

    public BasicScreen(Main game, String[] backgroundTextureNames) {
        this.game = game;
        this.backgroundTextureNames = backgroundTextureNames;
    }

    @Override
    public void show() {

        this.backgroundViewport = new FillViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        this.backgroundStage = new Stage(this.backgroundViewport);
        
        this.fadingStage = new Stage(this.backgroundViewport);
        fadingStage.addActor(Image.get("black").size(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT).position(-Main.SCREEN_WIDTH/2f, -Main.SCREEN_HEIGHT/2f));
        fadingStage.addAction(Actions.alpha(0, 0.15f));
        
        ((OrthographicCamera) backgroundStage.getCamera()).position.set(0f, 0f, 0f);
        
        this.viewport = new FitViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        this.stage = new Stage(this.viewport);

        this.camera = (OrthographicCamera) stage.getCamera();
        this.camera.position.set(0f, 0f, 0f);

        // create background images
        for (int i = 0; i < backgroundTextureNames.length; i++)
        {
            Image background = Image.get(backgroundTextureNames[i]).size(1280, 800).centerX(i * Main.SCREEN_WIDTH).centerY();
            this.backgroundStage.addActor(background);
        }

        Gdx.input.setInputProcessor(stage);
        
        for (int i = 0; i < tables.size; i++)
        {
            Table table = tables.get(i);

            table.setSize(Main.SCREEN_WIDTH - 100f, Main.SCREEN_HEIGHT - 100f);
            table.setPosition(-table.getWidth() / 2f + i * Main.SCREEN_WIDTH, -table.getHeight() / 2f);

            // table.debug();

            stage.addActor(table);
        }
        
        prepare();
    }

    protected float DELTA = 1/60f;
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // HANDLING CAMERA MOVEMENT
        if (movement.isRun())
        {
            this.camera.position.set(movement.getCurrent());
        }

        this.backgroundViewport.apply();
        this.backgroundStage.act(DELTA);
        this.backgroundStage.draw();
        
        step(DELTA);
        
        this.viewport.apply();
        this.stage.act(DELTA);
        this.stage.draw();
        
        if(fadingStage.getRoot().getActions().size > 0) {
	        this.backgroundViewport.apply();
	        this.fadingStage.act();
	        this.fadingStage.draw();
        }
        else if(FADING_OUT) {
        	this.backgroundViewport.apply();
	        this.fadingStage.act();
	        this.fadingStage.draw();

	        FADING_OUT = false;
        	game.doSetScreen(screenToChange);
        }
        
        // HANDLING BUTTON INPUT
        handleInput(); //Moving at the end of render method by Mateusz Gawel @2016
    }
    
    public void fadeOut(Screen screenToChange) {
    	this.screenToChange = screenToChange;
    	this.FADING_OUT = true;
    	fadingStage.addAction(Actions.alpha(1, 0.15f));
    }
    
    protected void handleInput() {
    	if(Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
    		game.setScreen(new MenuScreen(game));
    	}
    }
    
    protected void cameraMoveTo(float x, float y, float z)
    {
        movement.set(this.camera.position, new Vector3(x, y, z));
    }

    ClickListener previousTable = new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y)
        {
            if (camera.position.x > tables.first().getX() + tables.first().getWidth() / 2f)
            {
                cameraMoveTo(camera.position.x - Main.SCREEN_WIDTH, camera.position.y, camera.position.z);
            }
        }
    };

    ClickListener nextTable = new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y)
        {
            if (camera.position.x < tables.peek().getX() + tables.peek().getWidth() / 2f)
            {
                cameraMoveTo(camera.position.x + Main.SCREEN_WIDTH, camera.position.y, camera.position.z);
            }
        }
    };

    @Override
    public void resize(int width, int height) {
        this.backgroundStage.getViewport().update(width, height);
        this.stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
