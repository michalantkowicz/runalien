package com.apptogo.runalien.level.segment;

public class SegmentDefinitions {

	//available fields in segment
	public static final int EMPTY = 0;
	public static final int CRATE_BOT = 1;
	public static final int CRATE_TOP = 2;
	public static final int BIG_CRATE_BOT = 3;
	public static final int BIG_CRATE_TOP = 4;
	public static final int LOG = 5;
	public static final int BELL = 6;

	/* ------------------ LEVEL 0 ------------------ */
	public static final SegmentDefinition BIG_CRATE = new SegmentDefinition(0, 5, 10f,
			new int[][]
			{ 
				{ 6, 6, 6, 6, 3, 0, 0, 0 },
				{ 6, 6, 6, 6, 3, 0, 0, 0 },
				{ 6, 6, 6, 6, 3, 0, 0, 0 },
				{ 6, 6, 6, 6, 3, 6, 6, 6 },
				{ 6, 6, 6, 0, 3, 0, 6, 6 },
				{ 6, 6, 0, 5, 3, 3, 0, 6 },
				{ 6, 0, 5, 5, 3, 3, 0, 0 },
				{ 0, 5, 5, 5, 3, 3, 1, 0 },
				{ 5, 5, 5, 5, 3, 3, 1, 1 },
			});
	/* ------------------ LEVEL 1 ------------------ */
	/* ------------------ LEVEL 2 ------------------ */
	/* ------------------ LEVEL 3 ------------------ */
	/* ------------------ LEVEL 4 ------------------ */
	/* ------------------ LEVEL 5 ------------------ */
	/* ------------------ LEVEL 6 ------------------ */
	/* ------------------ LEVEL 7 ------------------ */
	/* ------------------ LEVEL 8 ------------------ */
	/* ------------------ LEVEL 9 ------------------ */
	/* ------------------ LEVEL 10 ----------------- */
	/* ------------------ LEVEL 12 ----------------- */
	/* ------------------ LEVEL 13 ----------------- */
	/* ------------------ LEVEL 14 ----------------- */
	

}
