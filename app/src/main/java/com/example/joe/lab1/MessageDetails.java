package com.example.joe.lab1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MessageDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_fragment_layout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            Bundle msgDetails = extras.getBundle("msgDetails");
            MessageFragment loadedFragment = new MessageFragment();
            loadedFragment.setArguments(msgDetails);
            getFragmentManager().beginTransaction()
                    .add(R.id.frameLayout, loadedFragment)
                    .commit();
        }
    }

    public void deleteMessage(long id, int position) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", id);
        resultIntent.putExtra("position", position);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();   //finish closes this empty activity on phones.
    }
}
