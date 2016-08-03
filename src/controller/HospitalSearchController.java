/**
 *   APPLICATION: LoginSystem
 *         CLASS: HospitalSearchControl
 *        AUTHOR: Samuel Myles
 *   JDK VERSION: 1.8.0_73
 *   JRE VERSION: 1.8.0_73
 *   APP PURPOSE: Prototype login system that supports a mock user database. Users are given the ability
 *                to create a new account and login from that point forward.
 * CLASS PURPOSE: Designated to be the controller for the hospital.fxml screen. It allows an Excel file of Hospital
 *                properties to be loaded and turned into Hospital objects. At that point this class provides methods
 *                to search the table according to different Hospital properties, as well as displaying row entries
 *                to the user.
 *       PACKAGE: controller
 *     PROFESSOR: Tanes Kanchanawanchai [CSC 202-061N]
 */

package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.DistanceCalculator;
import model.GeoLocation;
import model.Hospital;
import model.HospitalStorage;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import util.graph.GraphOperations;
import util.graph.IWeightedGraph;
import util.graph.WeightedGraph;
import util.queue.Heap;
import util.queue.IPriorityQueue;
import util.tree.BinarySearchTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Random;
import java.util.ResourceBundle;

public class HospitalSearchController implements Initializable{

    @FXML
    private TextField txtfldName;
    @FXML
    private TextField txtfldPhone;
    @FXML
    private TextField txtfldLatitude;
    @FXML
    private TextField txtfldLongitude;
    @FXML
    private TextField txtfldDistance;
    @FXML
    private Label lblError;
    @FXML
    private TableView<Hospital> tableHospitals;
    @FXML
    private Label lblHospitalName;
    @FXML
    private Label lblHospitalAddress;
    @FXML
    private Label lblHospitalPhone;
    @FXML
    private Label lblHospitalLocation;
    @FXML
    private Label lblDistance;
    @FXML
    private ImageView viewImage;

    private Image defaultHospitalImage = new Image(new File("src/resources/defaultHospital.jpg").toURI().toString());

    private GeoLocation userGeoLocation;

    private Random coordinateGenerator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Now create coordinates for the user to simulate their position
        coordinateGenerator = new Random();

        // The bounds of the latitude and longitude keep the user coordinates within the US
        double userLat = 31.0 + (43.0 - 31.0) * coordinateGenerator.nextDouble(); // In range [25* to 50*]
        double userLong = -(65.0 + (125.0 - 65.0) * coordinateGenerator.nextDouble()); // In range [-65* to -125*]

        // A DecimalFormat ensures the double doesn't end up with too many decimal places
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        Point2D userCoordinates = new Point2D(Double.parseDouble(df.format(userLat)), Double.parseDouble(df.format(userLong)));
        userGeoLocation = new GeoLocation("User", userCoordinates, userCoordinates);

        // Create the Hospital BST (sorts by latitude and longitude)
        BinarySearchTree<Hospital> hospitalTree = new BinarySearchTree<>();

