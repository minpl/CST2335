package com.example.joe.lab1;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MessageFragment extends Fragment {
    private Activity parent;
    private long database_id;
    private int message_id;
    private String message;
    private Bundle info;

    public MessageFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (getTargetRequestCode() == 10) {
        }
        Bundle passedInfo = getArguments();


        if (passedInfo != null) {
            database_id = passedInfo.getLong("message_database_id");
            message = passedInfo.getString("message");
            message_id = passedInfo.getInt("message_id");
        }
        parent = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, null);

        TextView messageView = (TextView) v.findViewById(R.id.messageTextView);
        TextView idView = (TextView) v.findViewById(R.id.idTextView);

        messageView.setText(message);
        idView.setText(database_id + " ");
        Button b = (Button) v.findViewById(R.id.deleteMessageButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getTargetRequestCode() == 10) {  //10 indicates activity started from phone
                    Intent intent = new Intent();
                    info = new Bundle();
                    info.putInt("idToDelete", message_id);

                    intent.putExtras(info);
                    parent.setResult(20, intent);   //20 means delete this message entry
                    parent.finish();
                    parent.getFragmentManager().beginTransaction().remove(MessageFragment.this).commit();
                }

                parent.getFragmentManager().beginTransaction().remove(MessageFragment.this).commit();
            }
        });


        return v;
    }
}