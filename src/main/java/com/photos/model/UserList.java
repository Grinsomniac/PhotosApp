package com.photos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 * UserList Class contains ArrayList of User Type Objects which each contain Album type objects which contain Picture Objects which contain Tag objects
 * UserList > List of User > List of Album > List of Picture > List of Tag
 * Each UserList has a unique Identifier and a list of Users
 *
 * @author Jason John, Ian Underwood
 */
public class UserList implements Serializable {

    static final long serialVersionUID = 1L;
    /**
     * The list of all users
     */
    public static List<User> userList = new ArrayList<User>();

    /**
     * Saves UserListInfo.dat file
     * Master save file containing Users > Albums > Pictures > Tags
     * @throws IOException in case file not found
     */
    public static void saveUserList() throws IOException{
        FileOutputStream userListSave = new FileOutputStream("src/main/resources/savedData/users/UserListInfo.dat");
        ObjectOutputStream outputStream = new ObjectOutputStream(userListSave);
        outputStream.writeObject(userList);
        outputStream.close();
        userListSave.close();
    }

    /**
     * Loads UserListInfo.dat file
     * Master save file containing Users > Albums > Pictures > Tags
     * @throws IOException in case file not found
     * @throws ClassNotFoundException in case class of a serialized object cannot be found
     */
    public static void loadUserList() throws IOException, ClassNotFoundException {
        try ( FileInputStream userListLoad = new FileInputStream("src/main/resources/savedData/users/UserListInfo.dat");
              ObjectInputStream inputStream = new ObjectInputStream(userListLoad);) {
            userList = (List<User>) inputStream.readObject();
        } catch(FileNotFoundException e){
                System.err.println("File not found: " + e.getMessage());

        }
    }
    /**
     * Adds a user to the UserList
     * @param user The User Object to be added
     */
    public static void addUser(User user){
        for (User u: userList){
            if (user.equals(u)){
                //add code to output errordialog fx
                System.out.println("User already exists");
            }
        }
        userList.add(user);
    }
}
