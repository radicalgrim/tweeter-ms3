package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FeedServiceProxy;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;

public class FeedIntegrationTest {
    private FeedRequest request;
    private FeedResponse expectedResponse;
    private FeedServiceProxy feedServiceProxy;

    @BeforeEach
    public void setup() {
        feedServiceProxy = new FeedServiceProxy();
        request = new FeedRequest("@TestUser", 10, "Feb. 2, 2021 1:00");
        expectedResponse = new FeedResponse(true);
    }

    @Test
    public void testFeed_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FeedResponse actualResponse = feedServiceProxy.getFeed(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), actualResponse.isSuccess());
    }
}
