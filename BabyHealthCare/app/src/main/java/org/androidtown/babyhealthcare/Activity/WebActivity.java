
package org.androidtown.babyhealthcare.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.androidtown.babyhealthcare.R;

import dmax.dialog.SpotsDialog;

import static org.androidtown.babyhealthcare.Const.IntentConst.WEB_URL;


public class WebActivity extends AppCompatActivity {

    WebView webView;
    AlertDialog alertDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = (WebView)findViewById(R.id.web_view);
        alertDialog = new SpotsDialog.Builder().setContext(this).build();
        alertDialog.show();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                alertDialog.dismiss();
            }
        });

        if (getIntent() != null) {
            if (!getIntent().getStringExtra(WEB_URL).isEmpty()) {
                webView.loadUrl(getIntent().getStringExtra(WEB_URL));
            }
        }

    }
}
