package com.example.derek.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.net.NetClient;
import com.android.volley.net.listener.INetClientListener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.derek.R;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.go_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick();
            }
        });
    }

    private void handleClick() {
        String url = "http://mbs.hao.360.cn/?c=config&m=index&v=6.8.5beta&prd=360androidbrowser&chl=up685beta&verc=685&wid=798372703ac38dd26fe5844284586730&device_os=4.2.2";
        NetClient.getInstance().executeGetRequest(url, new INetClientListener() {
            @Override
            public void onSuccess(String content, Object... msg) {
                Log.d("k.k", content);
            }

            @Override
            public void onFailure(int errorCode, Object msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
    }


}
