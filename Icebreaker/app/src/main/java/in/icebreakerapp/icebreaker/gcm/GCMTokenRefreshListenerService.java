package in.icebreakerapp.icebreaker.gcm;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by anip on 16/08/16.
 */
public class GCMTokenRefreshListenerService extends FirebaseInstanceIdService {

    //If the token is changed registering the device again
    @Override
    public void onTokenRefresh() {
//        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
//        startService(intent);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("", "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
//        sendRegistrationToServer(refreshedToken);
    }
}
