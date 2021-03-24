package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class RegisterServiceImpl implements RegisterService {
    @Override
    public RegisterResponse register(RegisterRequest request) {

        // TODO: Generates dummy data. Replace with a real implementation.
//        User user = new User("Test", "User",
//                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        UserDAO uDao = getUserDAO();
        return uDao.getRegisterResponse(request);
    }

    UserDAO getUserDAO() { return new UserDAO(); }

}
