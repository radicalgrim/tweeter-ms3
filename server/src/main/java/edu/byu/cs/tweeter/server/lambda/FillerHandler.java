package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.server.dao.Filler;
import edu.byu.cs.tweeter.server.service.FeedServiceImpl;

public class FillerHandler implements RequestHandler<Void, Void> {

    @Override
    public Void handleRequest(Void input, Context context) {
        Filler.fillDatabase();
        return null;
    }
}
