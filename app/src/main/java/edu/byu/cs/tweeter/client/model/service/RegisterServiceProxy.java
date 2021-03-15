package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterServiceProxy implements RegisterService {

    // TODO: Create endpoint and put URL path here
    static final String URL_PATH = "";

    @Override
    public RegisterResponse register(RegisterRequest request) throws IOException, TweeterRemoteException {

        // TODO: Implement register() in ServerFacade and replace this line
        RegisterResponse response = new RegisterResponse("Dummy");
        // RegisterResponse response = getServerFacade().register(request);

        if(response.isSuccess()) {
            loadImage(response.getUser());
        }

        return response;
    }

    /**
     * Loads the profile image data for the user.
     *
     * @param user the user whose profile image data is to be loaded.
     */
    private void loadImage(User user) throws IOException {
        byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
        user.setImageBytes(bytes);
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
