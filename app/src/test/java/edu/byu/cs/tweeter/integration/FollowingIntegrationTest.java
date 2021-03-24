package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowingServiceProxy;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;

public class FollowingIntegrationTest {
    private FollowingRequest request;
    private FollowingResponse expectedResponse;
    private FollowingServiceProxy FollowingServiceProxy;

    @BeforeEach
    public void setup() {
        FollowingServiceProxy = new FollowingServiceProxy();
        request = new FollowingRequest("@TestUser", 10, "@OtherTestUser");
        expectedResponse = new FollowingResponse(true);
    }

    @Test
    public void testFollowing_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowingResponse actualResponse = FollowingServiceProxy.getFollowees(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), actualResponse.isSuccess());
    }
}
