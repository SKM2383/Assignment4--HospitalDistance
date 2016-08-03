/**
 *   APPLICATION: LoginSystem
 *         CLASS: DistanceCalculator
 *        AUTHOR: Samuel Myles
 *   JDK VERSION: 1.8.0_73
 *   JRE VERSION: 1.8.0_73
 *   APP PURPOSE: Prototype login system that supports a mock user database. Users are given the ability
 *                to create a new account and login from that point forward.
 * CLASS PURPOSE: Provides static methods to determine the distance in miles between two coordinate points using their
 *                latitude and longitude.
 *       PACKAGE: model
 *     PROFESSOR: Tanes Kanchanawanchai [CSC 202-061N]
 */

package model;

import javafx.geometry.Point2D;

public class DistanceCalculator {
    public static double coordinatesToMiles(double latitude1, double longitude1, double latitude2, double longitude2){
        double theta = longitude1 - longitude2;
        double dist = Math.sin(getRadians(latitude1)) * Math.sin(getRadians(latitude2)) +
                      Math.cos(getRadians(latitude1)) * Math.cos(getRadians(latitude2)) *
                      Math.cos(getRadians(theta));

        dist = Math.acos(dist);
        dist = getDegrees(dist);
        dist = dist * 60 * 1.1515;

        return dist;
    }

    public static double coordinatesToMiles(Point2D coord1, Point2D coord2){
        return DistanceCalculator.coordinatesToMiles(coord1.getX(), coord1.getY(), coord2.getX(), coord2.getY());
    }

    public static double getRadians(double deg) { return deg * Math.PI / 180.0; }

    public static double getDegrees(double rad) { return rad * 180.0 / Math.PI; }
}
