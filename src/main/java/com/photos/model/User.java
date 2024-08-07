package com.photos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a user in UserList
 * Each user has a unique identifier, a username, and a list of albums.
 *
 * @author Jason John, Ian Underwood
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * The unique identifier for the user.
     */
    private String id;
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The list of albums associated with the user.
     */
    private List<Album> userAlbumList = new ArrayList<>();
    /**
     * Constructs a new user with the specified username.
     * @param username The username of the user.
     */
    public User(String username) {
        this.username = username;
        id = UUID.randomUUID().toString();
    }
    /**
     * Gets the unique identifier of the user.
     * @return The unique identifier of the user.
     */
    public String getId() {
        return id;
    }
    /**
     * Sets the unique identifier of the user.
     * @param id The new unique identifier for the user.
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * Gets the username of the user.
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Sets the username of the user.
     * @param username The new username for the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Gets the list of albums associated with the user.
     * @return The list of albums associated with the user.
     */
    public List<Album> getUserAlbumList() {
        return userAlbumList;
    }
    /**
     * Adds an album to the user's list of albums.
     * @param album The album to be added.
     */
    public void addAlbum(Album album) {
        userAlbumList.add(album);
    }
}
