package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

import java.math.BigDecimal;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthTokenDAO {
    AuthToken createAuthToken(String alias){
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                //.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-2"))
                .withRegion(Regions.US_EAST_2)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable("AuthToken");
        try {
            AuthToken auth_token = new AuthToken();
            System.out.println("Adding a new authtoken...");
            long date = 0;
            PutItemOutcome outcome = table
                    //Check that this authtoken doesnt already exist??? it can exist, but not under the same primary key
                    .putItem(new Item().withPrimaryKey("user_alias", alias)
                            .withString("auth_token", auth_token.retrieveStringAuthToken())
                            .withLong("time_stamp", System.currentTimeMillis()));

            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
            return auth_token;
        } catch (Exception e) {
            System.err.println("Unable to add auth token");
            System.err.println(e.getMessage());
            return null;
        }
    }

    BigDecimal getAuthTokenTime(String alias){
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                //.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-2"))
                .withRegion(Regions.US_EAST_2)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable("AuthToken");
        try {
            GetItemSpec spec = new GetItemSpec().withPrimaryKey("user_alias", alias);
            Item outcome = table.getItem(spec);
            BigDecimal time_stamp = outcome.getNumber("time_stamp"); //is big decimal okay??
            return time_stamp;
        } catch (Exception e) {
            System.err.println("Unable to add auth token");
            System.err.println(e.getMessage());
            return null;
        }

    }

    Boolean destroyAuthToken(User userToLogout){
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                //.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-2"))
                .withRegion(Regions.US_EAST_2)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable("AuthToken");

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey("user_alias", userToLogout.getAlias());
                //.withValueMap(new ValueMap().withNumber(":val", 5.0));

        // Conditional delete (we expect this to fail)

        try {
            //System.out.println("Attempting a conditional delete...");
            table.deleteItem(deleteItemSpec);
            System.out.println("DeleteItem succeeded");
            return true;
        }
        catch (Exception e) {
            //System.err.println("Unable to delete item: " + year + " " + title);

            System.err.println(e.getMessage());
            return false;
        }
    }
}
