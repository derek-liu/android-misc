package com.example.derek.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import com.example.derek.R;

/**
 * Created by liudingyu on 14/12/10.
 */
public class BrowserActivity extends Activity {

    protected WebView mWebview = null;
    protected Button mLoadBtn = null;
    protected Button mStopBtn = null;
    protected Button m163Btn = null;
    protected final static String URL = "http://carleolee.net/d/i.php";
    private MyWebViewClient mWebViewClient = new MyWebViewClient();
    private MyWebChromeClient mWebChromeClient = new MyWebChromeClient();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser_activity_layout);
        initView();
    }

    private void initView() {
        mWebview = (WebView) findViewById(R.id.webview);
        mWebview.setWebChromeClient(mWebChromeClient);
        mWebview.setWebViewClient(mWebViewClient);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mLoadBtn = (Button) findViewById(R.id.load);
        mLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebview != null) {
                    mWebview.loadUrl(URL);
                }
            }
        });
        mStopBtn = (Button) findViewById(R.id.stop);
        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebview != null) {
                    mWebview.stopLoading();
                }
            }
        });
        m163Btn = (Button) findViewById(R.id.jump163);
        m163Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebview != null) {
                    mWebview.loadUrl("http://3g.163.com");
                }
            }
        });
    }

    private static class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d("k.k", "onPageStarted " + " geturl " + view.getUrl() + " originUrl " + view.getOriginalUrl() + " url " + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d("k.k", "onPageFinished " + " geturl " + view.getUrl() + " originUrl " + view.getOriginalUrl() + " url " + url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("k.k", "shouldOverrideUrlLoading " + " geturl " + view.getUrl() + " originUrl " + view.getOriginalUrl() + " url " + url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            Log.d("k.k", "doUpdateVisitedHistory " + " geturl " + view.getUrl() + " originUrl " + view.getOriginalUrl() + " url " + url);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebview != null && mWebview.canGoBack()) {
            mWebview.goBack();
        }
        super.onBackPressed();
    }

    private static class MyWebChromeClient extends WebChromeClient {

    }
}