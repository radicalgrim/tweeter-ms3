package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

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
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";


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
//        Mockito.when(UserDAOSpy.getLoginResponse(loginRequest)).thenReturn(expectedLoginResponse);
//
//        LoginResponse response = UserDAOSpy.getLoginResponse(loginRequest);
//
//        Assertions.assertEquals(expectedLoginResponse, response);
        String username = "@allen_anderson";
        String password = "random_pass1";
        UserDAO uDAO_actual = new UserDAO();
        LoginRequest allen_request = new LoginRequest(username, password);


        LoginResponse allen_response = uDAO_actual.getLoginResponse(allen_request);
        Assertions.assertNotNull(allen_response.getUser());
    }

    @Test
    void testRegister_() {
//        Mockito.when(UserDAOSpy.getRegisterResponse(registerRequest)).thenReturn(registerResponse);
//
//        RegisterResponse response = UserDAOSpy.getRegisterResponse(registerRequest);
//
//        Assertions.assertEquals(registerResponse, response);

        String firstName = "Allen";
        String lastName = "Anderson";
        String username = "@allen_anderson";
        String password = "random_pass1";
        String imageUrl = MALE_IMAGE_URL;
        UserDAO uDAO_actual = new UserDAO();
        RegisterRequest allen_request = new RegisterRequest(firstName, lastName, username, password, imageUrl);


        RegisterResponse allen_response = uDAO_actual.getRegisterResponse(allen_request);
        Assertions.assertNotNull(allen_response.getUser());
    }

//    @Test
//    void makeUserTable() throws InterruptedException {
//        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
//                //.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://45qrwqgumi.execute-api.us-east-2.amazonaws.com/Tweeter", "us-east-2"))
//                .withRegion(Regions.US_EAST_2)
//                .build(); //WHAT IS THE ENDPOINT URL???
//        DynamoDB dynamoDB = new DynamoDB(client);
//        System.out.println("Tables: " + client.listTables().toString());
//        Table table = dynamoDB.createTable("User", Arrays.asList(new KeySchemaElement("alias", KeyType.HASH)), Arrays.asList(new AttributeDefinition("alias", ScalarAttributeType.S)), new ProvisionedThroughput(10L, 10L));
//        table.waitForActive();
//        System.out.println("table status: " + table.getDescription().getTableStatus());
//    }

    @Test
    void testLogout_() {
        Mockito.when(UserDAOSpy.getLogoutResponse(logoutRequest)).thenReturn(logoutResponse);

        LogoutResponse response = UserDAOSpy.getLogoutResponse(logoutRequest);

        Assertions.assertEquals(logoutResponse, response);
    }
}
