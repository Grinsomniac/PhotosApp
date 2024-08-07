package com.photos;

import com.photos.model.Album;
import com.photos.model.Picture;
import com.photos.model.User;
import com.photos.model.UserList;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * @author Jason John, Ian Underwood
 * Photos extends Application
 * Starts initial Stage/Scenes
 * Creates stock user, album and photos
 */
public class Photos extends Application {
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(Photos.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 370, 260);
        stage.setTitle("Photo Album Project");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try {
                    UserList.saveUserList();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        try {
            UserList.loadUserList();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        UserList.loadUserList();

        //CREATING STOCK USER
        for(User u : UserList.userList) {
            if (u.getUsername().equals("stock")) {
                return;
            }
        }
        User stock = new User("stock");
        UserList.addUser(stock);
        stock.addAlbum(new Album("stock"));

        File folder = new File("data/stock");
        File[] files = folder.listFiles();
        for(File file : files){
            System.out.println("NumFiles" + files.length);
        }
        for(File file : folder.listFiles()){
            if(file.isFile()){
                if(stock.getUserAlbumList().get(0).getAlbumName().equals("stock")) {
                    stock.getUserAlbumList().get(0).addPicture(new Picture(file.toString(), file));
                }
            }
        }
        //END CREATING STOCK USER

        UserList.saveUserList();
    }

    /**
     * Main method that calls launch();
     * @param args - Main method String args
     */
    public static void main(String[] args) {
        launch();
    }
}