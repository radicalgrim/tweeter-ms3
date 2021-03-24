package edu.byu.cs.tweeter.model.service.response;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;

/**
 * A paged response for a {@link FollowingRequest}.
 */
public class FollowingResponse extends FollowsResponse {

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FollowingResponse(String message) {
        super(false, message, false);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param users the followees to be included in the result.
     * @param hasMorePages an indicator of whether more data is available for the request.
     */
    public FollowingResponse(List<User> users, boolean hasMorePages) {
        super(true, hasMorePages);
        this.users = users;
    }

    public FollowingResponse(boolean success) {
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

        FollowingResponse that = (FollowingResponse) param;

        return (Objects.equals(users, that.users) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(users);
    }
}
