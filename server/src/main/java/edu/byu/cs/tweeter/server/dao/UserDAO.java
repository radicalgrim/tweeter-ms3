package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

//package com.amazonaws.codesamples.gsg;
//
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    private final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
    private final User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);

    private static final String TableName = "User";

    private static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-east-2")
            .build();
    private static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    public LoginResponse getLoginResponse(LoginRequest request) {
//        MILESTONE 3 CODE//////////////////
//        User user = new User("Test", "User", MALE_IMAGE_URL);
//        return new LoginResponse(user, new AuthToken());

        Table table = dynamoDB.getTable("User");
        String alias = request.getUsername();
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);
        Item outcome = null;
        try {
            System.out.println("Attempting to retrieve the user...");
            outcome = table.getItem(spec);
            System.out.println("GetItem succeeded: " + outcome);
            User user = new User(outcome.getString("first_name"),
                    outcome.getString("last_name"),
                    outcome.getString("alias"),
                    outcome.getString("image_url"));
            user.setFollowerCount(outcome.getInt("follower_count"));
            user.setFollowingCount(outcome.getInt("followee_count"));//make sure this is in the order the user model expects
            //cretae a new authToken associated with the alias
            AuthTokenDAO atDao = new AuthTokenDAO();
            AuthToken token = atDao.createAuthToken(alias);
            if(token != null){
                //check the password
                Integer hashedRequestPassword = request.getPassword().hashCode();
                if(outcome.getInt("password") != hashedRequestPassword){ //HASH THESE
                    return new LoginResponse("failed to login, password invalid");
                }
                return new LoginResponse(user, token);
            }
            else {
                return new LoginResponse("failed to login, authToken invalid");
            }
        } catch (Exception e) {
            System.err.println("Unable to read item: " + alias);
            System.err.println(e.getMessage());
            return new LoginResponse("failed to login");
        }
    }

    public LogoutResponse getLogoutResponse(LogoutRequest request) {
//        User user = new User("Test", "User", MALE_IMAGE_URL);
//        return new LogoutResponse(true);

        AuthTokenDAO aDao = new AuthTokenDAO();
        Boolean response = aDao.destroyAuthToken(request.getUser());
        if(response){
            return new LogoutResponse(true);
        }
        else {
            return new LogoutResponse("logout failed");
        }
    }

    public RegisterResponse getRegisterResponse(RegisterRequest request) {
//        MILESTONE 3 CODE//////////////////
//        User user = new User("Test", "User", MALE_IMAGE_URL);
//        return new RegisterResponse(user, new AuthToken());

        System.out.println("Tables: " + amazonDynamoDB.listTables().toString());
        Table table = dynamoDB.getTable("User");
//        infoMap.put("follower_count", request.getFollowerCount());
//        infoMap.put("followee_count", request.getFolloweeCount());
        try {
            //check that alias does not already exist
            System.out.println("setting spec,  where alias = " + request.getUsername());
            GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", request.getUsername());
            System.out.println("checkItem if exists...");
            Item checkIfExists = table.getItem(spec);
            //System.out.println("checkItemIfExists = " + checkIfExists.getString("alias"));
            if(checkIfExists != null/*checkIfExists.getString("alias") != null*/){
                return new RegisterResponse("register failed, alias already in use.");
            }
            //add the new user to the table
            System.out.println("Adding a new user...");
            Integer hashedPassword = request.getPassword().hashCode();
            PutItemOutcome outcome = table
                    .putItem(new PutItemSpec().withItem(new Item().withPrimaryKey("alias", request.getUsername())
                            //.withString("alias", request.getUsername())
                            .withString("first_name", request.getFirstName())
                            .withString("last_name", request.getLastName())
                            .withString("image_url", request.getImageUrl())
                            .withInt("password", hashedPassword)
                            .withInt("follower_count", 0)
                            .withInt("followee_count", 0)));
                            //.withBoolean("current_user", true))) ?????is this a good idea??;
            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
            User user = new User(request.getFirstName(),
                    request.getLastName(), request.getUsername(),
                    request.getImageUrl());
            // CREATE NEW AUTHTOKEN ALSO
            AuthTokenDAO atDao = new AuthTokenDAO();
            AuthToken token = atDao.createAuthToken(request.getUsername());
            if(token != null){
                return new RegisterResponse(user, token);
            }
            else {
                return new RegisterResponse("failed to register, authToken invalid");
            }
        } catch (Exception e) {
            System.err.println("Unable to add user: " + request.getUsername());
            System.err.println(e.getMessage());
            return new RegisterResponse("register failed");
        }
    }

    public User getUser(String userAlias) {
        User user = null;

        Table table = dynamoDB.getTable("User");

        try {
            GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", userAlias);
            Item outcome = table.getItem(spec);
            user = new User(outcome.getString("first_name"),
                    outcome.getString("last_name"), outcome.getString("alias"),
                    outcome.getString("image_url"));
        } catch (Exception e) {
            System.err.println("Unable to find user: " + userAlias);
            System.err.println(e.getMessage());
        }

        return user;
    }


    public void addUserBatch(List<User> users) {

        // Constructor for TableWriteItems takes the name of the table
        TableWriteItems items = new TableWriteItems(TableName);

        // Add each user into the TableWriteItems object
        for (User user : users) {
            Item item = new Item()
                    .withPrimaryKey("alias", user.getAlias())
                    .withString("first_name", user.getFirstName())
                    .withString("last_name", user.getLastName())
                    .withString("image_url", user.getImageUrl());
            items.addItemToPut(item);

            // 25 is the maximum number of items allowed in a single batch write.
            // Attempting to write more than 25 items will result in an exception being thrown
            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems(TableName);
            }
        }

        // Write any leftover items
        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items);
        }
    }

    private void loopBatchWrite(TableWriteItems items) {

        // The 'dynamoDB' object is of type DynamoDB and is declared statically in this example
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);
        //logger.log("Wrote User Batch");

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
            //logger.log("Wrote more Users");
        }
    }

}
