package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowRequest {

    private User user;

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public UnfollowRequest(User user, User currentUser) {
        this.user = user;
        this.currentUser = currentUser;
    }

    public UnfollowRequest() {}

    /**
     * Returns the username of the user to be unfollowed  by this request.
     *
     * @return the username.
     */
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
