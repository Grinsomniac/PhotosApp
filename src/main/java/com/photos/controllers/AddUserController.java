package com.photos.controllers;

import com.photos.model.User;
import com.photos.model.UserList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The AddUserController class manages the user interface
 * for adding a new user.  Displays information alerts for illegal inputs such as
 * attempting to create a new admin user, entering an empty username, or using
 * an already taken username.
 * */
 public class AddUserController {

@FXML private TextField createUserText;

    /**
     * Handles the action event for canceling the user creation process.
     * Closes the current window without making any changes.
     *
     * @param event The ActionEvent associated with the cancel user create action.
     * @throws IOException If there is an issue handling the cancellation.
     * */
    public void handleCancelUserCreate(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.hide();
    }

    /**
     * Handles the action event for creating a new user with the specified name.
     * Validates the input, checks for illegal usernames (e.g., admin, empty, or taken), and displays corresponding alert to user
     * Also updates the user list and the observable user list
     * in the AdminPageController upon successful user creation.
     *
     * @param event The ActionEvent associated with the user create action.
     * @throws IOException If there is an issue handling the user creation.
     */
    public void handleUserCreate(ActionEvent event) throws IOException {
        // Get the entered username and trim any leading or trailing spaces
        String user = createUserText.getText().trim().toLowerCase();
        if(user.equals("admin")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Illegal input");
            alert.setContentText("You may not create new admin user");
            alert.showAndWait();
            return;
        }
        // Check for an empty username and display an information alert
        if (user.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Illegal input");
            alert.setContentText("You must enter a new user name");
            alert.showAndWait();
            return;
        }else{
            // Check if the username is already taken
            for(User u : UserList.userList){
                if(u.getUsername().equals(user)){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Illegal input");
                    alert.setContentText("This username is taken");
                    alert.showAndWait();
                    return;
                }
            }
        }
        // Create a new user, add it to the user list, and update the observable user list
        User add = new User(user);
        UserList.userList.add(add);
        AdminPageController.users.add(add);
        // Close the current window
        createUserText.getScene().getWindow().hide();
    }
}

