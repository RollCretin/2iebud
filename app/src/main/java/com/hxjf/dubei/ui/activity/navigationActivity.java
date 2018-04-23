package com.hxjf.dubei.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.util.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Chen_Zhang on 2017/12/28.
 */

public class navigationActivity extends BaseActivity {
    @BindView(R.id.navigation_webview)
    WebView navigationWebview;
    private String oringin;
    private String dest;
    private String ownerName;
    private String address;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_navigation;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String loc = SPUtils.getString(this, "location", "114.07/22.62");
        String replace = loc.replace("/", ",");
        String[] split = replace.split(",");
        StringBuffer originsb = new StringBuffer();
        for (int i = 0; i < split.length; i++) {
            originsb.append(split[split.length-1-i]+",");
        }
        oringin = originsb.deleteCharAt(originsb.length() - 1).toString();
        dest = intent.getStringExtra("dest");
        String[] split1 = dest.split(",");
        StringBuffer destsb = new StringBuffer();
        for (int i = 0; i < split1.length; i++) {
            destsb.append(split1[split1.length-1-i]+",");
        }
        String destStr = destsb.deleteCharAt(destsb.length() - 1).toString();

        ownerName = intent.getStringExtra("ownerName");
        address = intent.getStringExtra("address");

        navigationWebview.getSettings().setDatabaseEnabled(true);//开启数据库

        navigationWebview.setFocusable(true);//获取焦点

        navigationWebview.requestFocus();

        String dir =this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();//设置数据库路径

        navigationWebview.getSettings().setCacheMode(navigationWebview.getSettings().LOAD_CACHE_ELSE_NETWORK);//本地缓存

        navigationWebview.getSettings().setBlockNetworkImage(false);//显示网络图像

        navigationWebview.getSettings().setLoadsImagesAutomatically(true);//显示网络图像

        navigationWebview.getSettings().setPluginState(WebSettings.PluginState.ON);//插件支持

        navigationWebview.getSettings().setSupportZoom(false);//设置是否支持变焦

        navigationWebview.getSettings().setJavaScriptEnabled(true);//支持JavaScriptEnabled

        navigationWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//支持JavaScriptEnabled

        navigationWebview.getSettings().setGeolocationEnabled(true);//定位

        navigationWebview.getSettings().setGeolocationDatabasePath(dir);//数据库

        navigationWebview.getSettings().setDomStorageEnabled(true);//缓存 （ 远程web数据的本地化存储）

        WebViewClient myWebViewClient =new WebViewClient();//建立对象

        navigationWebview.setWebViewClient(myWebViewClient);//调用

        navigationWebview.loadUrl("http://api.map.baidu.com/marker?location="+destStr+"&title="+ownerName+"&content="+address+"&output=html&src=dubei|dubei");//百度地图地址
        Log.d("navigation...", "init: replace:"+oringin+";destination:"+ destStr +";name:"+ ownerName);

        navigationWebview.setWebChromeClient(new WebChromeClient() {

//重写WebChromeClient的onGeolocationPermissionsShowPrompt

public void onGeolocationPermissionsShowPrompt(String origin,

                    GeolocationPermissions.Callback callback) {

                callback.invoke(origin, true, false);

                super.onGeolocationPermissionsShowPrompt(origin,callback);

            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && navigationWebview.canGoBack()){
            navigationWebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
