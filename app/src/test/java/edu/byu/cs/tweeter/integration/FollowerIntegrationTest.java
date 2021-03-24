package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowerServiceProxy;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;

public class FollowerIntegrationTest {
    private FollowerRequest request;
    private FollowerResponse expectedResponse;
    private FollowerServiceProxy FollowerServiceProxy;

    @BeforeEach
    public void setup() {
        FollowerServiceProxy = new FollowerServiceProxy();
        request = new FollowerRequest("@TestUser", 10, "@OtherTestUser");
        expectedResponse = new FollowerResponse(true);
    }

    @Test
    public void testFollower_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowerResponse actualResponse = FollowerServiceProxy.getFollowers(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), actualResponse.isSuccess());
    }
}
