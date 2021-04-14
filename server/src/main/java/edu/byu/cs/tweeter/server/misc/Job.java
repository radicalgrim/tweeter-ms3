package edu.byu.cs.tweeter.server.misc;

import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class Job {
    ArrayList<User> followers;
    Status status;

    public Job(Status status){
        this.status = status;
        followers = new ArrayList<>();
    }

    public Job() {
    }

    public void addToFollowers(User user){
        getFollowers().add(user);
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<User> followers) {
        this.followers = followers;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
