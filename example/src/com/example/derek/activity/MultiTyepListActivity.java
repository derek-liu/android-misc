package com.example.derek.activity;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.derek.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by liudingyu on 14/12/3.
 * <p/>
 * 在getview根据获得的item类型，就可以获得不同类型的itemView对应的不同类型的ViewHolder
 * 从而进行不同类型的item的显示
 * <p/>
 * PS:
 * listview内部是用数组存储不同类型的itemView，类型的字面值直接被当做数组下表使用
 * 所以定义类型时，必须从零累加上去，符合数组下标的规则才可以；否则会报数组越界错误
 */
public class MultiTyepListActivity extends ListActivity {
    private MultiTypeAdapter mAdatper = null;
    private View mTouchView = null;
    private int mDownX, mDownY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdatper = new MultiTypeAdapter(this);
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 50; i++) {
            ItemInfo info = new ItemInfo();
            switch (Math.abs(random.nextInt()) % 2) {
                case 0:
                    info.content = "send " + i;
                    info.type = MultiTypeAdapter.TYPE_SEND;
                    break;
                case 1:
                    info.content = "receive " + i;
                    info.type = MultiTypeAdapter.TYPE_RECEIVE;
                    break;
            }
            mAdatper.addItem(info);

        }
        TextView headerView = new TextView(this);
        headerView.setText("header view");
        getListView().addHeaderView(headerView);
        setListAdapter(mAdatper);
        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //Log.d("k.k", "onScrollStateChanged: lastvisiblePosition" + getListView().getLastVisiblePosition() + " headercount:" + getListView().getHeaderViewsCount());
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //Log.d("k.k", "onScroll firstVisibleItem: " + firstVisibleItem + " visibleItemCount: " + visibleItemCount + " totalItemCount: " + totalItemCount);
            }
        });
        getListView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onListTouch(v, event);
            }
        });
    }

    private boolean onListTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                ListView listView = (ListView)v;
                int position = listView.pointToPosition(mDownX, mDownY);
                mTouchView = listView.getChildAt(position - listView.getFirstVisiblePosition());
                break;
            case MotionEvent.ACTION_MOVE:
                if (mTouchView != null) {
                    int dx = (int)(mDownX - event.getX());
                    //dx = (dx > mTouchView.getWidth()) ? mTouchView.getWidth() : dx;
                    //dx = (dx < 0) ? 0 : dx;
                    if (mTouchView.getLeft() >= 0) {
                        mTouchView.layout(0 - dx, mTouchView.getTop(), mTouchView.getRight() - dx, mTouchView.getBottom());
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mTouchView.layout(0, mTouchView.getTop(), mTouchView.getWidth(), mTouchView.getBottom());
                break;
            default:
                break;
        }

        return v.onTouchEvent(event);
    }
}

class MultiTypeAdapter extends BaseAdapter {

    public final static int TYPE_SEND = 0;
    public final static int TYPE_RECEIVE = TYPE_SEND + 1;
    public final static int MAX_TYPE_COUNT = TYPE_RECEIVE + 1;
    List<ItemInfo> mData = null;
    LayoutInflater mInflater = null;

    public MultiTypeAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mData = new ArrayList<ItemInfo>();
    }

    public void addItem(ItemInfo item) {
        mData.add(item);
    }

    @Override
    public int getCount() {
        return (mData == null || mData.isEmpty()) ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("k.k", "position: " + position);
        ViewHolder holder = null;
        ItemInfo info = mData.get(position);
        if (convertView == null) {
            switch (getItemViewType(position)) {
                case TYPE_RECEIVE:
                    convertView = mInflater.inflate(R.layout.multi_type_list_receive, null);
                    holder = new ViewHolder();
                    holder.content = (TextView) convertView.findViewById(R.id.content);
                    break;
                case TYPE_SEND:
                    convertView = mInflater.inflate(R.layout.multi_type_list_send, null);
                    holder = new ViewHolder();
                    holder.content = (TextView) convertView.findViewById(R.id.content);
                    break;
                default:
                    break;
            }
            if (convertView != null) {
                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder != null) {
            switch (getItemViewType(position)) {
                case TYPE_RECEIVE:
                    holder.content.setText(info.content);
                    break;
                case TYPE_SEND:
                    holder.content.setText(info.content);
                    break;
                default:
                    break;
            }
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return MAX_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData == null || mData.isEmpty()) {
            return IGNORE_ITEM_VIEW_TYPE;
        } else {
            ItemInfo info = mData.get(position);
            return info.type;
        }
    }
}

class ItemInfo {
    public String content;
    public int type;
}

class ViewHolder {
    public TextView content;
}