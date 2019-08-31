package com.apptogo.runalien.manager;

import com.apptogo.runalien.exception.ResourcesManagerException;
import com.apptogo.runalien.scene2d.AtlasRegionComparator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class ResourcesManager {
	private static ResourcesManager INSTANCE;

	public static void create() {
		INSTANCE = new ResourcesManager();
	}

	public static void destroy() {
		INSTANCE.manager.clear();
		INSTANCE = null;
	}

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}

	public AssetManager manager;
	public Skin skin;

	private ResourcesManager() {
		manager = new AssetManager();

		manager.load("logo.png", Texture.class);
		manager.finishLoading();
	}

	public void loadResources() {
		//textures
		manager.load("menu_atlas.pack", TextureAtlas.class);
		manager.load("game_atlas.pack", TextureAtlas.class);

		//sounds
		//TODO muzyka w podfolderze i funkcja do zwracania instancji (jak przy regionach)
		manager.load("runscream.ogg", Sound.class);
		manager.load("slide.ogg", Sound.class);
		manager.load("jump.ogg", Sound.class);
		manager.load("doubleJump.ogg", Sound.class);
		manager.load("land.ogg", Sound.class);
		manager.load("chargeDown.ogg", Sound.class);
		manager.load("bell.ogg", Sound.class);
		manager.load("die.ogg", Sound.class);
		manager.load("whoosh.ogg", Sound.class);
		manager.load("click.ogg", Sound.class);
		manager.load("rocket.ogg", Sound.class);
		manager.load("weasel.ogg", Sound.class);
		manager.load("fallingTree.ogg", Sound.class);
		manager.load("explosion.ogg", Sound.class);
		manager.load("creak.ogg", Sound.class);
		manager.load("chain.ogg", Sound.class);
		manager.load("ballHit.ogg", Sound.class);
		manager.load("weaselHit.ogg", Sound.class);
	}

	public void loadSkin() {
		manager.finishLoading();
		skin = new Skin(Gdx.files.internal("skin.json"), manager.get("menu_atlas.pack", TextureAtlas.class));
	}

	public AtlasRegion getAtlasRegion(String regionName) {
		AtlasRegion region = null;

		for (TextureAtlas atlas : manager.getAll(TextureAtlas.class, new Array<>(TextureAtlas.class))) {
			region = atlas.findRegion(regionName);

			if (region != null) {
				break;
			}
		}
		if(region == null){
			throw new ResourcesManagerException("Couldn't find region: " + regionName);
		}
		return region;
	}

	public Array<AtlasRegion> getRegions(String pattern) {
		Array<AtlasRegion> regions = new Array<>(AtlasRegion.class);

		//TODO handle pattern with same beginning sorting without numbers
		for (TextureAtlas atlas : manager.getAll(TextureAtlas.class, new Array<>(TextureAtlas.class))) {
			for (AtlasRegion region : atlas.getRegions())
				if (region.name.startsWith(pattern))
					regions.add(region);
		}

		AtlasRegionComparator comparator = new AtlasRegionComparator();

		regions.sort(comparator);
		
		return regions;
	}

	public Sound getSound(String name) {
		return manager.get(name + ".ogg");
	}
	
	public <T> T get(String name) {
		return manager.get(name);
	}
}
