package org.droidplanner.android.gcs.follow;

import org.droidplanner.core.drone.Drone;
import org.droidplanner.core.helpers.coordinates.Coord2D;
import org.droidplanner.core.helpers.geoTools.GeoTools;
import org.droidplanner.core.helpers.units.Length;

import android.location.Location;
import android.util.Log;

public class FollowLeash extends FollowAlgorithm {

	public FollowLeash(Drone drone, Length radius) {
		super(drone, radius);
	}

	@Override
	public FollowModes getType() {
		return FollowModes.LEASH;
	}

	@Override
	public void processNewLocation(Location location) {
		Coord2D gcsCoord = new Coord2D(location.getLatitude(), location.getLongitude());
		
		Coord2D testLocation = new Coord2D(37.85775,-122.29285);
		double error = GeoTools.getDistance(gcsCoord, testLocation).valueInMeters();
		Log.d("gpserror",Double.toString(error));
		Log.d("gpserror","fusedEstimate: "+location.getAccuracy());
		
		if (GeoTools.getDistance(gcsCoord, drone.GPS.getPosition()).valueInMeters() > radius
				.valueInMeters()) {
			double headingGCStoDrone = GeoTools.getHeadingFromCoordinates(gcsCoord,
					drone.GPS.getPosition());
			Coord2D goCoord = GeoTools.newCoordFromBearingAndDistance(gcsCoord, headingGCStoDrone,
					radius.valueInMeters());
			drone.guidedPoint.newGuidedCoord(goCoord);
		}
	}

}
