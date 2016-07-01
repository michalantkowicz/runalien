package com.apptogo.runalien.plugin;

import java.util.LinkedList;
import java.util.Queue;

import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.TimeUtils;

public class AchievementPlugin extends AbstractPlugin {

	private final Logger logger = new Logger(getClass().getName(), Logger.DEBUG);

	public static final String NOOB = "CgkIpZ2MjMkXEAIQAg";
	public static final String SHORT_DISTANCE_RUNNER = "CgkIpZ2MjMkXEAIQAw";
	public static final String LONG_DISTANCE_RUNNER = "CgkIpZ2MjMkXEAIQBA";
	public static final String VILLAGERS_WILL_NEVER_GET_ME = "CgkIpZ2MjMkXEAIQBQ";
	public static final String MARATHON = "CgkIpZ2MjMkXEAIQBg";
	public static final String BEST_GAMER_EVER = "CgkIpZ2MjMkXEAIQBw";
	public static final String I_LIKE_THAT_GAME = "CgkIpZ2MjMkXEAIQEQ";
	public static final String I_HATE_WEASELS = "CgkIpZ2MjMkXEAIQLg";
	public static final String LUMBERJACK = "CgkIpZ2MjMkXEAIQLw";
	public static final String NIGHT_OWL = "CgkIpZ2MjMkXEAIQMA";
	public static final String DAY_RUSH = "CgkIpZ2MjMkXEAIQMQ";
	public static final String EDUCATED = "CgkIpZ2MjMkXEAIQMg";
	public static final String FAST_FINGERS = "CgkIpZ2MjMkXEAIQMw";
	public static final String JUMPER = "CgkIpZ2MjMkXEAIQNA";
	public static final String SLIDER = "CgkIpZ2MjMkXEAIQNQ";
	public static final String BALLS_OF_STEEL = "CgkIpZ2MjMkXEAIQNw";
	public static final String ROCKET_SCIENCE = "CgkIpZ2MjMkXEAIQOA";
	public static final String RUN_ALIEN = "CgkIpZ2MjMkXEAIQOQ";
	
	private final int JUMP_INTERVAL = 1000; //[ms]
	private final int JUMP_AMOUNT = 5;
	
	private Queue<Long> jumpMoments = new LinkedList<Long>();
	

	public void fire(String achievementName) {
		logger.debug("Achievement fired: " + achievementName);
		
		if(Main.getInstance().getGameScreen().isTutorial())
			return;
		
		switch (achievementName) {
			case NOOB:
				Main.gameCallback.unlockAchievement(NOOB);
				break;
			case SHORT_DISTANCE_RUNNER:
				Main.gameCallback.unlockAchievement(SHORT_DISTANCE_RUNNER);
				break;
			case LONG_DISTANCE_RUNNER:
				Main.gameCallback.unlockAchievement(LONG_DISTANCE_RUNNER);
				break;
			case VILLAGERS_WILL_NEVER_GET_ME:
				Main.gameCallback.unlockAchievement(VILLAGERS_WILL_NEVER_GET_ME);
				break;
			case MARATHON:
				Main.gameCallback.incrementAchievement(MARATHON);
				break;
			case BEST_GAMER_EVER:
				Main.gameCallback.incrementAchievement(BEST_GAMER_EVER);
				break;
			case I_LIKE_THAT_GAME:
				Main.gameCallback.incrementAchievement(I_LIKE_THAT_GAME);
				break;
			case I_HATE_WEASELS:
				Main.gameCallback.incrementAchievement(I_HATE_WEASELS);
				break;
			case LUMBERJACK:
				Main.gameCallback.incrementAchievement(LUMBERJACK);
				break;
			case NIGHT_OWL:
				Main.gameCallback.unlockAchievement(NIGHT_OWL);
				break;
			case DAY_RUSH:
				Main.gameCallback.unlockAchievement(DAY_RUSH);
				break;
			case EDUCATED:
				Main.gameCallback.unlockAchievement(EDUCATED);
				break;
			case FAST_FINGERS:
				Main.gameCallback.unlockAchievement(FAST_FINGERS);
				break;
			case JUMPER:
				Main.gameCallback.incrementAchievement(JUMPER);
				break;
			case SLIDER:
				Main.gameCallback.incrementAchievement(SLIDER);
				break;
			case BALLS_OF_STEEL:
				Main.gameCallback.incrementAchievement(BALLS_OF_STEEL);
				break;
			case ROCKET_SCIENCE:
				Main.gameCallback.incrementAchievement(ROCKET_SCIENCE);
				break;
			case RUN_ALIEN:
				Main.gameCallback.unlockAchievement(RUN_ALIEN);
				break;
		}
	}

	@Override
	public void run() {

	}

	@Override
	public void setUpDependencies() {

	}
	
	public void handleJump() {
		fire(AchievementPlugin.JUMPER);
		
		long currentMoment = TimeUtils.millis();
		Long lastMoment = jumpMoments.peek();
		if(lastMoment == null)
			lastMoment = 0L;
		
		jumpMoments.add(currentMoment);
		
		if(jumpMoments.size() >= JUMP_AMOUNT) {
			
			if(currentMoment - jumpMoments.poll() <= JUMP_INTERVAL) {
				fire(FAST_FINGERS);
				logger.debug("XXX SUCCESS! " + (currentMoment - lastMoment) );
			}
			else{
				logger.debug("XXX fail " + (currentMoment - lastMoment) );
			}
		}
		

		logger.debug("XXX jumps: " + jumpMoments.size() + "time: " + (currentMoment - lastMoment));
	}

}
