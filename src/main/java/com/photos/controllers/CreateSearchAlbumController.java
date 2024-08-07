package com.photos.controllers;

import com.photos.model.Picture;
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
 * The CreateSearchAlbumController class manages the user interface
 * for creating a new album from search results. Provides functionality to create and name a new album with search results
 * Displays information alerts for illegal inputs such as duplicate
 * album names or empty album names.
 *
 * @author Ian Underwood, Jason John
 */
public class CreateSearchAlbumController {

    @FXML private TextField createNewSearchAlbumText;
    @FXML private Button createNewSearchAlbum;
    @FXML private Button cancelCreateNewSearchAlbum;

    /**
     * Handles the action event for canceling the creation of a new search album.
     * Closes the current window.
     *
     * @param event The ActionEvent associated with the cancel create new search album action.
     * @throws IOException If there is an issue handling the cancellation.
     */
    public void handleCancelCreateNewSearchAlbum(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.hide();
    }

    /**
     * Handles the action event for creating a new search album with the specified name.
     * Validates the input, checks for duplicate album names, and adds the new album
     * with the search results to the user's album list.
     *
     * @param event The ActionEvent associated with the create new search album action.
     * @throws IOException If there is an issue handling the creation of the new album.
     */
    public void handleCreateNewSearchAlbum(ActionEvent event) throws IOException {
        String albumName = createNewSearchAlbumText.getText();
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
        for(Picture p : SearchController.searchResults){
            newAlbum.addPicture(p);
        }
        UserHomePageController.user.addAlbum(newAlbum);
        UserHomePageController.albumObservableList.add(newAlbum);
        createNewSearchAlbumText.getScene().getWindow().hide();
    }
}
