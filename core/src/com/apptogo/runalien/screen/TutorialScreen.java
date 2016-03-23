package com.apptogo.runalien.screen;

import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.scene2d.Image;
import com.apptogo.runalien.scene2d.Label;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class TutorialScreen extends GameScreen {

	private enum TutorialPhase {
		SLIDE,
		LONGSLIDE,
		JUMP,
		DOUBLEJUMP,
		CHARGEDOWN,
	}
	
	TutorialPhase currentPhase = TutorialPhase.SLIDE;
	
	Image rightHand, leftHand;
	Image rightHalf, leftHalf;
	Label slideLabel;
	
	Stage tutorialStage;
	FitViewport tutorialViewport;
	
	public TutorialScreen(Main game) {
		super(game);
	}
	
	@Override
	void prepare() {
		super.prepare();
		
		rightHalf = Image.get("screenHalf").size(Main.SCREEN_WIDTH/2f, Main.SCREEN_HEIGHT).scale(1/64f);
		rightHalf.setColor(0, 0, 0, 0.3f);
		
		gameworldStage.addActor(rightHalf);
		
		leftHand = Image.get("touchLeft").scale(1/64f);
		rightHand = Image.get("touchRight").scale(1/64f);
		
		leftHand.addAction(Actions.forever(Actions.sequence(Actions.alpha(0.4f, 0.2f), Actions.alpha(1f, 0.2f), Actions.delay(2))));
			rightHand.setVisible(false);	
		slideLabel = Label.get("Tap left side of screen to slide", "tutorial").centerX().centerY();//.scale(1/64f).centerX().centerY();
		
		gameworldStage.addActor(leftHand);
		gameworldStage.addActor(rightHand);
		
		
		tutorialViewport = new FitViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		tutorialStage = new Stage(tutorialViewport);
		tutorialStage.getCamera().position.set(0, 0, 0);
		
		tutorialStage.addActor(slideLabel);
	}
	
	@Override
	void step(float delta) {
		//simulate physics and handle body contacts
		contactListener.contacts.clear();
		world.step(delta, 3, 3);
		contactsSnapshot = contactListener.contacts;
				
		//act and draw main stage
		gameworldStage.act(delta);
		
		//setting hands and cover position
		rightHalf.position(gameworldStage.getCamera().position.x, 
		                   gameworldStage.getCamera().position.y - UnitConverter.toBox2dUnits(400));
		
		rightHand.position(gameworldStage.getCamera().position.x + UnitConverter.toBox2dUnits(300), 
				           gameworldStage.getCamera().position.y - UnitConverter.toBox2dUnits(380));
		
		leftHand.position(gameworldStage.getCamera().position.x - UnitConverter.toBox2dUnits(450), 
		                  gameworldStage.getCamera().position.y - UnitConverter.toBox2dUnits(380));
		
		gameworldStage.draw();

		//debug renderer
		debugRenderer.render(world, gameworldStage.getCamera().combined);
		
		//make player always on top
		player.toFront();
		
		tutorialStage.act();
		tutorialStage.draw();
	}
}
