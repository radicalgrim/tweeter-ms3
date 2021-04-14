package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.service.FollowFetcherImpl;


public class FollowFetcher implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        FollowFetcherImpl followFetcherImpl = new FollowFetcherImpl();
        followFetcherImpl.handleRequest(sqsEvent, context);
        return null;
    }
}
