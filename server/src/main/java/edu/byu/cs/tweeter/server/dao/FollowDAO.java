package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;


public class FollowDAO {
    // This is the hard coded follower data returned by the 'getFollowers()' method
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

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


    private static final String TableName = "follows";
    private static final String IndexName = "follows_index";

    private static final String FollowerHandleAttr = "follower_handle";
    private static final String FolloweeHandleAttr = "followee_handle";

    private static final UserDAO userDAO = new UserDAO();

    private static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-east-2")
            .build();
    private static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
    private static final BigDecimal hour= BigDecimal.valueOf(3600000.0);


    //FOLLOW DAO ORIGINAL CODE///////////////////////////////////////////////////////////////////////////
    public FollowResponse follow(FollowRequest request){
        //currentUser.incrementFollowingCount();

        //get the item in the follows table by looking up current user as primary key and user as secondary key. Update to follows
        //if not following and increment the following count for user
        //even if the user already follows the followee
        Table table = dynamoDB.getTable("follows");
        Table userTable = dynamoDB.getTable("User");
        String current_user_alias = request.getCurrentUser().getAlias();
        String user_to_follow_alias = request.getUser().getAlias();
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("follower_handle", current_user_alias, "followee_handle", user_to_follow_alias);
        Item checkIfExists = null;
        try {
            System.out.println("Attempting to find the follows instance...");
            //is there a better way than querying the authTokenTable??
            if(checkAuthTokenTime(request.getCurrentUser().getAlias()) == false){ //??what should i do here?
                return new FollowResponse(false, "authToken is no longer valid");
            }
            checkIfExists = table.getItem(spec);
            if(checkIfExists != null){
                return new FollowResponse(false, "User is already followed");
            }

            PutItemOutcome outcome = table
                    .putItem(new PutItemSpec().withItem(new Item().withPrimaryKey("follower_handle", request.getCurrentUser().getAlias(), "followee_handle", request.getUser().getAlias())));
                            //.withString("followee_handle", request.getUser().getAlias())));
            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
            System.out.println("Follow succeeded: " + outcome);
            //modify follower value for user, following value for current_user in the table??? use update item
            if(updateUserTableFollow(current_user_alias, user_to_follow_alias) == false){
                return new FollowResponse(false, "failed to update the follow count in user table");
            }
            request.getUser().setFollowerCount(request.getUser().getFollowerCount() + 1);
            request.getCurrentUser().setFollowingCount(request.getCurrentUser().getFollowingCount() + 1);//make sure this is in the order the user model expects
            return new FollowResponse(true);
        } catch (Exception e) {
            System.err.println("Unable to follow");
            System.err.println(e.getMessage());
            return new FollowResponse(false, "failed to follow");
        }
    }

