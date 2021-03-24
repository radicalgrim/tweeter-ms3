package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

public class FollowServiceProxyTest {

    private FollowRequest validRequest;
    private FollowRequest invalidRequest;

    private FollowResponse successResponse;
    private FollowResponse failureResponse;

    private FollowServiceProxy followServiceProxySpy;
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User userAllen = new User("Allen", "Anderson", MALE_IMAGE_URL);
        User fakeUser = new User("fakey", "fakerton", MALE_IMAGE_URL);

        // Setup request objects to use in the tests
        validRequest = new FollowRequest(userAllen);
        invalidRequest = new FollowRequest(fakeUser);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowResponse(true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.follow(validRequest, FollowServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new FollowResponse(false, "couldn't follow the user");
        Mockito.when(mockServerFacade.follow(invalidRequest, FollowServiceProxy.URL_PATH)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        followServiceProxySpy = Mockito.spy(new FollowServiceProxy());
        Mockito.when(followServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void FollowServiceSuccess() throws IOException, TweeterRemoteException {
        FollowResponse response = followServiceProxySpy.follow(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void FollowServiceFail() throws IOException, TweeterRemoteException {
        FollowResponse response = followServiceProxySpy.follow(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }

}
