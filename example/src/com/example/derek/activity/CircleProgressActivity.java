package com.example.derek.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import com.example.derek.R;
import com.example.derek.view.circleprogress.ArcProgress;
import com.example.derek.view.circleprogress.CircleProgress;
import com.example.derek.view.circleprogress.DonutProgress;

import java.util.Timer;
import java.util.TimerTask;

public class CircleProgressActivity extends ActionBarActivity {
    private Timer timer;
    private DonutProgress donutProgress1, donutProgress2,donutProgress3, donutProgress4;
    private CircleProgress circleProgress1, circleProgress2;
    private ArcProgress arcProgress1 , arcProgress2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_progress);
        donutProgress1 = (DonutProgress) findViewById(R.id.donut_progress1);
        donutProgress2 = (DonutProgress) findViewById(R.id.donut_progress2);
        donutProgress3 = (DonutProgress) findViewById(R.id.donut_progress3);
        donutProgress4 = (DonutProgress) findViewById(R.id.donut_progress4);
        circleProgress1 = (CircleProgress) findViewById(R.id.circle_progress1);
        circleProgress2 = (CircleProgress) findViewById(R.id.circle_progress2);
        arcProgress1 = (ArcProgress) findViewById(R.id.arc_progress1);
        arcProgress2 = (ArcProgress) findViewById(R.id.arc_progress2);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        donutProgress1.setProgress(donutProgress1.getProgress() + 1);
                        donutProgress2.setProgress(donutProgress2.getProgress() + 1);
                        donutProgress3.setProgress(donutProgress3.getProgress() + 1);
                        donutProgress4.setProgress(donutProgress4.getProgress() + 1);
                        circleProgress1.setProgress(circleProgress1.getProgress() + 1);
                        circleProgress2.setProgress(circleProgress2.getProgress() + 1);
                        arcProgress1.setProgress(arcProgress1.getProgress() + 1);
                        arcProgress2.setProgress(arcProgress2.getProgress() + 1);
                    }
                });
            }
        }, 1000, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
