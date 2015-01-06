package com.example.derek;

import android.app.Application;
import com.android.volley.net.VolleyController;

/**
 * Created by liudingyu on 1/6/15.
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VolleyController.init(this);
    }
}
