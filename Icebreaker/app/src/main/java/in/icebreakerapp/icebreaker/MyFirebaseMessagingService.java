package in.icebreakerapp.icebreaker;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.List;
import in.icebreakerapp.icebreaker.helpers.MessageDb;
import in.icebreakerapp.icebreaker.models.IcebreakerNotification;

/**
 * Created by anip on 18/08/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    static final public String BROADCAST_ACTION = "com.pavan.broadcast";


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

        IcebreakerNotification message = new IcebreakerNotification();
        message.setMessage(remoteMessage.getData().get("message"));
        message.setFrom(remoteMessage.getData().get("title"));
        message.setTo("13103630");
        MessageDb db = new MessageDb(MyFirebaseMessagingService.this);
        Log.i("hell", String.valueOf(db.getChatId(message)));
        if(db.getChatId(message)==0)
            db.addChat(message);
        db.addMessage(message.getMessage(),db.getChatId(message));
//        ChatActivity.adapter.notifyDataSetChanged();
        if(isAppIsInBackground(this))
        sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), Integer.parseInt(remoteMessage.getData().get("id")));
        Intent intent;
        intent = new Intent(BROADCAST_ACTION);
        sendBroadcast(intent);
//        bindService(UpdaterService,null,null);
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String title, String messageBody,int id) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
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
}

