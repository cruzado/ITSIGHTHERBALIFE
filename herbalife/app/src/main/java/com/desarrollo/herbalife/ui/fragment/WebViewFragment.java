package com.desarrollo.herbalife.ui.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.desarrollo.herbalife.R;
import com.desarrollo.herbalife.common.BaseFragment;
import com.desarrollo.herbalife.common.BasePresenter;
import com.desarrollo.herbalife.ui.activity.LoginActivity;
import com.desarrollo.herbalife.ui.activity.MenuActivity;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by Emily Lope Chavez.
 */

public class WebViewFragment extends BaseFragment {

    private String mUrl;


    @Bind(R.id.webView)
    WebView mWebView;

    @Bind(R.id.accept)
    Button btnAccept;

    public static WebViewFragment newInstance(String url){
        WebViewFragment fragment = new WebViewFragment();
        fragment.setUrl(url);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_web;
    }

    @Override
    public boolean hasOptionsMenuEnable() {
        return false;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void setupView(View view) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebClient());
        mWebView.loadUrl(mUrl);
        Typeface faceBold=Typeface.createFromAsset(getContext().getAssets(),"fonts/Gotham-Bold.ttf");
        btnAccept.setTypeface(faceBold);
    }

    @Override
    protected void onRestoreView(Bundle savedInstanceState) {
        mUrl = savedInstanceState.getString("bannerUrl");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebClient());
        mWebView.loadUrl(mUrl);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("bannerUrl", mUrl);

    }

    private class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    public void setUrl(String url){
        mUrl = url;
    }

    @OnClick(R.id.accept)
    public void onAccept(){
        MenuActivity menuActivity = (MenuActivity)getActivity();
        menuActivity.closeBanner();
    }
}
