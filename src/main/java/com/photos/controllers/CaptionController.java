package com.photos.controllers;

import com.photos.model.Album;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.photos.controllers.AlbumController;

/**
 * The CaptionController class manages the user interface
 * for captioning a selected photo. Displays information alerts for illegal inputs such
 * as entering a caption that exceeds the maximum length or attempting to caption
 * without providing a new caption.
 *
 * @author Ian Underwood, Jason John
 */
public class CaptionController {

    @FXML private TextArea captionText;

    /**
     * Initializes the CaptionController. Enables text wrapping for the caption text area.
     */
    public void initialize(){
        captionText.setWrapText(true);
    }

    /**
     * Handles the action event for captioning a photo with the provided text.
     * Validates the input, checks the length of the caption, and updates the
     * selected photo's caption. Displays information alerts for illegal inputs
     * such as exceeding the maximum caption length or attempting to caption without
     * providing a new caption.
     *
     * @param event The ActionEvent associated with the caption photo action.
     */
    public void handleCaptionPhoto(ActionEvent event){

        String newCaption = captionText.getText();
        if(!newCaption.isEmpty()) {
            if(newCaption.length() > 250){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Too many characters");
                alert.setContentText("Maximum caption length is 250 characters");
                alert.showAndWait();
            }else{
                AlbumController.selectedPicture.setCaption(newCaption);
                AlbumController.obsPictureList.clear();
                AlbumController.obsPictureList.addAll(AlbumController.album.getPhotos());
                captionText.getScene().getWindow().hide();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Illegal input");
            alert.setContentText("Enter a new caption");
            alert.showAndWait();
        }
    }

    /**
     * Handles the action event for canceling the captioning process.
     * Hides the current window without making any changes.
     *
     * @param event The ActionEvent associated with the cancel caption action.
     */
    public void handleCancelCaption(ActionEvent event){

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.hide();
    }
}
