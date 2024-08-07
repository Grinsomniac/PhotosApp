package com.photos.controllers;

import com.photos.model.Album;
import com.photos.model.Tag;
import com.photos.model.User;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import com.photos.model.Picture;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import java.io.IOException;
/**
 * The SearchController class handles the user interface and logic for searching pictures in the photo application.
 * It allows users to search for pictures based on a Date Range or Tags.
 *
 * @author Ian Underwood, Jason John
 */
public class SearchController {

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField tagTextField;
    @FXML private TableView<Picture> photoTable;
    @FXML private TableColumn<Picture, ImageView> photoColumn;
    @FXML private TableColumn<Picture, String> captionColumn;

    public ObservableList<Picture> obsSearchList = FXCollections.observableArrayList();
    public static List<Picture> searchResults = new ArrayList<>();
    private Date startDate;
    private Date endDate;

    /**
     * Initializes the search view, setting up the table columns and their respective cell factories.
     */
    public void initialize(){

        photoColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleObjectProperty<>(cellData.getValue().getImageView());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        captionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCaption()));

    }

    /**
     * Handles the action event for returning to the user's home page.
     *
     * @param event The ActionEvent triggered by the button.
     * @throws IOException If an error occurs while loading the user's home page.
     */
    public void handleSearchHome(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/photos/UserHomePage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

    }

    /**
     * Converts a LocalDate object to a Date object.
     *
     * @param localDate The LocalDate to convert.
     * @return The equivalent Date object.
     */
    private Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Handles the action event for creating a new album based on the search results.
     *
     * @param actionEvent The ActionEvent triggered by the button.
     * @throws IOException If an error occurs while loading the create album view.
     */
    public void handleSearchCreateAlbum(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/photos/createSearchAlbum.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Handles the action event for searching pictures based on tags.
     *
     * @param event The ActionEvent triggered by the button.
     */
    public void handleSearchByTag(ActionEvent event) {
        // Retrieve search criteria
        searchResults.clear();

        String tagText = tagTextField.getText().trim();

        if(tagText.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Enter Tag(s) to Search");
            alert.setContentText("Format is (TagName=TagValue) or (TagName=TagValue AND/OR TagName=TagValue)");
            alert.showAndWait();
            return;

        }
        // Search based on criteria
        String[] splitString = tagText.split("\\s+(AND|OR)\\s+");

        if (splitString.length == 2) {
            // Code for handling AND or OR case
            String operator = tagText.contains("AND") ? "AND" : "OR";

            searchTwoTagValue(splitString[0], operator, splitString[1]);

        } else if (splitString.length == 1) {
            // Code for handling single key-value pair
            searchOneTagValue(splitString[0]);

        }

        obsSearchList.clear();
        obsSearchList.addAll(searchResults);
        photoTable.setItems(obsSearchList);
        if(obsSearchList.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No photos matching search");
            alert.setContentText("Format is (TagName=TagValue) or (TagName=TagValue AND/OR TagName=TagValue)");
            alert.showAndWait();
            return;
        }
    }

    /**
     * Searches for pictures based on a single tag value and adds matching pictures to the search results.
     *
     * @param tag The tag value to search for.
     */
    private void searchOneTagValue(String tag){
        for(Album a : UserHomePageController.user.getUserAlbumList()){
            for(Picture p : a.getPhotos()){
                for(Tag t: p.getTagList()){
                    if(t.toString().equals(tag)){
                        if(!searchResults.contains(p)) {
                            searchResults.add(p);
                        }
                    }
                }
            }
        }
    }

    /**
     * Searches for pictures based on two tag values and a logical operator (AND/OR), adding matching pictures to the search results.
     *
     * @param tag1     The first tag value to search for.
     * @param operator The logical operator (AND/OR) to apply between the two tag values.
     * @param tag2     The second tag value to search for.
     */
    private void searchTwoTagValue(String tag1, String operator, String tag2) {
        for (Album a : UserHomePageController.user.getUserAlbumList()) {
            for (Picture p : a.getPhotos()) {
                boolean match1 = false;
                boolean match2 = false;
                for (Tag t : p.getTagList()) {
                    if (t.toString().equals(tag1)) {
                        match1 = true;
                    }
                    if (t.toString().equals(tag2)) {
                        match2 = true;
                    }
                }
                if (operator.equals("AND") && match1 && match2) {
                    if(!searchResults.contains(p)) {
                        searchResults.add(p);
                    }
                } else if (operator.equals("OR") && (match1 || match2)) {
                    if(!searchResults.contains(p)) {
                        searchResults.add(p);
                    }
                }
            }
        }
    }

    /**
     * Searches for pictures based on a date range and adds matching pictures to the search results.
     */
    public void handleSearchByDate(){
        searchResults.clear();
        if(startDatePicker.getValue() == null || endDatePicker.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Empty Date Range");
            alert.setContentText("Enter a start date and end date");
            alert.showAndWait();
            return;
        }else{
            this.startDate = convertLocalDateToDate(startDatePicker.getValue());
            this.endDate = convertLocalDateToDate(endDatePicker.getValue());
        }
        for(Album a : UserHomePageController.user.getUserAlbumList()){
            for(Picture p : a.getPhotos()){
                if(p.inDateRange(startDate,endDate)){
                    if(!searchResults.contains(p)) {
                        searchResults.add(p);
                    }
                }
            }
        }
        obsSearchList.clear();
        obsSearchList.addAll(searchResults);
        photoTable.setItems(obsSearchList);
    }
}
