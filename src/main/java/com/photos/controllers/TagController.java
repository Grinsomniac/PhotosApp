package com.photos.controllers;

import com.photos.model.Picture;
import com.photos.model.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

/**
 * The {@code TagController} class handles user interactions and logic related to creating tags for pictures.
 * It provides methods for confirming the creation of a new tag, canceling the tag creation, and validating tag names and values.
 *
 * This controller is associated with the "createTag.fxml" FXML file.
 *
 * @author Ian Underwood, Jason John
 */
public class TagController {
    @FXML private TextField createTagName;
    @FXML private TextField createTagValue;
    @FXML private Button cancelCreateTag;
    @FXML private Button confirmTagCreate;

    /**
     * Handles the cancel action for tag creation, closing the current window.
     *
     * @param event The ActionEvent triggered by the cancel button.
     */
    public void handleCancelTagCreate(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.hide();
    }

    /**
     * Handles the confirmation action for tag creation. Validates input, checks for existing tags, and adds the new tag to the picture.
     *
     * @param event The ActionEvent triggered by the confirm button.
     */
    public void handleTagConfirm(ActionEvent event) {
        String tagName = createTagName.getText().trim();
        String tagValue = createTagValue.getText().trim();
        List<Tag> tempTagList = PhotoDisplayController.picture.getTagList();

        // Check if either tag name or tag value is empty
        if (tagName.isEmpty() || tagValue.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incomplete Tag Information");
            alert.setHeaderText(null);
            alert.setContentText("Please enter both tag name and tag value.");
            alert.showAndWait();
            return;
        }
        // Check if the tag already exists
        if (tempTagList.stream().anyMatch(tag -> tag.getName().equals(tagName) && tag.getValue().equals(tagValue))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tag Already Exists");
            alert.setHeaderText(null);
            alert.setContentText("The tag you're trying to create already exists. Please choose a different value.");
            alert.showAndWait();
        } else {
            // Validate tagName and tagValue
            if (!isValidTagName(tagName) || !isValidTagValue(tagValue)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Tag");
                alert.setHeaderText(null);
                alert.setContentText("Invalid tag name or value. Please use only uppercase/lowercase characters, numbers, and limit the length to 20 characters.");
                alert.showAndWait();
                return;
            }
            // Tag does not exist, add it
            PhotoDisplayController.picture.addTag(tagName, tagValue);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            PhotoDisplayController.obsTagList.clear();
            PhotoDisplayController.obsTagList.addAll(PhotoDisplayController.picture.getTagList());
            stage.hide();
        }
    }

    /**
     * Validates the given tag name.
     *
     * @param tagName The tag name to validate.
     * @return True if the tag name is valid; otherwise, false.
     */
    private boolean isValidTagName(String tagName) {
        return tagName.matches("^[a-zA-Z0-9]{1,20}$");
    }

    /**
     * Validates the given tag value.
     *
     * @param tagValue The tag value to validate.
     * @return True if the tag value is valid; otherwise, false.
     */
    private boolean isValidTagValue(String tagValue) {
        return tagValue.matches("^[a-zA-Z0-9]{1,20}$");
    }
}
