package com.apptogo.runalien.level.segment;

import com.apptogo.runalien.physics.BodyBuilder;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.math.Vector2;

public class SegmentGenerator {
	private final SegmentFieldDefinitions segmentFieldDefinitions = new SegmentFieldDefinitions();
	
	public Segment getSegment(SegmentDefinition segmentDefinition) {
		int length = segmentDefinition.getDefinition().length;
		int[][] definition = new int[length][segmentDefinition.getDefinition()[0].length];
	    for (int i = 0; i < length; i++) {
	        System.arraycopy(segmentDefinition.getDefinition()[i], 0, definition[i], 0, segmentDefinition.getDefinition()[i].length);
	    }
		
		//TODO exception for maximum size
	    //TODO set name for each segment by type
		Segment segment = new Segment(segmentDefinition.getName(), segmentDefinition.getMinLevel(), segmentDefinition.getMaxLevel(), segmentDefinition.getBaseOffset() + SegmentFieldDefinitions.OBSTACLE_SIZE * definition[0].length);

		for (int i = 0; i < definition[0].length; i++) {
			for (int j = definition.length - 1; j >= 0; j--) {
				//iteration through columns(bottom->top) -> rows(left->right) because we need column blocks like bells or logs
				int value = definition[j][i];
				float positionX = i * SegmentFieldDefinitions.OBSTACLE_SIZE;
				float positionY = (definition.length - j - 1) * SegmentFieldDefinitions.OBSTACLE_SIZE;

				Vector2 transform = new Vector2(positionX, positionY);

				String fieldName = null;
				BodyBuilder fieldBodyDef = null;

				if(value == SegmentDefinitions.EMPTY) {
					//Do nothing
				}
				else {
					if(value == SegmentDefinitions.CRATE_BOT) {
						fieldName = segmentFieldDefinitions.CRATE;
						fieldBodyDef = segmentFieldDefinitions.CRATE_BODY;
					}
					else if(value == SegmentDefinitions.CRATE_TOP) {
						fieldName = segmentFieldDefinitions.CRATE;
						fieldBodyDef = segmentFieldDefinitions.CRATE_BODY;
					}
					else if(value == SegmentDefinitions.BIG_CRATE_BOT){
						if(j-1>=0 && i+1<definition[0].length && definition[j-1][i] == SegmentDefinitions.BIG_CRATE_BOT
								&& definition[j][i+1] == SegmentDefinitions.BIG_CRATE_BOT
								&& definition[j-1][i+1] == SegmentDefinitions.BIG_CRATE_BOT){
							
							//clear fields when big crate already used it
							definition[j-1][i] = SegmentDefinitions.EMPTY;
							definition[j][i+1] = SegmentDefinitions.EMPTY;
							definition[j-1][i+1] = SegmentDefinitions.EMPTY;
							
							fieldName = segmentFieldDefinitions.BIG_CRATE;
							fieldBodyDef = segmentFieldDefinitions.BIG_CRATE_BODY;	
						}
					}
					else if(value == SegmentDefinitions.BIG_CRATE_TOP){
						if(j-1>=0 && i+1<definition[0].length && definition[j-1][i] == SegmentDefinitions.BIG_CRATE_TOP
								&& definition[j][i+1] == SegmentDefinitions.BIG_CRATE_TOP
								&& definition[j-1][i+1] == SegmentDefinitions.BIG_CRATE_TOP){
							
							definition[j-1][i] = SegmentDefinitions.EMPTY;
							definition[j][i+1] = SegmentDefinitions.EMPTY;
							definition[j-1][i+1] = SegmentDefinitions.EMPTY;
							
							fieldName = segmentFieldDefinitions.BIG_CRATE;
							fieldBodyDef = segmentFieldDefinitions.BIG_CRATE_BODY;	
						}
					}
					else if(value == SegmentDefinitions.LOG) {
						//check how many in column
						int columnCounter = 1;
						for (int k = j - 1; k >= 0; k--) {
							if (definition[k][i] == SegmentDefinitions.LOG && columnCounter < 4) {
								columnCounter++;
							} else {
								break;
							}
						}
						//modify column index (jump to the top of current obstacle)
						j -= (columnCounter - 1);
						
						switch (columnCounter) {
						case 1:
							fieldName = segmentFieldDefinitions.LOG_1;
							fieldBodyDef = segmentFieldDefinitions.LOG1_BODY;
							break;
						case 2:
							fieldName = segmentFieldDefinitions.LOG_2;
							fieldBodyDef = segmentFieldDefinitions.LOG2_BODY;
							break;
						case 3:
							fieldName = segmentFieldDefinitions.LOG_3;
							fieldBodyDef = segmentFieldDefinitions.LOG3_BODY;
							break;
						case 4:
							fieldName = segmentFieldDefinitions.LOG_4;
							fieldBodyDef = segmentFieldDefinitions.LOG4_BODY;
							break;
						}
					}
					else if(value == SegmentDefinitions.BELL) {
						//check how many in column
						int columnCounter = 1;
						for (int k = j - 1; k >= 0; k--) {
							if (definition[k][i] == SegmentDefinitions.BELL && columnCounter < 7) {
								columnCounter++;
							} else {
								break;
							}
						}
						//modify column index (jump to the top of current obstacle)
						j -= (columnCounter - 1);
						
						switch (columnCounter) {
						case 1:
							fieldName = segmentFieldDefinitions.BELL_1;
							fieldBodyDef = segmentFieldDefinitions.BELL1_BODY;
							break;
						case 2:
							fieldName = segmentFieldDefinitions.BELL_2;
							fieldBodyDef = segmentFieldDefinitions.BELL2_BODY;
							break;
						case 3:
							fieldName = segmentFieldDefinitions.BELL_3;
							fieldBodyDef = segmentFieldDefinitions.BELL3_BODY;
							break;
						case 4:
							fieldName = segmentFieldDefinitions.BELL_4;
							fieldBodyDef = segmentFieldDefinitions.BELL4_BODY;
							break;
						case 5:
							fieldName = segmentFieldDefinitions.BELL_5;
							fieldBodyDef = segmentFieldDefinitions.BELL5_BODY;
							break;
						case 6:
							fieldName = segmentFieldDefinitions.BELL_6;
							fieldBodyDef = segmentFieldDefinitions.BELL6_BODY;
							break;
						case 7:
							fieldName = segmentFieldDefinitions.BELL_7;
							fieldBodyDef = segmentFieldDefinitions.BELL7_BODY;
							break;
						}
					}
					segment.addField(fieldName, fieldBodyDef, transform);
				}
			}
		}
		return segment;
	}
}
