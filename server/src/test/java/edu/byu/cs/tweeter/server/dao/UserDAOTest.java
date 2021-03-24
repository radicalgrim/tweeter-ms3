package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

class UserDAOTest {
    private LoginRequest loginRequest;
    private LoginResponse expectedLoginResponse;
    private RegisterRequest registerRequest;
    private RegisterResponse registerResponse;
    private LogoutRequest logoutRequest;
    private LogoutResponse logoutResponse;


    private final User user1 = new User("Daffy", "Duck", "");

    private UserDAO UserDAOSpy;

    @BeforeEach
    void setup() {
        UserDAOSpy = Mockito.spy(new UserDAO());
        AuthToken resultAuthToken1 = new AuthToken();
        // Setup a request object to use in the tests
        loginRequest = new LoginRequest("fakeUsername", "fakePassword");
        // Setup a mock UserDAO that will return known responses
        expectedLoginResponse = new LoginResponse(user1, resultAuthToken1);

        String firstName = "horace";
        String lastName = "fakeman";
        String username = "h_fakeman";
        String password = "imalie";
        String imageUrl = "www.fakeman.com";
        registerRequest = new RegisterRequest(firstName, lastName, username, password, imageUrl);
        registerResponse = new RegisterResponse(user1, resultAuthToken1);

        logoutRequest = new LogoutRequest(user1);
        logoutResponse = new LogoutResponse(true);
    }

    @Test
    void testLogin_() {
        Mockito.when(UserDAOSpy.getLoginResponse(loginRequest)).thenReturn(expectedLoginResponse);

        LoginResponse response = UserDAOSpy.getLoginResponse(loginRequest);

        Assertions.assertEquals(expectedLoginResponse, response);
    }

    @Test
    void testRegister_() {
        Mockito.when(UserDAOSpy.getRegisterResponse(registerRequest)).thenReturn(registerResponse);

        RegisterResponse response = UserDAOSpy.getRegisterResponse(registerRequest);

        Assertions.assertEquals(registerResponse, response);
    }

    @Test
    void testLogout_() {
        Mockito.when(UserDAOSpy.getLogoutResponse(logoutRequest)).thenReturn(logoutResponse);

        LogoutResponse response = UserDAOSpy.getLogoutResponse(logoutRequest);

        Assertions.assertEquals(logoutResponse, response);
    }
}
