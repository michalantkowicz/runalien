package com.apptogo.runalien.screen;

import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.scene2d.Image;
import com.apptogo.runalien.scene2d.Label;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class TutorialScreen extends GameScreen {

	private enum TutorialPhase {
		WELCOME,
		SLIDE,
		LONGSLIDE,
		JUMP,
		DOUBLEJUMP,
		CHARGEDOWN,
		END,
	}

	TutorialPhase currentPhase = TutorialPhase.WELCOME;
	
	Stage tutorialStage;
	FitViewport tutorialViewport;
	
	Image rightHalf, leftHalf;
	Image rightHand, leftHand;
	
	Label welcomeLabel, slideLabel, longSlideLabel, jumpLabel, doubleJumpLabel, chargeDownLabel, endLabel;
	
	RepeatAction tapOnce, tapTwice, tapping;
	
	public TutorialScreen(Main game) {
		super(game);
	}
	
	@Override
	void prepare() {
		super.prepare();
		
		tutorialViewport = new FitViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		tutorialStage = new Stage(tutorialViewport);
		tutorialStage.getCamera().position.set(0, 0, 0);
		
		//Setting up screen half covers
		leftHalf = Image.get("screenHalf").size(Main.SCREEN_WIDTH/2f, Main.SCREEN_HEIGHT).position(-Main.SCREEN_WIDTH/2f, 0).centerY();
		leftHalf.setColor(0, 0, 0, 0.2f);
		
		rightHalf = Image.get("screenHalf").size(Main.SCREEN_WIDTH/2f, Main.SCREEN_HEIGHT).position(0, 0).centerY();
		rightHalf.setColor(0, 0, 0, 0.2f);
		
		//Setting up hand icons		
		leftHand = Image.get("touchLeft").position(-500, -390);
		rightHand = Image.get("touchRight").position(350, -390);
		
		//Setting up labels
		welcomeLabel = Label.get("Welcome to the tutorial! Follow the instructions", "tutorial").centerX().centerY(300);
		slideLabel = Label.get("Tap left side of screen to slide", "tutorial").centerX().centerY(300);
		longSlideLabel = Label.get("Keep tapping left side of screen to do long slide", "tutorial").centerX().centerY(300);
		jumpLabel = Label.get("Tap right side of screen to jump", "tutorial").centerX().centerY(300);
		doubleJumpLabel = Label.get("Tap twice right side of screen to double jump", "tutorial").centerX().centerY(300);
		chargeDownLabel = Label.get("Tap left side of screen when airborne to charge down", "tutorial").centerX().centerY(300);
		endLabel = Label.get("That's all! Now you are ready for the adventure", "tutorial").centerX().centerY(300);
		
		//Setting up actions
		tapOnce = Actions.forever(Actions.sequence(Actions.alpha(0.4f, 0.2f), Actions.alpha(1f, 0.2f), Actions.delay(2)));
		tapTwice = Actions.forever(Actions.sequence(Actions.alpha(0.4f, 0.2f), Actions.alpha(1f, 0.2f), Actions.alpha(0.4f, 0.2f), Actions.alpha(1f, 0.2f), Actions.delay(2)));
		tapping = Actions.forever(Actions.sequence(Actions.alpha(0.4f, 0.2f), Actions.alpha(1f, 0.2f)));
		
		//Adding actors to stage
		tutorialStage.addActor(leftHalf);
		tutorialStage.addActor(rightHalf);
		tutorialStage.addActor(leftHand);
		tutorialStage.addActor(rightHand);		
		
		tutorialStage.addActor(welcomeLabel);
		tutorialStage.addActor(slideLabel);
		tutorialStage.addActor(longSlideLabel);
		tutorialStage.addActor(jumpLabel);
		tutorialStage.addActor(doubleJumpLabel);
		tutorialStage.addActor(chargeDownLabel);
		tutorialStage.addActor(endLabel);
		
		//Setting alpha of tutorial stage's actors to 0
		for(Actor actor : tutorialStage.getActors())
			actor.setColor(actor.getColor().r, actor.getColor().g, actor.getColor().b, 0);
	}
	
	@Override
	void step(float delta) {
		//simulate physics and handle body contacts
		contactListener.contacts.clear();
		world.step(delta, 3, 3);
		contactsSnapshot = contactListener.contacts;
				
		//act and draw main stage
		gameworldStage.act(delta);		
		gameworldStage.draw();
		
		//make player always on top
		player.toFront();
		
		tutorialStage.act();
		
		handleTutorialPhase();
		
		tutorialStage.draw();
	}
	
	private void handleTutorialPhase() {
		if(currentPhase == TutorialPhase.WELCOME) {
			
		}
		else if(currentPhase == TutorialPhase.SLIDE) {
			
		}
	}
}
