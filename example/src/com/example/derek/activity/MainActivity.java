package com.example.derek.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.derek.R;
import com.example.derek.utils.AppUtils;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int versioncode = AppUtils.getVersionCode(MainActivity.this, "");
                Toast.makeText(MainActivity.this,"" + versioncode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
    }


}
