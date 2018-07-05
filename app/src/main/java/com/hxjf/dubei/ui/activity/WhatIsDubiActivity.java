package com.hxjf.dubei.ui.activity;

import android.content.Context;
import android.view.KeyEvent;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/24.
 */

public class WhatIsDubiActivity extends BaseActivity {
    @BindView(R.id.welcome_contribute_back)
    ImageView welcomeContributeBack;
    @BindView(R.id.dubi_webview)
    WebView dubiWebview;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_what_is_dubi;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        dubiWebview.getSettings().setDatabaseEnabled(true);//开启数据库

        dubiWebview.setFocusable(true);//获取焦点

        dubiWebview.requestFocus();

        String dir =this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();//设置数据库路径

        dubiWebview.getSettings().setCacheMode(dubiWebview.getSettings().LOAD_CACHE_ELSE_NETWORK);//本地缓存

        dubiWebview.getSettings().setBlockNetworkImage(false);//显示网络图像

        dubiWebview.getSettings().setLoadsImagesAutomatically(true);//显示网络图像

        dubiWebview.getSettings().setPluginState(WebSettings.PluginState.ON);//插件支持

        dubiWebview.getSettings().setSupportZoom(false);//设置是否支持变焦

        dubiWebview.getSettings().setJavaScriptEnabled(true);//支持JavaScriptEnabled

        dubiWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//支持JavaScriptEnabled

        dubiWebview.getSettings().setGeolocationEnabled(true);//定位

        dubiWebview.getSettings().setGeolocationDatabasePath(dir);//数据库

        dubiWebview.getSettings().setDomStorageEnabled(true);//缓存 （ 远程web数据的本地化存储）

        WebViewClient myWebViewClient =new WebViewClient();//建立对象

        dubiWebview.setWebViewClient(myWebViewClient);//调用

        dubiWebview.loadUrl("http://test2.17dubei.com/aboutDubi.html");

        dubiWebview.setWebChromeClient(new WebChromeClient() {

//重写WebChromeClient的onGeolocationPermissionsShowPrompt

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {

                callback.invoke(origin, true, false);

                super.onGeolocationPermissionsShowPrompt(origin,callback);

            }

        });
    }

    @OnClick(R.id.welcome_contribute_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && dubiWebview.canGoBack()){
            dubiWebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
