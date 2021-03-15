package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FeedServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FeedService;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;

public class FeedPresenter {

    private final FeedPresenter.View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }
    
    public FeedPresenter(View view) {
        this.view = view;
    }

    public FeedResponse getFeed(FeedRequest request) throws IOException, TweeterRemoteException {
        return getFeedService().getFeed(request);
    }

    FeedService getFeedService() {
        return new FeedServiceProxy();
    }
}
