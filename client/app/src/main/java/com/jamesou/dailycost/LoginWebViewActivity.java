package com.jamesou.dailycost;
import android.content.Intent;
import android.os.Build;
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
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("index")) {
                    // 登录成功后，关闭当前的WebView Activity
                    // 并启动一个新的Activity
                    Intent intent = new Intent(LoginWebViewActivity.this, MainActivity.class);
                    startActivity(intent);
                    // 关闭当前的Activity
                    finish();
                    return true;
                }
                // 对于其他URL，让WebView加载它们
                return false;
            }
        });
        // 启用JavaScript，因为很多网站依赖它
        webView.getSettings().setJavaScriptEnabled(true);
        //todo保存登录状态，不用重复登录
        clearWebViewData();
        // 加载登录页面
        webView.loadUrl("http://192.168.68.70:8080");
    }
    private void clearWebViewData() {
        // 清除缓存
        webView.clearCache(true);

        // 获取CookieManager并清除所有Cookie
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 当Activity销毁时，清除WebView的缓存和Cookie
        clearWebViewData();
    }
}