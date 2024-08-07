package com.photos.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The {@code Album} class represents a collection of pictures and provides methods to manage them.
 * Each album has a name, a count of photos, and methods to add pictures, retrieve information, and rename the album.
 *
 * @author Jason John, Ian Underwood
 */
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The list containing pictures in the album.
     */
    private List<Picture> album = new ArrayList<>();

    /**
     * The name of the album.
     */
    private String albumName;

    /**
     * The count of photos in the album.
     */
    private int photoCount;

    /**
     * Creates a new album with the given name.
     * @param albumName The name of the album.
     */
    public Album(String albumName) {
        this.albumName = albumName;
        this.photoCount = 0;

    }

    /**
     * Gets the count of photos in the album.
     * @return The count of photos.
     */
    public int getPhotoCount() {
        return album.size();
    }

    /**
     * Gets the name of the album.
     * @return The name of the album.
     */
    public String getAlbumName() {
        return albumName;
    }

    /**
     * Gets the date range of photos in the album.
     * @return A formatted string representing the date range.
     */
    public String getDateRange() {
        List<Date> dates = new ArrayList<>();
        for (Picture picture : album) {
            dates.add(picture.getDateTime());
        }
        Collections.sort(dates); // Sort the dates in ascending order
        if (dates.isEmpty()) {
            return ""; // Return an empty string if the album is empty
        }



        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        String oldestDate = sdf.format(dates.get(0));
        String newestDate = sdf.format(dates.get(dates.size() - 1));

        return oldestDate + " - " + newestDate;
    }

    /**
     * Adds a picture to the album.
     * @param picture The picture to be added.
     */
    public void addPicture(Picture picture) {
        album.add(picture);
    }

    /**
     * Gets the current album.
     * @return The current album.
     */
    public Album getAlbum() {
        return this;
    }

    /**
     * Gets the list of photos in the album.
     * @return The list of photos.
     */
    public List<Picture> getPhotos() {
        return album;
    }

    /**
     * Renames the album with the given name.
     * @param newName The new name for the album.
     */
    public void renameAlbum(String newName) {
        this.albumName = newName;
    }
}
