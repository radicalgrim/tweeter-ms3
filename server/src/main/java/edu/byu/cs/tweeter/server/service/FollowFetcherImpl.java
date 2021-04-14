package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.lambda.FollowFetcher;
import edu.byu.cs.tweeter.server.lambda.JobHandler;
import edu.byu.cs.tweeter.server.misc.Job;
import edu.byu.cs.tweeter.server.misc.JsonSerializer;

import com.google.gson.Gson;

public class FollowFetcherImpl {
    private static final String PostQURL = "https://sqs.us-east-2.amazonaws.com/856583722669/PostQ";
    private static final String JobQURL = "https://sqs.us-east-2.amazonaws.com/856583722669/JobQ";
    private static final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    //retruves all followers of the user alias that wrote the incoming post
    //contacts the follow dao
    //send results to the job queue

    ArrayList<User> getFollowers(PostRequest request){
        FollowDAO fDao = new FollowDAO();
        ArrayList<User> followers = fDao.getFollowersByPostRequest(request);
        return followers;
    }

    public void handleRequest(SQSEvent input, Context context){
        //get the post request from the first queue and create jobs for every x number of followers you
        //return from followfetcher. serialize these jobs and send them to the jobQ
        //List<Message> messagesFromPostQ = sqs.receiveMessage(PostQURL).getMessages();
        ArrayList<User> followers = new ArrayList<>();
        for(SQSEvent.SQSMessage msg: input.getRecords()){
            PostRequest p_request = new Gson().fromJson(msg.getBody(), PostRequest.class);//(PostRequest) JsonSerializer.deserialize(messagesFromPostQ.get(0), PostRequest.class);
            Status status = p_request.getStatus();
//
            followers = getFollowers(p_request);
            Job job = new Job(status);
            job.setFollowers(followers);
            buildJob(job, followers);
        }
    }
    public void buildJob(Job job, ArrayList<User> followers){
        int numFollowersAdded = 0;
        while(numFollowersAdded < followers.size()){
            ArrayList<User> followerBatch = new ArrayList<>();
            for(int i = numFollowersAdded; i < 25; i++){
                if(numFollowersAdded == followers.size()){
                    break;
                }
                followerBatch.add(followers.get(i));
                numFollowersAdded += 1;
            }
            job.getFollowers().clear();
            job.setFollowers(followerBatch);
            sendToQueue(job);
        }
    }

    public void sendToQueue(Job job){
        //String messageBody = request.getBody();
        String messageBody = JsonSerializer.serialize(job);
        String jobQueueUrl = JobQURL;

        SendMessageBatchRequest send_batch_request = new SendMessageBatchRequest()
                .withQueueUrl(jobQueueUrl)
                .withEntries(
                        new SendMessageBatchRequestEntry(
                                "msg_1", messageBody));
        sqs.sendMessageBatch(send_batch_request);
    }
}
