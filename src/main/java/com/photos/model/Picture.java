package com.photos.model;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.file.Files;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The {@code Picture} class represents an image file with associated metadata, including name, caption, file path,
 * date and time of creation, formatted date and time, and a list of tags.
 * The class provides methods to interact with and retrieve information about the picture.
 *
 * @author Jason John, Ian Underwood
 */
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The name of the picture.
     */
    private String name;

    /**
     * The caption associated with the picture.
     */
    private String caption;

    /**
     * The file representing the picture.
     */
    private File file;

    /**
     * The Date and Time of the last modified date of the picture file
     */
    private Date dateTime;

    /**
     * A formatted representation of the date and time.
     */
    private String formattedDateTime;

    /**
     * A formatted representation of just the date.
     */
    private String formattedDate;

    /**
     * The list of tags associated with the picture.
     */
    private List<Tag> tagList = new ArrayList<>();
    /**
     * Constructs a new Picture object with the given name and file.
     * @param name The name of the picture.
     * @param file The file representing the picture.
     * @throws IOException If an I/O error occurs.
     */
    public Picture(String name, File file) throws IOException {
        this.name = name;
        this.file = file;
        Path filePath = FileSystems.getDefault().getPath(name);
        long lastModifiedTime = Files.getLastModifiedTime(filePath).toMillis();
        this.dateTime = new Date(lastModifiedTime);
        // Formatting dateTime to store only DD-MM-YYYY HH:mm
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        this.formattedDateTime = sdf.format(dateTime.getTime());
        SimpleDateFormat sdf_DateOnly = new SimpleDateFormat("MM-dd-yyyy");
        this.formattedDate = sdf_DateOnly.format(dateTime.getTime());
        this.caption = name;

    }

    /**
     * Gets an ImageView of the picture for display in JavaFX.
     * @return The ImageView of the picture.
     * @throws FileNotFoundException If the file is not found.
     */
    public ImageView getImageView() throws FileNotFoundException {
        InputStream stream = new FileInputStream(file);
        Image image = new Image(stream);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(125);
        imageView.setFitHeight(125);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    /**
     * Gets the Image object of the picture.
     * @return The Image object of the picture.
     * @throws FileNotFoundException If the file is not found.
     */
    public Image getImage() throws FileNotFoundException {
        InputStream stream = new FileInputStream(file);
        return new Image(stream);
    }

    /**
     * Gets the caption of the picture.
     * @return The caption of the picture.
     */
    public String getCaption() {
        return this.caption;
    }

    /**
     * Sets a new caption for the picture.
     * @param newCaption The new caption for the picture.
     */
    public void setCaption(String newCaption) {
        this.caption = newCaption;
    }

    /**
     * Gets the file representing the picture.
     * @return The file representing the picture.
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Adds a new tag to the picture.
     * @param tagName  The name of the tag.
     * @param tagValue The value of the tag.
     */
    public void addTag(String tagName, String tagValue) {
        tagList.add(new Tag(tagName, tagValue));
    }

    /**
     * Gets the list of tags associated with the picture.
     * @return The list of tags associated with the picture.
     */
    public List<Tag> getTagList() {
        return tagList;
    }

    /**
     * Removes a tag from the list of tags associated with the picture.
     * @param tag The tag to be removed.
     */
    public void removeTag(Tag tag) {
        tagList.remove(tag);
    }

    /**
     * Checks if the picture's creation date is within a specified date range.
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return True if the picture is within the date range, false otherwise.
     */
    public boolean inDateRange(Date startDate, Date endDate) {
        // Ensure startDate is before or equal to endDate
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            throw new IllegalArgumentException("Invalid date range");
        }

        // Check if the picture's dateTime is within the range (inclusive)
        return !dateTime.before(startDate) && !dateTime.after(endDate);
    }

    /**
     * Gets the formatted date and time of the picture.
     * @return The formatted date and time of the picture.
     */
    public String getFormattedDateTime() {
        return this.formattedDateTime;
    }

    /**
     * Gets the formatted date of the picture.
     * @return The formatted date of the picture.
     */
    public String getFormattedDate() {
        return this.formattedDate;
    }
    /**
     * Gets the raw Date object representing the creation date and time of the picture.
     * @return The Date object representing the creation date and time of the picture.
     */
    public Date getDateTime() {
        return dateTime;
    }
}
