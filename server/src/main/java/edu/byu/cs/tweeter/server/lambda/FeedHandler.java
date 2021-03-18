package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;

import edu.byu.cs.tweeter.server.service.FeedServiceImpl;

public class FeedHandler implements  RequestHandler<FeedRequest, FeedResponse> {
    @Override
    public FeedResponse handleRequest(FeedRequest feedRequest, Context context) {
        FeedServiceImpl feedService = new FeedServiceImpl();
        return feedService.getFeed(feedRequest);
    }
}
