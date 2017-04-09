package in.icebreakerapp.icebreaker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.icebreakerapp.icebreaker.adapters.ChatAdapter;
import in.icebreakerapp.icebreaker.helpers.MessageDb;
import in.icebreakerapp.icebreaker.models.IcebreakerNotification;
import in.icebreakerapp.icebreaker.models.SendMessage;

/**
 * Created by siddharth on 23-08-2016.
 */
public class ChatActivity extends ActionBarActivity {

    private EditText messageET;
    private ListView messagesContainer;
    private FloatingActionButton sendBtn;
    public static ChatAdapter adapter;
    Intent serviceIntent;
    BroadcastReceiver receiver;
    private long number;
    private long fno;
    private long time;
    String title;
    Intent intent;
    public static List<IcebreakerNotification> chatHistory;
    MessageDb db;
    SharedPreferences sp2;
    IcebreakerNotification chatMessage;
    private Menu menu;
    ClipboardManager myClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        intent = getIntent();
        title = intent.getStringExtra("title");
//        setTitle(title);

        sp2 = getApplicationContext().getSharedPreferences("user", 0);
        chatHistory = new ArrayList<IcebreakerNotification>();
        db =new MessageDb(ChatActivity.this);
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
//        actionBar.setDisplayShowCustomEnabled(true);
//
//        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflator.inflate(R.layout.custom_imageview, null);
//        ((TextView)v.findViewById(R.id.chat_id)).setText(title);
//        actionBar.setCustomView(v);

        initControls();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String time = intent.getStringExtra("time");
                String counter = intent.getStringExtra("counter");
                loadDummyHistory();
                scroll();


            }
        };

    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (FloatingActionButton) findViewById(R.id.chatSendButton);

        TextView meLabel = (TextView) findViewById(R.id.meLbl);
        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
//        companionLabel.setText("My Buddy");// Hard Coded
        loadDummyHistory();
        scroll();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }


                number = (long) Math.floor(Math.random() * 900000000L) + 10000000L;

                chatMessage = new IcebreakerNotification();
//                chatMessage.setId(122);//dummy
                chatMessage.setMessage(messageText);
                chatMessage.setTo(title);
                chatMessage.setFrom(sp2.getString("enroll",""));
                chatMessage.setSendType(1);
//                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(true);
                displayMessage(chatMessage);
                String serverURL1 = "http://anip.xyz/icebreakerlogin.php";
                new LongOperation2().execute(serverURL1);

            }
        });

    }

    public void displayMessage(IcebreakerNotification message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();

    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory(){



//        ChatMessage msg = new ChatMessage();
//        msg.setId(1);
//        msg.setMe(false);
//        msg.setMessage("Hi");
//        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
//        chatHistory.add(msg);
//        ChatMessage msg1 = new ChatMessage();
//        msg1.setId(2);
//        msg1.setMe(false);
//        msg1.setMessage("How r u doing???");
//        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
//        chatHistory.add(msg1);
        final IcebreakerNotification notification = new IcebreakerNotification();
        notification.setFrom(title);
        notification.setTo(getSharedPreferences("user",0).getString("enroll",""));
        chatHistory = db.getTodayFoodItems(db.getChatId(notification),title,getSharedPreferences("user",0).getString("enroll",""));


        adapter = new ChatAdapter(ChatActivity.this, db.getTodayFoodItems(db.getChatId(notification),title,getSharedPreferences("user",0).getString("enroll","")),title);
        messagesContainer.setAdapter(adapter);


        messagesContainer.setOnItemLongClickListener((new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int i, long l) {
//                Toast.makeText(getApplicationContext(), "Clicked on position" + i, Toast.LENGTH_LONG).show();
                final MenuItem delete = menu.findItem(R.id.nav_delete);
                final MenuItem copy = menu.findItem(R.id.nav_copy);
                delete.setVisible(true);
//                Log.i("hell", String.valueOf(adapterView.findViewById(R.id.chat_back)));
//                Log.i("hell", String.valueOf(view.getParent()));
//                Log.i("hell", String.valueOf(adapterView.getSelectedView().findViewById(R.id.chat_back)));
                final RelativeLayout chat_back = (RelativeLayout)view.findViewById(R.id.chat_back);
                        chat_back.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                copy.setVisible(true);
                copy.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        ClipData myClip;
                        myClip = ClipData.newPlainText("text", db.getTodayFoodItems(db.getChatId(notification),title,getSharedPreferences("user",0).getString("enroll","")).get(i).getMessage());
                        myClipboard.setPrimaryClip(myClip);
                        delete.setVisible(false);
                        copy.setVisible(false);
                       chat_back.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        return false;
                    }
                });
                delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        view.findViewById(R.id.chat_back).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        if(i!=0)
                db.deleteMessage(db.getTodayFoodItems(db.getChatId(notification),title,getSharedPreferences("user",0).getString("enroll","")).get(i).getId());
                else
                db.deleteMessage(db.getTodayFoodItems(db.getChatId(notification),title,getSharedPreferences("user",0).getString("enroll","")).get(0).getId());
                loadDummyHistory();
                        scroll();
                        delete.setVisible(false);
                        copy.setVisible(false);
                        chat_back.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                   return false;
                    }
                });
