package com.photos.controllers;

import com.photos.model.Album;
import com.photos.model.User;
import com.photos.model.UserList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The {@code UserHomePageController} class controls the user's home page view.
 * It handles interactions with the user interface components, such as buttons and tables,
 * to display and manage the user's albums.
 *
 * @author Ian Underwood, Jason John
 */
public class UserHomePageController {
    @FXML private TableView<Album> userHomePageTable;
    @FXML private TableColumn<Album, String> albumList;
    @FXML private TableColumn<Album, Integer> numPhotos;
    @FXML private TableColumn<Album, String> dateRange;
    @FXML private Button userHomePageOpenAlbum;
    @FXML private Button userHomePageCreateAlbum;
    @FXML private Button userHomePageSearch;
    @FXML private Button userHomePageDeleteAlbum;
    @FXML private Button userHomePageRenameAlbum;
    @FXML private Button userHomePageLogout;

    public static User user;
    public static ObservableList<Album> albumObservableList = FXCollections.observableArrayList();
    public static Album selectedAlbum;

    /**
     * Initializes the user home page view, setting up the album table and populating it with the user's albums.
     */
    public void initialize(){

        albumList.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlbumName()));
        numPhotos.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPhotoCount()).asObject());
        dateRange.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateRange()));

        albumObservableList.clear();
        albumObservableList.addAll(user.getUserAlbumList());

        userHomePageTable.setItems(albumObservableList);

    }

    /**
     * Handles double-click events on the album table to open the selected album.
     *
     * This method is invoked when the user double-clicks on an album in the user's home page table.
     * It sets the selected album in the `AlbumController` and loads the album view, updating the stage
     * with the selected album's content.
     *
     * @param event The MouseEvent triggered by the double-click event.
     * @throws IOException If an error occurs while loading the album view.
     */
    public void handleTableClick(MouseEvent event) throws IOException {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            // Double-click detected
            Album selectedAlbum = userHomePageTable.getSelectionModel().getSelectedItem();
            if (selectedAlbum != null) {
                AlbumController.album = selectedAlbum;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/photos/album.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    /**
     * Handles the action of opening the selected album when a right-click event is triggered.
     *
     * This method is invoked when the user right-clicks on an album in the user's home page table
     * and selects the "Open Album" option. It sets the selected album in the `AlbumController` and
     * loads the album view, updating the stage with the selected album's content.
     *
     * @param event The ActionEvent triggered by the "Open Album" action.
     * @throws IOException If an error occurs while loading the album view.
     */
    public void handleOpenAlbum(ActionEvent event) throws IOException {
        Album selectedAlbum = userHomePageTable.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            AlbumController.album = selectedAlbum;

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/photos/album.fxml"));
            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Handles the action of renaming the selected album.
     *
     * This method is invoked when the user selects the "Rename Album" option. It retrieves
     * the selected album, opens the rename album view, and allows the user to input a new name.
     *
     * @param event The ActionEvent triggered by the "Rename Album" action.
     * @throws IOException If an error occurs while loading the rename album view.
     */
    public void handleRenameAlbum(ActionEvent event) throws IOException {
        selectedAlbum = userHomePageTable.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/photos/renameAlbum.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Handles the action of deleting the selected album.
     *
     * This method is invoked when the user selects the "Delete Album" option. It retrieves
     * the selected album and removes it from both the UI table and the user's album list.
     *
     * @param event The ActionEvent triggered by the "Delete Album" action.
     */
    public void handleDeleteAlbum(ActionEvent event) {
        TableView.TableViewSelectionModel<Album> selectionModel = userHomePageTable.getSelectionModel();
        if (selectionModel.getSelectedCells().isEmpty()) {
            return;
        } else {
            Album album = selectionModel.getSelectedItem();
            int delete = -1;
            int count = 0;
            for (Album a : user.getUserAlbumList()) {
                if (a.equals((album))) {
                    delete = count;
                    userHomePageTable.getItems().remove(a);
                }
                count++;
            }
            if (delete != -1) {
                user.getUserAlbumList().remove(delete);
            }
        }
    }
    /**
     * Handles the action of opening the selected album from the user's home page.
     * This method is invoked when the user clicks the "Open Album" button. It retrieves the
     * selected album from the table and opens the album view, displaying the album's content.
     * @param event The ActionEvent triggered by the "Open Album" button.
     * @throws IOException If an error occurs while loading the album view.
     */
    public void handleUserHomePageOpenAlbum(ActionEvent event) throws IOException {
        TableView.TableViewSelectionModel<Album> selectionModel = userHomePageTable.getSelectionModel();
        if (selectionModel.getSelectedCells().isEmpty()) {
            return;
        } else {
            TablePosition<Album, ?> selectedCell = selectionModel.getSelectedCells().get(0);
            Album album = selectionModel.getSelectedItem();
            AlbumController.album = album;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/photos/album.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Handles the action of creating a new album when the "Create Album" button is clicked.
     *
     * This method is invoked when the user clicks the "Create Album" button on the user's home page.
     * It loads the create album view, allowing the user to input details for a new album, and opens
     * a new stage to display the create album form.
     *
     * @param event The ActionEvent triggered by the "Create Album" button click.
     * @throws IOException If an error occurs while loading the create album view.
     */
    public void handleUserHomePageCreateAlbum(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/photos/createAlbum.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Handles the action of initiating a search when the "Search" button is clicked.
     *
     * This method is invoked when the user clicks the "Search" button on the user's home page.
     * It loads the search view, allowing the user to input search criteria, and opens a new stage
     * to display the search form.
     *
     * @param event The ActionEvent triggered by the "Search" button click.
     * @throws IOException If an error occurs while loading the search view.
     */
    public void handleUserHomePageSearch(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/photos/search.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Handles the action of deleting the selected album from the user's home page.
     *
     * This method is invoked when the user clicks the "Delete Album" button. It retrieves the
     * selected album from the table and removes it from both the UI table and the user's album list.
     *
     * @param actionEvent The ActionEvent triggered by the "Delete Album" button click.
     */
    public void handleUserHomePageDeleteAlbum(ActionEvent actionEvent) {
        TableView.TableViewSelectionModel<Album> selectionModel = userHomePageTable.getSelectionModel();
        if (selectionModel.getSelectedCells().isEmpty()) {
            return;
        } else {
            Album album = selectionModel.getSelectedItem();
            int delete = -1;
            int count = 0;
            for (Album a : user.getUserAlbumList()) {
                if (a.equals((album))) {
                    delete = count;
                    userHomePageTable.getItems().remove(a);
                }
                count++;
            }
            if (delete != -1) {
                user.getUserAlbumList().remove(delete);
            }
        }
    }

    /**
     * Handles the action of renaming the selected album from the user's home page.
     *
     * This method is invoked when the user clicks the "Rename Album" button. It retrieves the
     * selected album, opens the rename album view, and allows the user to input a new name.
     *
     * @param actionEvent The ActionEvent triggered by the "Rename Album" button click.
     * @throws IOException If an error occurs while loading the rename album view.
     */
    public void handleUserHomePageRenameAlbum(ActionEvent actionEvent) throws IOException {
        selectedAlbum = userHomePageTable.getSelectionModel().getSelectedItem();
        if(selectedAlbum != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/photos/renameAlbum.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No album selected");
            alert.setContentText("Select an album to rename");
            alert.showAndWait();
            return;
        }
    }

    /**
     * Handles the action of logging out from the user's home page.
     *
     * This method is invoked when the user clicks the "Logout" button. It loads the login view,
     * closes the current stage, and saves the user list.
     *
     * @param event The ActionEvent triggered by the "Logout" button click.
     * @throws IOException If an error occurs while loading the login view.
     */
    public void handleUserHomePageLogout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/photos/login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        UserList.saveUserList();
    }

    /**
     * Retrieves the TableView of albums displayed on the user's home page.
     *
     * @return The TableView containing the user's albums.
     */
    public TableView<Album> getUserHomePageTable() {
        return userHomePageTable;
    }
}
