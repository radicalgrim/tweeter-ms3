package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutServiceProxy implements LogoutService {

    // TODO: Create endpoint and put URL path here
    static final String URL_PATH = "";

    @Override
    public LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException {

        // TODO: Implement logout() in ServerFacade and replace this line
        LogoutResponse response = new LogoutResponse(true);
        // LogoutResponse response = getServerFacade().logout(request);

        if(response.isSuccess()) { }

        return response;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