    Boolean updateUserTableFollow(String currentUser, String followedUser){
        //update the followees for the currentUser

        Table table = dynamoDB.getTable("User");
        //getcurrentuser
        Item user = table.getItem("alias", currentUser);
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("alias", currentUser)
                .withUpdateExpression("set followee_count = :r")
                .withValueMap(new ValueMap().withNumber(":r", user.getInt("followee_count") + 1));
                //.withReturnValues(ReturnValue.UPDATED_NEW); //??? what?
        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            System.out.println("UpdateItem succeeded:\n");

        }
        catch (Exception e) {
            System.err.println("Unable to update user table, follow failed");
            System.err.println(e.getMessage());
            return false;
        }
        //now updatde the followers for the other user
        Item user_followed = table.getItem("alias", currentUser);
        UpdateItemSpec updateItemSpec2 = new UpdateItemSpec().withPrimaryKey("alias", followedUser)
                .withUpdateExpression("set follower_count = :r")
                .withValueMap(new ValueMap().withNumber(":r", user_followed.getInt("follower_count") + 1));
        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome2 = table.updateItem(updateItemSpec2);
            System.out.println("UpdateItem succeeded:\n");

        }
        catch (Exception e) {
            System.err.println("Unable to update user table, follow failed");
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    Boolean updateUserTableUnfollow(String currentUser, String followedUser){
        //update the followees for the currentUser
        Table table = dynamoDB.getTable("User");
        Item user = table.getItem("alias", currentUser);
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("alias", currentUser)
                .withUpdateExpression("set followee_count = :r")
                .withValueMap(new ValueMap().withNumber(":r", user.getInt("followee_count") - 1));
        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            System.out.println("UpdateItem succeeded:\n");

        }
        catch (Exception e) {
            System.err.println("Unable to update user table, unfollow failed.");
            System.err.println(e.getMessage());
            return false;
        }
        //now updatde the followers for the other user
        Item user_unfollowed = table.getItem("alias", currentUser);
        UpdateItemSpec updateItemSpec2 = new UpdateItemSpec().withPrimaryKey("alias", followedUser)
                .withUpdateExpression("set follower_count = :r")
                .withValueMap(new ValueMap().withNumber(":r", user.getInt("follower_count") - 1));
        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome2 = table.updateItem(updateItemSpec2);
            System.out.println("UpdateItem succeeded:\n");

        }
        catch (Exception e) {
            System.err.println("Unable to update user table, unfollow failed");
            System.err.println(e.getMessage());
            return false;
        }
        return true;
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

    //UNFOLLOW DAO ORIGINAL CODE///////////////////////////////////////////////////////////////////////////
    public UnfollowResponse unfollow(UnfollowRequest request){
//        UnfollowResponse response = new UnfollowResponse(true);
//        //currentUser.decrementFollowingCount();
//        //int testVar = currentUser.getFollowingCount();
//        return response;
        Table table = dynamoDB.getTable("follows");
        String current_user_alias = request.getCurrentUser().getAlias();
        String user_to_unfollow_alias = request.getUser().getAlias();
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("follower_handle", current_user_alias, "followee_handle", user_to_unfollow_alias);
        DeleteItemSpec deleteSpec = new DeleteItemSpec().withPrimaryKey("follower_handle", current_user_alias, "followee_handle", user_to_unfollow_alias);
        Item checkIfExists = null;
        try {
            System.out.println("Attempting to find the follows instance...");
            //is there a better way than querying the authTokenTable??
            if(checkAuthTokenTime(request.getCurrentUser().getAlias()) == false){ //??what should i do here?
                return new UnfollowResponse("authToken is no longer valid");
            }
            checkIfExists = table.getItem(spec);
            if(checkIfExists != null){
                table.deleteItem(deleteSpec);
                //return new FollowResponse(false, "User is already followed");
                if(updateUserTableUnfollow(current_user_alias, user_to_unfollow_alias) == false){
                    return new UnfollowResponse("failed to update the table, unfollow failed.");
                }
                System.out.println("DeleteItem succeeded");
                request.getUser().setFollowerCount(request.getUser().getFollowerCount() + 1);
                request.getCurrentUser().setFollowingCount(request.getCurrentUser().getFollowingCount() + 1);
            }
            else {
                return new UnfollowResponse("User was already not followed");
            }
            return new UnfollowResponse(true);
        } catch (Exception e) {
            System.err.println("Unable to follow");
            System.err.println(e.getMessage());
            return new UnfollowResponse("failed to unfollow, exception caught");
        }
    }

    //FOLLOWER DAO ORIGINAL CODE///////////////////////////////////////////////////////////////////////////
//    public Integer getFollowerCount(User followee) {
//        assert followee != null;
//        return getDummyFollowers().size();
//    }

    public FollowerResponse getFollowers(FollowerRequest request) {

//        List<User> followers = new ArrayList<>();
//
//        Map<String, String> attrNames = new HashMap<>();
//        attrNames.put("#followee", FolloweeHandleAttr);     // Follower?
//
//        Map<String, AttributeValue> attrValues = new HashMap<>();
//        attrValues.put(":followee", new AttributeValue().withS(request.getFolloweeAlias()));
//
//        QueryRequest queryRequest = new QueryRequest()
//                .withIndexName(IndexName)
//                //.withTableName(TableName)
//                .withKeyConditionExpression("#followee = :followee")
//                .withExpressionAttributeNames(attrNames)
//                .withExpressionAttributeValues(attrValues)
//                .withScanIndexForward(true)
//                .withLimit(request.getLimit());
//
//        if (isNonEmptyString(request.getLastFollowerAlias())) {
//            Map<String, AttributeValue> startKey = new HashMap<>();
//            startKey.put(FolloweeHandleAttr, new AttributeValue().withS(request.getFolloweeAlias()));       // userAlias
//            startKey.put(FollowerHandleAttr, new AttributeValue().withS(request.getLastFollowerAlias()));           // timestamp
//
//            queryRequest = queryRequest.withExclusiveStartKey(startKey);
//        }
//
//        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
//        List<Map<String, AttributeValue>> items = queryResult.getItems();
//        if (items != null) {
//            for (Map<String, AttributeValue> item : items) {
//                User user = userDAO.getUser(item.get(FollowerHandleAttr).getS());
//                followers.add(user);
//            }
//        }
//
//        boolean hasMorePages = false;
//        if (queryResult.getLastEvaluatedKey() != null) {
//            hasMorePages = true;
//        }

        List<User> followers = new ArrayList<>();

        Index tableIndex = dynamoDB.getTable(TableName).getIndex(IndexName);

        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("#ee", FolloweeHandleAttr);

        // Value that we are searching for
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put(":ee", request.getFolloweeAlias());

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#ee = :ee")
                .withNameMap(nameMap)
                .withValueMap(valueMap)
                .withMaxResultSize(request.getLimit())
                .withScanIndexForward(false);

        if (isNonEmptyString(request.getLastFollowerAlias())) {
            querySpec.withExclusiveStartKey(FolloweeHandleAttr, request.getFolloweeAlias(),
                    FollowerHandleAttr, request.getLastFollowerAlias());
        }

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;
        try {
            items = tableIndex.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                User user = userDAO.getUser(item.getString(FollowerHandleAttr));
                followers.add(user);
            }
        } catch (Exception ignored) { }

        boolean hasMorePages = false;
        if (items != null) {
            if (items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null) {
                hasMorePages = true;
            }
        }

        return new FollowerResponse(followers, hasMorePages);
    }

    //        assert request.getLimit() > 0;