//
                return true;
            }
//        for(int i=0; i<chatHistory.size(); i++) {
//            IcebreakerNotification message = chatHistory.get(i);
//            displayMessage(message);
//
// }
        }));
    }
    private class LongOperation2 extends AsyncTask<String, Void, SendMessage> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        // private String Content;
        private String Error = null;
        private SendMessage result;
        String studentId;
        private ProgressDialog Dialog = new ProgressDialog(ChatActivity.this);
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

//            Dialog.setMessage("Please wait..");
//            Dialog.show();
            // try{
            // Set Request parameter
            SharedPreferences sp = getApplicationContext().getSharedPreferences("user", 0);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("to", title);
            jsonObject.addProperty("message",messageET.getText().toString());
            jsonObject.addProperty("from", sp.getString("enroll",""));
            jsonObject.addProperty("time", time);
            jsonObject.addProperty("id",fno);
            jsonObject.addProperty("type","simple");
            messageET.setText("");


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
        protected SendMessage doInBackground(String... urls) {


            HttpURLConnection httpcon;

            try {

                httpcon = (HttpURLConnection) ((new URL("http://anip.xyz:8080/send/").openConnection()));
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

                result = gson2.fromJson(sb.toString(), SendMessage.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Append Server Response To Content String


            /*****************************************************/
            return result;
        }

        protected void onPostExecute(SendMessage response) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            Log.i("response", String.valueOf(response) + Error);
            if (response.getStatus().equalsIgnoreCase("true")) {

//                Intent intent = new Intent(Lo.this, MainActivity.class);
//                startActivity(intent);
                MessageDb db = new MessageDb(ChatActivity.this);
                Log.i("hell", String.valueOf(db.getChatId(chatMessage)));
                if(db.getChatId(chatMessage)==0)
                    db.addChat(chatMessage,System.currentTimeMillis());
                db.updateChat(db.getChatId(chatMessage),response.getTime());
                db.addMessage(chatMessage.getMessage(),db.getChatId(chatMessage),fno=number,1,response.getTime(),1);
            } else {
                Toast.makeText(ChatActivity.this, response.getStatus(), Toast.LENGTH_LONG).show();
            }


            // Show Response Json On Screen (activity)
            // uiUpdate.setText(Content);

            /****************** Start Parse Response JSON Data *************/

            // String OutputData = ""
//                               	SharedPreferences sp = getApplicationContext()


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        serviceIntent = new Intent(getApplicationContext(),
                UpdaterService.class);
//        startService(serviceIntent);

        registerReceiver(receiver, new IntentFilter(
                UpdaterService.BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(serviceIntent);
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        setResult(Activity.RESULT_OK, intent);
//        finish();
//        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
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

