package com.apptogo.runalien.scene2d;

import java.util.Comparator;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class AtlasRegionComparator implements Comparator<AtlasRegion>
{
	@Override
	public int compare(AtlasRegion arg0, AtlasRegion arg1) {
		int no0 = Integer.parseInt(arg0.name.replaceAll( "[^\\d]", ""));
		int no1 = Integer.parseInt(arg1.name.replaceAll( "[^\\d]", ""));
		
		return (no0 - no1);
	}
}