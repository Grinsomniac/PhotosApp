package com.photos.controllers;

import com.photos.model.Album;
import com.photos.model.Picture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;

/**
 * The MovePhotoController class manages the user interface
 * for moving a selected photo to another album. It provides functionality to
 * initialize the controller, handle the cancellation of the move operation,
 * and confirm the move operation by selecting a destination album.
 *
 * @author Ian Underwood, Jason John
 */
public class MovePhotoController {

    @FXML ComboBox<Album> albumMoveList;
    ObservableList<Album> obsMoveList = FXCollections.observableArrayList();

    /**
     * Initializes the MovePhotoController. Sets up the ComboBox for selecting
     * the destination album and populates it with the user's album list.
     */
    public void initialize() {
        // Set up the ComboBox converter to display album names
        albumMoveList.setConverter(new StringConverter<Album>() {
            @Override
            public String toString(Album album) {
                return album.getAlbumName();
            }

            @Override
            public Album fromString(String s) {
                return null;
            }
        });

        // Populate the ComboBox with the user's album list
        obsMoveList.addAll(UserHomePageController.user.getUserAlbumList());
        albumMoveList.setItems(obsMoveList);
    }

    /**
     * Handles the action event for canceling the move operation.
     * Redirects the user back to the album view.
     *
     * @param event The ActionEvent associated with the cancel move action.
     * @throws IOException If there is an issue loading the album view.
     */
    public void handleCancelMove(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/photos/album.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Handles the action event for confirming the move operation.
     * Moves the selected photo to the chosen destination album.
     *
     * @param event The ActionEvent associated with the move confirm action.
     * @throws IOException If there is an issue loading the album view.
     */
    public void handleMoveConfirm(ActionEvent event) throws IOException {

        // Get the selected destination album
        Album moveAlbum = albumMoveList.getSelectionModel().getSelectedItem();
        if (moveAlbum == null) {
            // Display an information alert for illegal input (no destination album selected)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Illegal input");
            alert.setContentText("Select an album to move the photo into");
            alert.showAndWait();
            return;
        }

        // Check for duplicate photos in the destination album
        for (Picture p : moveAlbum.getPhotos()) {
            if (p.getFile().equals(AlbumController.selectedPicture.getFile())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Duplicate Photo");
                alert.setContentText("The photo is already in this album");
                alert.showAndWait();
                return;
            }
        }

        // Move the selected photo to the destination album and update the view
        moveAlbum.addPicture(AlbumController.selectedPicture);
        AlbumController.album.getPhotos().remove(AlbumController.selectedPicture);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/photos/album.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
}

