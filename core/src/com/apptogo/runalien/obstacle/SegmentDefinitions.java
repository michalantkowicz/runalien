package com.apptogo.runalien.obstacle;

public class SegmentDefinitions {
	//size of one block in segment
	public static final float OBSTACLE_SIZE = 0.7f;
	
	//available blocks in segment
	public static final int EMPTY = 0;
	public static final int CRATE = 1;
	public static final int LOG = 2;
	public static final int BELL = 3;
	
	/** ---------- segment definitions ---------- **/
	
	//notice that there are some not allowed configuration like:
	//1
	//0
	//1
	//algorithm will create 3 in column
	
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
	
	public static final int SMALL_LOG_PYRAMID[][] = 
		{ 
			{ 0, 0, 0 }, 
			{ 0, 2, 0 },
			{ 2, 2, 2 },
		};
	
	public static final int BIG_LOG_PYRAMID[][] = 
		{ 
			{ 0, 0, 0, 0, 0 }, 
			{ 0, 0, 2, 0, 0 }, 
			{ 0, 0, 2, 0, 0 }, 
			{ 0, 2, 2, 2, 0 }, 
			{ 2, 2, 2, 2, 2 }, 
		};
	
	public static final int BELLS[][] = 
		{ 
			{ 3, 3, 3, 3, 3 }, 
			{ 0, 3, 3, 3, 0 }, 
			{ 0, 0, 3, 0, 0 }, 
			{ 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 0 }, 
		};
}
