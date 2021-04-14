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
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

public class FeedDAO {

    private final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
    private final String password1 = "randomPass1";
    private final User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);
    private final String password2 = "randomPass2";
    private final User user3 = new User("Bob", "Bobson", MALE_IMAGE_URL);
    private final String password3 = "randomPass3";
    private final User user4 = new User("Bonnie", "Beatty", FEMALE_IMAGE_URL);
    private final String password4 = "randomPass4";
    private final User user5 = new User("Chris", "Colston", MALE_IMAGE_URL);
    private final String password5 = "randomPass5";
    private final User user6 = new User("Cindy", "Coats", FEMALE_IMAGE_URL);
    private final String password6 = "randomPass6";
    private final User user7 = new User("Dan", "Donaldson", MALE_IMAGE_URL);
    private final String password7 = "randomPass7";
    private final User user8 = new User("Dee", "Dempsey", FEMALE_IMAGE_URL);
    private final String password8 = "randomPass8";
    private final User user9 = new User("Elliott", "Enderson", MALE_IMAGE_URL);
    private final String password9 = "randomPass9";
    private final User user10 = new User("Elizabeth", "Engle", FEMALE_IMAGE_URL);
    private final String password10 = "randomPass10";
    private final User user11 = new User("Frank", "Frandson", MALE_IMAGE_URL);
    private final User user12 = new User("Fran", "Franklin", FEMALE_IMAGE_URL);
    private final User user13 = new User("Gary", "Gilbert", MALE_IMAGE_URL);
    private final User user14 = new User("Giovanna", "Giles", FEMALE_IMAGE_URL);
    private final User user15 = new User("Henry", "Henderson", MALE_IMAGE_URL);
    private final User user16 = new User("Helen", "Hopwell", FEMALE_IMAGE_URL);
    private final User user17 = new User("Igor", "Isaacson", MALE_IMAGE_URL);
    private final User user18 = new User("Isabel", "Isaacson", FEMALE_IMAGE_URL);
    private final User user19 = new User("Justin", "Jones", MALE_IMAGE_URL);
    private final User user20 = new User("Jill", "Johnson", FEMALE_IMAGE_URL);

    private final Status status21 = new Status("Hello World 21", "@user1", "https://google.com", "Feb. 2, 2021 1:00", user2);
    private final Status status22 = new Status("Hello World 22", "@user2", "https://google.com", "Feb. 2, 2021 2:00", user3);
    private final Status status23 = new Status("Hello World 23", "@user3", "https://google.com", "Feb. 2, 2021 3:00", user4);
    private final Status status24 = new Status("Hello World 24", "@user4", "https://google.com", "Feb. 2, 2021 4:00", user5);
    private final Status status25 = new Status("Hello World 25", "@user5", "https://google.com", "Feb. 2, 2021 5:00", user6);
    private final Status status26 = new Status("Hello World 26", "@user6", "https://google.com", "Feb. 2, 2021 6:00", user7);
    private final Status status27 = new Status("Hello World 27", "@user7", "https://google.com", "Feb. 2, 2021 7:00", user8);
    private final Status status28 = new Status("Hello World 28", "@user8", "https://google.com", "Feb. 2, 2021 8:00", user9);
    private final Status status29 = new Status("Hello World 29", "@user9", "https://google.com", "Feb. 2, 2021 9:00", user10);
    private final Status status30 = new Status("Hello World 30", "@user10", "https://google.com", "Feb. 2, 2021 10:00", user11);
    private final Status status31 = new Status("Hello World 31", "@user11", "https://google.com", "Feb. 2, 2021 11:00", user12);
    private final Status status32 = new Status("Hello World 32", "@user12", "https://google.com", "Feb. 2, 2021 12:00", user13);
    private final Status status33 = new Status("Hello World 33", "@user13", "https://google.com", "Feb. 2, 2021 13:00", user14);
    private final Status status34 = new Status("Hello World 34", "@user14", "https://google.com", "Feb. 2, 2021 14:00", user15);
    private final Status status35 = new Status("Hello World 35", "@user15", "https://google.com", "Feb. 2, 2021 15:00", user16);
    private final Status status36 = new Status("Hello World 36", "@user16", "https://google.com", "Feb. 2, 2021 16:00", user17);
    private final Status status37 = new Status("Hello World 37", "@user17", "https://google.com", "Feb. 2, 2021 17:00", user18);
    private final Status status38 = new Status("Hello World 38", "@user18", "https://google.com", "Feb. 2, 2021 18:00", user19);
    private final Status status39 = new Status("Hello World 39", "@user19", "https://google.com", "Feb. 2, 2021 19:00", user20);
    private final Status status40 = new Status("Hello World 40", "@user20", "https://google.com", "Feb. 2, 2021 20:00", user20);



    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";
    private final User dummyUser = new User("Dummy", "Dummy", MALE_IMAGE_URL);
    //private final Status status1 = new Status("Hello World 1", "@user1", "https://google.com", "Feb. 2, 2021 1:00", dummyUser);

    private static final String TableName = "Feed";

    private static final String AssociatedUserAttr = "associated_user";
    private static final String TimeStampAttr = "time_stamp";
    private static final String FollowedUserAttr = "followed_user";
    private static final String MessageAttr = "message";
    private static final String MentionAttr = "mention";
    private static final String LinkAttr = "link";

    private static final UserDAO userDAO = new UserDAO();

    private static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-east-2")
            .build();
    private static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    public FeedResponse getFeed(FeedRequest request) {
        List<Status> statuses = new ArrayList<>();

        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#user", AssociatedUserAttr);

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
            startKey.put(AssociatedUserAttr, new AttributeValue().withS(request.getUserAlias()));
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
                String timestamp = item.get(TimeStampAttr).getS();
                User user = userDAO.getUser(item.get(FollowedUserAttr).getS());
                if (user == null) {
                    user = dummyUser;
                }
                statuses.add(new Status(message, mention, link, timestamp, user));
            }
        }

        boolean hasMorePages = false;
        if (queryResult.getLastEvaluatedKey() != null) {
            hasMorePages = true;
        }

        return new FeedResponse(statuses, hasMorePages);
    }

    private static boolean isNonEmptyString(String value) {
        if (value == null) {
            return false;
        }
        return (value.length() > 0);
    }

    List<Status> getDummyFeed() {
        return Arrays.asList(status21, status22, status23, status24, status25, status26, status27,
                status28, status29, status30, status31, status32, status33, status34, status35,
                status36, status37, status38, status39, status40);
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
//            int statusIndex = getStatusesStartingIndex(request.getLastTimestamp(), statuses);
//            for (int limitCount = 0; statusIndex < statuses.size() && limitCount < request.getLimit(); statusIndex++, limitCount++) {
//                responseStatuses.add(statuses.get(statusIndex));
//            }
//            hasMorePages = statusIndex < statuses.size();
//        }
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
