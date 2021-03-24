package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;


public class UnfollowDAOTest {
    private UnfollowRequest request;
    private UnfollowResponse expectedResponse;
    private UnfollowDAO UnfollowDAOSpy;

    @BeforeEach
    public void setup() {
        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        // Setup a request object to use in the tests
        request = new UnfollowRequest(resultUser1);

        // Setup a mock UnfollowDAO that will return known responses
        expectedResponse = new UnfollowResponse(true);
        UnfollowDAOSpy = Mockito.spy(new UnfollowDAO());
        Mockito.when(UnfollowDAOSpy.unfollow(request)).thenReturn(expectedResponse);

        //PostServiceImplSpy = Mockito.spy(PostServiceImpl.class);
        //Mockito.when(PostServiceImplSpy.getUnfollowDAO()).thenReturn(mockUnfollowDAO);
    }

    @Test
    void testUnfollow_() {
        //Mockito.when(UserDAOSpy.getLoginResponse(loginRequest)).thenReturn(expectedLoginResponse);

        UnfollowResponse response = UnfollowDAOSpy.unfollow(request);

        Assertions.assertEquals(expectedResponse, response);
    }
}
