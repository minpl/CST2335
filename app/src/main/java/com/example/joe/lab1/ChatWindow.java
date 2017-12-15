package com.example.joe.lab1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    static final String TABLE_NAME = "MESSAGES";
    static final String KEY_MESSAGE = "KEY_MESSAGE";
    static public ArrayList<String> messages;
    static SQLiteDatabase db;
    static ChatAdapter messageAdapter;
    final int DELETE_REQUEST = 10;
    public Cursor c;
    ListView lv;
    EditText et;
    Button sendButton;
    ChatDatabaseHelper cdh;
    boolean isTablet;

    protected static void deleteMessage(long id, int position) {
        messages.remove(position);
        db.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID + "=" + id, null);
        messageAdapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        messages = new ArrayList<>();
        Log.i("Method: ", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        //Log.i("vewi", get);


        lv = (ListView) findViewById(R.id.listView1);

        et = (EditText) findViewById(R.id.editText1);
        sendButton = (Button) findViewById(R.id.sendButton);

        cdh = new ChatDatabaseHelper(this);
        db = cdh.getWritableDatabase();

        c = db.query(false, TABLE_NAME, new String[]{ChatDatabaseHelper.KEY_ID, KEY_MESSAGE}, null, null, null, null, null, null);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            String message = c.getString(c.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            messages.add(message);
            Log.i("SQL File Contained: ", message);
            c.moveToNext();
        }

        Log.i("ChatWindow", "Cursor’s  column count = " + c.getColumnCount());
        Log.i("ChatWindow", "Database Column 0 Name = " + c.getColumnName(0));

        messageAdapter = new ChatAdapter(this);
        lv.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages.add(et.getText().toString());
                ContentValues newMessage = new ContentValues();
                newMessage.put(ChatDatabaseHelper.KEY_MESSAGE, et.getText().toString());

                db.insert(ChatDatabaseHelper.TABLE_NAME, null, newMessage);
                messageAdapter.notifyDataSetChanged();

                et.setText("");
            }
        });

        isTablet = findViewById(R.id.frameLayout) != null;
        final Bundle info = new Bundle();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                c.moveToPosition(position);
                info.putLong("message_database_id", messageAdapter.getItemID(position));
                info.putString("message", messages.get(position));
                info.putInt("message_id", position);

                if (isTablet) {
                    Log.i("launching details for", "tablet");
                    MessageFragment messageFragment = new MessageFragment();
                    messageFragment.setArguments(info);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, messageFragment).commit();
                } else {
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtra("msgDetails", info);
                    startActivityForResult(intent, DELETE_REQUEST);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == DELETE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle extras = data.getExtras();
                long id = extras.getLong("id");
                int position = extras.getInt("position");
                deleteMessage(id, position);
                messageAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("Method: ", "onDestroy");
        super.onDestroy();
        db.close();
        cdh.close();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        startActivity(new Intent(this, ChatWindow.class));
    }

    class ChatAdapter extends ArrayAdapter<String> {
        ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return messages.size();
        }

        public long getItemID(int position) {
            c.moveToPosition(position);
            long id = c.getLong(0);
            return id;
        }

        public String getItem(int position) {
            return messages.get(position);
        }

        @SuppressWarnings("NullableProblems")
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
