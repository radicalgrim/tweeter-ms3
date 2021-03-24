package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.PostServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

public class PostIntegrationTest {
    private PostRequest request;
    private PostResponse expectedResponse;
    private PostServiceProxy postProxy;

    @BeforeEach
    public void setup() {
        String fakePost = "i am a fake post";
        postProxy = new PostServiceProxy();
        request = new PostRequest(fakePost);
        expectedResponse = new PostResponse();
    }

    @Test
    public void testlogout_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        PostResponse response = postProxy.post(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
    }
}
