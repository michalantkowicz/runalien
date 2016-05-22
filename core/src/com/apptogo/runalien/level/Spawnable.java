package com.apptogo.runalien.level;

public interface Spawnable {
	void setPoolIndex(int poolIndex);
	int getPoolIndex();
	float getBaseOffset();
	int getMinLevel();
	int getMaxLevel();
}
