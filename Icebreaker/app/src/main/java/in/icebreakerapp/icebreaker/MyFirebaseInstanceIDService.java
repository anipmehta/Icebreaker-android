package in.icebreakerapp.icebreaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anip on 18/08/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private String refreshedToken;

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
        String serverURL1 = "10.0.2.2:8000/gcm/v1/device/register/";
        new LongOperation1().execute(serverURL1);
    }
    private class LongOperation1 extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String result;
        private String Error = null;

        String data = "";

        int sizeData = 0;

        protected void onPreExecute() {

            data += "dev_id" + "="+ "13103630"+ "&"
                    + "reg_id=" +refreshedToken ;

        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            BufferedReader reader = null;

            // Send data
            try {

                // Defined URL where to send data
                // URL url = new URL(urls[0]);

                // Send POST data request
                HttpURLConnection conn = (HttpURLConnection) ((new URL(urls[0])
                        .openConnection()));
                conn.setRequestMethod("POST");
                conn.connect();

                OutputStreamWriter wr = new OutputStreamWriter(
                        conn.getOutputStream());
                wr.write(data);
                wr.flush();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.close();
                os.close();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "\n");
                }

                // Append Server Response To Content String
                result = sb.toString();
            } catch (Exception ex) {
                Error = ex.getMessage();
                ex.printStackTrace();
            } finally {
                try {

                    reader.close();
                }

                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            /*****************************************************/
            return null;
        }

        protected void onPostExecute(Void unused) {

            if (Error != null) {
                // uiUpdate.setText("Output : " + Error);
                {
                    Log.i("hell",Error.toString());
                    Toast toast = Toast.makeText(MyFirebaseInstanceIDService.this,
                            "No internet connection"+Error.toString(), Toast.LENGTH_LONG);
                    toast.show();
                }

            }
                 else {
                    Toast toast = Toast.makeText(MyFirebaseInstanceIDService.this, "Invalid Mobile Number", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        }



}
