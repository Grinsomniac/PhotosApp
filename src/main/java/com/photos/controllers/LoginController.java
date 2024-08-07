package com.photos.controllers;

import com.photos.model.Album;
import com.photos.model.Picture;
import com.photos.model.User;
import com.photos.model.UserList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * The LoginController class handles user login attempts
 * Redirects users to their respective home page or the admin to the admin page
 *
 * @author Ian Underwood, Jason John
 */public class LoginController {

    @FXML private Button loginButton;
    @FXML private TextField userNameTextField;

    /**
     * Attempts to log in based on the entered username and redirects the user
     * to the appropriate home page or the admin page. Displays alerts
     * for illegal inputs such as empty username or non-existent users.
     *
     * @param event The ActionEvent associated with the login attempt.
     * @throws IOException If there is an issue loading the home page or admin page.
     */
    @FXML
    void attemptLogin(ActionEvent event) throws IOException {

        String uName = userNameTextField.getText().trim();

        if (uName.equals("admin") || uName.equals("Admin")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/photos/admin.fxml"));
            Parent root = loader.load();
            Node n = (Node) event.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            Scene scene = new Scene(root, 385, 525);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
        if (uName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Illegal input");
            alert.setContentText("You must enter a user name");
            alert.showAndWait();
            return;
        }
        for (User u : UserList.userList) {
            if (u.getUsername().equals(uName)) {
                UserHomePageController.user = u;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/photos/userHomePage.fxml"));
                Parent root = loader.load();
                Node n = (Node) event.getSource();
                Stage stage = (Stage) n.getScene().getWindow();
                Scene scene = new Scene(root, 750, 435);
                stage.setScene(scene);
                stage.show();
                stage.centerOnScreen();
                return;
            }
        }
        if (!uName.equals("admin") && !uName.equals("Admin")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Illegal input");
            alert.setContentText("User does not exist. Try \"admin\"");
            alert.showAndWait();
        }
    }
}

