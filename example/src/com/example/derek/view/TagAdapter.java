package com.example.derek.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.example.derek.R;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter {
    private List<String> mTagDatas;
    private OnDataChangedListener mOnDataChangedListener;

    private LayoutInflater mInflater;
    private boolean mIsEdit = false;
    private int mHidePosition = -1;

    public TagAdapter(Context context, List<String> datas) {
        mTagDatas = new ArrayList<String>(datas);
        mInflater = LayoutInflater.from(context);
    }

    static interface OnDataChangedListener {
        void onChanged();
    }

    void setOnDataChangedListener(OnDataChangedListener listener) {
        mOnDataChangedListener = listener;
    }

    public int getCount() {
        return mTagDatas == null ? 0 : mTagDatas.size();
    }

    public void setEdit(boolean edit) {
        mIsEdit = edit;
        notifyDataChanged();
    }

    public boolean isEdit() {
        return mIsEdit;
    }

    public void notifyDataChanged() {
        mOnDataChangedListener.onChanged();
    }

    public String getItem(int position) {
        return mTagDatas.get(position);
    }

    public View getView(FlowLayout parent, int position) {
        View view = mInflater.inflate(R.layout.flow_item, parent, false);
        TextView tv = (TextView) view.findViewById(R.id.text);
        tv.setText(getItem(position));
        view.findViewById(R.id.delete).setVisibility(mIsEdit ? View.VISIBLE : View.INVISIBLE);
        return view;
    }

    public int getHidePosition() {
        return mHidePosition;
    }

    public void setHideItem(int postion) {
        mHidePosition = postion;
        notifyDataChanged();
    }

    public void reorderItems(int oldPos, int newPos) {
        if (oldPos < 0 || newPos < 0 || oldPos > mTagDatas.size() || newPos > mTagDatas.size()) {
            notifyDataChanged();
            return;
        }
        mTagDatas.add(newPos, mTagDatas.remove(oldPos));
        notifyDataChanged();
    }
}