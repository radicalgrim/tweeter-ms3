package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterServiceProxyTest {
    private RegisterRequest validRequest;
    private RegisterRequest invalidRequest;

    private RegisterResponse successResponse;
    private RegisterResponse failureResponse;

    private RegisterServiceProxy registerServiceProxySpy;
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User userAllen = new User("Allen", "Anderson", MALE_IMAGE_URL);

        // Setup request objects to use in the tests
        validRequest = new RegisterRequest("Allen", "Anderson", "@AllenAnderson", "randomPass1", MALE_IMAGE_URL);
        invalidRequest = new RegisterRequest("Mr","FakeFool", "@fakeyFakerton", "liarsPassword", MALE_IMAGE_URL);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new RegisterResponse(userAllen, new AuthToken());
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.register(validRequest, RegisterServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new RegisterResponse("couldn't find the user");
        Mockito.when(mockServerFacade.register(invalidRequest, RegisterServiceProxy.URL_PATH)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        registerServiceProxySpy = Mockito.spy(new RegisterServiceProxy());
        Mockito.when(registerServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void registerServiceSuccess() throws IOException, TweeterRemoteException {
        RegisterResponse response = registerServiceProxySpy.register(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void registerServiceFail() throws IOException, TweeterRemoteException {
        RegisterResponse response = registerServiceProxySpy.register(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
