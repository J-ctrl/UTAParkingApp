package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class EventsList extends AppCompatActivity {

    //Reference the webview
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        //hook the class with the layout
        mWebView = (WebView) findViewById(R.id.activity_event_webview);


        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.loadUrl("https://www.facebook.com/");

        //Make the url stay inside the app
        mWebView.setWebViewClient(new WebViewClient());
    }
}
