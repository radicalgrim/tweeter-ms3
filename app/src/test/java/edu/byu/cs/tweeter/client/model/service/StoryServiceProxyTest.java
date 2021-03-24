package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

public class StoryServiceProxyTest {

    private StoryRequest validRequest;
    private StoryRequest invalidRequest;

    private StoryResponse successResponse;
    private StoryResponse failureResponse;

    private StoryServiceProxy storyServiceProxySpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User testUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Status currentStatus = new Status("Hello World", "@user",
                "https://google.com", "1:00", testUser);

        Status resultStatus1 = new Status("Hello World1", "@user1",
                "https://google.com", "1:00", testUser);
        Status resultStatus2 = new Status("Hello World2", "@user2",
                "https://google.com", "2:00", testUser);
        Status resultStatus3 = new Status("Hello World3", "@user3",
                "https://google.com", "3:00", testUser);

        // Setup request objects to use in the tests
        validRequest = new StoryRequest(currentStatus.getUser().getAlias(), 3, null);
        invalidRequest = new StoryRequest(null, 0, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new StoryResponse(Arrays.asList(resultStatus1, resultStatus2, resultStatus3), false);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getStory(validRequest, StoryServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new StoryResponse("An exception occurred");
        Mockito.when(mockServerFacade.getStory(invalidRequest, StoryServiceProxy.URL_PATH)).thenReturn(failureResponse);

        // Create a StoryService instance and wrap it with a spy that will use the mock service
        storyServiceProxySpy = Mockito.spy(new StoryServiceProxy());
        Mockito.when(storyServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetStory_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StoryResponse response = storyServiceProxySpy.getStory(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetStory_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        StoryResponse response = storyServiceProxySpy.getStory(validRequest);

        for(Status status : response.getStatuses()) {
            Assertions.assertNotNull(status.getUser().getImageBytes());
        }
    }

    @Test
    public void testGetStory_invalidRequest_returnsNoStory() throws IOException, TweeterRemoteException {
        StoryResponse response = storyServiceProxySpy.getStory(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}

