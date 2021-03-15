package edu.byu.cs.tweeter.client.view.asyncTasks.follows;

import edu.byu.cs.tweeter.model.service.response.FollowsResponse;

public interface GetFollowsTask {

    interface Observer {
        void usersRetrieved(FollowsResponse response);
        void handleException(Exception exception);
    }
}
