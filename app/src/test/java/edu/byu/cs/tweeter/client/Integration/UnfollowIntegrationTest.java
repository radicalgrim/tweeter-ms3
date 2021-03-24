package edu.byu.cs.tweeter.client.Integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.UnfollowServiceProxy;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UnfollowIntegrationTest {
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private UnfollowRequest request;
    private UnfollowResponse expectedResponse;
    private UnfollowServiceProxy unfollowProxy;

    @BeforeEach
    public void setup() {
        unfollowProxy = new UnfollowServiceProxy();
        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        // Setup a request object to use in the tests
        request = new UnfollowRequest(resultUser1);
        // Setup a mock FollowDAO that will return known responses
        expectedResponse = new UnfollowResponse(true);
    }

    @Test
    public void testunfollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UnfollowResponse response = unfollowProxy.unfollow(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
    }
}
