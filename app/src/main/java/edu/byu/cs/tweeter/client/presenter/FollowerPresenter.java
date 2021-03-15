package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowerServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowerService;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;

public class FollowerPresenter {

    private final View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public FollowerPresenter(View view) {
        this.view = view;
    }

    public FollowerResponse getFollower(FollowerRequest request) throws IOException, TweeterRemoteException {
        return getFollowerService().getFollowers(request);
    }

    FollowerService getFollowerService() {
        return new FollowerServiceProxy();
    }
        
}
