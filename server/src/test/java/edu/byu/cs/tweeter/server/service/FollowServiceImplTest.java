package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowServiceImplTest {
    private FollowRequest request;
    private FollowResponse expectedResponse;
    private FollowDAO mockFollowDAO;
    private FollowServiceImpl followServiceImplSpy;

    @BeforeEach
    public void setup() {
        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User allen_user = new User("Allen", "Anderson", "@allen_anderson");


        // Setup a request object to use in the tests
        request = new FollowRequest(resultUser1, allen_user);

        // Setup a mock FollowDAO that will return known responses
        expectedResponse = new FollowResponse(true);
        mockFollowDAO = Mockito.mock(FollowDAO.class);
        Mockito.when(mockFollowDAO.follow(request)).thenReturn(expectedResponse);

        followServiceImplSpy = Mockito.spy(FollowServiceImpl.class);
        Mockito.when(followServiceImplSpy.getFollowDAO()).thenReturn(mockFollowDAO);
    }

    @Test
    public void testfollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowResponse response = followServiceImplSpy.follow(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
