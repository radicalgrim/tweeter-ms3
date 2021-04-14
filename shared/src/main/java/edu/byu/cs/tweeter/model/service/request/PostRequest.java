package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class PostRequest {

    private String body;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private User currentUser;

    public PostRequest(String body, User currentUser) {
        this.body = body;
        this.currentUser = currentUser;
    }


    public PostRequest() {}

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
