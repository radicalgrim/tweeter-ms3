package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
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
    private final User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);
    private final User user3 = new User("Bob", "Bobson", MALE_IMAGE_URL);
    private final User user4 = new User("Bonnie", "Beatty", FEMALE_IMAGE_URL);
    private final User user5 = new User("Chris", "Colston", MALE_IMAGE_URL);
    private final User user6 = new User("Cindy", "Coats", FEMALE_IMAGE_URL);
    private final User user7 = new User("Dan", "Donaldson", MALE_IMAGE_URL);
    private final User user8 = new User("Dee", "Dempsey", FEMALE_IMAGE_URL);
    private final User user9 = new User("Elliott", "Enderson", MALE_IMAGE_URL);
    private final User user10 = new User("Elizabeth", "Engle", FEMALE_IMAGE_URL);
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
    public Integer getFollowerCount(User followee) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert followee != null;
        return getDummyFollowers().size();
    }

    public FollowerResponse getFollowers(FollowerRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getFolloweeAlias() != null;

        List<User> allFollowers = getDummyFollowers();
        List<User> responseFollowers = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowers != null) {
                int followersIndex = getFollowersStartingIndex(request.getLastFollowerAlias(), allFollowers);

                for(int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < request.getLimit(); followersIndex++, limitCounter++) {
                    responseFollowers.add(allFollowers.get(followersIndex));
                }

                hasMorePages = followersIndex < allFollowers.size();
            }
        }

        return new FollowerResponse(responseFollowers, hasMorePages);
    }

    private int getFollowersStartingIndex(String lastFollowerAlias, List<User> allFollowers) {

        int followersIndex = 0;

        if(lastFollowerAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowers.size(); i++) {
                if(lastFollowerAlias.equals(allFollowers.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followersIndex = i + 1;
                    break;
                }
            }
        }

        return followersIndex;
    }

    List<User> getDummyFollowers() {
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
    }

    //FOLLOWING DAO ORIGINAL CODE///////////////////////////////////////////////////////////////////////////
    public Integer getFolloweeCount(User follower) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert follower != null;
        return getDummyFollowees().size();
    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getFollowerAlias() != null;

        List<User> allFollowees = getDummyFollowees();
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowees != null) {
                int followeesIndex = getFolloweesStartingIndex(request.getLastFolloweeAlias(), allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }

        return new FollowingResponse(responseFollowees, hasMorePages);
    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFolloweeAlias the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFolloweeAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<User> getDummyFollowees() {
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
    }
}