        try (FileInputStream fileStream = new FileInputStream("src/resources/HospitalListReformatted.xls")) {

            // Extract the first workbook sheet from the excel file
            HSSFSheet excelSheet = new HSSFWorkbook(fileStream).getSheetAt(0);

            // Variables to hold the data for each hospital
            String hospitalName;
            String street;
            String city;
            String state;
            String zip;
            Double hospitalLat;
            Double hospitalLon;
            String hospitalPhone;
            String hospitalImage;

            // Create a DataFormatter to turn each cell into its appropriate data representation
            final DataFormatter formatter = new DataFormatter();

            Iterator rowIterator = excelSheet.rowIterator();
            while (rowIterator.hasNext()) {
                HSSFRow row = (HSSFRow) rowIterator.next();

                // Extract the data from each cell into the appropriate variable
                hospitalName = formatter.formatCellValue(row.getCell(0));

                // The hasNext method also returns the blank row at the end of the Excel, so test is the
                // hospitalName is empty, indicating the last blank row. If this isn't stopped a NullPointerException
                // will be thrown when the Numeric values try to be extracted
                if (hospitalName.equals("")) {
                    break;
                }

                street = formatter.formatCellValue(row.getCell(1));
                city = formatter.formatCellValue(row.getCell(2));
                state = formatter.formatCellValue(row.getCell(3));
                zip = formatter.formatCellValue(row.getCell(4));
                hospitalLat = row.getCell(5).getNumericCellValue();
                hospitalLon = row.getCell(6).getNumericCellValue();
                hospitalPhone = formatter.formatCellValue(row.getCell(7));
                hospitalImage = formatter.formatCellValue(row.getCell(8));

                // Create a new Hospital object
                Hospital newHospital = new Hospital(hospitalName,
                        street,
                        city,
                        state,
                        zip,
                        hospitalLat,
                        hospitalLon,
                        hospitalPhone,
                        hospitalImage,
                        userCoordinates);

                // Add this hospital to the Hospital BST
                hospitalTree.add(newHospital);
            }

            // Iterate through the hospitalTree and add each Hospital to the TableView
            hospitalTree.reset(hospitalTree.INORDER);
            for (int i = 0; i < hospitalTree.size(); i++) {
                Hospital currentHospital = hospitalTree.getNext(hospitalTree.INORDER);

                tableHospitals.getItems().add(currentHospital);
            }

            // Now set the HospitalStorage database to our hospital BST to be used elsewhere
            HospitalStorage.setHospitalDatabase(hospitalTree);
        }
        catch(IOException ioe){
            lblError.setText("The database could not be opened");
            lblError.setVisible(true);
        }
    }

    @FXML
    public void searchDatabase(){
        lblError.setVisible(false);

        // Extract the original database and create a new BST to hold the results as the database gets filtered
        BinarySearchTree<Hospital> originalDatabase = HospitalStorage.getHospitalDatabase();
        BinarySearchTree<Hospital> filteredDatabase = new BinarySearchTree<>();

        // Create a Heap to hold hospitals that are closest to the user, this Heap is used when the radius filter is set
        IPriorityQueue<GeoLocation> closestHospitals = new Heap<>(originalDatabase.size());

        try {
            originalDatabase.reset(originalDatabase.INORDER);
            for (int i = 0; i < originalDatabase.size(); i++) {
                Hospital nextHospital = originalDatabase.getNext(originalDatabase.INORDER);

                int comparisonResult = 0;

                // In order to filter the entries, each field must first be checked if it was filled out, if it
                //  wasn't that means the user isn't using that field to search for an entry so it can be skipped.
                if (txtfldName != null && !txtfldName.getText().trim().equals("")) {
                    comparisonResult = (txtfldName.getText().compareTo(nextHospital.getName()));
                }

                // At this point comparisonResult is checked if its still 0, meaning the nextHospital still matches
                //  our search parameters, if it is then the next field can be compared. Otherwise nextHospital doesn't
                //  match our search parameters, so we can skip back to the beginning of the loop
                if (txtfldPhone != null && !txtfldPhone.getText().trim().equals("")) {
                    if (comparisonResult == 0)
                        comparisonResult = (txtfldPhone.getText().compareTo(nextHospital.getPhoneNumber()));
                }

                if (txtfldLatitude != null && !txtfldLatitude.getText().trim().equals("")) {
                    if (comparisonResult == 0) {
                        double enteredValue = Double.parseDouble(txtfldLatitude.getText());
                        comparisonResult = Double.compare(enteredValue, nextHospital.getLatitude());
                    }
                }

                if (txtfldLongitude != null && !txtfldLongitude.getText().trim().equals("")) {
                    if (comparisonResult == 0) {
                        double enteredValue = Double.parseDouble(txtfldLongitude.getText());
                        comparisonResult = Double.compare(enteredValue, nextHospital.getLongitude());
                    }
                }

                if(!txtfldDistance.getText().trim().equals("")){
                    if (comparisonResult == 0) {
                        double enteredRadius = Double.parseDouble(txtfldDistance.getText());
                        double hospitalDistanceFromUser = DistanceCalculator.coordinatesToMiles(nextHospital.getLatitude(),
                                                                                                nextHospital.getLongitude(),
                                                                                                userGeoLocation.getCoordinates().getX(),
                                                                                                userGeoLocation.getCoordinates().getY());

                        // Since comparing doubles for equality using the equality (==, <=, >=) operators is inaccurate, the Double
                        // class compare method is used which returns an int based on the comparison, we must check for less than and equal to
                        int distanceResult = Double.compare(hospitalDistanceFromUser, enteredRadius);
                        if(distanceResult < 0 || distanceResult == 0){
                            // Create a new GeoLocation for this Hospital
                            GeoLocation hospitalLocation = new GeoLocation(nextHospital,
                                                                           new Point2D(nextHospital.getLatitude(), nextHospital.getLongitude()),
                                                                           userGeoLocation.getCoordinates());

                            // Now add it to the closestHospital Heap, it will get sorted based on how far away
                            // the hospital is from the user
                            closestHospitals.enqueue(hospitalLocation);
                        }
                        else{ continue; }
                    }
                }

                // If comparisonResult is still zero then the nextHospital matched all criteria, so add it
                if(comparisonResult == 0)
                    filteredDatabase.add(nextHospital);
            }

            //Since the closestHospitals was the most specific filter it must be checked if it has any entries. If so display those.
            if(!closestHospitals.isEmpty()){
                int numOfMidpoints = coordinateGenerator.nextInt((10 - 5) + 1) + 5;

                IWeightedGraph<GeoLocation> locationGraph = new WeightedGraph<>(closestHospitals.size() + numOfMidpoints + 1);
                locationGraph.addVertex(userGeoLocation);

                // Generate a random set of 5-10 coordinates to mimic locations from the user to the hospitals
                for(int r = 0; r < numOfMidpoints; r++){
                    double newLat = 31.0 + (43.0 - 31.0) * coordinateGenerator.nextDouble();
                    double newLong = -(65.0 + (125.0 - 65.0) * coordinateGenerator.nextDouble());

                    String midpointName = "Midpoint" + Integer.toString(r);
                    GeoLocation newMidpoint = new GeoLocation(midpointName, new Point2D(newLat, newLong), userGeoLocation.getCoordinates());

                    locationGraph.addVertex(newMidpoint);
                    locationGraph.addEdge(userGeoLocation, newMidpoint, (int) newMidpoint.getDistanceFromMarker());

                    while(!closestHospitals.isEmpty()){
                        GeoLocation nextHospital = closestHospitals.dequeue();

                        int distanceFromMidToHospital = (int) DistanceCalculator.coordinatesToMiles(newMidpoint.getCoordinates(), nextHospital.getCoordinates());
                        locationGraph.addVertex(nextHospital);
                        locationGraph.addEdge(newMidpoint, nextHospital, distanceFromMidToHospital);
                    }
                }

                // Now determine the shortest paths
                IPriorityQueue<GeoLocation> shortestPaths = GraphOperations.getShortestPaths(locationGraph, userGeoLocation);

                tableHospitals.getItems().clear();
                while(!shortestPaths.isEmpty()){
                    // Since not all the vertexes are Hospitals, we must determine which one is and only show those
                    try{
                        GeoLocation nextLocation = shortestPaths.dequeue();
                        Hospital nextClosestHospital = (Hospital) nextLocation.getItemHere();

                        nextClosestHospital.setPathDistance(nextLocation.getDistanceFromMarker());

                        //nextClosestHospital.setRadius(nextLocation.getDistanceFromMarker());
                        tableHospitals.getItems().add(nextClosestHospital);
                    }
                    catch(ClassCastException e){ }
                }
            }
            // If heap was empty the filteredDatabase must be checked since it is the next most specific filter
            else if(!filteredDatabase.isEmpty()){
                tableHospitals.getItems().clear();
                filteredDatabase.reset(filteredDatabase.INORDER);
                for(int i = 0; i < filteredDatabase.size(); i++) {
                    Hospital nextHospital = filteredDatabase.getNext(filteredDatabase.INORDER);
                    nextHospital.setPathDistance(nextHospital.getRadius());
                    tableHospitals.getItems().add(nextHospital);
                }
            }
            // Otherwise just display the original list of hospitals
            else{
                tableHospitals.getItems().clear();
                lblError.setText("There wasn't a match in the database");
                lblError.setVisible(true);

                originalDatabase.reset(originalDatabase.INORDER);
                for(int i = 0; i < originalDatabase.size(); i++){
                    Hospital currentHospital = originalDatabase.getNext(originalDatabase.INORDER);

                    tableHospitals.getItems().add(currentHospital);
                }
            }
        }
        catch(NumberFormatException nfe){
            lblError.setText("The latitude, longitude, and distance filters must be numbers");
            lblError.setVisible(true);
        }
        catch(Exception e){
            e.printStackTrace();
            lblError.setText("An error occurred while processing the query");
            lblError.setVisible(true);
        }
    }

    @FXML
    public void showRowInformation(){
        try {
            lblError.setVisible(false);

            // Get the Hospital object from the row the user clicked on
            Hospital selectedHospital = tableHospitals.getSelectionModel().getSelectedItem();

            // Now set each of the information labels to the Hospital properties
            lblHospitalName.setText(selectedHospital.getName());
            lblHospitalName.setVisible(true);

            lblHospitalAddress.setText(selectedHospital.getAddress());
            lblHospitalAddress.setVisible(true);

            lblHospitalPhone.setText(selectedHospital.getPhoneNumber());
            lblHospitalPhone.setVisible(true);

            lblHospitalLocation.setText(selectedHospital.getLatitude() + "," + selectedHospital.getLongitude());
            lblHospitalLocation.setVisible(true);

            lblDistance.setText(Double.toString(selectedHospital.getRadius()) + " miles");
            lblDistance.setVisible(true);

            // Now set the hospital image by getting the image from the image url and presenting it
            Image hospitalImage = new Image(selectedHospital.getImage());

            // If an error occurred while loading it then show the default hospital image
            if(hospitalImage.isError()){
                viewImage.setImage(defaultHospitalImage);
            }
            else{
                viewImage.setImage(hospitalImage);
            }
        }
        catch(NullPointerException np){}
    }
}
