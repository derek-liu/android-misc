package com.example.derek.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.derek.R;

import java.util.Set;

public class TagFlowLayout extends FlowLayout implements TagAdapter.OnDataChangedListener {
    private TagAdapter mTagAdapter;
    private boolean mAutoSelectEffect = true;
    private int mSelectedMax = -1;//-1为不限制数量
    private static final String TAG = "TagFlowLayout";
    private MotionEvent mMotionEvent;
    private Handler mHandler = new Handler();
    private int mTouchSlop;

    /**
     * 刚开始拖拽的item对应的View
     */
    private View mStartDragItemView = null;

    private int mDownX, mDownY;
    private int mPoint2ItemTop, mPoint2ItemLeft;
    private int mOffset2Left, mOffset2Top;
    /**
     * 我们拖拽的item对应的Bitmap 只需要通过bitmap知道view的宽高
     */
    private Bitmap mDragBitmap;

    private boolean mIsDrag = false;

    /**
     * 状态栏的高度
     */
    private int mStatusHeight;

    /**
     * item镜像的布局参数
     */
    private WindowManager.LayoutParams mWindowLayoutParams;
    /**
     * 用于拖拽的镜像
     */
    private TextView mDragTextView;

    private WindowManager mWindowManager;

    /**
     * 正在拖拽的position
     */
    private int mDragPosition;

    // 用来处理是否为长按的Runnable
    private Runnable mLongClickRunnable = new Runnable() {

        @Override
        public void run() {
            mIsDrag = true; // 设置可以拖拽
            mTagAdapter.setEdit(true);
            if (mStartDragItemView != null) {
                mStartDragItemView.setVisibility(View.INVISIBLE);// 隐藏该item
            }

            // 根据我们按下的点显示item镜像
            createDragImage(mDragBitmap, mDownX, mDownY);
        }
    };


    public TagFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
        mAutoSelectEffect = ta.getBoolean(R.styleable.TagFlowLayout_auto_select_effect, true);
        mSelectedMax = ta.getInt(R.styleable.TagFlowLayout_max_select, -1);
        ta.recycle();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (mAutoSelectEffect) {
            setClickable(true);
        }
        mStatusHeight = getStatusBarHeight(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(Context context) {
        this(context, null);
    }

    public int getStatusBarHeight(Context context) {
        if (context == null) {
            return 0;
        }
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            TagView tagView = (TagView) getChildAt(i);
            if (tagView.getVisibility() == View.GONE) continue;
            if (tagView.getTagView().getVisibility() == View.GONE) {
                tagView.setVisibility(View.GONE);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface OnSelectListener {
        void onSelected(Set<Integer> selectPosSet);
    }

    private OnSelectListener mOnSelectListener;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
        if (mOnSelectListener != null) setClickable(true);
    }

    public interface OnTagClickListener {
        boolean onTagClick(View view, int position, FlowLayout parent);
    }

    private OnTagClickListener mOnTagClickListener;


    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        mOnTagClickListener = onTagClickListener;
        if (onTagClickListener != null) setClickable(true);
    }


    public void setAdapter(TagAdapter adapter) {
        mTagAdapter = adapter;
        mTagAdapter.setOnDataChangedListener(this);
        changeAdapter();

    }

    /**
     * 创建拖动的镜像
     *
     * @param bitmap
     * @param downX  按下的点相对父控件的X坐标
     * @param downY  按下的点相对父控件的X坐标
     */
    private void createDragImage(Bitmap bitmap, int downX, int downY) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        int height = (int) (bitmap.getHeight() * 1.1);
        int width = (int) (bitmap.getWidth() * 1.1);
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; // 图片之外的其他地方透明
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
        mWindowLayoutParams.alpha = 1; // 透明度
        mWindowLayoutParams.width = width;
        mWindowLayoutParams.height = height;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mDragTextView = new TextView(getContext());
        mDragTextView.setText(mTagAdapter.getItem(mDragPosition));
        mDragTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mDragTextView.setBackgroundColor(Color.RED);
        mDragTextView.setGravity(Gravity.CENTER);
        mWindowManager.addView(mDragTextView, mWindowLayoutParams);
    }

    /**
     * 从界面上面移动拖动镜像
     */
    private void removeDragImage() {
        if (mDragTextView != null && mDragTextView.getParent() != null) {
            mWindowManager.removeView(mDragTextView);
            mDragTextView = null;
        }
    }

