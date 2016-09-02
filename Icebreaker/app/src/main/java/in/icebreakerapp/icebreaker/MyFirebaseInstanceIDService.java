package in.icebreakerapp.icebreaker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
    private String dev_id;
    private int code =0 ;

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        dev_id = telephonyManager.getDeviceId();
        SharedPreferences sharedPreferences = this.getSharedPreferences("deviceConfig",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dev_id",dev_id);
        editor.putString("token",refreshedToken);
        editor.commit();
        String serverURL1 = "http://anip.xyz:8080/gcm/v1/device/register/";
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
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("dev_id",dev_id);
            jsonObject.addProperty("reg_id", refreshedToken);


            Gson gson2 = new Gson();

            String jsonString = gson2.toJson(jsonObject);
            Log.i("hell", jsonString);

            data +=jsonString;

        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            HttpURLConnection httpcon;

            try {

                httpcon = (HttpURLConnection) ((new URL("http://anip.xyz:8080/gcm/v1/device/register/").openConnection()));
                httpcon.setDoOutput(true);
                httpcon.setRequestProperty("Content-Type", "application/json");
                httpcon.setRequestProperty("Accept", "application/json");
                httpcon.setRequestMethod("POST");
                httpcon.connect();

                OutputStream os = httpcon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.close();
                os.close();
                Log.i("hel", String.valueOf(httpcon.getErrorStream()) + httpcon.getResponseMessage() + httpcon.getResponseCode());


                BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));
                code = httpcon.getResponseCode();
                String line;
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                Gson gson2 = new Gson();
                Log.i("he;;", sb.toString());

                result = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Append Server Response To Content String


            /*****************************************************/
            return null;
        }

        protected void onPostExecute(Void unused) {

            if (Error != null) {
                // uiUpdate.setText("Output : " + Error);
                {
                    Log.i("hell", Error.toString());
                    Toast toast = Toast.makeText(MyFirebaseInstanceIDService.this,
                            "No internet connection" + Error.toString(), Toast.LENGTH_LONG);
                    toast.show();
                }

            } else {
                if (code == 200) {

                    Toast toast = Toast.makeText(MyFirebaseInstanceIDService.this, "Success", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        }
    }}




