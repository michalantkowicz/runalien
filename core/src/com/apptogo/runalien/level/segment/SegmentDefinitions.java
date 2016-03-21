package com.apptogo.runalien.level.segment;

public class SegmentDefinitions {

	//available fields in segment
	public static final int EMPTY = 0;
	public static final int CRATE_BOT = 1;
	public static final int CRATE_TOP = 2;
	public static final int LOG = 3;
	public static final int BELL = 4;
	
	/** ---------- segment definitions ---------- **/

	/* ------------------ LEVEL 0 ------------------ */
	public static final SegmentDefinition BOTTOM_CRATE = new SegmentDefinition(0, 3, 10f,
			new int[][]
			{ 
				{ 1 }
			});
	

	public static final SegmentDefinition ONE_BELL = new SegmentDefinition(0, 3, 10f,
			new int[][]
			{ 

				{ 4 },
				{ 4 },
				{ 4 },
				{ 4 },
				{ 0 },
				{ 0 }
			});
	
	/* ------------------ LEVEL 1 ------------------ */
	public static final SegmentDefinition TOP_CRATE = new SegmentDefinition(1, 4, 10f,
			new int[][]
			{ 
				{ 2 },
				{ 0 },
				{ 0 },
				{ 0 }
			});
	
	public static final SegmentDefinition BOTTOM_FOUR_CRATE = new SegmentDefinition(1, 5, 10f,
			new int[][]
			{ 
				{ 1, 1 },
				{ 1, 1 }
			});
	
	public static final SegmentDefinition CRATES_WITH_BELLS = new SegmentDefinition(1, 5, 10f,
			new int[][]
			{ 
				{ 4, 4, 4 },
				{ 4, 4, 4 },
				{ 4, 4, 4 },
				{ 0, 4, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 1, 1, 1 },
			});
	
	public static final SegmentDefinition SMALL_LOG_PYRAMID = new SegmentDefinition(1, 5, 10f,
			new int[][]
			{ 
				{ 0, 3, 0 },
				{ 3, 3, 3 },
			});
	
	/* ------------------ LEVEL 2 ------------------ */
	public static final SegmentDefinition SINGLE_CRATE_WITH_BELL = new SegmentDefinition(2, 6, 10f,
			new int[][]
			{ 
				{ 0, 4, 0 },
				{ 0, 4, 0 },
				{ 0, 4, 0 },
				{ 0, 4, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
				{ 0, 3, 0 },
				{ 0, 3, 0 },
			});
	
	public static final SegmentDefinition BIG_WALL_TO_SLIDE = new SegmentDefinition(2, 6, 10f,
			new int[][]
			{ 
				{ 0, 2, 0 },
				{ 0, 2, 0 },
				{ 0, 2, 0 },
				{ 0, 2, 0 },
				{ 0, 2, 0 },
				{ 0, 2, 0 },
				{ 0, 2, 0 },
				{ 0, 2, 0 },
				{ 0, 2, 0 },
				{ 0, 2, 0 },
				{ 0, 2, 0 },
				{ 0, 2, 0 },
				{ 0, 2, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
			});
	
	public static final SegmentDefinition BIG_BELL_TO_SLIDE = new SegmentDefinition(2, 6, 10f,
			new int[][]
			{ 
				{ 0, 4, 0 },
				{ 0, 4, 0 },
				{ 0, 4, 0 },
				{ 0, 4, 0 },
				{ 0, 0, 0 },
				{ 0, 0, 0 },
			});
	
	/* ------------------ LEVEL 3 ------------------ */
	public static final SegmentDefinition JUMP_AND_SLIDE = new SegmentDefinition(3, 8, 15f,
			new int[][]
			{ 
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
				{ 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
				{ 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			});
	
	public static final SegmentDefinition BIG_LOG_PYRAMID = new SegmentDefinition(3, 8, 15f,
			new int[][]
			{ 
				{ 0, 0, 0, 3, 0, 0, 0 }, 
				{ 0, 0, 3, 3, 3, 0, 0 }, 
				{ 0, 3, 3, 3, 3, 3, 0 }, 
				{ 3, 3, 3, 3, 3, 3, 3 }, 
			});
	
	public static final SegmentDefinition LONG_SLIDE = new SegmentDefinition(3, 8, 15f,
			new int[][]
			{ 
				{ 4, 4, 4, 4, 4, 4, 4, 4 },
				{ 4, 4, 4, 4, 4, 4, 4, 4 },
				{ 4, 4, 4, 4, 4, 4, 4, 4 },
				{ 4, 4, 4, 4, 4, 4, 4, 4 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 },
			});
	
	public static final SegmentDefinition TALL_CRATES = new SegmentDefinition(3, 10, 15f,
			new int[][]
			{ 
				{ 0, 2 }, 
				{ 2, 0 }, 
				{ 0, 2 }, 
				{ 2, 0 }, 
				{ 0, 2 }, 
				{ 2, 0 }, 
				{ 0, 2 }, 
				{ 2, 0 }, 
			});
	
	/* ------------------ LEVEL 4 ------------------ */

	public static final SegmentDefinition LONG_CRATE_JUMP = new SegmentDefinition(4, 10, 20f,
			new int[][]
			{ 
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
			});
	
	public static final SegmentDefinition TALL_JUMP = new SegmentDefinition(4, 10, 20f,
			new int[][]
			{ 
				{ 2 },
				{ 2 },
				{ 2 },
				{ 0 },
				{ 0 },
				{ 0 },
				{ 0 },
				{ 0 },
				{ 0 },
				{ 3 },
				{ 3 },
				{ 3 },
				{ 3 },
				{ 1 },
			});
	
	/* ------------------ LEVEL 5 ------------------ */
	public static final SegmentDefinition LONG_JUMPING_SEQUENCE = new SegmentDefinition(5, 8, 45f,
			new int[][]
			{ 
				{ 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,  0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 3, 0, 0, 0, 3 }, 
			});
	
	public static final SegmentDefinition RANDOM_LOGS = new SegmentDefinition(5, 10, 15f,
			new int[][]
			{ 
				{ 0, 3, 0, 3, 0, 3, 0 }, 
				{ 0, 3, 0, 3, 3, 3, 0 }, 
				{ 3, 3, 3, 3, 3, 3, 3 }, 
				{ 3, 3, 3, 3, 3, 3, 3 }, 
			});
	
}
