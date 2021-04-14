package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public class Filler {

    // How many follower users to add
    private final static int NUM_USERS = 10000;

    // The alias of the user to be followed by each user created
    private final static String FOLLOW_TARGET = "@allen_anderson";
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    public static void main(String[] args) {
        fillDatabase();
    }

    public static void fillDatabase() {

        UserDAO userDAO = new UserDAO();
        FollowDAO followDAO = new FollowDAO();

        List<String> followers = new ArrayList<>();
        List<User> users = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 1; i <= NUM_USERS; i++) {

            String firstName = "Guy";
            String lastName = "#" + i;
            String alias = "guy" + i;

            // Note that in this example, a UserDTO only has a name and an alias.
            // The url for the profile image can be derived from the alias in this example
            User user = new User();
            user.setAlias(alias);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setImageUrl(MALE_IMAGE_URL);
            users.add(user);

            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            followers.add(alias);
        }

        // Call the DAOs for the database logic
        if (users.size() > 0) {
            userDAO.addUserBatch(users);
        }
        if (followers.size() > 0) {
            followDAO.addFollowersBatch(followers, FOLLOW_TARGET);
        }
    }
}
