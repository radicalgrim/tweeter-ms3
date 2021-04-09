package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FollowerService;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowerServiceImpl implements FollowerService {
    @Override
    public FollowerResponse getFollowers(FollowerRequest followerRequest) {
        return getFollowDAO().getFollowers(followerRequest);
    }

    FollowDAO getFollowDAO() { return new FollowDAO(); }
}
