package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

public class PostServiceProxy implements PostService {

    // TODO: Create endpoint and put URL path here
    static final String URL_PATH = "/post";

    @Override
    public PostResponse post(PostRequest request) throws IOException, TweeterRemoteException {

        // TODO: Implement post() in ServerFacade and replace this line
        PostResponse response = getServerFacade().post(request, URL_PATH);
        // PostResponse response = getServerFacade().post(request);

        if(response.isSuccess()) { }

        return response;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
