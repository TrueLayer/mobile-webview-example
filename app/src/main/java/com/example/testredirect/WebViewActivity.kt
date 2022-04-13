package com.example.testredirect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.EditText

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView = findViewById(R.id.myCustomWebView)

        // Required to load more than just plain html and css
        val settings: WebSettings = webView.settings
        settings.domStorageEnabled = true
        settings.javaScriptEnabled = true

        val url = intent.getStringExtra("LINK")
        url?.let {
            println("Loading WebView")
            webView.loadUrl(it)
        }
    }
}