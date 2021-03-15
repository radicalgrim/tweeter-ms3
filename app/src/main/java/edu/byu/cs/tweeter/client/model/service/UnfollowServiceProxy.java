package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.UnfollowService;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UnfollowServiceProxy implements UnfollowService {

    // TODO: Create endpoint and put URL path here
    static final String URL_PATH = "";

    @Override
    public UnfollowResponse unfollow(UnfollowRequest request) throws IOException, TweeterRemoteException {

        // TODO: Implement unfollow() in ServerFacade and replace this line
        UnfollowResponse response = new UnfollowResponse("Dummy");
        // UnfollowResponse response = getServerFacade().unfollow(request);

        if(response.isSuccess()) { }

        return response;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
