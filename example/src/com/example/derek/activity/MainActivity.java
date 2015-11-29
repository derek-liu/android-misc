package com.example.derek.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.text.ICUCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.derek.R;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends Activity {

    private Button mBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mBtn = (Button) findViewById(R.id.go_test);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handleClick();
                printLocaleInfo();
            }
        });
    }

    private void handleClick() {
        Intent intent = new Intent(this, LanguageActivity.class);
        startActivity(intent);
    }

    private void printLocaleInfo() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("getCountry", Locale.getDefault().getCountry());
            obj.put("getDisplayCountry", Locale.getDefault().getDisplayCountry());
            obj.put("getDisplayLanguage", Locale.getDefault().getDisplayLanguage());
            obj.put("getLanguage", Locale.getDefault().getLanguage());
            obj.put("getISO3Country", Locale.getDefault().getISO3Country());
            obj.put("getISO3Language", Locale.getDefault().getISO3Language());
            obj.put("maximizeAndGetScriptt", ICUCompat.maximizeAndGetScript(Locale.getDefault()));
            Log.d("d.d", obj.toString());
        } catch (Exception e) {
        }
    }
}
