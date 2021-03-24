package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LoginServiceImplTest {
    private LoginRequest request;
    private LoginResponse expectedResponse;
    private UserDAO mockUserDAO;
    private LoginServiceImpl LoginServiceImplSpy;

    @BeforeEach
    public void setup() {
        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken resultAuthToken1 = new AuthToken();

        // Setup a request object to use in the tests
        request = new LoginRequest("fakeUsername", "fakePassword");

        // Setup a mock UserDAO that will return known responses
        expectedResponse = new LoginResponse(resultUser1, resultAuthToken1);
        mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.getLoginResponse(request)).thenReturn(expectedResponse);

        LoginServiceImplSpy = Mockito.spy(LoginServiceImpl.class);
        Mockito.when(LoginServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    @Test
    public void testlogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LoginResponse response = LoginServiceImplSpy.login(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
