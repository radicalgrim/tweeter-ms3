package edu.byu.cs.tweeter.client.view.asyncTasks.status;

import edu.byu.cs.tweeter.model.service.response.StatusResponse;

public interface GetStatusTask {

        interface Observer {
                void statusesRetrieved(StatusResponse response);
                void handleException(Exception exception);
        }
}
