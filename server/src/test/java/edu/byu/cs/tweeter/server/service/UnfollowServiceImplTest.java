package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class UnfollowServiceImplTest {
    private UnfollowRequest request;
    private UnfollowResponse expectedResponse;
    private FollowDAO mockFollowDAO;
    private UnfollowServiceImpl UnfollowServiceImplSpy;

    @BeforeEach
    public void setup() {
        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User allen_user = new User("Allen", "Anderson", "@allen_anderson");


        // Setup a request object to use in the tests
        request = new UnfollowRequest(resultUser1, allen_user);

        // Setup a mock FollowDAO that will return known responses
        expectedResponse = new UnfollowResponse(true);
        mockFollowDAO = Mockito.mock(FollowDAO.class);
        Mockito.when(mockFollowDAO.unfollow(request)).thenReturn(expectedResponse);

        UnfollowServiceImplSpy = Mockito.spy(UnfollowServiceImpl.class);
        Mockito.when(UnfollowServiceImplSpy.getFollowDAO()).thenReturn(mockFollowDAO);
    }

    @Test
    public void testunfollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UnfollowResponse response = UnfollowServiceImplSpy.unfollow(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
