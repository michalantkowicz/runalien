package com.apptogo.runalien.obstacle;

public class SegmentDefinitions {

	//available blocks in segment
	public static final int EMPTY = 0;
	public static final int CRATE_BOT = 1;
	public static final int CRATE_TOP = 2;
	public static final int LOG = 3;
	public static final int BELL = 4;
	
	/** ---------- segment definitions ---------- **/

	public static final int BOTTOM_CRATE[][] = 
		{ 
			{ 1 }
		};
	
	public static final int SMALL_CRATE_PYRAMID[][] = 
		{ 
			{ 0, 0, 0 }, 
			{ 0, 1, 0 },
			{ 1, 1, 1 },
		};
	
	public static final int BIG_CRATE_PYRAMID[][] = 
		{ 
			{ 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0 }, 
			{ 0, 0, 1, 0, 0 }, 
			{ 0, 1, 1, 1, 0 }, 
			{ 1, 1, 1, 1, 1 }, 
		};
	
	public static final int BIG_UPPER_CRATE_PYRAMID[][] = 
		{ 
			{ 2, 2, 2, 2, 2 }, 
			{ 0, 2, 2, 2, 0 }, 
			{ 0, 0, 2, 0, 0 }, 
			{ 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0 }, 
		};
	
	public static final int TWO_SIDE_PYRAMID[][] = 
		{ 
			{ 0, 2, 2, 2, 0 }, 
			{ 0, 0, 2, 0, 0 }, 
			{ 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0 }, 
			{ 0, 0, 1, 0, 0 }, 
			{ 0, 1, 1, 1, 0 }, 
		};
	
	public static final int BIG_CRATE_DICK[][] = 
		{ 
			{ 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0 }, 
			{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, 
			{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
		};
	
	public static final int BIG_LOG_PYRAMID[][] = 
		{ 
			{ 0, 0, 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 3, 0, 0, 0 }, 
			{ 0, 0, 3, 3, 3, 0, 0 }, 
			{ 0, 3, 3, 3, 3, 3, 0 }, 
			{ 3, 3, 3, 3, 3, 3, 3 }, 
		};
	
	public static final int BIG_BELL_PYRAMID[][] = 
		{ 
			{ 4, 4, 4, 4, 4, 4, 4 }, 
			{ 0, 4, 4, 4, 4, 4, 0 }, 
			{ 0, 0, 4, 4, 4, 0, 0 }, 
			{ 0, 0, 0, 4, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0 }, 
		};
}
