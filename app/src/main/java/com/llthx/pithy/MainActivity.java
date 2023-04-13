package com.llthx.pithy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.llthx.pithy.event.PithyEventCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PithyEventCallback {
    public String TAG = "MainActivity";
    public int onClickNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PithyClient.getInstance().register(TAG,"testOnclick",this::pithyEventCallback);
    }

    @Override
    public void pithyEventCallback(String eventKey, String jonsDate) {
        try {
            JSONObject data = new JSONObject(jonsDate);
            int onClickNumber = (int) data.get("onClickNumber");
            Toast.makeText(this,"pithyEventCallback, onClickNumber : " + onClickNumber,Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public void testOnclick(View view) {
        Log.v(TAG,"testOnclick()");
        onClickNumber++;
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("onClickNumber",onClickNumber);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        PithyClient.getInstance().post(TAG,"testOnclick", jsonObject.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PithyClient.getInstance().unRegister(TAG,"testOnclick");
    }
}