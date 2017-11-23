package com.example.joe.lab1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    ListView lv;
    EditText et;
    Button b;
    ChatDatabaseHelper cdh;
    SQLiteDatabase db;

    static final String TABLE_NAME = "MESSAGES";
    static final String KEY_MESSAGE = "KEY_MESSAGE";

    final ArrayList<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        lv = (ListView) findViewById(R.id.listView1);
        et = (EditText) findViewById(R.id.editText1);
        b = (Button) findViewById(R.id.sendButton);

        cdh = new ChatDatabaseHelper(this);
        db = cdh.getWritableDatabase();

        Cursor c = db.query(false, TABLE_NAME, new String[]{KEY_MESSAGE}, null, null, null, null, null, null);
        c.close();

        c.moveToFirst();
        while (!c.isAfterLast()) {
            String message = c.getString(c.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            messages.add(message);
            Log.i("SQL File Contained: ", message);
            c.moveToNext();
        }

        Log.i("ChatWindow", "Cursor’s  column count = " + c.getColumnCount());
        Log.i("ChatWindow", "Database Column 0 Name = " + c.getColumnName(0));
        Log.i("ChatWindow", "Database Column 1 Name = " + c.getColumnName(1));


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages.add(et.getText().toString());
                ContentValues newMessage = new ContentValues();
                newMessage.put(ChatDatabaseHelper.KEY_MESSAGE, et.getText().toString());
                db.insert(ChatDatabaseHelper.TABLE_NAME, null, newMessage);
                et.setText("");
            }
        });

        ChatAdapter messageAdapter = new ChatAdapter(this);
        lv.setAdapter(messageAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    class ChatAdapter extends ArrayAdapter<String> {
        ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return messages.size();
        }

        public String getItem(int position) {
            return messages.get(position);
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView) result.findViewById(R.id.message_text);  //create reference to inflated view's textView - message_text
            message.setText(getItem(position)); //attach actual message to into the view's textView
            return result;  //return the textView that now contains the message
        }
    }
}
