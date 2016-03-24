package com.apptogo.runalien.screen;

import com.apptogo.runalien.main.Main;
import com.apptogo.runalien.plugin.RunningPlugin;
import com.apptogo.runalien.plugin.TouchSteeringPlugin;
import com.apptogo.runalien.scene2d.Image;
import com.apptogo.runalien.scene2d.Label;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class TutorialScreen extends GameScreen {

	private enum TutorialPhase {
		WELCOME,
		STARTPREPARE,
		START,
		SLIDEPREPARE,
		SLIDE,
		LONGSLIDEPREPARE,
		LONGSLIDE,
		JUMPPREPARE,
		JUMP,
		DOUBLEJUMPPREPARE,
		DOUBLEJUMP,
		CHARGEDOWNPREPARE,
		CHARGEDOWN,
		SPEEDPREPARE,
		SPEED,
		ENDPREPARE,
		END,
		
		NONE,
		TRANSITION,
	}

	TutorialPhase currentPhase = TutorialPhase.NONE;
	TutorialPhase nextPhase = TutorialPhase.NONE;
	
	Stage tutorialStage;
	FitViewport tutorialViewport;
	
	Group currentGroup;
	
	Image rightHalf, leftHalf;
	Image rightHand, leftHand;
	
	Label welcomeLabel, startLabel, slideLabel, longSlideLabel, jumpLabel, doubleJumpLabel, chargeDownLabel, speedLabel, endLabel;
	
	float longSlideCounter = 0;
		
	public TutorialScreen(Main game) {
		super(game);
	}
	
	@Override
	void prepare() {
		super.prepare();
		
		scoreLabel.setVisible(false);
		
		tutorialViewport = new FitViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		tutorialStage = new Stage(tutorialViewport);
		stagesToFade.add(tutorialStage);
		tutorialStage.getCamera().position.set(0, 0, 0);
		
		//Initializing currentGroup
		currentGroup = new Group();
		
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
		startLabel = Label.get("Tap anywhere to start running", "tutorial").centerX().centerY(300);
		slideLabel = Label.get("Tap left side of screen to slide", "tutorial").centerX().centerY(300);
		longSlideLabel = Label.get("Keep tapping left side of screen to do long slide", "tutorial").centerX().centerY(300);
		jumpLabel = Label.get("Tap right side of screen to jump", "tutorial").centerX().centerY(300);
		doubleJumpLabel = Label.get("Tap twice right side of screen to double jump", "tutorial").centerX().centerY(300);
		chargeDownLabel = Label.get("Tap left side of screen when airborne to charge down", "tutorial").centerX().centerY(300);
		speedLabel = Label.get("Remember that alien is running faster and faster!", "tutorial").centerX().centerY(300);
		endLabel = Label.get("That's all! Now you are ready for the adventure", "tutorial").centerX().centerY(300);

		//Setting alpha of tutorial stage's actors to 0
		for(Actor actor : tutorialStage.getActors())
			actor.setColor(actor.getColor().r, actor.getColor().g, actor.getColor().b, 0);
				
		addToCurrentGroup(welcomeLabel);
		currentPhase = TutorialPhase.WELCOME;
	}
		
	private void handleTutorialPhase() {
		
		switch(currentPhase) {
			case TRANSITION:
				if(currentGroup.getActions().size == 0) {
					currentGroup.clear();
					currentPhase = nextPhase;
					nextPhase = TutorialPhase.NONE;
				}
				break;
				
			case WELCOME:
				if(currentGroup.getActions().size == 0) {
					currentPhase = TutorialPhase.TRANSITION;
					nextPhase = TutorialPhase.STARTPREPARE;
					fadeOutCurrentGroup(4);
				}
				break;
			
			case STARTPREPARE:
				addToCurrentGroup(rightHand, startLabel);	
				
				rightHand.addAction(tapOnce());
							
				currentPhase = TutorialPhase.START;
				break;
				
			case START:
				if(currentGroup.getActions().size == 0 && ((RunningPlugin)player.getPlugin("RunningPlugin")).isStarted()) {
					currentPhase = TutorialPhase.TRANSITION;
					nextPhase = TutorialPhase.SLIDEPREPARE;
					fadeOutCurrentGroup(0.5f);
				}
				break;
				
			case SLIDEPREPARE:
				addToCurrentGroup(rightHalf, leftHand, slideLabel);	
				
				leftHand.addAction(tapOnce());
							
				currentPhase = TutorialPhase.SLIDE;
				break;
				
			case SLIDE:
				if(currentGroup.getActions().size == 0 && ((TouchSteeringPlugin)player.getPlugin("TouchSteeringPlugin")).isSliding()) {
					currentPhase = TutorialPhase.TRANSITION;
					nextPhase = TutorialPhase.LONGSLIDEPREPARE;
					fadeOutCurrentGroup(0.5f);
				}
				break;
				
			case LONGSLIDEPREPARE:
				addToCurrentGroup(rightHalf, leftHand, longSlideLabel);	
				
				leftHand.addAction(tapping());
				
				currentPhase = TutorialPhase.LONGSLIDE;
				break;
				
			case LONGSLIDE:
				if(!((TouchSteeringPlugin)player.getPlugin("TouchSteeringPlugin")).isSliding()) {
					longSlideCounter = 0;
				}
				else if(Gdx.input.justTouched()) {
					longSlideCounter++;
				}
				
				if(currentGroup.getActions().size == 0 && ((TouchSteeringPlugin)player.getPlugin("TouchSteeringPlugin")).isSliding() && longSlideCounter > 10) {
					currentPhase = TutorialPhase.TRANSITION;
					nextPhase = TutorialPhase.JUMPPREPARE;
					fadeOutCurrentGroup(0.5f);
				}
				break;
				
			case JUMPPREPARE:
				addToCurrentGroup(leftHalf, rightHand, jumpLabel);
								
				rightHand.addAction(tapOnce());
				
				currentPhase = TutorialPhase.JUMP;
				break;
				
			case JUMP:
				if(currentGroup.getActions().size == 0 && ((TouchSteeringPlugin)player.getPlugin("TouchSteeringPlugin")).isJumping()) {
					currentPhase = TutorialPhase.TRANSITION;
					nextPhase = TutorialPhase.DOUBLEJUMPPREPARE;
					fadeOutCurrentGroup(0.5f);
				}
				break;
				
			case DOUBLEJUMPPREPARE:
				addToCurrentGroup(leftHalf, rightHand, doubleJumpLabel);
				
				rightHand.addAction(tapTwice());
				
				currentPhase = TutorialPhase.DOUBLEJUMP;
				break;
				
			case DOUBLEJUMP:
				if(currentGroup.getActions().size == 0 && ((TouchSteeringPlugin)player.getPlugin("TouchSteeringPlugin")).isDoubleJumping()) {
					currentPhase = TutorialPhase.TRANSITION;
					nextPhase = TutorialPhase.CHARGEDOWNPREPARE;
					fadeOutCurrentGroup(0.5f);
				}
				break;
				
			case CHARGEDOWNPREPARE:
				addToCurrentGroup(leftHand, rightHand, chargeDownLabel);
				
				rightHand.addAction(tapTwice());
				leftHand.addAction(tapOnceAirborne());
				
				currentPhase = TutorialPhase.CHARGEDOWN;
				break;
				
			case CHARGEDOWN:
				//TODO przerobic mapowanie klawiszy na dotkniecia
				if(currentGroup.getActions().size == 0 && ((TouchSteeringPlugin)player.getPlugin("TouchSteeringPlugin")).isDoubleJumping() && Gdx.input.isKeyJustPressed(Keys.L)) {
					currentPhase = TutorialPhase.TRANSITION;
					nextPhase = TutorialPhase.SPEEDPREPARE;
					fadeOutCurrentGroup(0.5f);
				}
				break;
			case SPEEDPREPARE:
				addToCurrentGroup(speedLabel);
				
				currentPhase = TutorialPhase.SPEED;
				break;
			case SPEED:
				if(currentGroup.getActions().size == 0) {
					currentPhase = TutorialPhase.TRANSITION;
					nextPhase = TutorialPhase.ENDPREPARE;
					fadeOutCurrentGroup(3);
				}
				break;
			case ENDPREPARE:
				addToCurrentGroup(endLabel);
				fadeOutCurrentGroup(3);
				currentPhase = TutorialPhase.END;
				break;
			case END:
				if(currentGroup.getActions().size == 0) {
					game.setScreen(new GameScreen(game));
				}
				break;
			case NONE:
				//Do nothing
				break;
			default:
				break;
		}
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
		grass.toFront();
		
		tutorialStage.act();
		
		handleTutorialPhase();
		
		tutorialStage.draw();
	}
	
	private void fadeOutCurrentGroup(float delay) {
		for(Actor actor : currentGroup.getChildren()) {
			actor.clearActions();
		}
		currentGroup.addAction(Actions.sequence(Actions.delay(delay), Actions.alpha(0, 1f)));
	}
	
	private void addToCurrentGroup(Actor... actors) {
		for(Actor actor : actors) {
			currentGroup.addActor(actor);
		}
		
		currentGroup.addAction(Actions.alpha(1, 1f));
		
		currentGroup.remove();
		tutorialStage.addActor(currentGroup);
	}
	
	private RepeatAction tapOnce() {
		return Actions.forever(Actions.sequence(Actions.alpha(0.4f, 0.2f), Actions.alpha(1f, 0.2f), Actions.delay(2)));
	}
	
	private RepeatAction tapOnceAirborne() {
		return Actions.forever(Actions.sequence(Actions.delay(0.8f), Actions.alpha(0.4f, 0.2f), Actions.alpha(1f, 0.2f), Actions.delay(1.6f)));
	}
	
	private RepeatAction tapTwice() {
		return Actions.forever(Actions.sequence(Actions.alpha(0.4f, 0.2f), Actions.alpha(1f, 0.2f), Actions.alpha(0.4f, 0.2f), Actions.alpha(1f, 0.2f), Actions.delay(2)));
	}
	
	private RepeatAction tapping() {
		return Actions.forever(Actions.sequence(Actions.alpha(0.4f, 0.2f), Actions.alpha(1f, 0.2f)));
	}
}
