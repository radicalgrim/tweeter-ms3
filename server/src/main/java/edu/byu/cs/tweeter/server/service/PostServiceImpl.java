package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.misc.JsonSerializer;

public class PostServiceImpl implements PostService {
    @Override
    public PostResponse post(PostRequest request) {
        sendToQueue(request);
        return getStoryDAO().post(request);
    }
    StoryDAO getStoryDAO() { return new StoryDAO(); }

    public void sendToQueue(PostRequest request){
//        String entityBody = JsonSerializer.serialize(requestInfo);
//        JsonSerializer.deserialize(responseString, returnType);

        String messageBody = JsonSerializer.serialize(request);
        String postQueueUrl = "https://sqs.us-east-2.amazonaws.com/856583722669/PostQ";

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(postQueueUrl)
                .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

        String msgId = send_msg_result.getMessageId();
        System.out.println("Message ID: " + msgId);
    }

}
