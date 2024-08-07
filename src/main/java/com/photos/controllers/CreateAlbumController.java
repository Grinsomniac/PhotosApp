package com.photos.controllers;

import com.photos.model.User;
import com.photos.model.UserList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import java.io.IOException;
import com.photos.model.Album;

/**
 * The CreateAlbumController class manages the user interface for creating a new album.
 * Provides functionality to name and create a new album
 * Displays information alerts for illegal inputs such as duplicate album names or empty album names.
 * Additionally, saves the updated user list after creating a new album.
 *
 * @author Ian Underwood, Jason John
 */
public class CreateAlbumController {

@FXML private TextField createNewAlbumText;
@FXML private Button createNewAlbum;
@FXML private Button cancelCreateNewAlbum;

    /**
     * Handles the action event for canceling the creation of a new album.
     * Closes the current window.
     *
     * @param event The ActionEvent associated with the cancel create new album action.
     * @throws IOException If there is an issue handling the cancellation.
     */
    public void handleCancelCreateNewAlbum(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.hide();
    }

    /**
     * Handles the action event for creating a new album with the specified name.
     * Validates the input, checks for duplicate album names, and adds the new album
     * to the user's album list. Displays information alerts for illegal inputs such as
     * duplicate album names or empty album names. Additionally, saves the updated user list.
     *
     * @param event The ActionEvent associated with the create new album action.
     * @throws IOException If there is an issue handling the creation of the new album.
     */
    public void handleCreateNewAlbum(ActionEvent event) throws IOException {
        String albumName = createNewAlbumText.getText();
        for(Album a : UserHomePageController.user.getUserAlbumList()){
            if(a.getAlbumName().equals(albumName)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Duplicate Album Name");
                alert.setContentText("An album with this name already exists");
                alert.showAndWait();
                return;
            }
        }
        if(albumName.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Illegal input");
            alert.setContentText("You must enter a new album name");
            alert.showAndWait();
            return;
        }
        Album newAlbum = new Album(albumName);
        UserHomePageController.user.addAlbum(newAlbum);
        UserHomePageController.albumObservableList.add(newAlbum);
        createNewAlbumText.getScene().getWindow().hide();
        UserList.saveUserList();
    }
}
