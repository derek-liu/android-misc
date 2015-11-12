package com.example.derek.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.derek.R;
import com.example.derek.app.DateTimeFormat;

import java.util.Calendar;

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
                handleClick();
            }
        });

    }
    private void handleClick() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 12);
        long target = calendar.getTimeInMillis();

        DateTimeFormat format = new DateTimeFormat(this);
        String time = format.format(target);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(time).show();
    }
}
