package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

import java.util.HashMap;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import java.util.Date;

public class AuthTokenDAO {
    AuthToken createAuthToken(String alias){
        AuthToken token = new AuthToken();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-2"))
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable("AuthToken");
        try {
            System.out.println("Adding a new authtoken...");
            long date = 0;
            PutItemOutcome outcome = table
                    //Check that this authtoken doesnt already exist??? it can exist, but not under the same primary key
                    .putItem(new Item().withPrimaryKey("alias", alias)
                            .withString("auth_token", token.toString())
                            .withLong("time_stamp", System.currentTimeMillis()));

            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
            return token;
        } catch (Exception e) {
            System.err.println("Unable to add auth token");
            System.err.println(e.getMessage());
            return null;
        }
    }
}
