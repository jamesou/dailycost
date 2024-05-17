package com.jamesou.dailycost;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
public class LoginWebViewActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_webview);
        webView = findViewById(R.id.login_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
//        clearWebViewData();
        String url = getResources().getString(R.string.web_login_url);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient());
    }
    private void clearWebViewData() {
        webView.clearCache(true);
        //clear cookie
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewData();
    }
}