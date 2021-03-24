package edu.byu.cs.tweeter.client.Integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterIntegrationTest {
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private RegisterRequest request;
    private RegisterResponse expectedResponse;
    private RegisterServiceProxy registerProxy;

    @BeforeEach
    public void setup() {
        String firstName = "horace";
        String lastName = "fakeman";
        String username = "h_fakeman";
        String password = "imalie";
        String imageUrl = "www.fakeman.com";
        User user = new User("Test", "User", MALE_IMAGE_URL);
        AuthToken resultAuthToken1 = new AuthToken();
        registerProxy = new RegisterServiceProxy();
        request = new RegisterRequest(firstName, lastName, username, password, imageUrl);
        expectedResponse = new RegisterResponse(user, resultAuthToken1);
    }

    @Test
    public void testregister_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        RegisterResponse response = registerProxy.register(request);
        Assertions.assertEquals(expectedResponse.getUser(), response.getUser());
        Assertions.assertNotNull(response.getAuthToken());
    }
}
