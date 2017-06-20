package in.icebreakerapp.icebreaker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

import in.icebreakerapp.icebreaker.helpers.InternetCheck;
import in.icebreakerapp.icebreaker.helpers.MessageDb;
import in.icebreakerapp.icebreaker.models.ProfileUpdateStatus;
import in.icebreakerapp.icebreaker.models.SignupStatus;
import in.icebreakerapp.icebreaker.util.CircleTransform;

/**
 * Created by anip on 29/09/16.
 */
public class Profile extends AppCompatActivity {
    FloatingActionButton button;
    ImageView imageView;
    private MessageDb db;
    Context context;
    private TextView enroll, status, gender, batch, college;
    private ImageView edit_status, edit_dets;
    private String URL_EDIT_PROFILE = "http://anip.xyz:8080/edit/";
    String updated_status, data = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        button = (FloatingActionButton) findViewById(R.id.edit_image);
        imageView = (ImageView) findViewById(R.id.image);
        enroll = (TextView) findViewById(R.id.enroll);
        status = (TextView) findViewById(R.id.status);
        batch = (TextView) findViewById(R.id.batch);
        gender = (TextView) findViewById(R.id.gender);
        college = (TextView) findViewById(R.id.college);
        edit_status = (ImageView) findViewById(R.id.btn_status);
        edit_dets = (ImageView) findViewById(R.id.btn_basic_info);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("user", 0);
        enroll.setText(sp.getString("enroll", ""));
        college.setText(sp.getString("college", "").replace("\"", ""));
        batch.setText(sp.getString("batch", "B9").replace("\"", ""));
        gender.setText(sp.getString("gender", "Male").replace("\"", "").toUpperCase());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, ImageUpload.class);
                startActivityForResult(intent, 1010);
            }
        });

        edit_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //status.setText(et_updated_status.getText().toString());

                if (InternetCheck.internetCheck(context)) {
                    String serverURL1 = "http://anip.xyz:8080/edit/";
                    new LongOperation2().execute(serverURL1);
                } else {
                    Toast.makeText(getApplicationContext(), "You need a network connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Picasso.with(this).setLoggingEnabled(true);
        Picasso.with(this).setIndicatorsEnabled(true);
        Picasso.with(this)
//                .load(data.getSt)
                .load("http://anip.xyz:8080/image/" + getSharedPreferences("user", 0).getString("enroll", "") + "/")
//                .resize(50, 50)
//                .centerCrop()
                .fit()
//                .memoryPolicy(MemoryPolicy.NO_CACHE)

                .centerCrop()
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .transform(new CircleTransform())
                .placeholder(R.drawable.icebreaker)
                .error(R.drawable.icebreaker)
                .into(imageView);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("hell", "http://anip.xyz:8080/image/" + getSharedPreferences("user", 0).getString("enroll", "") + "/");
        if (requestCode == 1010 && resultCode == RESULT_OK) {
            Picasso.with(this)
                    .load(data.getStringExtra("file"))
//                    .load("http://anip.xyz:8080/image/"+getSharedPreferences("user",0).getString("enroll","")+"/")
//                .resize(50, 50)
//                .centerCrop()
                    .fit()
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)

                    .centerCrop()
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.icebreaker)
                    .error(R.drawable.icebreaker)
                    .into(imageView);
        }
    }

    private class LongOperation2 extends AsyncTask<Object, Object, ProfileUpdateStatus> {

        private String Error = null;
        private ProfileUpdateStatus result;
        private ProgressDialog progressDialog = new ProgressDialog(Profile.this);

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            enroll = (TextView) findViewById(R.id.enroll);
            batch = (TextView) findViewById(R.id.batch);
            gender = (TextView) findViewById(R.id.gender);
            college = (TextView) findViewById(R.id.college);

            JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("status", status + "");
                jsonObject.addProperty("enroll", enroll.getText().toString());
                jsonObject.addProperty("gender", gender.getText().toString());
                jsonObject.addProperty("college", college.getText().toString());
                jsonObject.addProperty("batch", batch.getText().toString());
                jsonObject.addProperty("branch", "CSE");

                Gson gson2 = new Gson();

                String jsonString = gson2.toJson(jsonObject);
                Log.i("hell", jsonString);

                data = jsonString;

        }

        @Override
        protected ProfileUpdateStatus doInBackground(Object... strings) {
            HttpURLConnection httpcon;
            try {

                httpcon = (HttpURLConnection) ((new URL(URL_EDIT_PROFILE).openConnection()));
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

                result = gson2.fromJson(sb.toString(), ProfileUpdateStatus.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(ProfileUpdateStatus profileUpdateStatus) {

            if (profileUpdateStatus.isStatus()) {
                progressDialog.dismiss();
                status = (TextView) findViewById(R.id.tv_status);
                status.setText("done!");
                Toast.makeText(getApplicationContext(),"Update",Toast.LENGTH_LONG).show();
            }

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
