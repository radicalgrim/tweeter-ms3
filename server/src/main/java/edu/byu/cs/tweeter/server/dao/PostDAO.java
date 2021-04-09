package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

public class PostDAO {
    public PostResponse post(PostRequest request){
        return new PostResponse();
    }
}
