package edu.byu.cs.tweeter.model.service.request;

public class PostRequest {

    private String body;

    public PostRequest(String body) {
        this.body = body;
    }


    public PostRequest() {
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
