package com.apptogo.runalien.manager;

import com.apptogo.runalien.scene2d.AtlasRegionComparator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
        manager.load("ground.png", Texture.class);
        manager.finishLoading();
    }

    public void loadResources() {
        manager.load("atlas.pack", TextureAtlas.class);
        manager.load("alien.pack", TextureAtlas.class);
    }

    public void loadSkin() {
        manager.finishLoading();
        skin = new Skin(Gdx.files.internal("skin.json"), manager.get(
                "atlas.pack", TextureAtlas.class));
    }
    
    public AtlasRegion getAtlasRegion(String regionName) {
        AtlasRegion region = null;

        for (TextureAtlas atlas : manager.getAll(TextureAtlas.class,
                new Array<TextureAtlas>())) {
            region = atlas.findRegion(regionName);

            if (region != null) {
                break;
            }
        }

        return region;
    }

    public Array<AtlasRegion> getRegions(String pattern) {
        Array<AtlasRegion> regions = new Array<AtlasRegion>();

        for (TextureAtlas atlas : manager.getAll(TextureAtlas.class,
                new Array<TextureAtlas>())) {
            for (AtlasRegion region : atlas.getRegions())
                if (region.name.startsWith(pattern))
                    regions.add(region);
        }

        AtlasRegionComparator comparator = new AtlasRegionComparator();

        regions.sort(comparator);

        return regions;
    }
    
    public <T> T get(String name){
        return manager.get(name);
    }
}
