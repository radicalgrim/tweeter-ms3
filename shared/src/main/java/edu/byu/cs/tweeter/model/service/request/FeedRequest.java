package edu.byu.cs.tweeter.model.service.request;

public class FeedRequest {

    private String userAlias;
    private int limit;
    private String lastTimestamp;

    public FeedRequest() {
    }

    public FeedRequest(String userAlias, int limit, String lastTimestamp) {
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastTimestamp = lastTimestamp;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public int getLimit() {
        return limit;
    }

    public String getLastTimestamp() {
        return lastTimestamp;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setLastTimestamp(String lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }
}
