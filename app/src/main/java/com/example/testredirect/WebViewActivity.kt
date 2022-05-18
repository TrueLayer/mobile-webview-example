package com.example.testredirect

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.Browser
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.net.URISyntaxException


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
            return shouldOverrideUrlLoadingWithBasicIntent(request)
//             return shouldOverrideUrlLoadingChromeEdition(view, request)
        }

        private fun shouldOverrideUrlLoadingWithBasicIntent(
            request: WebResourceRequest?
        ): Boolean {
            val uri = request?.url
            println("shouldOverrideUrlLoading executing....")
            println(uri)
            if (uri != null && uri.host?.contains("truelayer") == false) {
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
                return true
            }
            return false
        }

        // Based on https://android.googlesource.com/platform/frameworks/webview/+/4dcabae/chromium/java/com/android/webview/chromium/WebViewContentsClientAdapter.java
        private fun shouldOverrideUrlLoadingChromeEdition(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val url = request?.url ?: return false

            // Perform generic parsing of the URI to turn it into an Intent.
            val intent: Intent = try {
                Intent.parseUri(url.toString(), Intent.URI_INTENT_SCHEME)
            } catch (ex: URISyntaxException) {
                print(ex.message)
                return false
            }
            // Sanitize the Intent, ensuring web pages can not bypass browser
            // security (only access to BROWSABLE activities).
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.component = null
            // Pass the package name as application ID so that the intent from the
            // same application can be opened in the same tab.
            intent.putExtra(
                Browser.EXTRA_APPLICATION_ID,
                view!!.context.packageName
            )
            try {
                view.context.startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                print(ex.message)
                return false
            }
            return true
        }
    }
}