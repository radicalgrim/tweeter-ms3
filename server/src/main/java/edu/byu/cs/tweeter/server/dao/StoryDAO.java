package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class StoryDAO {
//    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
//    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";
//
//    private final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
//    private final String password1 = "randomPass1";
//    private final Status status1 = new Status("Hello World 1", "@user1", "https://google.com", "Feb. 2, 2021 1:00", user2);

//    private final User testUser = new User("Allen", "Anderson", "@allen_anderson", MALE_IMAGE_URL);

    private static final String TableName = "Story";

    private static final String UserAliasAttr = "user_alias";
    private static final String TimeStampAttr = "time_stamp";
    private static final String MessageAttr = "message";
    private static final String MentionAttr = "mention";
    private static final String LinkAttr = "link";
    private static final BigDecimal hour= BigDecimal.valueOf(3600000.0);

    private static final UserDAO userDAO = new UserDAO();

    private static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-east-2")
            .build();
    private static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    public StoryResponse getStory(StoryRequest request) {
        List<Status> statuses = new ArrayList<>();

        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#user", UserAliasAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":user", new AttributeValue().withS(request.getUserAlias()));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TableName)
                .withKeyConditionExpression("#user = :user")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(request.getLimit());

        if (isNonEmptyString(request.getLastTimestamp())) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(UserAliasAttr, new AttributeValue().withS(request.getUserAlias()));
            startKey.put(TimeStampAttr, new AttributeValue().withS(request.getLastTimestamp()));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            for (Map<String, AttributeValue> item : items) {
                String message = item.get(MessageAttr).getS();
                String mention = item.get(MentionAttr).getS();
                String link = item.get(LinkAttr).getS();
                statuses.add(new Status(message, mention, link, userDAO.getUser(request.getUserAlias())));
            }
        }

//        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
//        if (lastKey != null) {
//            result.setLastKey(lastKey.get(LocationAttr).getS());
//        }

//        assertValidRequest(request.getLimit(), request.getUserAlias());
//        List<Status> dummyStatuses = getDummyStory();
//        List<Status> responseStatuses = new ArrayList<>(request.getLimit());
//        boolean hasMorePages = false;
//
//        if (request.getLimit() > 0) {
//            int statusIndex = getStatusesStartingIndex(request.getLastTimestamp(), dummyStatuses);
//            for (int limitCount = 0; statusIndex < dummyStatuses.size() && limitCount < request.getLimit(); statusIndex++, limitCount++) {
//                responseStatuses.add(dummyStatuses.get(statusIndex));
//            }
//            hasMorePages = statusIndex < dummyStatuses.size();
//        }

        return new StoryResponse(statuses, false);  // FIXME
    }

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    public PostResponse post(PostRequest request){
        System.out.println("Tables: " + amazonDynamoDB.listTables().toString());
        Table table = dynamoDB.getTable("Story");
        try {
            //check that alias does not already exist
            //System.out.println("setting spec,  where alias = " + request.getUsername());
            //GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", request.getUsername());
            //System.out.println("checkItem if exists...");
            //Item checkIfExists = table.getItem(spec);
            //System.out.println("checkItemIfExists = " + checkIfExists.getString("alias"));
//            if(checkIfExists != null/*checkIfExists.getString("alias") != null*/){
//                return new RegisterResponse("register failed, alias already in use.");
//            }
            //add the new user to the table
            //System.out.println("Adding a new user...");
            //Integer hashedPassword = request.getPassword().hashCode();
            if(checkAuthTokenTime(request.getStatus().getUser().getAlias()) == false){ //??what should i do here?
                return new PostResponse("authToken is no longer valid");
            }
            PutItemOutcome outcome = table
                    .putItem(new PutItemSpec().withItem(new Item().withPrimaryKey("user_alias", request.getStatus().getUser().getAlias())
                            //.withString("alias", request.getUsername())
                            .withNumber("time_stamp", System.currentTimeMillis())
                            .withString("link", request.getStatus().getLink())
                            .withString("mention", request.getStatus().getMention())));
            //.withBoolean("current_user", true))) ?????is this a good idea??;
            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
        } catch (Exception e) {
            System.err.println("Unable to post status to user story.");
            System.err.println(e.getMessage());
            return new PostResponse("post failed");
        }

        return new PostResponse();
    }

    Boolean checkAuthTokenTime(String alias){
        AuthTokenDAO aDao = new AuthTokenDAO();
        BigDecimal authTokenTime = aDao.getAuthTokenTime(alias);
        BigDecimal currentTime = BigDecimal.valueOf(System.currentTimeMillis());
        if((authTokenTime.subtract(currentTime)).compareTo(hour) > 0){ //??what should i do here?
            return false;
        }
        return true;
    }

}
