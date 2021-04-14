package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class PostServiceImplTest {
    private PostRequest request;
    private PostResponse expectedResponse;
    private StoryDAO mockStoryDAO;
    private PostServiceImpl PostServiceImplSpy;

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    @BeforeEach
    public void setup() {
        String newPost = "new fake post, I am a fake";
        User allen_user = new User("Allen", "Anderson", "@allen_anderson", MALE_IMAGE_URL);
        Status status = new Status("Test", "@Test", "https://google.com", allen_user);


        // Setup a request object to use in the tests
        request = new PostRequest(status);

        // Setup a mock PostDAO that will return known responses
        expectedResponse = new PostResponse();
        mockStoryDAO = Mockito.mock(StoryDAO.class);
        Mockito.when(mockStoryDAO.post(request)).thenReturn(expectedResponse);

        PostServiceImplSpy = Mockito.spy(PostServiceImpl.class);
        Mockito.when(PostServiceImplSpy.getStoryDAO()).thenReturn(mockStoryDAO);
    }

    @Test
    public void testlogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        PostResponse response = PostServiceImplSpy.post(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
