package com.photos.controllers;

import com.photos.model.Album;
import com.photos.model.Picture;
import com.photos.model.Tag;
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

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The PhotoDisplayController class manages the user interface
 * for displaying and navigating through photos, as well as handling photo-related actions
 * such as adding and removing tags.
 *
 * @author Ian Underwood, Jason John
 */
public class PhotoDisplayController {

    @FXML private Label displayCaption;
    @FXML private Label displayDate;
    @FXML private ImageView displayPhoto;
    @FXML private Button returnToAlbumButton;
    @FXML private TableView<Tag> tagTable;
    @FXML private TableColumn<Tag, String> tagColumn;

    public static ObservableList<Tag> obsTagList = FXCollections.observableArrayList();

    public static Picture picture;
    private Album album = AlbumController.album;
    private int photoPointer;

    /**
     * Initializes the PhotoDisplayController. Sets up the display and populates
     * the necessary information for the selected photo.
     *
     * @throws FileNotFoundException If the file for the selected photo is not found.
     */
    public void initialize() throws FileNotFoundException {
        // Find the index of the selected picture in the album and set the photoPointer
        for (int i = 0; i < album.getPhotos().size(); i++) {
            if (album.getPhotos().get(i).equals(AlbumController.selectedPicture)) {
                photoPointer = i;
            }
        }
        // Set properties for the display elements
        displayCaption.setWrapText(true);
        picture = AlbumController.selectedPicture;
        displayPhoto.setImage(AlbumController.selectedPicture.getImage());
        displayCaption.setText(picture.getCaption());
        displayDate.setText(picture.getFormattedDateTime());
        tagColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        obsTagList.clear();
        obsTagList.addAll(AlbumController.selectedPicture.getTagList());
        tagTable.setItems(obsTagList);
    }

    /**
     * Handles the action event for navigating to the next photo in the album.
     *
     * @param event The ActionEvent associated with the next photo action.
     * @throws FileNotFoundException If the file for the next photo is not found.
     */
    public void handleNextPhoto(ActionEvent event) throws FileNotFoundException {
        // Check if there is a next photo in the album
        if (photoPointer + 1 >= album.getPhotos().size()) {
            photoPointer = 0;
        } else {
            photoPointer++;
        }
        // Update the display with the information for the next photo
        picture = album.getPhotos().get(photoPointer);
        displayPhoto.setImage(picture.getImage());
        displayCaption.setText(picture.getCaption());
        displayDate.setText(picture.getFormattedDateTime());
        obsTagList.clear();
        obsTagList.addAll(picture.getTagList());
    }

    /**
     * Handles the action event for navigating to the previous photo in the album.
     *
     * @param event The ActionEvent associated with the previous photo action.
     * @throws FileNotFoundException If the file for the previous photo is not found.
     */
    public void handlePrevPhoto(ActionEvent event) throws FileNotFoundException {
        // Check if there is a previous photo in the album
        if (photoPointer - 1 < 0) {
            photoPointer = album.getPhotoCount() - 1;
        } else {
            photoPointer--;
        }
        // Update the display with the information for the previous photo
        picture = album.getPhotos().get(photoPointer);
        displayPhoto.setImage(picture.getImage());
        displayCaption.setText(picture.getCaption());
        displayDate.setText(picture.getFormattedDateTime());
        obsTagList.clear();
        obsTagList.addAll(picture.getTagList());
    }

    /**
     * Handles the action event for returning to the album view.
     *
     * @param event The ActionEvent associated with the return to album action.
     * @throws IOException If there is an issue loading the album view.
     */
    public void handleReturnToAlbum(ActionEvent event) throws IOException {
        // Load the album view and display it in a new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/photos/album.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) returnToAlbumButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Handles the action event for removing a selected tag from the photo.
     *
     * @param event The ActionEvent associated with the remove tag action.
     * @throws IOException If there is an issue updating the tag list.
     */
    @FXML
    private void handleRemoveTag(ActionEvent event) throws IOException {
        // Implement the logic for removing a tag from the selected photo
        Tag selectedTag = tagTable.getSelectionModel().getSelectedItem();
        int count = -1;
        if (selectedTag != null) {
            for (Tag t : picture.getTagList()) {
                count++;
                if (t.equals(selectedTag))
                    break;
            }
            picture.getTagList().remove(count);
            obsTagList.clear();
            obsTagList.addAll(picture.getTagList());
        }
    }

    /**
     * Handles the action event for adding a new tag to the photo.
     *
     * @param event The ActionEvent associated with the add tag action.
     * @throws IOException If there is an issue loading the tag editor view.
     */
    public void handleAddTag(ActionEvent event) throws IOException {
        // Load the tag editor view and display it in a new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/photos/tagEditor.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
}





