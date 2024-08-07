package com.photos.controllers;

import com.photos.model.Album;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.photos.controllers.UserHomePageController.user;

/**
 * The RenameAlbumController class handles user interactions related to renaming an album in the user interface.
 *
 * @author Ian Underwood, Jason John
 */
public class RenameAlbumController {

    @FXML private TextField renameAlbumText;

    /**
     * Handles the action event triggered when the user attempts to rename an album.
     * @param event The ActionEvent associated with the rename action.
     */
    public void handleRenameAlbum(ActionEvent event) {
        // Initialize count variable to -1
        int count = -1;
        // Retrieve the new album name from the input field
        String albumName = renameAlbumText.getText();

        // Check if the input field is not empty
        if (!albumName.isEmpty()) {
            // Iterate through the user's album list to check for duplicate album names
            for (Album a : user.getUserAlbumList()) {
                if (albumName.equals(a.getAlbumName())) {
                    // Display an information alert for duplicate album name
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Duplicate Album Name");
                    alert.setContentText("An album with this name already exists");
                    alert.showAndWait();
                    return;
                }
            }

            // Rename the selected album, update the observable list, and close the window
            UserHomePageController.selectedAlbum.renameAlbum(albumName);
            UserHomePageController.albumObservableList.clear();
            UserHomePageController.albumObservableList.addAll(UserHomePageController.user.getUserAlbumList());
            renameAlbumText.getScene().getWindow().hide();
        } else {
            // Display an information alert for illegal input (empty album name)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Illegal input");
            alert.setContentText("Enter a new album name");
            alert.showAndWait();
            return;
        }
    }

    /**
     * Handles the action event triggered when the user cancels the album renaming process.
     * @param event The ActionEvent associated with the cancel action.
     */
    public void handleCancelRenameAlbum(ActionEvent event) {
        // Retrieve the stage from the event source and hide it
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
    }
}

