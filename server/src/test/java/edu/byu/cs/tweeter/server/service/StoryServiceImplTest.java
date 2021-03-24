package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class StoryServiceImplTest {

    private StoryRequest request;
    private StoryResponse expectedResponse;
    private StoryServiceImpl storyServiceImplSpy;

    @BeforeEach
    public void setup() {
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
        request = new StoryRequest(currentStatus.getUser().getAlias(), 3, null);

        // Setup a mock StoryDAO that will return known responses
        expectedResponse = new StoryResponse(Arrays.asList(resultStatus1, resultStatus2, resultStatus3), false);
        StoryDAO mockStoryDAO = Mockito.mock(StoryDAO.class);
        Mockito.when(mockStoryDAO.getStory(request)).thenReturn(expectedResponse);

        storyServiceImplSpy = Mockito.spy(StoryServiceImpl.class);
        Mockito.when(storyServiceImplSpy.getStoryDAO()).thenReturn(mockStoryDAO);
    }

    @Test
    public void testGetStory_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StoryResponse response = storyServiceImplSpy.getStory(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}


