package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

public class PostServiceProxyTest {
    private PostRequest validRequest;
    private PostRequest invalidRequest;

    private PostResponse successResponse;
    private PostResponse failureResponse;

    private PostServiceProxy PostServiceSpy;
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {

        // Setup request objects to use in the tests
        User userAllen = new User("Allen", "Anderson", MALE_IMAGE_URL);
        User fakeUser = new User("fakey", "fakerton", MALE_IMAGE_URL);
        Status validNewStatus = new Status("real Post", "@realOtherUser", "link", userAllen);
        Status invalidNewStatus = new Status("fake Post", "@fakeOtherUser", "link", fakeUser);
        validRequest = new PostRequest(validNewStatus);
        invalidRequest = new PostRequest(invalidNewStatus);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new PostResponse();
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.post(validRequest, PostServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new PostResponse("Could not post");
        Mockito.when(mockServerFacade.post(invalidRequest, PostServiceProxy.URL_PATH)).thenReturn(failureResponse);

        // Create a postingService instance and wrap it with a spy that will use the mock service
        PostServiceSpy = Mockito.spy(new PostServiceProxy());
        Mockito.when(PostServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void PostServiceSuccess() throws IOException, TweeterRemoteException {
        PostResponse response = PostServiceSpy.post(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void PostServiceFail() throws IOException, TweeterRemoteException {
        PostResponse response = PostServiceSpy.post(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}