package com.example.joe.lab1;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MessageDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_fragment_layout);

        Bundle info = getIntent().getExtras();

        info.putString("Key", "From phone");
        //start Transaction to insert fragment in screen:
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        MessageFragment mf = new MessageFragment();
        mf.setArguments(info);
        ft.add(R.id.frameLayout, mf);
        ft.commit();
    }
}
