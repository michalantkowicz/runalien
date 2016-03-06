package com.apptogo.runalien.tools;

public class UnitConverter {

	private static double PPM = 64;

	/**
	 * @param graphicsUnit units for graphics representation. Usually pixels
	 * @return box2d world units. Usually meters
	 */
	public static double toBox2dUnits(double graphicsUnit) {
		return graphicsUnit / PPM;
	}

	/**
	 * @param box2dUnit box2d world units. Usually meters
	 * @return units for graphics representation. Usually pixels
	 */
	public static double toGraphicsUnits(double box2dUnit) {
		return box2dUnit * PPM;
	}
}
