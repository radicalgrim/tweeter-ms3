package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;

public class FeedDAOTest {

    private final User user1 = new User("Daffy", "Duck", "");

    private final Status status1 = new Status("Hello World 1", "@user1", "https://google.com", "Feb. 2, 2021 1:00", user1);
    private final Status status2 = new Status("Hello World 2", "@user2", "https://google.com", "Feb. 2, 2021 2:00", user1);
    private final Status status3 = new Status("Hello World 3", "@user3", "https://google.com", "Feb. 2, 2021 3:00", user1);
    private final Status status4 = new Status("Hello World 4", "@user4", "https://google.com", "Feb. 2, 2021 4:00", user1);
    private final Status status5 = new Status("Hello World 5", "@user5", "https://google.com", "Feb. 2, 2021 5:00", user1);
    private final Status status6 = new Status("Hello World 6", "@user6", "https://google.com", "Feb. 2, 2021 6:00", user1);
    private final Status status7 = new Status("Hello World 7", "@user7", "https://google.com", "Feb. 2, 2021 7:00", user1);
    private final Status status8 = new Status("Hello World 8", "@user8", "https://google.com", "Feb. 2, 2021 8:00", user1);

    private FeedDAO feedDAOSpy;

    @BeforeEach
    void setup() { feedDAOSpy = Mockito.spy(new FeedDAO()); }

    @Test
    void testGetFeed_noStatusesForUser() {
        List<Status> statuses = Collections.emptyList();
        Mockito.when(feedDAOSpy.getDummyFeed()).thenReturn(statuses);

        FeedRequest request = new FeedRequest(status1.getUser().getAlias(), 10, null);
        FeedResponse response = feedDAOSpy.getFeed(request);

        Assertions.assertEquals(0, response.getStatuses().size());
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFeed_oneStatusForUser_limitGreaterThanStatuses() {
        List<Status> statuses = Collections.singletonList(status2);
        Mockito.when(feedDAOSpy.getDummyFeed()).thenReturn(statuses);

        FeedRequest request = new FeedRequest(status1.getUser().getAlias(), 10, null);
        FeedResponse response = feedDAOSpy.getFeed(request);

        Assertions.assertEquals(1, response.getStatuses().size());
        Assertions.assertTrue(response.getStatuses().contains(status2));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetStatuses_twoStatusesForUser_limitEqualsStatuses() {
        List<Status> statuses = Arrays.asList(status2, status3);
        Mockito.when(feedDAOSpy.getDummyFeed()).thenReturn(statuses);

        FeedRequest request = new FeedRequest(status3.getUser().getAlias(), 2, null);
        FeedResponse response = feedDAOSpy.getFeed(request);

        Assertions.assertEquals(2, response.getStatuses().size());
        Assertions.assertTrue(response.getStatuses().contains(status2));
        Assertions.assertTrue(response.getStatuses().contains(status3));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFeed_limitLessThanStatuses_endsOnPageBoundary() {
        List<Status> statuses = Arrays.asList(status2, status3, status4, status5, status6, status7);
        Mockito.when(feedDAOSpy.getDummyFeed()).thenReturn(statuses);

        FeedRequest request = new FeedRequest(status5.getUser().getAlias(), 2, null);
        FeedResponse response = feedDAOSpy.getFeed(request);

        // Verify first page
        Assertions.assertEquals(2, response.getStatuses().size());
        Assertions.assertTrue(response.getStatuses().contains(status2));
        Assertions.assertTrue(response.getStatuses().contains(status3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FeedRequest(status5.getUser().getAlias(), 2, response.getStatuses().get(1).getTimestamp());
        response = feedDAOSpy.getFeed(request);

        Assertions.assertEquals(2, response.getStatuses().size());
        Assertions.assertTrue(response.getStatuses().contains(status4));
        Assertions.assertTrue(response.getStatuses().contains(status5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new FeedRequest(status5.getUser().getAlias(), 2, response.getStatuses().get(1).getTimestamp());
        response = feedDAOSpy.getFeed(request);

        Assertions.assertEquals(2, response.getStatuses().size());
        Assertions.assertTrue(response.getStatuses().contains(status6));
        Assertions.assertTrue(response.getStatuses().contains(status7));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFeed_limitLessThanStatuses_notEndsOnPageBoundary() {
        List<Status> statuses = Arrays.asList(status2, status3, status4, status5, status6, status7, status8);
        Mockito.when(feedDAOSpy.getDummyFeed()).thenReturn(statuses);

        FeedRequest request = new FeedRequest(status6.getUser().getAlias(), 2, null);
        FeedResponse response = feedDAOSpy.getFeed(request);

        // Verify first page
        Assertions.assertEquals(2, response.getStatuses().size());
        Assertions.assertTrue(response.getStatuses().contains(status2));
        Assertions.assertTrue(response.getStatuses().contains(status3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FeedRequest(status6.getUser().getAlias(), 2, response.getStatuses().get(1).getTimestamp());
        response = feedDAOSpy.getFeed(request);

        Assertions.assertEquals(2, response.getStatuses().size());
        Assertions.assertTrue(response.getStatuses().contains(status4));
        Assertions.assertTrue(response.getStatuses().contains(status5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new FeedRequest(status6.getUser().getAlias(), 2, response.getStatuses().get(1).getTimestamp());
        response = feedDAOSpy.getFeed(request);

        Assertions.assertEquals(2, response.getStatuses().size());
        Assertions.assertTrue(response.getStatuses().contains(status6));
        Assertions.assertTrue(response.getStatuses().contains(status7));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify fourth page
        request = new FeedRequest(status6.getUser().getAlias(), 2, response.getStatuses().get(1).getTimestamp());
        response = feedDAOSpy.getFeed(request);

        Assertions.assertEquals(1, response.getStatuses().size());
        Assertions.assertTrue(response.getStatuses().contains(status8));
        Assertions.assertFalse(response.getHasMorePages());
    }
}
