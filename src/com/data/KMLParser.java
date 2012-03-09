/*
 * Sachin's part
 */

package com.data;
import java.io.File;
import java.io.FileNotFoundException;

import de.micromata.opengis.kml.v_2_2_0.Kml;

public class KMLParser {

	/**
	 * HelloKML Sample project
	 */
	public static void main(String[] args) throws FileNotFoundException {
		final Kml kml = new Kml();
		kml.createAndSetPlacemark()
		   .withName("London, UK").withOpen(Boolean.TRUE)
				.createAndSetPoint().addToCoordinates(-0.126236, 51.500152);
		//marshals to console
		kml.marshal();
		//marshals into file
		kml.marshal(new File("HelloKml.kml"));
	}
}