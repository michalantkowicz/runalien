package com.apptogo.runalien.level.segment;

import com.apptogo.runalien.level.Spawnable;

public class SegmentDefinition {
	private int definition[][];
	private int minLevel;
	private int maxLevel;
	private float baseOffset;

	public SegmentDefinition(int minLevel, int maxLevel, float basicOffset, int definition[][]) {
		super();
		this.definition = definition;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.baseOffset = basicOffset + definition.length;
		
		SegmentDefinitions.SEGMENT_DEFINITIONS.add(this);
	}

	/**
	 * @return 2d array with definition of the segment
	 */
	public int[][] getDefinition() {
		return definition;
	}

	/**
	 * @return min level when can be spawned
	 */
	public int getMinLevel() {
		return minLevel;
	}

	/**
	 * @return max level when can be spawned
	 */
	public int getMaxLevel() {
		return maxLevel;
	}

	/**
	 * @return offset not modified by speed after which next obstacle can be spawned
	 */
	public float getBaseOffset() {
		return baseOffset;
	}

}