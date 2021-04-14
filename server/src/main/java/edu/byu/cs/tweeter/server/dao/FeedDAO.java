package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;


public class FeedDAO {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
    private final Status status1 = new Status("Hello World 1", "@user1", "https://google.com", "Feb. 2, 2021 1:00", user1);

    private static final String TableName = "Feed";

    private static final String AssociatedUserAttr = "associated_user";
    private static final String TimeStampAttr = "time_stamp";
    private static final String FollowedUserAttr = "followed_user";
    private static final String MessageAttr = "message";
    private static final String MentionAttr = "mention";
    private static final String LinkAttr = "link";

    private static final UserDAO userDAO = new UserDAO();
    private static final FollowDAO followDAO = new FollowDAO();

    private static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-east-2")
            .build();
    private static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    public FeedResponse getFeed(FeedRequest request) {
        List<Status> statuses = new ArrayList<>();

//        // TODO: Adapt to feed table
//        Map<String, String> attrNames = new HashMap<>();
//        attrNames.put("#user", AssociatedUserAttr);
//
//        Map<String, AttributeValue> attrValues = new HashMap<>();
//        attrValues.put(":user", new AttributeValue().withS(request.getUserAlias()));
//
//        QueryRequest queryRequest = new QueryRequest()
//                .withTableName(TableName)
//                .withKeyConditionExpression("#user = :user")
//                .withExpressionAttributeNames(attrNames)
//                .withExpressionAttributeValues(attrValues)
//                .withLimit(request.getLimit());
//
//        if (isNonEmptyString(request.getLastTimestamp())) {
//            Map<String, AttributeValue> startKey = new HashMap<>();
//            startKey.put(AssociatedUserAttr, new AttributeValue().withS(request.getUserAlias()));
//            startKey.put(TimeStampAttr, new AttributeValue().withS(request.getLastTimestamp()));
//
//            queryRequest = queryRequest.withExclusiveStartKey(startKey);
//        }
//
//        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
//        List<Map<String, AttributeValue>> items = queryResult.getItems();
//        if (items != null) {
//            for (Map<String, AttributeValue> item : items) {
//                String message = item.get(MessageAttr).getS();
//                String mention = item.get(MentionAttr).getS();
//                String link = item.get(LinkAttr).getS();
//                String timestamp = item.get(TimeStampAttr).getS();
//                User user = userDAO.getUser(item.get(FollowedUserAttr).getS());
//                statuses.add(new Status(message, mention, link, timestamp, user));
//            }
//        }
//
//        boolean hasMorePages = false;
//        if (queryResult.getLastEvaluatedKey() != null) {
//            hasMorePages = true;
//        }

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
//            int statusIndex = getStatusesStartingIndex(request.getLastTimestamp(), statuses);
//            for (int limitCount = 0; statusIndex < statuses.size() && limitCount < request.getLimit(); statusIndex++, limitCount++) {
//                responseStatuses.add(statuses.get(statusIndex));
//            }
//            hasMorePages = statusIndex < statuses.size();
//        }

        boolean hasMorePages = false;
        statuses.add(status1);

        return new FeedResponse(statuses, hasMorePages);  // FIXME: Paginate
    }

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

//    private void assertValidRequest(int limit, String userAlias) {
//        //Used in place of assert statements because Android does not support them
//        assert limit >= 0;
//
////            throw new AssertionError();
//        assert userAlias != null;
//    }

//    private int getStatusesStartingIndex(String lastStatusTimestamp, List<Status> statuses) {
//
//        int statusIndex = 0;
//        if (lastStatusTimestamp != null) {
//            // This is a paged request for something after the first page. Find the first item
//            // we should return
//            for (int i = 0; i < statuses.size(); i++) {
//                if (lastStatusTimestamp.equals(statuses.get(i).getTimestamp())) {
//                    // We found the index of the last item returned last time. Increment to get
//                    // to the first one we should return
//                    statusIndex = i + 1;
//                    break;
//                }
//            }
//        }
//        return statusIndex;
//    }
}
