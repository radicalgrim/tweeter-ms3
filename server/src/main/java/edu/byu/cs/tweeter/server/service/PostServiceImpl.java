package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.dao.PostDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class PostServiceImpl implements PostService {
    @Override
    public PostResponse post(PostRequest request) {
        return getPostDAO().post(request);
    }
    PostDAO getPostDAO() { return new PostDAO(); }

}
