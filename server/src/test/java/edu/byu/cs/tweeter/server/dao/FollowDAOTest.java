package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;


public class FollowDAOTest {
    private FollowRequest request;
    private FollowResponse expectedResponse;
    private FollowDAO FollowDAOSpy;

    @BeforeEach
    public void setup() {
        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        // Setup a request object to use in the tests
        request = new FollowRequest(resultUser1);

        // Setup a mock FollowDAO that will return known responses
        expectedResponse = new FollowResponse(true);
        FollowDAOSpy = Mockito.spy(new FollowDAO());
        Mockito.when(FollowDAOSpy.follow(request)).thenReturn(expectedResponse);

        //PostServiceImplSpy = Mockito.spy(PostServiceImpl.class);
        //Mockito.when(PostServiceImplSpy.getFollowDAO()).thenReturn(mockFollowDAO);
    }

    @Test
    void testFollow_() {
        //Mockito.when(UserDAOSpy.getLoginResponse(loginRequest)).thenReturn(expectedLoginResponse);

        FollowResponse response = FollowDAOSpy.follow(request);

        Assertions.assertEquals(expectedResponse, response);
    }
}
