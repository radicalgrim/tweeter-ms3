package edu.byu.cs.tweeter.client.Integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

public class LoginIntegrationTest {
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private LoginRequest request;
    private LoginResponse expectedResponse;
    private LoginServiceProxy loginProxy;

    @BeforeEach
    public void setup() {
        User user = new User("Test", "User", MALE_IMAGE_URL);
        AuthToken resultAuthToken1 = new AuthToken();
        loginProxy = new LoginServiceProxy();
        request = new LoginRequest("fakeUsername", "fakePassword");
        expectedResponse = new LoginResponse(user, resultAuthToken1);
    }

    @Test
    public void testlogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LoginResponse response = loginProxy.login(request);
        Assertions.assertEquals(expectedResponse.getUser(), response.getUser());
        Assertions.assertNotNull(response.getAuthToken());
    }
}