    /**
     * 拖动item，在里面实现了item镜像的位置更新，item的相互交换以及GridView的自行滚动
     */
    private void onDragItem(int moveX, int moveY) {
        if (mDragTextView != null) {
            mWindowLayoutParams.x = moveX - mPoint2ItemLeft + mOffset2Left;
            mWindowLayoutParams.y = moveY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
            mWindowManager.updateViewLayout(mDragTextView, mWindowLayoutParams); // 更新镜像的位置
        }
        onSwapItem(moveX, moveY);
    }

    private void onSwapItem(int moveX, int moveY) {
        final int tempPosition = findPosByView(findChild(moveX, moveY));
        if (tempPosition < 0 || tempPosition == mDragPosition) {
            return;
        }
        mTagAdapter.setHideItem(tempPosition);
        mTagAdapter.reorderItems(mDragPosition, tempPosition);
        final ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                mDragPosition = tempPosition;
                return true;
            }
        });
    }

    /**
     * 停止拖拽我们将之前隐藏的item显示出来，并将镜像移除
     */
    private void onStopDrag() {
        mTagAdapter.setHideItem(-1);
        removeDragImage();
    }

    private void changeAdapter() {
        removeAllViews();
        TagAdapter adapter = mTagAdapter;
        TagView tagViewContainer = null;
        for (int i = 0; i < adapter.getCount(); i++) {
            boolean hide = (i == adapter.getHidePosition());
            View tagView = adapter.getView(this, i);
            tagViewContainer = new TagView(getContext());
            tagView.setDuplicateParentStateEnabled(true);
            tagViewContainer.setLayoutParams(tagView.getLayoutParams());
            tagViewContainer.addView(tagView);
            addView(tagViewContainer);
            if (hide) {
                tagViewContainer.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();

                mStartDragItemView = findChild(mDownX, mDownY);
                if (mStartDragItemView == null) {
                    return super.dispatchTouchEvent(ev);
                }
                mHandler.postDelayed(mLongClickRunnable, ViewConfiguration.getLongPressTimeout());
                mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();
                mPoint2ItemTop = mDownY - mStartDragItemView.getTop();

                mOffset2Left = (int) (ev.getRawX() - mDownX);
                mOffset2Top = (int) (ev.getRawY() - mDownY);

                mStartDragItemView.setDrawingCacheEnabled(true);
                mDragPosition = findPosByView(mStartDragItemView);
                if (mDragBitmap != null && !mDragBitmap.isRecycled()) {
                    mDragBitmap.recycle();
                }
                mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
                mStartDragItemView.destroyDrawingCache();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                if (!mIsDrag && (Math.abs(moveX - mDownX) > mTouchSlop || Math.abs(moveY - mDownY) > mTouchSlop)
                        && mTagAdapter.isEdit()) {
                    mHandler.removeCallbacks(mLongClickRunnable);
                    mHandler.post(mLongClickRunnable);
                } else if (Math.abs(moveY - mDownY) > mTouchSlop) {// 只要GridView滚动了就移除长按事件
                    mHandler.removeCallbacks(mLongClickRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mHandler.removeCallbacks(mLongClickRunnable);
                mStartDragItemView = null;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsDrag && mDragTextView != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    onDragItem((int) event.getX(), (int) event.getY());
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    onStopDrag();
                    mMotionEvent = MotionEvent.obtain(event);
                    mIsDrag = false;
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        if (mMotionEvent == null) return super.performClick();

        int x = (int) mMotionEvent.getX();
        int y = (int) mMotionEvent.getY();
        mMotionEvent = null;

        TagView child = findChild(x, y);
        int pos = findPosByView(child);
        if (child != null) {
            if (mOnTagClickListener != null) {
                return mOnTagClickListener.onTagClick(child.getTagView(), pos, this);
            }
        }
        return true;
    }

    private int findPosByView(View child) {
        final int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View v = getChildAt(i);
            if (v == child) return i;
        }
        return -1;
    }

    private TagView findChild(int x, int y) {
        final int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            TagView v = (TagView) getChildAt(i);
            if (v.getVisibility() == View.GONE) continue;
            Rect outRect = new Rect();
            v.getHitRect(outRect);
            if (outRect.contains(x, y)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public void onChanged() {
        changeAdapter();
    }
}
