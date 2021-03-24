package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutIntegrationTest {
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private LogoutRequest request;
    private LogoutResponse expectedResponse;
    private LogoutServiceProxy logoutProxy;

    @BeforeEach
    public void setup() {
        User user = new User("Test", "User", MALE_IMAGE_URL);
        AuthToken resultAuthToken1 = new AuthToken();
        logoutProxy = new LogoutServiceProxy();
        request = new LogoutRequest(user);
        expectedResponse = new LogoutResponse(true);
    }

    @Test
    public void testlogout_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutProxy.logout(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
    }
}
