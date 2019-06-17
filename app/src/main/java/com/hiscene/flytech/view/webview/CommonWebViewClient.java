package com.hiscene.flytech.view.webview;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.hiscene.flytech.ui.WebActivity;


public class CommonWebViewClient extends android.webkit.WebViewClient {


    private AppCompatActivity mActivity;

    public CommonWebViewClient(AppCompatActivity activity) {

        mActivity = activity;
    }

    @Override
    public void onLoadResource(WebView view, String url) {

        super.onLoadResource(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {


        if (url != null && url.startsWith("orpheus")) {
            return true;
        }
        if (url != null && url.startsWith("http")) {
            WebActivity.start(mActivity, url);
            return true;
        }
        return true;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {

        super.onPageFinished(view, url);
        ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(view.getTitle());
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        super.onReceivedError(view, errorCode, description, failingUrl);
    }

}
