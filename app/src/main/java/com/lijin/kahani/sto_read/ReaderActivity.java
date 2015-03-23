package com.lijin.kahani.sto_read;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class ReaderActivity extends ActionBarActivity {
    private WebView mWebView;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {

                String js = "javascript:function initialize() { " +
                        "var d = document.getElementsByTagName('body')[0];" +
                        "var ourH = window.innerHeight; " +
                        "var ourW = window.innerWidth; " +
                        "var fullH = d.offsetHeight; " +
                        "var pageCount = Math.floor(fullH/ourH)+1;" +
                        "var currentPage = 0; " +
                        "var newW = pageCount*ourW; " +
                        "d.style.height = ourH+'px';" +
                        "d.style.width = newW+'px';" +
                        "d.style.webkitColumnGap = '2px'; " +
                        "d.style.margin = 0; " +
                        "d.style.webkitColumnCount = pageCount;" +
                        "}";
                mWebView.loadUrl(js);
                mWebView.loadUrl("javascript:initialize()");

            }
        });
        Intent intent=getIntent();

        mWebView.loadData(intent.getStringExtra("STORY"), "text/html; charset=UTF-8", null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
