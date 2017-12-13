package com.example.joe.lab1;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MessageFragment extends Fragment {

    //This fragment holds the details of the message and a delete button.
    //it is either attached to a frame layout in an empty activity in phones,
    //or to the right-hand, in layout, frame layout in tablets.

    TextView messageView;
    TextView idView;
    private Activity parent;
    private long database_id;
    private int message_id;
    private String message;

    public MessageFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle info = getArguments();

        if (info != null) {
            database_id = info.getLong("message_database_id");
            message = info.getString("message");
            message_id = info.getInt("message_id");
        }

        Log.i("passed info", database_id + " - " + message);

        View v = inflater.inflate(R.layout.fragment_message, container, false);

        messageView = (TextView) v.findViewById(R.id.messageTextView);
        idView = (TextView) v.findViewById(R.id.idTextView);

        messageView.setText("Message: " + message);
        idView.setText("Database ID: " + database_id);

        Button deleteButton = (Button) v.findViewById(R.id.deleteMessageButton);

        switch (parent.getLocalClassName()) {
            case "ChatWindow":  //define delete behavior for tablets
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChatWindow.deleteMessage(database_id, message_id);
                        parent.getFragmentManager().beginTransaction().
                                remove(MessageFragment.this).commit();
                    }
                });
                break;

            case "MessageDetails":  //define delete button for phones
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MessageDetails) parent).deleteMessage(database_id, message_id);
                    }
                });
                break;
        }
        return v;
    }
}