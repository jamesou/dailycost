package com.jamesou.dailycost;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
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
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("/index")) {
                    // Login successfully, close webview and go to main activity
                    Intent intent = new Intent(LoginWebViewActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        clearWebViewData();
        String url = getResources().getString(R.string.web_login_url);
        webView.loadUrl(url);
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