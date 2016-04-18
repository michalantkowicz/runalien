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
	public static final SegmentDefinition CRATE_1 = new SegmentDefinition(0, 1, 6f,
			new int[][]
			{ 
				{ 1 },
			});
	
	public static final SegmentDefinition LOG_3 = new SegmentDefinition(0, 5, 6f,
			new int[][]
			{ 
				{ 5 },
				{ 5 },
				{ 5 },
			});
	
	public static final SegmentDefinition LOG_2 = new SegmentDefinition(0, 3, 6f,
			new int[][]
			{ 
				{ 5 },
				{ 5 },
			});
	
	public static final SegmentDefinition BIG_BELL = new SegmentDefinition(0, 5, 6f,
			new int[][]
			{ 
				{ 6 },
				{ 6 },
				{ 6 },
				{ 6 },
				{ 6 },
				{ 6 },
				{ 6 },
				{ 0 },
			});
	/* ------------------ LEVEL 1 ------------------ */
	public static final SegmentDefinition LOG_BELL = new SegmentDefinition(1, 5, 6f,
			new int[][]
			{ 
				{ 6 },
				{ 6 },
				{ 6 },
				{ 0 },
				{ 0 },
				{ 0 },
				{ 0 },
				{ 5 },
				{ 5 },
			});

	public static final SegmentDefinition BIG_BELL_CRATE = new SegmentDefinition(1, 5, 6f,
			new int[][]
			{ 
				{ 6, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 2, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			});

	/* ------------------ LEVEL 2 ------------------ */
	
	public static final SegmentDefinition BIG_CRATE = new SegmentDefinition(2, 4, 6f,
			new int[][]
			{ 
				{ 3, 3 },
				{ 3, 3 },
			});
	
	public static final SegmentDefinition SMALL_LOG_PYRAMID = new SegmentDefinition(2, 4, 6f,
			new int[][]
			{ 
				{ 0, 5, 0 },
				{ 5, 5, 5 },
			});
	
	public static final SegmentDefinition LOG_4 = new SegmentDefinition(2, 5, 6f,
			new int[][]
			{ 
				{ 5 },
				{ 5 },
				{ 5 },
				{ 5 },
			});
	/* ------------------ LEVEL 3 ------------------ */
	public static final SegmentDefinition PYRAMID_BELL = new SegmentDefinition(3, 6, 6f,
			new int[][]
			{ 
				{ 0, 6, 0 },
				{ 0, 6, 0 },
				{ 0, 6, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 5, 0 },
				{ 5, 5, 5 },
			});
	
	public static final SegmentDefinition BIG_CRATE_WITH_SMALL = new SegmentDefinition(3, 6, 6f,
			new int[][]
			{ 
				{ 0, 1 },
				{ 3, 3 },
				{ 3, 3 },
			});
	
	public static final SegmentDefinition TWO_BIG_CRATES = new SegmentDefinition(3, 7, 6f,
			new int[][]
			{ 
				{ 4, 4 },
				{ 4, 4 },
				{ 0, 0 },
				{ 0, 0 },
				{ 0, 0 },
				{ 0, 0 },
				{ 3, 3 },
				{ 3, 3 },
			});
	/* ------------------ LEVEL 4 ------------------ */
	public static final SegmentDefinition LONG_SLIDE_BELLS = new SegmentDefinition(4, 8, 5f,
			new int[][]
			{ 
				{ 6, 6, 6, 6, 6, 6, 6, 6, 6 },
				{ 6, 6, 6, 6, 6, 6, 6, 6, 6 },
				{ 6, 6, 6, 6, 6, 6, 6, 6, 6 },
				{ 6, 6, 6, 6, 6, 6, 6, 6, 6 },
				{ 6, 6, 6, 6, 6, 6, 6, 6, 6 },
				{ 6, 6, 6, 6, 6, 6, 6, 6, 6 },
				{ 6, 6, 6, 6, 6, 6, 6, 6, 6 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			});
	
	public static final SegmentDefinition THREE_PYRAMIDS = new SegmentDefinition(4, 8, 6f,
			new int[][]
			{ 
				{ 0, 5, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 5, 0},
				{ 5, 5, 5, 0, 0, 0, 5, 5, 5, 0, 0, 0, 5, 5, 5},
			});
	
	public static final SegmentDefinition WALL_AND_BELL = new SegmentDefinition(4, 8, 6f,
			new int[][]
			{ 
				{ 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 6 },
				{ 5, 0, 0, 0, 6 },
				{ 5, 0, 0, 0, 6 },
				{ 5, 0, 0, 0, 6 },
				{ 5, 0, 0, 0, 0 },
			});

	/* ------------------ LEVEL 5 ------------------ */
	public static final SegmentDefinition BIG_LOG_PYRAMID = new SegmentDefinition(5, 9, 6f,
			new int[][]
			{ 
				{ 0, 0, 5, 0, 0 },
				{ 0, 5, 5, 5, 0 },
				{ 5, 5, 5, 5, 5 },
			});
	
	public static final SegmentDefinition BIG_CRATE_BELLS = new SegmentDefinition(5, 9, 6f,
			new int[][]
			{ 
				{ 6, 6 },
				{ 6, 6 },
				{ 0, 6 },
				{ 0, 0 },
				{ 0, 0 },
				{ 0, 0 },
				{ 1, 0 },
				{ 3, 3 },
				{ 3, 3 },
			});
	
	public static final SegmentDefinition LONG_JUMP = new SegmentDefinition(5, 9, 6f,
			new int[][]
			{ 
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			});
	/* ------------------ LEVEL 6 ------------------ */
	public static final SegmentDefinition PYRAMID_BELL_2 = new SegmentDefinition(6, 10, 6f,
			new int[][]
			{ 
				{ 0, 6, 0, 0, 0, 0, 6 },
				{ 0, 6, 0, 0, 0, 0, 6 },
				{ 0, 6, 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 0, 0, 6 },
				{ 0, 5, 0, 0, 0, 0, 0 },
				{ 5, 5, 5, 0, 0, 0, 0 },
			});
	public static final SegmentDefinition JUMP_SEQUENCE = new SegmentDefinition(6, 10, 6f,
			new int[][]
			{ 
				{ 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1 }
			});
	
	/* ------------------ LEVEL 7 ------------------ */
	/* ------------------ LEVEL 8 ------------------ */
	/* ------------------ LEVEL 9 ------------------ */
	/* ------------------ LEVEL 10 ----------------- */
	/* ------------------ LEVEL 12 ----------------- */
	/* ------------------ LEVEL 13 ----------------- */
	/* ------------------ LEVEL 14 ----------------- */
	

}
