package com.example.testredirect

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


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

        webView.webViewClient = webClient

        val url = intent.getStringExtra("LINK")
        url?.let {
            println("Loading WebView")
            webView.loadUrl("https://payment.truelayer-sandbox.com/test-redirect")
        }
    }

    fun showMessage(message: String?) {
        Toast.makeText(this.applicationContext, message, Toast.LENGTH_SHORT)
    }

    private val webClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            showMessage(url)
        }

        // Seems to be only calling when top level URL Change
        // but not on original load of the WebView ¯\_(ツ)_/¯
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val uri = request?.url
            println("shouldOverrideUrlLoading executing....")
            println(uri)
            if (uri != null && uri.host?.contains("truelayer") ==  false) {
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
                return true
            }
            return false
        }
    }
}