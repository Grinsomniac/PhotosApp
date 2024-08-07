package com.photos.controllers;

import com.photos.model.Picture;
import com.photos.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import com.photos.model.Album;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
/**
 * The CopyController class manages the user interface
 * for copying a selected photo to another album.  Displays information alerts
 * for illegal inputs such as not selecting a destination album or attempting
 * to copy a photo that already exists in the destination album.
 *
 * @author Ian Underwood, Jason John
 */
public class CopyController {

    @FXML ComboBox<Album> albumCopyList;
    ObservableList<Album> obsCopyList = FXCollections.observableArrayList();

    /**
     * Initializes the CopyController. Sets up the ComboBox for selecting
     * the destination album and populates it with the user's album list.
     */
    public void initialize() {
        albumCopyList.setConverter(new StringConverter<Album>() {
            @Override
            public String toString(Album album) {
                return album.getAlbumName();
            }
            @Override
            public Album fromString(String s) {
                return null;
            }
        });
        obsCopyList.addAll(UserHomePageController.user.getUserAlbumList());
        albumCopyList.setItems(obsCopyList);
    }
    /**
     * Handles the action event for confirming the copy operation.
     * Copies the selected photo to the chosen destination album.
     *
     * @param event The ActionEvent associated with the copy confirm action.
     * @throws IOException If there is an issue loading the album view.
     */
    public void handleCopyConfirm(ActionEvent event) throws IOException {
        Album copyAlbum = albumCopyList.getSelectionModel().getSelectedItem();
        if(copyAlbum == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Illegal input");
            alert.setContentText("Select an album to copy photo into");
            alert.showAndWait();
            return;
        }
        for(Picture p : copyAlbum.getPhotos()){
            if(p.getFile().equals(AlbumController.selectedPicture.getFile())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Duplicate Photo");
                alert.setContentText("The photo is already in this album");
                alert.showAndWait();
                return;
            }
        }
        copyAlbum.addPicture(AlbumController.selectedPicture);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/photos/album.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
    /**
     * Handles the action event for canceling the copy operation.
     * Redirects the user back to the album view.
     *
     * @param event The ActionEvent associated with the cancel copy action.
     * @throws IOException If there is an issue loading the album view.
     */
    public void handleCancelCopy(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/photos/album.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
}
