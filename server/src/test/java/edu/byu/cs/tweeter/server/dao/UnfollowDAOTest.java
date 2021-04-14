package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;


public class UnfollowDAOTest {
    private UnfollowRequest request;
    private UnfollowResponse expectedResponse;
    private FollowDAO followDAOSpy;
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";


    @BeforeEach
    public void setup() {
        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User allen_user = new User("Allen", "Anderson", "@allen_anderson", MALE_IMAGE_URL);

        // Setup a request object to use in the tests
//        request = new UnfollowRequest(resultUser1, allen_user);
//
//        // Setup a mock UnfollowDAO that will return known responses
//        expectedResponse = new UnfollowResponse(true);
//        followDAOSpy = Mockito.spy(new FollowDAO());
//        Mockito.when(followDAOSpy.unfollow(request)).thenReturn(expectedResponse);

        //PostServiceImplSpy = Mockito.spy(PostServiceImpl.class);
        //Mockito.when(PostServiceImplSpy.getUnfollowDAO()).thenReturn(mockUnfollowDAO);
    }

    @Test
    void testUnfollow_() {
        //Mockito.when(UserDAOSpy.getLoginResponse(loginRequest)).thenReturn(expectedLoginResponse);

//        UnfollowResponse response = followDAOSpy.unfollow(request);
//
//        Assertions.assertEquals(expectedResponse, response);

        String firstName = "Bruce";
        String lastName = "Wayne";
        String username = "@bwayne";
        String password = "batman";
        String imageUrl = MALE_IMAGE_URL;
        UserDAO uDAO_actual = new UserDAO();
//        RegisterRequest bruce_request = new RegisterRequest(firstName, lastName, username, password, imageUrl);
//
//        RegisterResponse bruce_response = uDAO_actual.getRegisterResponse(bruce_request);
        User bruce_wayne = new User(firstName, lastName, username, imageUrl);

        User allen_user = new User("Allen", "Anderson", "@allen_anderson", imageUrl);

        FollowDAO fDao = new FollowDAO();
        UnfollowRequest request_real = new UnfollowRequest(bruce_wayne, allen_user);
        UnfollowResponse response_real = fDao.unfollow(request_real);
        Assertions.assertNotNull(response_real);
    }
}
