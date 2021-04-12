package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class AuthTokenDAOTest {

    @Test
    void testAddAuthToken_() {
//        Mockito.when(UserDAOSpy.getRegisterResponse(registerRequest)).thenReturn(registerResponse);
//
//        RegisterResponse response = UserDAOSpy.getRegisterResponse(registerRequest);
//
//        Assertions.assertEquals(registerResponse, response);

//        String firstName = "Allen";
//        String lastName = "Anderson";
//        String username = "@allen_anderson";
//        String password = "random_pass1";
//        String imageUrl = MALE_IMAGE_URL;
        AuthTokenDAO aDAO_actual = new AuthTokenDAO();
        //RegisterRequest allen_request = new RegisterRequest(firstName, lastName, username, password, imageUrl);
        String alias = "@allen_anderson";

        AuthToken returnToken = aDAO_actual.createAuthToken(alias);

        //RegisterResponse allen_response = uDAO_actual.getRegisterResponse(allen_request);
        Assertions.assertNotNull(returnToken.retrieveStringAuthToken());
    }

    @Test
    void testDeleteAuthToken(){
        String firstName = "Allen";
        String lastName = "Anderson";
        String username = "@allen_anderson";
        //String password = "random_pass1";
        //String imageUrl = MALE_IMAGE_URL;

        User user_allen = new User(firstName, lastName, username, "www.fake.com");
        AuthTokenDAO aDAO_actual = new AuthTokenDAO();
        String alias = "@allen_anderson";
        aDAO_actual.createAuthToken(alias);

        Boolean success = aDAO_actual.destroyAuthToken(user_allen);
        Assertions.assertTrue(success);
    }
}
