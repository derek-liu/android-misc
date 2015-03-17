package com.example.derek.activity;

import android.app.Activity;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.android.volley.net.NetClient;
import com.android.volley.net.listener.INetClientListener;
import com.example.derek.R;

public class MainActivity extends Activity {

    private Button mBtn;
    private Handler handler = null;
    private HandlerThread mHandlerThread = new HandlerThread("example-handlethread");
    private Handler mHandlerTest = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mBtn = (Button) findViewById(R.id.go_test);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(0);
                mHandlerTest.sendEmptyMessage(1);
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("k.k", "normal handler: " + Thread.currentThread() + " mainLooper:" + (Looper.myLooper() == Looper.getMainLooper()));
            }
        };

        mHandlerThread.start();
        mHandlerTest = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("k.k", "handler thread: " + Thread.currentThread() + " mainLooper:" + (Looper.myLooper() == Looper.getMainLooper()));
            }
        };

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
