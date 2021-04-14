package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.server.misc.JsonSerializer;
import edu.byu.cs.tweeter.server.service.PostServiceImpl;
import edu.byu.cs.tweeter.server.service.StoryServiceImpl;


public class PostHandler implements RequestHandler<PostRequest, PostResponse> {
    @Override
    public PostResponse handleRequest(PostRequest postRequest, Context context) {
        //write to users story
        //sendToQueue(postRequest);
//        FollowFetcher fetcher = new FollowFetcher();
//        fetcher.retrieveFromQueue();

        PostServiceImpl postService = new PostServiceImpl();
        return postService.post(postRequest);
    }

}
