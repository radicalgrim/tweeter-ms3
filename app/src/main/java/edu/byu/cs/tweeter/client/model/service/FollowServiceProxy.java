package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;

public class FollowServiceProxy implements FollowService {

    // TODO: Create endpoint and put URL path here
    static final String URL_PATH = "/follow";

    @Override
    public FollowResponse follow(FollowRequest request) throws IOException, TweeterRemoteException {

        // TODO: Implement follow() in ServerFacade and replace this line
        FollowResponse response = getServerFacade().follow(request, URL_PATH);
        // FollowResponse response = getServerFacade().follow(request);

        if(response.isSuccess()) { }

        return response;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
