package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

public class FollowDAO {
    public FollowResponse follow(FollowRequest request){
        //currentUser.incrementFollowingCount();
        return new FollowResponse(true);
    }
}
