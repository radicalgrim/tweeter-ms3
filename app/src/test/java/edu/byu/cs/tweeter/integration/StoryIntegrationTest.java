package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.StoryServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

public class StoryIntegrationTest {
    private StoryRequest request;
    private StoryResponse expectedResponse;
    private StoryServiceProxy storyServiceProxy;

    @BeforeEach
    public void setup() {
        storyServiceProxy = new StoryServiceProxy();
        request = new StoryRequest("@TestUser", 10, "Feb. 2, 2021 1:00");
        expectedResponse = new StoryResponse(true);
    }

    @Test
    public void testStory_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StoryResponse actualResponse = storyServiceProxy.getStory(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), actualResponse.isSuccess());
    }
}
