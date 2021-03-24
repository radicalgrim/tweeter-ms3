package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.dao.PostDAO;

public class PostServiceImplTest {
    private PostRequest request;
    private PostResponse expectedResponse;
    private PostDAO mockPostDAO;
    private PostServiceImpl PostServiceImplSpy;

    @BeforeEach
    public void setup() {
        String newPost = "new fake post, I am a fake";

        // Setup a request object to use in the tests
        request = new PostRequest(newPost);

        // Setup a mock PostDAO that will return known responses
        expectedResponse = new PostResponse();
        mockPostDAO = Mockito.mock(PostDAO.class);
        Mockito.when(mockPostDAO.post(request)).thenReturn(expectedResponse);

        PostServiceImplSpy = Mockito.spy(PostServiceImpl.class);
        Mockito.when(PostServiceImplSpy.getPostDAO()).thenReturn(mockPostDAO);
    }

    @Test
    public void testlogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        PostResponse response = PostServiceImplSpy.post(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
