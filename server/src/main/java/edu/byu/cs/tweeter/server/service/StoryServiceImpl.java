package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.StoryService;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class StoryServiceImpl implements StoryService {
    @Override
    public StoryResponse getStory(StoryRequest request) {
        return getStoryDAO().getStory(request);
    }

    public StoryDAO getStoryDAO() { return new StoryDAO(); }
}
