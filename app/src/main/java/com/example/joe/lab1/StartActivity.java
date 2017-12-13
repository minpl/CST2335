package com.example.joe.lab1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {

    private static final String ACTIVITY_NAME = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME, "In onCreate()");


        Button b1 = (Button) findViewById(R.id.launchButton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, 10);
                onActivityResult(10, 10, intent);
            }
        });

        Button b2 = (Button) findViewById(R.id.chatButton);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, ChatWindow.class);
                startActivityForResult(intent, 10);
                onActivityResult(10, 10, intent);
                Log.i(ACTIVITY_NAME, "User clicked Start Chat button");
            }
        });

        Button b3 = (Button) findViewById(R.id.weatherButton);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, WeatherForecast.class);
                startActivityForResult(intent, 10);
                onActivityResult(10, 10, intent);
                Log.i(ACTIVITY_NAME, "User clicked Start Weather Forecast button");
            }
        });

        Button b4 = (Button) findViewById(R.id.launchTestToolbar);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, TestToolbar.class);
                startActivityForResult(intent, 10);
                onActivityResult(10, 10, intent);
                Log.i(ACTIVITY_NAME, "User clicked Start Toolbar Test button");
            }
        });
    }

    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    protected void onRestart() {
        super.onRestart();
        Log.i(ACTIVITY_NAME, "In onRestart()");
    }

    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("Response");
                Toast.makeText(getApplicationContext(), "ListItemsActivity Passed: " + result, Toast.LENGTH_SHORT).show(); //R.string.result_message_title
            }
        }
    }

}
