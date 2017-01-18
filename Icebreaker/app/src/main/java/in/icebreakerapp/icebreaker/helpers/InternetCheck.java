package in.icebreakerapp.icebreaker.helpers;

import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by DravitLochan on 18-01-2017.
 */

public class InternetCheck {

    public static boolean internetCheck ()
    {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection)
                    (new URL("http://clients3.google.com/generate_204")
                            .openConnection());
            urlConnection.setRequestProperty("User-Agent", "Android");
            urlConnection.setRequestProperty("Connection", "close");
            urlConnection.setConnectTimeout(1500);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 204 &&
                    urlConnection.getContentLength() == 0) {
                Log.d("Network Checker", "Successfully connected to internet");
                return true;
            }
        } catch (IOException e) {
            Log.e("Network Checker", "Error checking internet connection", e);
        }
        return false;
    }
}
