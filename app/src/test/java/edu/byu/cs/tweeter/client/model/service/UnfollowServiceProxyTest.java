package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UnfollowServiceProxyTest {

    private UnfollowRequest validRequest;
    private UnfollowRequest invalidRequest;

    private UnfollowResponse successResponse;
    private UnfollowResponse failureResponse;

    private UnfollowServiceProxy unfollowServiceProxySpy;
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User userAllen = new User("Allen", "Anderson", MALE_IMAGE_URL);
        User fakeUser = new User("fakey", "fakerton", MALE_IMAGE_URL);

        // Setup request objects to use in the tests
        validRequest = new UnfollowRequest(userAllen);
        invalidRequest = new UnfollowRequest(fakeUser);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new UnfollowResponse(true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.unfollow(validRequest, UnfollowServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new UnfollowResponse("couldn't Unfollow the user");
        Mockito.when(mockServerFacade.unfollow(invalidRequest, UnfollowServiceProxy.URL_PATH)).thenReturn(failureResponse);

        // Create a UnfollowingService instance and wrap it with a spy that will use the mock service
        unfollowServiceProxySpy = Mockito.spy(new UnfollowServiceProxy());
        Mockito.when(unfollowServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void UnfollowServiceSuccess() throws IOException, TweeterRemoteException {
        UnfollowResponse response = unfollowServiceProxySpy.unfollow(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void UnfollowServiceFail() throws IOException, TweeterRemoteException {
        UnfollowResponse response = unfollowServiceProxySpy.unfollow(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }

}

