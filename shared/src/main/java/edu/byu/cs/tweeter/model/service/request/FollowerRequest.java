package edu.byu.cs.tweeter.model.service.request;

public class FollowerRequest {

    private String followeeAlias;
    private int limit;
    private String lastFollowerAlias;

    public FollowerRequest(String followeeAlias, int limit, String lastFollowerAlias) {
        this.followeeAlias = followeeAlias;
        this.limit = limit;
        this.lastFollowerAlias = lastFollowerAlias;
    }

    public FollowerRequest() {
    }

    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public int getLimit() {
        return limit;
    }

    public String getLastFollowerAlias() {
        return lastFollowerAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setLastFollowerAlias(String lastFollowerAlias) {
        this.lastFollowerAlias = lastFollowerAlias;
    }
}
