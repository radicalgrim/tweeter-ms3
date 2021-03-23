package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowServiceImpl implements FollowService {
    @Override
    public FollowResponse follow(FollowRequest request) {
        FollowDAO fDao = new FollowDAO();
        return fDao.follow(request);
        //return getFollowDAO().follow(followRequest);
    }

    //FollowerDAO getFollowDAO() { return new FollowDAO(); }
}
