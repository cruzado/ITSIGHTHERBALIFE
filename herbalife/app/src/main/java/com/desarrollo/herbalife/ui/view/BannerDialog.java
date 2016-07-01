package com.desarrollo.herbalife.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.desarrollo.herbalife.R;

/**
 * Created by oswaldo on 26/01/16.
 */
public class BannerDialog {
    Context context;

    public BannerDialog(Context context){
        this.context = context;
    }


    public Dialog buildImage(int res, View.OnClickListener listener, String url) {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(res);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageView = (ImageView) dialog.findViewById(R.id.imgBanner);
        Button accept = (Button) dialog.findViewById(R.id.accept);

        if (accept != null) {
            accept.setOnClickListener(listener);
        }
        return dialog;
    }

    public Dialog buildWeb(int res, View.OnClickListener listener, String mUrl) {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(res);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WebView mWebView = (WebView) dialog.findViewById(R.id.webView);
        mWebView.getSettings().setBuiltInZoomControls(true);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebClient());
        mWebView.loadUrl(mUrl);

        Button accept = (Button) dialog.findViewById(R.id.accept);

        if (accept != null) {
            accept.setOnClickListener(listener);
        }
        return dialog;
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

}
