package in.icebreakerapp.icebreaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import in.icebreakerapp.icebreaker.models.SignupStatus;
import in.icebreakerapp.icebreaker.models.WebkioskStatus;
import in.icebreakerapp.icebreaker.util.CircleTransform;

/**
 * Created by anip on 12/08/16.
 */
public class Signup extends AppCompatActivity {
    private static final int REQUEST_CODE_READ_PHONE_STATE = 13;
    private Button signup;
    private EditText _branch;
    private EditText _batch;
    private EditText pass_e;
    private String branch,batch;
    private int code;
    private String dev_id;
    private String reg_id;
    private ImageView profile_image;
    private FloatingActionButton button;
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        signup = (Button) findViewById(R.id.btn_signup);
        imageView = (ImageView) findViewById(R.id.image);
        _branch = (EditText) findViewById(R.id.branch);
        _batch = (EditText) findViewById(R.id.batch);
        button = (FloatingActionButton) findViewById(R.id.edit_image);
//        dob_e = (EditText) findViewById(R.id.dob);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this,ImageUpload.class);
                startActivityForResult(intent,1010);
            }
        });
        profile_image = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load("http://anip.xyz:8080/image/13103622/").transform(new CircleTransform()).into(profile_image);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch = _branch.getText().toString();
                batch = _batch.getText().toString();
//                password = pass_e.getText().toString();
//                if (ContextCompat.checkSelfPermission(Signup.this, android.Manifest.permission.READ_PHONE_STATE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    // Should we show an explanation?
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(Signup.this,
//                            android.Manifest.permission.READ_PHONE_STATE)) {
//                        // Show an expanation to the user *asynchronously* -- don't block
//                        // this thread waiting for the user's response! After the user
//                        // sees the explanation, try again to request the permission.
//                    } else {
//                        // No explanation needed, we can request the permission.
//                        ActivityCompat.requestPermissions(Signup.this,
//                                new String[]{Manifest.permission.READ_PHONE_STATE},
//                                REQUEST_CODE_READ_PHONE_STATE);
//                    }
//                } else {
//                    // permission was granted, yay! Do the task you need to do.
//
////                    int permissionCheck = ContextCompat.checkSelfPermission(Signup.this,
////                            android.Manifest.permission.READ_PHONE_STATE);
////                    ActivityCompat.requestPermissions(Signup.this, new String[]{android.Manifest.permission.READ_PHONE_STATE},
////                            REQUEST_CODE_READ_PHONE_STATE);
////                    while (true) {
////                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
////                            break;
////                        }
////                    }
//
                String serverURL1 = "http://anip.xyz/icebreakerlogin.php";
                new LongOperation2().execute(serverURL1);


//                eno = eno_e.getText().toString();
//                dob = dob_e.getText().toString();
//                password = pass_e.getText().toString();
//                int permissionCheck = ContextCompat.checkSelfPermission(Signup.this,
//                        android.Manifest.permission.READ_PHONE_STATE);
//                ActivityCompat.requestPermissions(Signup.this, new String[]{android.Manifest.permission.READ_PHONE_STATE},
//                        REQUEST_CODE_READ_PHONE_STATE);
//                while (true) {
//                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//                        break;
//                    }
            }


//                String serverURL1 = "http://anip.xyz/icebreakerlogin.php";
//                new LongOperation2().execute(serverURL1);

//            }

        });
}

private class LongOperation2 extends AsyncTask<String, Void, SignupStatus> {

    // Required initialization

    // private final HttpClient Client = new DefaultHttpClient();
    // private String Content;
    private String Error = null;
    private SignupStatus result;
    String studentId;
    private ProgressDialog Dialog = new ProgressDialog(Signup.this);
    String data = "";

    int sizeData = 0;

