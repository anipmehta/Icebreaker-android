package in.icebreakerapp.icebreaker.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import in.icebreakerapp.icebreaker.models.Contact;
import in.icebreakerapp.icebreaker.models.HomeChat;
import in.icebreakerapp.icebreaker.models.IcebreakerNotification;

/**
 * Created by anip on 24/08/16.
 */
public class MessageDb extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "icebreaker";

    public MessageDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE chat (receiver VARCHAR(8),sender VARCHAR(8),chat_id INTEGER PRIMARY KEY AUTOINCREMENT,lastActive INTEGER)";
        String query2 = "CREATE TABLE messages(id INTEGER PRIMARY KEY,chat_id INTEGER,message TEXT NOT NULL,deliver INTEGER DEFAULT 0,send_type INTEGER DEFAULT 0,message_id TEXT NOT NULL,time INTEGER,read INTEGER,FOREIGN KEY(chat_id) REFERENCES chat(chat_id))";
        String query3 = "CREATE TABLE contacts(id INTEGER PRIMARY KEY AUTOINCREMENT,enroll TEXT NOT NULL,status TEXT)";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query2);
        sqLiteDatabase.execSQL(query3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS chat");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS messages");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(sqLiteDatabase);
    }

    public void addChat(IcebreakerNotification data,long lastActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues values = new ContentValues();
        values.put("receiver", data.getTo());
        values.put("sender", data.getFrom());
        values.put("lastActive",lastActive);
        db.insert("chat", null, values);
        db.close();
    }

    public void addMessage(String data, int id, long message_id, int type, long time, int read) {
        SQLiteDatabase db = this.getWritableDatabase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues values = new ContentValues();
        values.put("message", data);
        values.put("chat_id", id);
        values.put("message_id", message_id);
        values.put("send_type", type);
        values.put("time", time);
        values.put("read", read);
        db.insert("messages", null, values);
        db.close();
    }

    public int getChatId(IcebreakerNotification data) {
        String countQuery = "SELECT * FROM " + "chat where receiver=" + data.getTo() + " and sender=" + data.getFrom() + "";
        Log.i("hell", countQuery);
        String query2 = "SELECT * FROM " + "chat where receiver=" + data.getFrom() + " and sender=" + data.getTo() + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        Cursor cursor2 = db.rawQuery(query2, null);
//        Log.i("hell", String.valueOf(cursor.getColumnCount()));
        if (cursor.getCount() == 0) {
            if (cursor2.getCount() == 0)
                return 0;
            else {
                cursor2.moveToFirst();
                return cursor2.getInt(2);
            }
        } else {
            cursor.moveToFirst();
            return cursor.getInt(2);
        }
    }

    public void addContact(String enroll) {
        SQLiteDatabase db = this.getWritableDatabase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues values = new ContentValues();
        values.put("enroll", enroll);
        db.insert("contacts", null, values);
        db.close();
    }

    public List<IcebreakerNotification> getTodayFoodItems(int chatId, String to, String from) {
        List<IcebreakerNotification> messageList = new ArrayList<IcebreakerNotification>();
        String selectQuery = "SELECT * FROM messages where chat_id=" + chatId + " ORDER BY time ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                IcebreakerNotification message = new IcebreakerNotification();
                message.setMessage(cursor.getString(2));
//                String countQuery = "SELECT * FROM " + "chat where chat_id="+cursor.getString(1)+"";
//                Cursor curso2 = db.rawQuery(countQuery,null);
//                curso2.moveToFirst();
//                updateRead(Integer.parseInt(cursor.getString(0)));
                if (Integer.parseInt(cursor.getString(4)) == 1)
                    message.setFrom(from);
                message.setTo(to);
                message.setId(Integer.parseInt(cursor.getString(0)));
                message.setSendType(Integer.parseInt(cursor.getString(4)));
                message.setDeliver(Integer.parseInt(cursor.getString(3)));
                Log.i("hell", cursor.getString(2) + message.getFrom() + message.getTo() + "type" + message.getSendType());
                messageList.add(message);
            } while (cursor.moveToNext());
        }

        return messageList;
    }

    public ArrayList<HomeChat> getChats(String enroll) {
        ArrayList<HomeChat> messageList = new ArrayList<HomeChat>();
        String selectQuery = "SELECT * FROM chat ORDER BY lastActive DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HomeChat message = new HomeChat();
                if (cursor.getString(1).equalsIgnoreCase(enroll))
                    message.setEnroll(cursor.getString(0));
                else
                    message.setEnroll(cursor.getString(1));
                message.setChat_id(Integer.parseInt(cursor.getString(2)));
                Log.i("hell", cursor.getString(1));
                messageList.add(message);
            } while (cursor.moveToNext());
        }

        return messageList;
    }

    public ArrayList<Contact> getContact() {
        ArrayList<Contact> messageList = new ArrayList<Contact>();
        String selectQuery = "SELECT * FROM contacts";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
//        Log.i("hell",cursor.getString(1));


        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setEnroll(cursor.getString(1));
                Log.i("hell", cursor.getString(1));
                messageList.add(contact);
            } while (cursor.moveToNext());
        }

        return messageList;
    }
    public void updateChat(int chat_id,long lastActive){
        String selectQuery = "UPDATE chat SET lastActive="+lastActive+" WHERE chat_id=" + chat_id + "";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(selectQuery);
    }

    public void updateRead(int mId) {
        String selectQuery = "UPDATE messages SET read=1 WHERE id=" + mId + "";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(selectQuery);

    }

    public void updateMessage(String id) {
        String selectQuery = "UPDATE messages SET deliver=1 WHERE message_id=" + id + "";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(selectQuery);
    }

    public String lastMessage(int id) {
        String selectQuery = "SELECT * FROM messages where chat_id=" + id + "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        return cursor.getString(2);
    }

    public int unread() {
        String selectQuery = "SELECT * FROM messages where read=0" + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        return cursor.getCount();
    }

    public int unreadChat() {
        String selectQuery = "SELECT * FROM messages where read=0" + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        TreeSet chats = new TreeSet();
        if (cursor.moveToFirst()) {
            do {
                chats.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
    return chats.size();
    }

}
