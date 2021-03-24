package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutServiceProxy implements LogoutService {

    // TODO: Create endpoint and put URL path here
    //static final String URL_PATH = "";

    public static final String URL_PATH = "/logout";

    public LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException {
        LogoutResponse response = getServerFacade().logout(request, URL_PATH);

        if(response.isSuccess()) {
            //loadImage(response.getUser()); WHAT TO DO HERE??
        }

        return response;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
