package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;


public class LogoutPresenter {
    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public LogoutPresenter(View view) {
        this.view = view;
    }


    public LogoutResponse logout(LogoutRequest logoutRequest) throws IOException, TweeterRemoteException {
        return getLogoutService().logout(logoutRequest);
    }

    public LogoutService getLogoutService(){
        return new LogoutServiceProxy();
    }
}
