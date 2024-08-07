package com.photos.controllers;

import com.photos.model.Album;
import com.photos.model.Picture;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Controller class for managing the display and actions related to a specific album.
 * Handles the initialization of the photo display, setting up the table columns, cell factories,
 * and observable picture list. Populates the photo table with pictures from the
 * current album.
 *
 * @author Jason John, Ian Underwood
 */
public class AlbumController {

    @FXML private TableView<Picture> photoTable;
    @FXML private TableColumn<Picture, ImageView> photoColumn;
    @FXML private TableColumn<Picture, String> captionColumn;
    @FXML private Button addPhoto;
    @FXML private Button removePhoto;
    @FXML private Button captionPhoto;
    @FXML private Button displayPhoto;
    @FXML private Button copyPhoto;
    @FXML private Button movePhoto;
    @FXML private Button returnToHome;
    @FXML private Button addTag;
    @FXML private Button removeTag;

    public static Album album;
    public static Picture selectedPicture;
    public static ObservableList<Picture> obsPictureList = FXCollections.observableArrayList();

    /**
     * Initializes the photo display, setting up the table columns, cell factories,
     * and observable picture list. Populates the photo table with pictures from the
     * current album.
     *
     * @throws FileNotFoundException If there is an issue loading images.
     */
    public void initialize() throws FileNotFoundException {
        // Set up photo column with image views
        photoColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleObjectProperty<>(cellData.getValue().getImageView());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        // Set up caption column with multi-line cell factory
        captionColumn.setCellFactory(getMultiLineCellFactory());
        captionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCaption()));

        // Clear and add all pictures from the album to the observable picture list
        obsPictureList.clear();
        obsPictureList.addAll(album.getPhotos());

        // Set the observable picture list as the data source for the photo table
        photoTable.setItems(obsPictureList);
    }

    /**
     * Returns a multi-line cell factory for the caption column.
     *
     * @return The multi-line cell factory.
     */
    private Callback<TableColumn<Picture, String>, TableCell<Picture, String>> getMultiLineCellFactory() {
        return column -> {
            TableCell<Picture, String> cell = new TableCell<Picture,String>() {
                private final Label textArea = new Label();
                {
                    textArea.setWrapText(true);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        textArea.setText(item);
                        setText(null);
                        setGraphic(textArea);
                    }
                }
            };
            return cell;
        };
    }

    /**
     * Handles the action event for adding a new photo to the album.
     * Opens a file chooser dialog for selecting an image file.
     * Checks for duplicates in the current album and across all user albums.
     * Displays an information alert for duplicate photos.
     *
     * @param event The ActionEvent associated with the add photo action.
     * @throws IOException If there is an issue handling the photo addition.
     */
    @FXML
    private void addPhoto(ActionEvent event) throws IOException {
        // Open a file chooser dialog for selecting an image file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog((Stage) ((Button) event.getSource()).getScene().getWindow());
        // Check for duplicate photos in the current album
        for (Picture p : album.getPhotos()) {
            if (p.getFile().equals(selectedFile)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Illegal input");
                alert.setContentText("This photo already exists in the album");
                alert.showAndWait();
                return;
            }
        }
        // Check for duplicate photos across all user albums
        for (Album a : UserHomePageController.user.getUserAlbumList()) {
            for (Picture p : a.getPhotos()) {
                if (p.getFile().equals(selectedFile)) {
                    album.addPicture(p);
                    photoTable.getItems().add(p);
                    return;
                }
            }
        }
        // Add the new picture to the album and update the photo table
        if (selectedFile != null) {
            Picture newPicture = new Picture(selectedFile.toString(), selectedFile);
            album.addPicture(newPicture);
            photoTable.getItems().add(newPicture);
        }
    }

    /**
     * Removes the selected photo from the album. Displays an information alert
     * if no photo is selected.
     */
    @FXML
    private void removePhoto() {
        TableView.TableViewSelectionModel<Picture> selectionModel = photoTable.getSelectionModel();
        if (selectionModel.getSelectedCells().isEmpty()) {
            return;
        } else {
            Picture picture = selectionModel.getSelectedItem();
            int delete = -1;
            int count = 0;
            for (Picture p : album.getPhotos()) {
                if (p.equals(picture)) {
                    delete = count;
                    photoTable.getItems().remove(p);
                }
                count++;
            }
            if (delete != -1) {
                album.getPhotos().remove(delete);
            }
        }
    }

    /**
     * Handles the action event for captioning the selected photo. Opens a new
     * window for captioning the photo. Displays an information alert if no
     * photo is selected.
     *
     * @param event The ActionEvent associated with the caption photo action.
     * @throws IOException If there is an issue handling the captioning.
     */
    @FXML
    private void captionPhoto(ActionEvent event) throws IOException {
        selectedPicture = photoTable.getSelectionModel().getSelectedItem();
        if (selectedPicture != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/photos/caption.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No photo selected");
            alert.setContentText("Select a photo to caption");
            alert.showAndWait();
            return;
        }
    }

    /**
     * Handles the action event for displaying the selected photo. Opens a new
     * window for displaying the photo. Displays an information alert if no
     * photo is selected.
     *
     * @param event The ActionEvent associated with the display photo action.
     * @throws IOException If there is an issue handling the photo display.
     */
    @FXML
    private void displayPhoto(ActionEvent event) throws IOException {
        selectedPicture = photoTable.getSelectionModel().getSelectedItem();
        if (selectedPicture != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/photos/photoDisplay.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No picture selected");
            alert.setContentText("Select a picture to display");
            alert.showAndWait();
            return;
        }
    }

    /**
     * Handles the action event for copying the selected photo. Opens a new
     * window for copying the photo. Displays an information alert if no
     * photo is selected.
     *
     * @param event The ActionEvent associated with the copy photo action.
     * @throws IOException If there is an issue handling the photo copy.
     */
    @FXML
    private void copyPhoto(ActionEvent event) throws IOException {
        selectedPicture = photoTable.getSelectionModel().getSelectedItem();
        if (selectedPicture == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No picture selected");
            alert.setContentText("Select a picture to copy");
            alert.showAndWait();
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/photos/copy.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Handles the action event for moving the selected photo. Opens a new
     * window for moving the photo. Displays an information alert if no
     * photo is selected.
     *
     * @param event The ActionEvent associated with the move photo action.
     * @throws IOException If there is an issue handling the photo move.
     */
    @FXML
    private void movePhoto(ActionEvent event) throws IOException {
        selectedPicture = photoTable.getSelectionModel().getSelectedItem();
        if (selectedPicture == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No picture selected");
            alert.setContentText("Select a picture to move");
            alert.showAndWait();
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/photos/movePhoto.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Handles the action event for returning to the home page. Opens a new
     * window for the user's home page.
     *
     * @param event The ActionEvent associated with the return to home action.
     * @throws IOException If there is an issue handling the return to home action.
     */
    @FXML
    private void handleAlbumReturnToHome(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/photos/UserHomePage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
}