package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LogoutServiceImplTest {
    private LogoutRequest request;
    private LogoutResponse expectedResponse;
    private UserDAO mockUserDAO;
    private LogoutServiceImpl LogoutServiceImplSpy;

    @BeforeEach
    public void setup() {
        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken resultAuthToken1 = new AuthToken();

        // Setup a request object to use in the tests
        request = new LogoutRequest(resultUser1);

        // Setup a mock UserDAO that will return known responses
        expectedResponse = new LogoutResponse(true);
        mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.getLogoutResponse(request)).thenReturn(expectedResponse);

        LogoutServiceImplSpy = Mockito.spy(LogoutServiceImpl.class);
        Mockito.when(LogoutServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    @Test
    public void testlogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LogoutResponse response = LogoutServiceImplSpy.logout(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