    protected void onPreExecute() {
        // NOTE: You can call UI Element here.

        // Start Progress Dialog (Message)
        // String studentid="";

        // Intent intent = getIntent();

        // if (intent != null) {

        // emailId = intent.getStringExtra("emailId");

        // }

        Dialog.setMessage("Please wait..");
        Dialog.show();
        // try{
        // Set Request parameter
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("enroll", getSharedPreferences("user",0).getString("enroll",""));
        jsonObject.addProperty("batch", batch);
        jsonObject.addProperty("branch",branch);
        jsonObject.addProperty("college","JIIT");
        jsonObject.addProperty("gender", "female");


        Gson gson2 = new Gson();

        String jsonString = gson2.toJson(jsonObject);
        Log.i("hell", jsonString);
        // data +=
        // "{\"order\":{\"instructions\":\"\",\"paymentMethod\":\"COD\",\"items\":[{\"itemId\":962,\"name\":\"Cottage Cheese & Grilled Veggies Salad\",\"smallImageUrl\":\"/static/21/962_Cottage_Cheese_Salad200x200.jpg\",\"price\":249,\"itemType\":\"Veggies\",\"instructions\":\"instructions Abc\",\"quantity\":1},{\"itemId\":867,\"name\":\"Greek Salad\",\"smallImageUrl\":\"/static/21/867_Greek_Salad200x200.jpg\",\"price\":219,\"itemType\":\"Veggies\",\"instructions\":\"ABC\",\"quantity\":1}],\"deliveryCharges\":30,\"discountAmount\":117,\"discountPercentage\":25,\"finalOrderAmount\":397,\"discountList\":[{\"id\":4,\"name\":\"Corprate Discount\",\"category\":\"Discount\",\"type\":\"PERCENTAGE\",\"value\":25}],\"deliveryDateTime\":\"12-7-2015 19:45\"},\"customer\":{\"name\":\"hhhhh null\",\"phone\":9540095277,\"email\":\"rahul@cookedspecially.com\",\"address\":\"nvdiv eiv iwr\",\"deliveryArea\":\"DLF Phase 3\",\"city\":\"Gurgaon\",\"id\":9970}}";
        // data += "{" + "\"phoneNumber\"" + ":\"" +
        // mobileno.getText().toString()
        // + "\"}";
        data = jsonString;
        // data +=
        // "%7B%22order%22%3A%7B%22instructions%22%3A%22%22%2C%22paymentMethod%22%3A%22COD%22%2C%22items%22%3A%5B%7B%22itemId%22%3A962%2C%22name%22%3A%22Cottage+Cheese+%26+Grilled+Veggies+Salad%22%2C%22smallImageUrl%22%3A%22%2Fstatic%2F21%2F962_Cottage_Cheese_Salad200x200.jpg%22%2C%22price%22%3A249%2C%22itemType%22%3A%22Veggies%22%2C%22instructions%22%3A%22instructions+Abc%22%2C%22quantity%22%3A1%7D%2C%7B%22itemId%22%3A867%2C%22name%22%3A%22Greek+Salad%22%2C%22smallImageUrl%22%3A%22%2Fstatic%2F21%2F867_Greek_Salad200x200.jpg%22%2C%22price%22%3A219%2C%22itemType%22%3A%22Veggies%22%2C%22instructions%22%3A%22ABC%22%2C%22quantity%22%3A1%7D%5D%2C%22deliveryCharges%22%3A30%2C%22discountAmount%22%3A117%2C%22discountPercentage%22%3A25%2C%22finalOrderAmount%22%3A397%2C%22discountList%22%3A%5B%7B%22id%22%3A4%2C%22name%22%3A%22Corprate+Discount%22%2C%22category%22%3A%22Discount%22%2C%22type%22%3A%22PERCENTAGE%22%2C%22value%22%3A25%7D%5D%2C%22deliveryDateTime%22%3A%2212-7-2015+19%3A45%22%7D%2C%22customer%22%3A%7B%22name%22%3A%22hhhhh+null%22%2C%22phone%22%3A9540095277%2C%22email%22%3A%22rahul%40cookedspecially.com%22%2C%22address%22%3A%22nvdiv+eiv+iwr%22%2C%22deliveryArea%22%3A%22DLF+Phase+3%22%2C%22city%22%3A%22Gurgaon%22%2C%22id%22%3A9970%7D%7D";
    }

    // Call after onPreExecute method
    protected SignupStatus doInBackground(String... urls) {


        HttpURLConnection httpcon;

        try {

            httpcon = (HttpURLConnection) ((new URL("http://anip.xyz:8080/signup/").openConnection()));
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

            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            Gson gson2 = new Gson();
            Log.i("he;;", sb.toString());

            result = gson2.fromJson(sb.toString(), SignupStatus.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Append Server Response To Content String


        return result;
    }

    protected void onPostExecute(SignupStatus response) {
        // NOTE: You can call UI Element here.

        // Close progress dialog
        Dialog.dismiss();

        Log.i("response", String.valueOf(response) + Error);
        if (response.getStatus().equalsIgnoreCase("Created") ) {
            SharedPreferences sp = getApplicationContext().getSharedPreferences("user", 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("signup", "true");
            editor.putString("batch", String.valueOf(response.getProfile().get("batch")));
            editor.putString("branch", String.valueOf(response.getProfile().get("branch")));
            editor.putString("college", String.valueOf(response.getProfile().get("college")));
            editor.putString("id", String.valueOf(response.getProfile().get("id")));

            editor.putString("gender", String.valueOf(response.getProfile().get("gender")));
            editor.commit();

            Intent intent = new Intent(Signup.this,Home.class);
            startActivity(intent);

        }


        // Show Response Json On Screen (activity)
        // uiUpdate.setText(Content);

        /****************** Start Parse Response JSON Data *************/

        // String OutputData = ""
//                               	SharedPreferences sp = getApplicationContext()


    }

}
       /* private class LongOperation1 extends AsyncTask<String, Void, Void> {

            // Required initialization

            // private final HttpClient Client = new DefaultHttpClient();
            private String result;
            private String Error = null;

            String data = "";

            int sizeData = 0;

            protected void onPreExecute() {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("dev_id",dev_id);
                jsonObject.addProperty("reg_id", reg_id);
                jsonObject.addProperty("name",eno);


                Gson gson2 = new Gson();

                String jsonString = gson2.toJson(jsonObject);
                Log.i("hell", jsonString);

                data +=jsonString;

            }

            // Call after onPreExecute method
            protected Void doInBackground(String... urls) {


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



                return null;
            }

            protected void onPostExecute(Void unused) {

                if (Error != null) {
                    // uiUpdate.setText("Output : " + Error);
                    {
                        Log.i("hell", Error.toString());
                        Toast toast = Toast.makeText(Signup.this,
                                "No internet connection" + Error.toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }

                } else {
                    if (code == 200) {
                        Intent intent = new Intent(Signup.this,Home.class);
                        startActivity(intent);

                        Toast toast = Toast.makeText(Signup.this, "Success", Toast.LENGTH_LONG);
                        toast.show();
                    }

                }
            }
        }
*/


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the task you need to do.
                    String serverURL1 = "http://anip.xyz/icebreakerlogin.php";
                    new LongOperation2().execute(serverURL1);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("hell","http://anip.xyz:8080/image/"+getSharedPreferences("user",0).getString("enroll","")+"/");
        if(requestCode == 1010 && resultCode==RESULT_OK){
            Picasso.with(this)
                    .load("http://anip.xyz:8080/image/"+getSharedPreferences("user",0).getString("enroll","")+"/")
//                .resize(50, 50)
//                .centerCrop()
                    .fit()
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.icebreaker)
                    .error(R.drawable.icebreaker)
                    .into(imageView);
        }
    }
    }

