package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;


public class FollowDAOTest {
    private FollowRequest request;
    private FollowResponse expectedResponse;
    private FollowDAO FollowDAOSpy;
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    @BeforeEach
    public void setup() {
        User currentUser = new User("horace", "mcringle",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        // Setup a request object to use in the tests
        request = new FollowRequest(resultUser1, currentUser);

        // Setup a mock FollowDAO that will return known responses
        expectedResponse = new FollowResponse(true);
        FollowDAOSpy = Mockito.spy(new FollowDAO());
        //Mockito.when(FollowDAOSpy.follow(request)).thenReturn(expectedResponse);

        //PostServiceImplSpy = Mockito.spy(PostServiceImpl.class);
        //Mockito.when(PostServiceImplSpy.getFollowDAO()).thenReturn(mockFollowDAO);
    }

    @Test
    void testFollow_() {
        //Mockito.when(UserDAOSpy.getLoginResponse(loginRequest)).thenReturn(expectedLoginResponse);

//        FollowResponse response = FollowDAOSpy.follow(request);
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
        FollowRequest request_real = new FollowRequest(bruce_wayne, allen_user);
        FollowResponse response_real = fDao.follow(request_real);
        Assertions.assertNotNull(response_real);
    }
}
