package com.android.volley.net;


import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Volley控制类
 */
public class VolleyController {

    private static final String TAG = VolleyController.class.getSimpleName();
    private static Context sContext;
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;
    private LruBitmapCache mLruBitmapCache;
    private Byte[] mLock = new Byte[0];
    private static volatile VolleyController INSTANCE;
    public static int sMaxWidth;
    public static int sMaxHeight;

    private VolleyController() {
    }

    public static void init(Context context) {
        if (context == null) {
            throw new NullPointerException("Volley context can not be null");
        }
        sContext = context;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        sMaxWidth = dm.widthPixels;
        sMaxHeight = dm.heightPixels;
    }

    public static VolleyController getInstance() {
        if (sContext == null) {
            throw new NullPointerException("Volley context can not be null please init");
        }
        if (INSTANCE == null) {
            synchronized (VolleyController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VolleyController();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 拿到一个RequestQueue队列
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            synchronized (mLock) {
                if (mRequestQueue == null) {
                    mRequestQueue = Volley.newRequestQueue(sContext);
                }
            }
        }
        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> Request addToRequestQueue(Request<T> req, String tag) {
        if (null != req) {
            // set the default tag if tag is empty
            req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
            VolleyLog.d("Adding request to queue: %s", req.getUrl());
            return getRequestQueue().add(req);
        } else {
        }
        return null;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            synchronized (mLock) {
                if (mImageLoader == null) {
                    LruBitmapCache lruBitmapCache = getLruBitmapCache();
                    mImageLoader = new ImageLoader(this.mRequestQueue, lruBitmapCache);
                }
            }
        }
        return mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (null == mLruBitmapCache) {
            synchronized (mLock) {
                if (null == mLruBitmapCache) {
                    mLruBitmapCache = new LruBitmapCache(sContext);
                }
            }
        }
        return mLruBitmapCache;
    }

    /**
     * 清空内存中的图片缓存
     * <p/>
     * <strong>Note:<strong/> 该方法清空LruCache中的所有图片，但是内存回收需要一定的时间，内存不会立刻降低
     * <p/>
     */
    public void clearImageCache() {
        if (mLruBitmapCache != null) {
            mLruBitmapCache.evictAll();
        }
    }

//    private static class SingletonHolder {
//        public static final VolleyController INSTANCE = new VolleyController(BrowserApplicationContext.INSTANCE);
//    }


}
