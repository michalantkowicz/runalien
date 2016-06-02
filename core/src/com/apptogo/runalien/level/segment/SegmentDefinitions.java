package com.apptogo.runalien.level.segment;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class SegmentDefinitions {

	//available fields in segment
	public static final int EMPTY = 0;
	public static final int CRATE_BOT = 1;
	public static final int CRATE_TOP = 2;
	public static final int BIG_CRATE_BOT = 3;
	public static final int BIG_CRATE_TOP = 4;
	public static final int LOG = 5;
	public static final int BELL = 6;

	//all segment definitions are stored here
	public static final List<SegmentDefinition> SEGMENT_DEFINITIONS = new ArrayList<SegmentDefinition>();
	
	/* ------------------ LEVEL 0 ------------------ */

	final static float DEFAULT_OFFSET = 7f;
	
	private static final SegmentDefinition LOG_1 = new SegmentDefinition(0, 2, DEFAULT_OFFSET,
			new int[][]
			{ 
				{ 5 },
			});
	
	private static final SegmentDefinition LOG_3 = new SegmentDefinition(0, 4, DEFAULT_OFFSET,
			new int[][]
			{ 
				{ 5 },
				{ 5 },
				{ 5 },
			});
	
	private static final SegmentDefinition BIG_BELL = new SegmentDefinition(0, 4, DEFAULT_OFFSET - 2,
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
	private static final SegmentDefinition LOG_BELL = new SegmentDefinition(1, 5, DEFAULT_OFFSET,
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

	private static final SegmentDefinition BELL_THEN_BELL_AND_LOG = new SegmentDefinition(1, 5, DEFAULT_OFFSET,
			new int[][]
			{ 
				{ 6, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5 },
			});

	/* ------------------ LEVEL 2 ------------------ */
	
	private static final SegmentDefinition BIG_CRATE = new SegmentDefinition(2, 4, DEFAULT_OFFSET,
			new int[][]
			{ 
				{ 3, 3, 0},
				{ 3, 3, 1},
			});
	
	private static final SegmentDefinition BIG_CRATE_WITH_SMALL = new SegmentDefinition(2, 6, DEFAULT_OFFSET,
			new int[][]
			{ 
				{ 0, 1 },
				{ 3, 3 },
				{ 3, 3 },
			});
	
	private static final SegmentDefinition SMALL_LOG_PYRAMID = new SegmentDefinition(2, 4, DEFAULT_OFFSET,
			new int[][]
			{ 
				{ 0, 5, 0 },
				{ 5, 5, 5 },
			});
	/* ------------------ LEVEL 3 ------------------ */
	private static final SegmentDefinition PYRAMID_BELL = new SegmentDefinition(3, 6, DEFAULT_OFFSET,
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
	/* ------------------ LEVEL 4 ------------------ */	
	private static final SegmentDefinition GREAT_PYRAMID = new SegmentDefinition(4, 8, DEFAULT_OFFSET,
			new int[][]
			{ 
				{ 0, 0, 0, 5, 0, 0 },
				{ 0, 0, 5, 5, 0, 0 },
				{ 0, 5, 5, 5, 5, 0 },
				{ 5, 5, 5, 5, 5, 5 },
			});
	
	private static final SegmentDefinition WALL_AND_BELL = new SegmentDefinition(4, 8, DEFAULT_OFFSET,
			new int[][]
			{ 
				{ 0, 0, 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 0, 0, 6 },
				{ 5, 0, 0, 0, 0, 0, 6 },
				{ 5, 0, 0, 0, 0, 0, 6 },
				{ 5, 0, 0, 0, 0, 0, 6 },
				{ 5, 0, 0, 0, 0, 0, 0 },
			});

	/* ------------------ LEVEL 5 ------------------ */
	private static final SegmentDefinition BIG_LOG_PYRAMID = new SegmentDefinition(5, 9, DEFAULT_OFFSET,
			new int[][]
			{ 
				{ 0, 0, 5, 0, 0 },
				{ 0, 5, 5, 5, 0 },
				{ 5, 5, 5, 5, 5 },
			});
	
	/* ------------------ LEVEL 6 ------------------ */
	private static final SegmentDefinition PYRAMID_BELL_2 = new SegmentDefinition(6, 10, DEFAULT_OFFSET,
			new int[][]
			{ 
				{ 0, 6, 0, 0, 0, 0, 0, 6 },
				{ 0, 6, 0, 0, 0, 0, 0, 6 },
				{ 0, 6, 0, 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 0, 0, 0, 6 },
				{ 0, 5, 0, 0, 0, 0, 0, 0 },
				{ 5, 5, 5, 0, 0, 0, 0, 0 },
			});
	//TODO create more types of definitions
	/* ------------------ LEVEL 7 ------------------ */
	/* ------------------ LEVEL 8 ------------------ */
	/* ------------------ LEVEL 9 ------------------ */
	/* ------------------ LEVEL 10 ----------------- */
	/* ------------------ LEVEL 12 ----------------- */
	/* ------------------ LEVEL 13 ----------------- */
	/* ------------------ LEVEL 14 ----------------- */
	

}
