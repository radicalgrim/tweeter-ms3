package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.FollowerService;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

public class FollowerServiceProxy implements FollowerService {

    // TODO: Create endpoint and put URL path here
    static final String URL_PATH = "";

    @Override
    public FollowerResponse getFollowers(FollowerRequest request) throws IOException {

        // TODO: Implement getFollowers() in ServerFacade and replace this line
        FollowerResponse response = new FollowerResponse("Dummy");
        // FollowerResponse response = getServerFacade().getFollowers(request);

        if(response.isSuccess()) {
            loadImages(response);
        }

        return response;
    }

    private void loadImages(FollowerResponse response) throws IOException {
        for(User user : response.getUsers()) {
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
        }
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
