package in.icebreakerapp.icebreaker.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        String query = "CREATE TABLE chat (receiver VARCHAR(8),sender VARCHAR(8),chat_id INTEGER PRIMARY KEY AUTOINCREMENT)";
        String query2 = "CREATE TABLE messages(id INTEGER PRIMARY KEY,chat_id INTEGER,message TEXT NOT NULL,deliver INTEGER DEFAULT 0,message_id TEXT NOT NULL,FOREIGN KEY(chat_id) REFERENCES chat(chat_id))";
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
    public void addChat(IcebreakerNotification data){
        SQLiteDatabase db = this.getWritableDatabase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues values = new ContentValues();
        values.put("receiver", data.getTo());
        values.put("sender", data.getFrom());
        db.insert("chat", null, values);
        db.close();
    }
    public void addMessage(String data,int id,long message_id){
        SQLiteDatabase db = this.getWritableDatabase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues values = new ContentValues();
        values.put("message", data);
        values.put("chat_id",id);
        values.put("message_id",message_id);
        db.insert("messages", null, values);
        db.close();
    }
    public int getChatId(IcebreakerNotification data) {
        String countQuery = "SELECT * FROM " + "chat where receiver=" + data.getTo() + " and sender=" + data.getFrom()+"";
        Log.i("hell",countQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
//        Log.i("hell", String.valueOf(cursor.getColumnCount()));
        if (cursor.getCount()==0)
            return 0;
        else {
            cursor.moveToFirst();
            return cursor.getInt(2);
        }
    }

    public void addContact(String enroll){
        SQLiteDatabase db = this.getWritableDatabase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues values = new ContentValues();
        values.put("enroll", enroll);
        db.insert("contacts", null, values);
        db.close();
    }

    public List<IcebreakerNotification> getTodayFoodItems(int chatId) {
        List<IcebreakerNotification> messageList = new ArrayList<IcebreakerNotification>();
        String selectQuery = "SELECT * FROM messages where chat_id="+chatId+"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                IcebreakerNotification message = new IcebreakerNotification();
                message.setMessage(cursor.getString(2));
                String countQuery = "SELECT * FROM " + "chat where chat_id="+cursor.getString(1)+"";
                Cursor curso2 = db.rawQuery(countQuery,null);
                curso2.moveToFirst();
                message.setFrom(curso2.getString(1));
                message.setTo(curso2.getString(0));
                message.setDeliver(Integer.parseInt(cursor.getString(3)));
                Log.i("hell",cursor.getString(2) + message.getFrom()+ message.getTo());
                messageList.add(message);
            } while (cursor.moveToNext());
        }

        return messageList;
    }

    public ArrayList<HomeChat> getChats() {
        ArrayList<HomeChat> messageList = new ArrayList<HomeChat>();
        String selectQuery = "SELECT * FROM chat";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HomeChat message = new HomeChat();
                message.setTitle(cursor.getString(1));
                Log.i("hell",cursor.getString(1));
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
                Log.i("hell",cursor.getString(1));
                messageList.add(contact);
            } while (cursor.moveToNext());
        }

        return messageList;
    }
    public void updateMessage(String id){
        String selectQuery = "UPDATE messages SET deliver=1 WHERE message_id="+id+"";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(selectQuery);
    }

}
