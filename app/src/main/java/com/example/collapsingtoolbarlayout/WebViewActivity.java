package com.example.collapsingtoolbarlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        myWebView = new WebView(getApplicationContext());
//        setContentView(myWebView);
//        myWebView.loadUrl("https://www.google.com");

        setContentView(R.layout.activity_web_view);

        myWebView = findViewById(R.id.webview);
        WebViewClient mWebViewClient = new WebViewClient();
        myWebView.setWebViewClient(mWebViewClient);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("https://www.google.com");
    }
}