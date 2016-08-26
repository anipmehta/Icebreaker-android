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
        String query = "CREATE TABLE chat ('to' VARCHAR(8),'from' VARCHAR(8),chat_id INTEGER PRIMARY KEY AUTOINCREMENT)";
        String query2 = "CREATE TABLE messages(id INTEGER PRIMARY KEY,chat_id INTEGER,message TEXT NOT NULL,FOREIGN KEY(chat_id) REFERENCES chat(chat_id))";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS chat");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS messages");
        onCreate(sqLiteDatabase);
    }
    public void addChat(IcebreakerNotification data){
        SQLiteDatabase db = this.getWritableDatabase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues values = new ContentValues();
        values.put("'to'", data.getTo());
        values.put("'from'", data.getTo());
        db.insert("chat", null, values);
        db.close();
    }
    public void addMessage(String data,int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues values = new ContentValues();
        values.put("message", data);
        values.put("chat_id",id);
        db.insert("messages", null, values);
        db.close();
    }
    public int getChatId(IcebreakerNotification data) {
        String countQuery = "SELECT  * FROM " + "chat where 'to'='" + data.getTo() + "' and 'from'='" + data.getFrom()+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor.getCount()==0)
            return 0;
        else
            return cursor.getInt(2);
    }
    public List<IcebreakerNotification> getTodayFoodItems() {
        List<IcebreakerNotification> messageList = new ArrayList<IcebreakerNotification>();
        String selectQuery = "SELECT * FROM messages";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                IcebreakerNotification message = new IcebreakerNotification();
                message.setMessage(cursor.getString(2));
                Log.i("hell",cursor.getString(2));
                messageList.add(message);
            } while (cursor.moveToNext());
        }

        return messageList;
    }
}
