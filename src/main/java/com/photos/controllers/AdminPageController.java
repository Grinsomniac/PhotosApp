package com.photos.controllers;
import com.photos.model.User;
import com.photos.model.UserList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The AdminPageController class manages the user interface
 * and actions on the administrator page. provides functionality to initialize
 * the controller, handle the admin log-out, create user, and delete user actions.

 * */
public class AdminPageController {

    @FXML private Button adminLogOutButton;
    @FXML private Button createUserButton;
    @FXML private Button deleteUserButton;
    @FXML private TableView<User> listOfUsers;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private Button createUser;
    @FXML private Button cancelUserCreate;
    @FXML private TextField createUserText;

    public static ObservableList<User> users;

    /**
     * Initializes the AdminPageController. Sets up the user list table with a
     * username column, populates the list with existing users, and displays it
     * in the user interface.
     */
    public void initialize(){
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        users = FXCollections.observableArrayList();
        users.addAll(UserList.userList);
        listOfUsers.setItems(users);
    }

    /**
     * Handles the action event for logging out the administrator. Loads the login
     * page and displays it in a new window, then saves the user list.
     *
     * @param event The ActionEvent associated with the admin log-out action.
     * @throws IOException If there is an issue handling the admin log-out.
     */
    public void handleAdminLogOutButton(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/photos/login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        UserList.saveUserList();
    }

    /**
     * Handles the action event for creating a new user. Loads the user creation
     * page and displays it in a new window.
     *
     * @param event The ActionEvent associated with the create user action.
     * @throws IOException If there is an issue handling the user creation.
     */
    public void handleCreateUserButton(ActionEvent event) throws IOException {

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/photos/userAdd.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Add User");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Handles the action event for deleting a selected user. Checks if a user
     * is selected and removes the user from both the displayed list and the
     * underlying user list. Displays an information alert if no user is selected.
     *
     * @param event The ActionEvent associated with the delete user action.
     * @throws IOException If there is an issue handling the user deletion.
     */
    public void handleDeleteUserButton(ActionEvent event) throws IOException {

        TableView.TableViewSelectionModel<User> selectionModel = listOfUsers.getSelectionModel();
        if(selectionModel.getSelectedCells().isEmpty()){
                return;
        }else{
            TablePosition<User,?> selectedCell = selectionModel.getSelectedCells().get(0);
            User selectedItem = listOfUsers.getItems().get(selectedCell.getRow());

            String name = selectedItem.getUsername();
            int count = -1;
            for(User u : users){
                count++;
                if(u.getUsername().equals(name)){
                    users.remove(count);
                    break;
                }
            }
            count = -1;
            for(User u : UserList.userList){
                count++;
                if(u.getUsername().equals(name)){
                    UserList.userList.remove(count);
                    break;
                }
            }
        }
    }
}
