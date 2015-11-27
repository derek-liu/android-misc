package com.example.derek.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import com.example.derek.R;

import java.util.Locale;

public class LanguageActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_activity);
        findViewById(R.id.changeEN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Configuration config = getResources().getConfiguration();
                DisplayMetrics dm = getResources().getDisplayMetrics();
                config.locale = Locale.ENGLISH;
                getResources().updateConfiguration(config, dm);
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        findViewById(R.id.changeZH).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Configuration config = getResources().getConfiguration();
                DisplayMetrics dm = getResources().getDisplayMetrics();
                config.locale = Locale.SIMPLIFIED_CHINESE;
                getResources().updateConfiguration(config, dm);
            }
        });
    }
}