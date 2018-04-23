package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.util.ADFilterTool;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen_Zhang on 2017/8/24.
 */

public class agreementActivity extends BaseActivity {
    @BindView(R.id.webview_name)
    TextView webviewName;
    @BindView(R.id.webview_back)
    ImageView webviewBack;
    @BindView(R.id.webview_content)
    WebView webviewContent;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_agreement;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        if (("http://test1.17dubei.com/agreement.html").equals(url)){
            webviewName.setText("用户协议");
        }else if (("http://test1.17dubei.com/rule.html").equals(url)){
            webviewName.setText("挑战规则");
        }else if (("http://test1.17dubei.com/getChangCard.html").equals(url)){
            webviewName.setText("领取畅读卡");
        }

        webviewContent.loadUrl(url);

        webviewContent.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                url= url.toLowerCase();
                if(!ADFilterTool.hasAd(agreementActivity.this,url)){
                    return super.shouldInterceptRequest(view,url);//正常加载
                }else{
                    return new WebResourceResponse(null,null,null);//含有广告资源屏蔽请求
                }
            }
        });


        WebSettings webSettings = webviewContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);// support zoom
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
    }


    @OnClick(R.id.webview_back)
    public void onClick() {
        finish();
    }
}
