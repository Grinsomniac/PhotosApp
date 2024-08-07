package com.photos.model;

import java.io.Serializable;

/**
 * The {@code Tag} class represents a metadata tag with a name and a corresponding value.
 * Tags are used to categorize and describe pictures within the application.
 * Each tag has a name and a value, and methods to retrieve and modify them.
 *
 * @author Jason John, Ian Underwood
 */
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The name of the tag.
     */
    private String name;

    /**
     * The value of the tag.
     */
    private String value;

    /**
     * Creates a new tag with the given name and value.
     * @param name  The name of the tag.
     * @param value The value of the tag.
     */
    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the name of the tag.
     * @return The name of the tag.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name for the tag.
     * @param newName The new name for the tag.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Gets the value of the tag.
     * @return The value of the tag.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets a new value for the tag.
     * @param newValue The new value for the tag.
     */
    public void setValue(String newValue) {
        this.value = newValue;
    }

    /**
     * Compares this tag to the specified object. The result is true if and only if the argument is not null
     * and is a Tag object with the same name and value.
     * @param obj The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tag tag = (Tag) obj;
        return name.equals(tag.name) && value.equals(tag.value);
    }
    /**
     * Returns a string representation of the tag in the format "name=value".
     * @return A string representation of the tag.
     */
    @Override
    public String toString() {
        return name + "=" + value;
    }
}
