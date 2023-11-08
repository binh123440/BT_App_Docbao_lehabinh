package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewsItemActivity extends AppCompatActivity {

    TextView tv_des;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);

        webView = findViewById(R.id.webView);
        Intent intent = getIntent();
        if (intent != null)
        {
            String link = intent.getStringExtra("link");
            webView.loadUrl(link);
        }
    }
}