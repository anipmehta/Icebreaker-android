package in.icebreakerapp.icebreaker;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.renderscript.RenderScript;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
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
import java.util.Date;
import java.util.List;
import in.icebreakerapp.icebreaker.helpers.MessageDb;
import in.icebreakerapp.icebreaker.models.IcebreakerNotification;
import in.icebreakerapp.icebreaker.models.RandomChat;
import in.icebreakerapp.icebreaker.models.SendMessage;

/**
 * Created by anip on 18/08/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    static final public String BROADCAST_ACTION = "com.pavan.broadcast";
    private JsonObject jsonObject=null;
    private static final String ICEBREAKER = "icebreaker";
    final static String GROUP_KEY_EMAILS = "group_key_emails";
    final static public String BROADCAST_HOME = "in.icebreaker.home";
    int count = 0;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.i("hell", "From: " + remoteMessage.getData());
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("message"));
//        UpdaterService.Updater updater new UpdaterService.Updater();
        //Calling method to generate notification
//        scroll();
        SharedPreferences sharedPreferences = this.getSharedPreferences("user",0);

        IcebreakerNotification message = new IcebreakerNotification();
        message.setMessage(remoteMessage.getData().get("message"));
        message.setFrom(remoteMessage.getData().get("title"));
        message.setTo(sharedPreferences.getString("enroll",""));
        MessageDb db = new MessageDb(MyFirebaseMessagingService.this);
        if (remoteMessage.getData().get("type").equalsIgnoreCase("deliver")){
            db.updateMessage(remoteMessage.getData().get("id"));

        }
        else if(remoteMessage.getData().get("type").equalsIgnoreCase("random")){
            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"),1,1,1);
            RandomChat randomChat;
            Gson gson = new Gson();
            randomChat = gson.fromJson(remoteMessage.getData().get("profile"),RandomChat.class);
            db.addRandom(randomChat,Long.parseLong(remoteMessage.getData().get("time")));


        }
        else {
            jsonObject = new JsonObject();
            jsonObject.addProperty("to",message.getFrom());
            jsonObject.addProperty("from", remoteMessage.getFrom());
            jsonObject.addProperty("id",remoteMessage.getData().get("id"));
            jsonObject.addProperty("type","deliver");
            sendDeliver();
            Log.i("hell", String.valueOf(db.getChatId(message)));
            if (db.getChatId(message) == 0){
                db.addChat(message,System.currentTimeMillis());
//                db.addContact(message.getFrom());
            }
            db.updateChat(db.getChatId(message),System.currentTimeMillis());

            db.addMessage(message.getMessage(), db.getChatId(message),Long.parseLong(remoteMessage.getData().get("id")),0,Long.parseLong(remoteMessage.getData().get("time")),0);
//        ChatActivity.adapter.notifyDataSetChanged();
            if (isAppIsInBackground(this)) {

                sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), db.unread(),db.unreadChat(),0);
                db.close();
            }
            else
            {
                final MediaPlayer mp = MediaPlayer.create(this, R.raw.sound);
                mp.start();
            }
            }
        Intent intent;
        intent = new Intent(BROADCAST_ACTION);
        sendBroadcast(intent);
        Intent intent2;
        intent2 = new Intent(BROADCAST_HOME);
        sendBroadcast(intent2);

//        bindService(UpdaterService,null,null);
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String title, String messageBody,int count,int chatcount,int id) {
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("title",title);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String message;
        if(count<=1){
            message = messageBody;
        }
        else{
            if(chatcount==1)
                message = count +" new messages ";
            else {


                message = count + " new messages from " + chatcount + " chats.";
                title="Icebreaker";
            }

        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setGroupSummary(true)
                .setGroup(GROUP_KEY_EMAILS)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id, notificationBuilder.build());
    }
    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
    private class LongOperation2 extends AsyncTask<String, Void, SendMessage> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        // private String Content;
        private String Error = null;
        private SendMessage result;
        String studentId;
//        private ProgressDialog Dialog = new ProgressDialog(Ch.this);
        String data = "";

        int sizeData = 0;

        protected void onPreExecute()
        {
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
//            SharedPreferences sp = getApplicationContext().getSharedPreferences("user", 0);



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

                httpcon = (HttpURLConnection) ((new URL("http://anip.xyz:8080/deliver/").openConnection()));
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
//            Dialog.dismiss();

            Log.i("response", String.valueOf(response) + Error);
//            if (response.getStatus().equalsIgnoreCase("true")) {
//                Intent intent = new Intent(Lo.this, MainActivity.class);
//                startActivity(intent);

//            } else {
//                Toast.makeText(ChatActivity.this, response.getStatus(), Toast.LENGTH_LONG).show();
//            }


            // Show Response Json On Screen (activity)
            // uiUpdate.setText(Content);

            /****************** Start Parse Response JSON Data *************/

            // String OutputData = ""
//                               	SharedPreferences sp = getApplicationContext()


        }

    }
    public void sendDeliver(){
        String serverURL1 = "http://anip.xyz/icebreakerlogin.php";
        new LongOperation2().execute(serverURL1);

    }

}