//        assert request.getFolloweeAlias() != null;
//
//        List<User> allFollowers = getDummyFollowers();
//        List<User> responseFollowers = new ArrayList<>(request.getLimit());
//
//        boolean hasMorePages = false;
//
//        if(request.getLimit() > 0) {
//            if (allFollowers != null) {
//                int followersIndex = getFollowersStartingIndex(request.getLastFollowerAlias(), allFollowers);
//
//                for(int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < request.getLimit(); followersIndex++, limitCounter++) {
//                    responseFollowers.add(allFollowers.get(followersIndex));
//                }
//
//                hasMorePages = followersIndex < allFollowers.size();
//            }
//        }
//    private int getFollowersStartingIndex(String lastFollowerAlias, List<User> allFollowers) {
//
//        int followersIndex = 0;
//
//        if(lastFollowerAlias != null) {
//            // This is a paged request for something after the first page. Find the first item
//            // we should return
//            for (int i = 0; i < allFollowers.size(); i++) {
//                if(lastFollowerAlias.equals(allFollowers.get(i).getAlias())) {
//                    // We found the index of the last item returned last time. Increment to get
//                    // to the first one we should return
//                    followersIndex = i + 1;
//                    break;
//                }
//            }
//        }
//
//        return followersIndex;
//    }
//
//    List<User> getDummyFollowers() {
//        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
//                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
//                user19, user20);
//    }
//
//    //FOLLOWING DAO ORIGINAL CODE///////////////////////////////////////////////////////////////////////////
//    public Integer getFolloweeCount(User follower) {
//        assert follower != null;
//        return getDummyFollowees().size();
//    }

    public FollowingResponse getFollowees(FollowingRequest request) {

//        List<User> followees = new ArrayList<>();
//
//        Map<String, String> attrNames = new HashMap<>();
//        attrNames.put("#follower", FollowerHandleAttr);
//
//        Map<String, AttributeValue> attrValues = new HashMap<>();
//        attrValues.put(":follower", new AttributeValue().withS(request.getFollowerAlias()));
//
//        QueryRequest queryRequest = new QueryRequest()
//                .withTableName(TableName)
//                .withKeyConditionExpression("#follower = :follower")
//                .withExpressionAttributeNames(attrNames)
//                .withExpressionAttributeValues(attrValues)
//                .withScanIndexForward(true)
//                .withLimit(request.getLimit());
//
//        if (isNonEmptyString(request.getLastFolloweeAlias())) {
//            Map<String, AttributeValue> startKey = new HashMap<>();
//            startKey.put(FollowerHandleAttr, new AttributeValue().withS(request.getFollowerAlias()));       // userAlias
//            startKey.put(FolloweeHandleAttr, new AttributeValue().withS(request.getLastFolloweeAlias()));   // timestamp
//
//            queryRequest = queryRequest.withExclusiveStartKey(startKey);
//        }
//
//        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
//        List<Map<String, AttributeValue>> items = queryResult.getItems();
//        if (items != null) {
//            for (Map<String, AttributeValue> item : items) {
//                User user = userDAO.getUser(item.get(FolloweeHandleAttr).getS());
//                followees.add(user);
//            }
//        }
//
//        boolean hasMorePages = false;
//        if (queryResult.getLastEvaluatedKey() != null) {
//            hasMorePages = true;
//        }

        List<User> followees = new ArrayList<>();

        Table table = dynamoDB.getTable(TableName);

        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("#er", FollowerHandleAttr);

        // Value that we are searching for
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put(":er", request.getFollowerAlias());

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#er = :er")
                .withNameMap(nameMap)
                .withValueMap(valueMap)
                .withMaxResultSize(request.getLimit())
                .withScanIndexForward(false);

        if (isNonEmptyString(request.getLastFolloweeAlias())) {
            querySpec.withExclusiveStartKey(FolloweeHandleAttr, request.getFollowerAlias(),
                    FollowerHandleAttr, request.getLastFolloweeAlias());
        }

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;
        try {
            items = table.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                User user = userDAO.getUser(item.getString(FolloweeHandleAttr));
                followees.add(user);
            }
        } catch (Exception ignored) { }

        boolean hasMorePages = false;
        if (items != null) {
            if (items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null) {
                hasMorePages = true;
            }
        }

        return new FollowingResponse(followees, hasMorePages);
    }

    private static boolean isNonEmptyString(String value) {
        if (value == null) {
            return false;
        }
        return (value.length() > 0);
    }

    //        assert request.getLimit() > 0;
