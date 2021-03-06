package com.example.derek.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.example.derek.R;
import com.example.derek.view.FlowLayout;
import com.example.derek.view.TagAdapter;
import com.example.derek.view.TagFlowLayout;

import java.util.Arrays;

public class MainActivity extends Activity {

    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView", "Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView"};

    private TagFlowLayout mFlowLayout;
    private TagAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final LayoutInflater layoutInflater = LayoutInflater.from(this);
        mFlowLayout = (TagFlowLayout) findViewById(R.id.flowlayout);
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(MainActivity.this, mVals[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mAdapter = new TagAdapter(this, Arrays.asList(mVals));
        mFlowLayout.setAdapter(mAdapter);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setEdit(!mAdapter.isEdit());
            }
        });
    }
}
