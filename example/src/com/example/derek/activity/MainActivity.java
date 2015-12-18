package com.example.derek.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.derek.R;
import com.example.derek.view.FlowLayout;
import com.example.derek.view.TagAdapter;
import com.example.derek.view.TagFlowLayout;

public class MainActivity extends Activity {

    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView", "Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView"};

    private TagFlowLayout mFlowLayout;

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
        mFlowLayout.setAdapter(new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String str) {
                View view = layoutInflater.inflate(R.layout.flow_item, mFlowLayout, false);
                TextView tv = (TextView) view.findViewById(R.id.text);
                tv.setText(str);
                return view;
            }
        });
    }
}
