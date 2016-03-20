package com.apptogo.runalien.level.segment;

public class SegmentDefinitions {

	//available fields in segment
	public static final int EMPTY = 0;
	public static final int CRATE_BOT = 1;
	public static final int CRATE_TOP = 2;
	public static final int LOG = 3;
	public static final int BELL = 4;
	
	/** ---------- segment definitions ---------- **/

	public static final SegmentDefinition BOTTOM_CRATE = new SegmentDefinition(0, 5, 10f,
			new int[][]
			{ 
				{ 1 }
			});
	
	public static final SegmentDefinition SMALL_CRATE_PYRAMID = new SegmentDefinition(5, 10, 10f,
			new int[][]
			{ 
				{ 0, 0, 0 }, 
				{ 0, 1, 0 },
				{ 1, 1, 1 },
			});
	
//	public static final SegmentDefinition BIG_CRATE_PYRAMID = new SegmentDefinition(1, 5, 10f,
//			new int[][]
//			{ 
//				{ 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 1, 0, 0 }, 
//				{ 0, 1, 1, 1, 0 }, 
//				{ 1, 1, 1, 1, 1 }, 
//			});
//	
//	public static final SegmentDefinition BIG_UPPER_CRATE_PYRAMID = new SegmentDefinition(1, 5, 10f,
//			new int[][]
//			{ 
//				{ 2, 2, 2, 2, 2 }, 
//				{ 0, 2, 2, 2, 0 }, 
//				{ 0, 0, 2, 0, 0 }, 
//				{ 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 0, 0, 0 }, 
//			});
//	
//	public static final SegmentDefinition TWO_SIDE_PYRAMID = new SegmentDefinition(1, 5, 10f,
//			new int[][]
//			{ 
//				{ 0, 2, 2, 2, 0 }, 
//				{ 0, 0, 2, 0, 0 }, 
//				{ 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 1, 0, 0 }, 
//				{ 0, 1, 1, 1, 0 }, 
//			});
//	
//	public static final SegmentDefinition BIG_CRATE_DICK = new SegmentDefinition(1, 5, 10f,
//			new int[][]
//			{ 
//				{ 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 },
//				{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 },
//				{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0 }, 
//				{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, 
//				{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
//			});
//	
//	public static final SegmentDefinition BIG_LOG_PYRAMID = new SegmentDefinition(1, 5, 10f,
//			new int[][]
//			{ 
//				{ 0, 0, 0, 0, 0, 0, 0 }, 
//				{ 0, 0, 0, 3, 0, 0, 0 }, 
//				{ 0, 0, 3, 3, 3, 0, 0 }, 
//				{ 0, 3, 3, 3, 3, 3, 0 }, 
//				{ 3, 3, 3, 3, 3, 3, 3 }, 
//			});
//	
//	public static final SegmentDefinition BIG_BELL_PYRAMID = new SegmentDefinition(1, 5, 10f,
//			new int[][]
//			{ 
//				{ 4, 4, 4, 4, 4, 4, 4 }, 
//				{ 0, 4, 4, 4, 4, 4, 0 }, 
//				{ 0, 0, 4, 4, 4, 0, 0 }, 
//				{ 0, 0, 0, 4, 0, 0, 0 }, 
//				{ 0, 0, 0, 0, 0, 0, 0 }, 
//			});

}
