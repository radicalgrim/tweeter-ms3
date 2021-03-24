package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class RegisterServiceImplTest {
    private RegisterRequest request;
    private RegisterResponse expectedResponse;
    private UserDAO mockUserDAO;
    private RegisterServiceImpl RegisterServiceImplSpy;

    @BeforeEach
    public void setup() {
        String firstName = "horace";
        String lastName = "fakeman";
        String username = "h_fakeman";
        String password = "imalie";
        String imageUrl = "www.fakeman.com";
        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken resultAuthToken1 = new AuthToken();

        // Setup a request object to use in the tests
        request = new RegisterRequest(firstName, lastName, username, password, imageUrl);

        // Setup a mock UserDAO that will return known responses
        expectedResponse = new RegisterResponse(resultUser1, resultAuthToken1);
        mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.getRegisterResponse(request)).thenReturn(expectedResponse);

        RegisterServiceImplSpy = Mockito.spy(RegisterServiceImpl.class);
        Mockito.when(RegisterServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    @Test
    public void testlogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        RegisterResponse response = RegisterServiceImplSpy.register(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