//        assert request.getFollowerAlias() != null;
//
//        List<User> allFollowees = getDummyFollowees();
//        List<User> responseFollowees = new ArrayList<>(request.getLimit());
//
//        boolean hasMorePages = false;
//
//        if(request.getLimit() > 0) {
//            if (allFollowees != null) {
//                int followeesIndex = getFolloweesStartingIndex(request.getLastFolloweeAlias(), allFollowees);
//
//                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
//                    responseFollowees.add(allFollowees.get(followeesIndex));
//                }
//
//                hasMorePages = followeesIndex < allFollowees.size();
//            }
//        }
//    private int getFolloweesStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {
//
//        int followeesIndex = 0;
//
//        if(lastFolloweeAlias != null) {
//            // This is a paged request for something after the first page. Find the first item
//            // we should return
//            for (int i = 0; i < allFollowees.size(); i++) {
//                if(lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
//                    // We found the index of the last item returned last time. Increment to get
//                    // to the first one we should return
//                    followeesIndex = i + 1;
//                    break;
//                }
//            }
//        }
//
//        return followeesIndex;
//    }
//
//    List<User> getDummyFollowees() {
//        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
//                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
//                user19, user20);
//    }

    public void addFollowersBatch(List<String> followers, String followTarget) {

        // Constructor for TableWriteItems takes the name of the table
        TableWriteItems items = new TableWriteItems(TableName);

        // Add each user into the TableWriteItems object
        for (String followerHandle : followers) {
            Item item = new Item()
                    .withPrimaryKey(FollowerHandleAttr, followerHandle, FolloweeHandleAttr, followTarget);
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

    public ArrayList<User> getFollowersByPostRequest(PostRequest request) {
//        List<User> followers = new ArrayList<>();
//
//        Map<String, String> attrNames = new HashMap<>();
//        attrNames.put("#followee", FolloweeHandleAttr);     // Follower?
//
//        Map<String, AttributeValue> attrValues = new HashMap<>();
//        attrValues.put(":followee", new AttributeValue().withS(request.getFolloweeAlias()));
//
//        QueryRequest queryRequest = new QueryRequest()
//                .withIndexName(IndexName)
//                //.withTableName(TableName)
//                .withKeyConditionExpression("#followee = :followee")
//                .withExpressionAttributeNames(attrNames)
//                .withExpressionAttributeValues(attrValues)
//                .withScanIndexForward(true)
//                .withLimit(request.getLimit());
//
//        if (isNonEmptyString(request.getLastFollowerAlias())) {
//            Map<String, AttributeValue> startKey = new HashMap<>();
//            startKey.put(FolloweeHandleAttr, new AttributeValue().withS(request.getFolloweeAlias()));       // userAlias
//            startKey.put(FollowerHandleAttr, new AttributeValue().withS(request.getLastFollowerAlias()));           // timestamp
//
//            queryRequest = queryRequest.withExclusiveStartKey(startKey);
//        }
//
//        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
//        List<Map<String, AttributeValue>> items = queryResult.getItems();
//        if (items != null) {
//            for (Map<String, AttributeValue> item : items) {
//                User user = userDAO.getUser(item.get(FollowerHandleAttr).getS());
//                followers.add(user);
//            }
//        }
//
//        boolean hasMorePages = false;
//        if (queryResult.getLastEvaluatedKey() != null) {
//            hasMorePages = true;
//        }
        ArrayList<User> followers = new ArrayList<>();
        Index tableIndex = dynamoDB.getTable(TableName).getIndex(IndexName);
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("#ee", FolloweeHandleAttr);
        // Value that we are searching for
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put(":ee", request.getStatus().getUser().getAlias());
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#ee = :ee")
                .withNameMap(nameMap)
                .withValueMap(valueMap)
                .withScanIndexForward(false);
//
//        if (isNonEmptyString(request.getLastFollowerAlias())) {
//            querySpec.withExclusiveStartKey(FolloweeHandleAttr, request.getFolloweeAlias(),
//                    FollowerHandleAttr, request.getLastFollowerAlias());
//        }
        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;
        try {
            items = tableIndex.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                User user = userDAO.getUser(item.getString(FollowerHandleAttr));
                followers.add(user);
            }
        } catch (Exception ignored) { }
        return followers;
    }

    List<User> getDummyUsers() {
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
    }

}
