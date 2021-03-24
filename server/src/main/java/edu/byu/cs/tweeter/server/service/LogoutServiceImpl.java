package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LogoutServiceImpl implements LogoutService {
    @Override
    public LogoutResponse logout(LogoutRequest request) {

        // TODO: Generates dummy data. Replace with a real implementation.
//        User user = new User("Test", "User",
//                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        UserDAO uDao = getUserDAO();
        return uDao.getLogoutResponse(request);
    }

    UserDAO getUserDAO() { return new UserDAO(); }

}
