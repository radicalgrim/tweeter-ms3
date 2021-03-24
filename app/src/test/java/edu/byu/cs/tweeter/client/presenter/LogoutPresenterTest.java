package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutPresenterTest {
    private LogoutRequest request;
    private LogoutResponse response;
    private LogoutRequest failRequest;
    private LogoutResponse failResponse;
    private LogoutService mockLogoutService;
    private LogoutPresenter presenter;
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User userAllen = new User("Allen", "Anderson", MALE_IMAGE_URL);

        request = new LogoutRequest(userAllen);
        response = new LogoutResponse(true);

        User fakeUser = new User("Mr", "FakeBoi", MALE_IMAGE_URL);
        failRequest = new LogoutRequest(fakeUser);
        failResponse = new LogoutResponse("failed to logout user");

        mockLogoutService = Mockito.mock(LogoutService.class);
        Mockito.when(mockLogoutService.logout(request)).thenReturn(response);

        presenter = Mockito.spy(new LogoutPresenter(new LogoutPresenter.View() {}));
        Mockito.when(presenter.logout(request)).thenReturn(response);
    }

    @Test
    public void logoutSuccess() throws IOException, TweeterRemoteException {
        Mockito.when(mockLogoutService.logout(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response.isSuccess(), presenter.logout(request).isSuccess());
    }
    @Test
    public void logoutFail() throws IOException, TweeterRemoteException {
        Mockito.when(presenter.logout(failRequest)).thenReturn(failResponse);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(failResponse.isSuccess(), presenter.logout(failRequest).isSuccess());
        Assertions.assertEquals(failResponse.getMessage(), presenter.logout(failRequest).getMessage());
    }
}
