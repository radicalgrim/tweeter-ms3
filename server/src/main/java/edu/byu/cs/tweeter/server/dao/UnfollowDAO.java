package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UnfollowDAO {

    public UnfollowResponse unfollow(UnfollowRequest request){
        UnfollowResponse response = new UnfollowResponse(true);
        //currentUser.decrementFollowingCount();
        //int testVar = currentUser.getFollowingCount();
        return response;
    }
}
