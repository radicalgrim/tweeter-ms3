package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowServiceProxy;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

public class FollowIntegrationTest {
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private FollowRequest request;
    private FollowResponse expectedResponse;
    private FollowServiceProxy followProxy;

    @BeforeEach
    public void setup() {
        followProxy = new FollowServiceProxy();
        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        // Setup a request object to use in the tests
        request = new FollowRequest(resultUser1, resultUser1);
        // Setup a mock FollowDAO that will return known responses
        expectedResponse = new FollowResponse(true);
    }

    @Test
    public void testfollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowResponse response = followProxy.follow(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
    }
}
