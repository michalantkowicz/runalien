package com.apptogo.runalien.plugin;

import java.util.HashMap;
import java.util.Map;

import com.apptogo.runalien.exception.SoundException;
import com.apptogo.runalien.manager.ResourcesManager;
import com.badlogic.gdx.audio.Sound;

public class SoundHandler extends AbstractPlugin {

	private Map<String, Sound> sounds = new HashMap<String, Sound>();

	//trzeba dodac zmiane glosnosci sounda w zaleznosci od pozycji kamery

	public SoundHandler(String... soundNames) {
		for (String soundName : soundNames)
			addSound(soundName);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public Map<String, Sound> getSounds() {
		return sounds;
	}

	public Sound getSound(String soundName) {
		Sound sound = sounds.get(soundName);
		if (sound == null)
			throw new SoundException("Sound: '" + soundName + "' not registered in SoundHandler of: '" + actor.getName() + "'");
		return sound;
	}

	public void addSound(String soundName, Sound sound) {
		if (sounds.containsKey(soundName))
			throw new SoundException("Sound: '" + soundName + "' is already defined for: '" + actor.getName() + "'");
		this.sounds.put(soundName, sound);
	}

	public void addSound(String soundName) {
		Sound sound = ResourcesManager.getInstance().getSound(soundName);
		if (sounds.containsKey(soundName))
			throw new SoundException("Sound: '" + soundName + "' is already defined for: '" + actor.getName() + "'");
		this.sounds.put(soundName, sound);
	}

	public void playSound(String soundName) {
		get(soundName).play();
	}

	public void loopSound(String soundName) {
		get(soundName).loop();
	}

	public void stopSound(String soundName) {
		get(soundName).stop();
	}

	public void pauseSound(String soundName) {
		get(soundName).pause();
	}

	public void resumeSound(String soundName) {
		get(soundName).resume();
	}

	private Sound get(String soundName) {
		Sound sound = sounds.get(soundName);
		if (sound == null)
			throw new SoundException("Sound: '" + soundName + "' not registered in SoundHandler of: '" + actor.getName() + "'");
		return sound;
	}

	@Override
	public void setUpDependencies() {
		// TODO Auto-generated method stub

	}

	/** ---------- STATIC METHODS ---------- **/

	/**
	 * Play sound if loaded in resources manager
	 * 
	 * @param soundName to play
	 * @return id of played sound
	 */
	public static long playSingleSound(String soundName) {
		Sound sound = ResourcesManager.getInstance().getSound(soundName);
		return sound.play();
	}

	/**
	 * Loop sound if loaded in resources manager
	 * 
	 * @param soundName to play
	 * @return id of played sound
	 */
	public static long loopSingleSound(String soundName) {
		Sound sound = ResourcesManager.getInstance().getSound(soundName);
		return sound.loop();
	}

	/**
	 * Pause sound if loaded in resources manager
	 * 
	 * @param soundName to play
	 * @return id of played sound
	 */
	public static void pauseSingleSound(String soundName) {
		Sound sound = ResourcesManager.getInstance().getSound(soundName);
		sound.pause();
	}

	/**
	 * Stop sound if loaded in resources manager
	 * 
	 * @param soundName to play
	 * @return id of played sound
	 */
	public static void stopSingleSound(String soundName) {
		Sound sound = ResourcesManager.getInstance().getSound(soundName);
		sound.stop();
	}
}
