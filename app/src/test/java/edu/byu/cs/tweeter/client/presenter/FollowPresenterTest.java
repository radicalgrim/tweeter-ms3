package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

public class FollowPresenterTest {
    private FollowRequest request;
    private FollowResponse response;
    private FollowService mockFollowService;
    private FollowPresenter presenter;
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    @BeforeEach
    public void setup() throws IOException {
        User userAllen = new User("Allen", "Anderson", MALE_IMAGE_URL);

        request = new FollowRequest(userAllen, userAllen);
        response = new FollowResponse(true);

        User fakeUser = new User("Mr", "FakeBoi", MALE_IMAGE_URL);
        FollowRequest failRequest = new FollowRequest(fakeUser, fakeUser);
        FollowResponse failResponse = new FollowResponse(false, "couldn't follow the user");

        mockFollowService = Mockito.mock(FollowService.class);
        //Mockito.when(mockFollowService.follow(request)).thenReturn(response);

        presenter = Mockito.spy(new FollowPresenter(new FollowPresenter.View() {}));
        //Mockito.when(presenter.follow(request)).thenReturn(response);
        Mockito.when(presenter.getFollowService()).thenReturn(mockFollowService);
    }

    @Test
    public void followSuccess() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowService.follow(request)).thenReturn(response);
        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response.isSuccess(), presenter.follow(request).isSuccess());
    }
    @Test
    public void followFail() throws IOException, TweeterRemoteException {
        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Mockito.when(mockFollowService.follow(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.follow(request);
        });;
//        Assertions.assertEquals(failResponse.getMessage(), presenter.follow(failRequest).getMessage());
    }
}
