package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.server.misc.Job;
import edu.byu.cs.tweeter.server.misc.JsonSerializer;

public class JobHandlerImpl {
    private static final String PostQURL = "https://sqs.us-east-2.amazonaws.com/856583722669/PostQ";
    private static final String JobQURL = "https://sqs.us-east-2.amazonaws.com/856583722669/JobQ";
    private static final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    private static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-east-2")
            .build();
    private static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    public void handleJobs(SQSEvent input, Context context){
        //List<Message> messagesFromJobQ = sqs.receiveMessage(JobQURL).getMessages();
        ArrayList<User> followers = new ArrayList<>();
        for(SQSEvent.SQSMessage msg: input.getRecords()){
            Job job = new Gson().fromJson(msg.getBody(), Job.class);//(PostRequest) JsonSerializer.deserialize(messagesFromPostQ.get(0), PostRequest.class);
            Status status = job.getStatus();
            followers = job.getFollowers();
            addUserBatch(followers, status);
        }
    }

    public void addUserBatch(List<User> users, Status status) {

        // Constructor for TableWriteItems takes the name of the table, which I have stored in TABLE_USER
        TableWriteItems items = new TableWriteItems("Feed");

        // Add each user into the TableWriteItems object
        for (User user : users) {
            Item item = new Item()
                    .withPrimaryKey("associated_user", user.getAlias())
                    .withNumber("time_stamp", System.currentTimeMillis())
                    .withString("followed_user", status.getUser().getAlias())
                    .withString("link", status.getLink())
                    .withString("mention", status.getMention())
                    .withString("message", status.getMessage());
            items.addItemToPut(item);

            // 25 is the maximum number of items allowed in a single batch write.
            // Attempting to write more than 25 items will result in an exception being thrown
            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems("Feed");
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
        System.out.println("Wrote Batch");

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
            System.out.println("Wrote more");
        }
    }
}
