/**
 *   APPLICATION: LoginSystem
 *         CLASS: GeoLocation
 *        AUTHOR: Samuel Myles
 *   JDK VERSION: 1.8.0_73
 *   JRE VERSION: 1.8.0_73
 *   APP PURPOSE: Prototype login system that supports a mock user database. Users are given the ability
 *                to create a new account and login from that point forward.
 * CLASS PURPOSE: This class is used to represent actual geographical locations. It allows an object of any class to
 *                be encapsulated within this class as an Object to represent the landmark at a certain location, then
 *                allows a user to specify actual geographic coordinates. It also allows two GeoLocations to be compared
 *                based on their distance from a marker. The distance is measured in miles.
 *       PACKAGE: model
 *     PROFESSOR: Tanes Kanchanawanchai [CSC 202-061N]
 */

package model;

import javafx.geometry.Point2D;

public class GeoLocation implements Comparable<GeoLocation>{
    private Point2D coordinates;
    private Object itemAtThisLocation;
    private double distanceFromMarker;
    private GeoLocation previousLocation;
    private GeoLocation nextLocation;

    public GeoLocation(Object locationData, double latitude, double longitude, double markerLat, double markerLong){
        coordinates = new Point2D(latitude, longitude);
        itemAtThisLocation = locationData;

        distanceFromMarker = DistanceCalculator.coordinatesToMiles(latitude, longitude, markerLat, markerLong);
    }

    public GeoLocation(Object locationData, Point2D thisLocation, Point2D markerLocation){
        coordinates = thisLocation;
        itemAtThisLocation = locationData;

        distanceFromMarker = DistanceCalculator.coordinatesToMiles(thisLocation, markerLocation);
    }

    public Point2D getCoordinates(){ return coordinates; }
    public void setCoordinates(Point2D newCoord){ coordinates = newCoord; }
    public void setCoordinates(double newLat, double newLong){ coordinates = new Point2D(newLat, newLong); }

    public double getDistanceFromMarker(){ return distanceFromMarker; }
    public void setMarkerCoordinates(double markLat, double markLong){
        distanceFromMarker = (int) DistanceCalculator.coordinatesToMiles(coordinates, new Point2D(markLat, markLong));
    }
    public void setMarkerCoordinates(Point2D markerCoords){
        setMarkerCoordinates(markerCoords.getX(), markerCoords.getY());
    }

    public void setDistanceFromMarker(double dis){
        distanceFromMarker = dis;
    }

    public GeoLocation getPreviousLocation(){ return previousLocation; }
    public void setPreviousLocation(GeoLocation newLoc){ previousLocation = newLoc; }

    public GeoLocation getNextLocation(){ return nextLocation; }
    public void setNextLocation(GeoLocation newLoc){ nextLocation = newLoc; }

    public Object getItemHere(){ return itemAtThisLocation; }
    public void setItemHere(Object newItem){ itemAtThisLocation = newItem;}

    @Override
    public int compareTo(GeoLocation otherLocation) {
        if(distanceFromMarker > otherLocation.getDistanceFromMarker())
            return -1;
        else if(distanceFromMarker < otherLocation.getDistanceFromMarker())
            return 1;
        else
            return 0;
    }

    @Override
    public String toString(){
        return "GeoLocation -> " + Double.toString(distanceFromMarker);
    }
}
