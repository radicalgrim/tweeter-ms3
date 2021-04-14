package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

public class PostPresenterTest {
    private PostRequest request;
    private PostResponse response;
    private PostService mockPostService;
    private PostPresenter presenter;
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    @BeforeEach
    public void setup() throws IOException {
        String post = "A real post";
        String fakePost = "A fake post";
        User userAllen = new User("Allen", "Anderson", MALE_IMAGE_URL);
        User fakeUser = new User("fakey", "fakerton", MALE_IMAGE_URL);
        Status validNewStatus = new Status("real Post", "@realOtherUser", "link", userAllen);
        Status invalidNewStatus = new Status("fake Post", "@fakeOtherUser", "link", fakeUser);

        request = new PostRequest(validNewStatus);
        response = new PostResponse();

        PostRequest failRequest = new PostRequest(invalidNewStatus);
        PostResponse failResponse = new PostResponse("couldn't post");

        mockPostService = Mockito.mock(PostService.class);
        //Mockito.when(mockPostResponse.follow(request)).thenReturn(response);

        presenter = Mockito.spy(new PostPresenter(new PostPresenter.View() {}));
        //Mockito.when(presenter.follow(request)).thenReturn(response);
        Mockito.when(presenter.getPostService()).thenReturn(mockPostService);
    }

    @Test
    public void followSuccess() throws IOException, TweeterRemoteException {
        Mockito.when(mockPostService.post(request)).thenReturn(response);
        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response.isSuccess(), presenter.post(request).isSuccess());
    }
    @Test
    public void followFail() throws IOException, TweeterRemoteException {
        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Mockito.when(mockPostService.post(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.post(request);
        });;
//        Assertions.assertEquals(failResponse.getMessage(), presenter.follow(failRequest).getMessage());
    }
}
