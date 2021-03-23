package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.dao.PostDAO;

public class PostServiceImpl implements PostService {
    @Override
    public PostResponse post(PostRequest request) {

        // TODO: Generates dummy data. Replace with a real implementation.
//        User user = new User("Test", "User",
//                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        PostDAO pDao = new PostDAO();
        return pDao.post(request);
    }
}
