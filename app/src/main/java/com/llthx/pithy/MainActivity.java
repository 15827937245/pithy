package com.llthx.pithy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.llthx.pithy.event.PithyEventCallback;

public class MainActivity extends AppCompatActivity implements PithyEventCallback {
    public String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PithyClient.getInstance().register(TAG,"testOnclick",this::pithyEventCallback);
    }

    @Override
    public void pithyEventCallback() {
        Toast.makeText(this,"pithyEventCallback",Toast.LENGTH_SHORT).show();
    }


    public void testOnclick(View view) {
        Log.v(TAG,"testOnclick()");

        PithyClient.getInstance().post(TAG,"testOnclick");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PithyClient.getInstance().unRegister(TAG,"testOnclick");
    }
}