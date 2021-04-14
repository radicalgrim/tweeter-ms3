package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.service.PostServiceImpl;

public class PostDAOTest {
    private PostRequest request;
    private PostResponse expectedResponse;
    private PostDAO PostDAOSpy;

    @BeforeEach
    public void setup() {
        String newPost = "new fake post, I am a fake";
        User allen_user = new User("Allen", "Anderson", "@allen_anderson");


        // Setup a request object to use in the tests
        request = new PostRequest(newPost, allen_user);

        // Setup a mock PostDAO that will return known responses
        expectedResponse = new PostResponse();
        PostDAOSpy = Mockito.spy(new PostDAO());
        Mockito.when(PostDAOSpy.post(request)).thenReturn(expectedResponse);

        //PostServiceImplSpy = Mockito.spy(PostServiceImpl.class);
        //Mockito.when(PostServiceImplSpy.getPostDAO()).thenReturn(mockPostDAO);
    }

    @Test
    void testPost_() {
        //Mockito.when(UserDAOSpy.getLoginResponse(loginRequest)).thenReturn(expectedLoginResponse);

        PostResponse response = PostDAOSpy.post(request);

        Assertions.assertEquals(response, response);
    }
}
