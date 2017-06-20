package in.icebreakerapp.icebreaker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import in.icebreakerapp.icebreaker.fragments.ContactFragment;
import in.icebreakerapp.icebreaker.helpers.InternetCheck;
import in.icebreakerapp.icebreaker.helpers.MessageDb;
import in.icebreakerapp.icebreaker.models.Contact;
import in.icebreakerapp.icebreaker.models.ContactModel;
import in.icebreakerapp.icebreaker.models.RandomChat;
import in.icebreakerapp.icebreaker.models.SendMessage;
import in.icebreakerapp.icebreaker.util.CircleTransform;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by anip on 04/09/16.
 */
public class AddContact extends AppCompatActivity {
    private EditText search;
    private MessageDb messageDb;
    private Button add;
    private Context currContext;
    private ImageView picture;
    private ConstraintLayout normalView;
    private ConstraintLayout foundView;
    private ConstraintLayout notFoundView;
    private Button invite;
    private Button save;
    private TextView not_found_text;
    private TextView invite_text;
    private TextView nick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Contact");
        setContentView(R.layout.add_contact);
        search = (EditText)findViewById(R.id.eno);
        add = (Button)findViewById(R.id.add);
        picture = (ImageView) findViewById(R.id.profile);
        currContext=getApplicationContext();
        normalView = (ConstraintLayout) findViewById(R.id.normalView);
        foundView = (ConstraintLayout) findViewById(R.id.foundView);
        notFoundView = (ConstraintLayout) findViewById(R.id.notFoundView);
        invite = (Button) findViewById(R.id.invite);
        save = (Button) findViewById(R.id.save);
        normalView.setVisibility(View.VISIBLE);
        foundView.setVisibility(View.GONE);
        notFoundView.setVisibility(View.GONE);
        not_found_text = (TextView) findViewById(R.id.not_found_text);
        invite_text = (TextView) findViewById(R.id.invite_text);
        nick = (TextView) findViewById(R.id.nick);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(InternetCheck.internetCheck(currContext))
                {
                    String serverURL1 = "http://anip.xyz/icebreakerlogin.php";
                    new LongOperation2().execute(serverURL1);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"You need a network connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private class LongOperation2 extends AsyncTask<String, Void, ContactModel> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        // private String Content;
        private String Error = null;
        private ContactModel result;
        String studentId;
        private ProgressDialog Dialog = new ProgressDialog(AddContact.this);
        String data = "";

        int sizeData = 0;

        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            // Start Progress Dialog (Message)
            // String studentid="";

            // Intent intent = getIntent();

            // if (intent != null) {

            // emailId = intent.getStringExtra("emailId");

            // }*/

            Dialog.setMessage("Please wait..");
            Dialog.show();
            // try{
            // Set Request parameter
            JsonObject jsonObject = new JsonObject();
            SharedPreferences sp = getApplicationContext().getSharedPreferences("user", 0);
            jsonObject.addProperty("sender",sp.getString("enroll",""));
            jsonObject.addProperty("search", search.getText().toString());


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
        protected ContactModel doInBackground(String... urls) {


            HttpURLConnection httpcon;

            try {

                httpcon = (HttpURLConnection) ((new URL("http://anip.xyz:8080/add/").openConnection()));
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

                result = gson2.fromJson(sb.toString(), ContactModel.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Append Server Response To Content String


            /*****************************************************/
            return result;
        }

        protected void onPostExecute(final ContactModel response) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            Log.i("response", String.valueOf(response) + Error);
            if (response.getStatus().equalsIgnoreCase("found")) {
                normalView.setVisibility(View.GONE);
                foundView.setVisibility(View.VISIBLE);
                Picasso.with(AddContact.this)
                        .load("http://anip.xyz:8080/image/"+search.getText().toString()+"/")
                        .resize(500, 500)
                        .centerCrop()
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.icebreaker)
                        .error(R.drawable.icebreaker)
                        .into(picture);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        messageDb =new MessageDb(AddContact.this);
                        Contact contact;
                        Gson gson = new Gson();
                        contact = gson.fromJson(response.getProfile(),Contact.class);
                        if(nick.getText().toString()!="" && nick.getText().toString()!= "optional") {
                            contact.setNick_name(nick.getText().toString());
                        }
                        else
                        {
                            contact.setNick_name(null);
                        }
                        messageDb.addContact(contact);
                        messageDb.close();
                        Intent intent = getIntent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });

            }
            else if (response.getStatus().equalsIgnoreCase("already")){
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            else {
                normalView.setVisibility(View.GONE);
                foundView.setVisibility(View.GONE);
                notFoundView.setVisibility(View.VISIBLE);
                not_found_text.setText(search.getText().toString()+" "+ "this student has not yet registered!!");
                invite_text.setText("You can invite "+search.getText().toString()+" by clicking the button below.");
                invite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String message = getString(R.string.invite);
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                        shareIntent.setType("text/plain");
                        startActivity(shareIntent);
                    }
                });
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
