/**
 *   APPLICATION: LoginSystem
 *         CLASS: Hospital
 *        AUTHOR: Samuel Myles
 *   JDK VERSION: 1.8.0_73
 *   JRE VERSION: 1.8.0_73
 *   APP PURPOSE: Prototype login system that supports a mock user database. Users are given the ability
 *                to create a new account and login from that point forward.
 * CLASS PURPOSE: Defines the data model for a Hospital. Includes properties such as name, address, etc. Also provides
 *                getters and setters as well as a compareTo method that determines the order of this Hospital and another
 *                based on latitude and longitude respectively
 *       PACKAGE: controller
 *     PROFESSOR: Tanes Kanchanawanchai [CSC 202-061N]
 */

package model;

import javafx.geometry.Point2D;

public class Hospital implements Comparable<Hospital>{
   private String name;
   private String street;
   private String city;
   private String state;
   private String zipCode;
   private String phoneNumber;
   private String image;
   private double latitude;
   private double longitude;
   private int radius;
   private int pathDistance;
   
   public Hospital(String name, String street,String city, String state, String zip, double lat, double lon, String phone, String imageUrl, Point2D userCoords){
      this.name = name;

      // Set all the address variables
      this.street = street;
      this.city = city;
      this.state = state;
      this.zipCode = zip;

      // Then set the location variables
      this.latitude = lat;
      this.longitude = lon;

      // Find the distance from the user
      this.radius = (int) DistanceCalculator.coordinatesToMiles(latitude, longitude, userCoords.getX(), userCoords.getY());
      this.pathDistance = (int) DistanceCalculator.coordinatesToMiles(latitude, longitude, userCoords.getX(), userCoords.getY());

      // Set the misc variables
      this.phoneNumber = phone;
      this.image = imageUrl;
   }
   
   public String getName(){ return this.name; }
   public void setName(String newName){ this.name = newName; }

   public String getStreet(){ return this.street; }
   public void setStreet(String newStreet){ this.street = newStreet; }

   public String getCity(){ return this.city; }
   public void setCity(String newCity){ this.city = newCity; }

   public String getState(){ return this.state; }
   public void setState(String newState){ this.state = newState; }

   public String getZipCode(){ return this.zipCode; }
   public void setZipCode(String newZip){ this.zipCode = newZip; }

   public String getAddress(){
      return street + " " + city + "," + state + " " + zipCode;
   }

   public double getLatitude(){ return this.latitude; }
   public void setLatitude(double newLat){ this.latitude = newLat; }

   public double getLongitude(){ return this.longitude; }
   public void setLongitude(double newLon){ this.longitude = newLon; }

   public int getRadius(){ return this.radius; }
   public void setRadius(double theLat, double theLong){
      this.radius = (int) DistanceCalculator.coordinatesToMiles(latitude, longitude, theLat, theLong);
   }
   public void setRadius(double markerDistance){
      this.radius = (int) markerDistance;
   }

   public int getPathDistance(){ return this.pathDistance; }
   public void setPathDistance(double distance){
      this.pathDistance = (int) distance;
   }

   public String getPhoneNumber(){ return this.phoneNumber; }
   public void setPhoneNumber(String phone){ this.phoneNumber = phone; }
   
   public String getImage(){ return this.image; }
   public void setImage(String imageLink){ this.image = imageLink; }
   
   @Override
   public int compareTo(Hospital otherHospital){
      int compareResult = Double.compare(this.getLatitude(), otherHospital.getLatitude());
      if(compareResult != 0){ return compareResult; }
      
      compareResult = Double.compare(this.getLongitude(), otherHospital.getLongitude());
      if(compareResult != 0){ return compareResult; }
      
      return 0;
   }

   @Override
   public String toString(){
       return ("[" + name + "," +
               latitude + "::" +
               longitude + "," +
               radius + "," +
               phoneNumber + "," +
               image + "]");
   }
}