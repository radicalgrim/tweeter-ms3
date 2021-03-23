package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowRequest {

    private User user;

    public FollowRequest(User user) {
        this.user = user;
    }

    public FollowRequest() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
