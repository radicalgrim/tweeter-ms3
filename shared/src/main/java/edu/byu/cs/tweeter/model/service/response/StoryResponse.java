package edu.byu.cs.tweeter.model.service.response;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.Status;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.model.service.request.StoryRequest}.
 */
public class StoryResponse extends StatusResponse {

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public StoryResponse(String message) {
        super(false, message, false);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param story the statuses to be included in the result.
     * @param hasMorePages an indicator of whether more data is available for the request.
     */
    public StoryResponse(List<Status> statuses, boolean hasMorePages) {
        super(true, hasMorePages);
        this.statuses = statuses;
    }

    public StoryResponse(boolean success) {
        super(success, false);
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        StoryResponse that = (StoryResponse) param;

        return (Objects.equals(statuses, that.statuses) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(statuses);
    }

}
