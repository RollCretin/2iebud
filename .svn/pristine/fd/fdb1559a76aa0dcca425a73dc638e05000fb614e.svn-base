package com.hxjf.dubei.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.google.gson.Gson;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.CheckVersionBean;
import com.hxjf.dubei.bean.UserBean;
import com.hxjf.dubei.network.ImLoginHelper;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.fragment.BookshelfFragment;
import com.hxjf.dubei.ui.fragment.DiscoveryFragment;
import com.hxjf.dubei.ui.fragment.HomeFragment;
import com.hxjf.dubei.ui.fragment.MeFragment;
import com.hxjf.dubei.ui.fragment.ZoneFragment;
import com.hxjf.dubei.util.NetUtils;
import com.hxjf.dubei.util.PermissionUtil;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.util.StatusBarUtils;
import com.hxjf.dubei.util.UpdateUtils;
import com.hxjf.dubei.widget.CommonProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_PERMISSION_CODE_TAKE_PIC = 100;
    private static final int REQUEST_PERMISSION_SEETING = 200;
    private static final String DOWNLOAD_NAME = "DuBei";
    @BindView(R.id.action_bar_container)
    FrameLayout actionBarContainer;
    @BindView(R.id.frag_container)
    FrameLayout fragContainer;
    @BindView(R.id.tab_bookshelf)
    RadioButton tabBookshelf;
    @BindView(R.id.tab_discovery)
    RadioButton tabDiscovery;
    @BindView(R.id.tab_me)
    RadioButton tabMe;
    @BindView(R.id.tab_container)
    RadioGroup tabContainer;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;

    private Fragment oldFragment = new Fragment();
    List<Fragment> fragmentList;
    private FragmentTransaction fragmentTransaction;
    private Fragment mFrag;
    private String id;
    private UserBean userbean;
    private String[] mPermissionList;
    private String currentVersion;
    private String updateLog;
    private String newVersion;
    private String apkFileUrl;
    private String targetSize;
    private CommonProgressDialog pBar;

    long exitTime = 0;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        //状态栏沉浸
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //设置状态栏的颜色
        StatusBarUtils.setWindowStatusBarColor(MainActivity.this,R.color.note_bg);

        //权限的申请
        mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        requePermiss();

        String bindbeanStr = SPUtils.getString(this, "bindbean", "");
        Gson gson = new Gson();
        userbean = gson.fromJson(bindbeanStr, UserBean.class);
        //登录阿里账号
        if (userbean != null && userbean.getResponseData() != null) {
            loginIM();
        }

        fragmentList = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        BookshelfFragment bookshelfFragment = new BookshelfFragment();
        DiscoveryFragment discoveryFragment = new DiscoveryFragment();
        ZoneFragment zoneFragment = new ZoneFragment(); //新发现
        MeFragment meFragment = new MeFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(bookshelfFragment);
        fragmentList.add(discoveryFragment);
        fragmentList.add(zoneFragment);
        fragmentList.add(meFragment);

        // 获取本版本号，是否更新

        currentVersion = UpdateUtils.getVersion(this);
        Call<CheckVersionBean> checkVersionCall = ReaderRetroift.getInstance(MainActivity.this).getApi().checkVersionCall();
        checkVersionCall.enqueue(new Callback<CheckVersionBean>() {
            @Override
            public void onResponse(Call<CheckVersionBean> call, Response<CheckVersionBean> response) {
                CheckVersionBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    CheckVersionBean.ResponseDataBean dataBean = bean.getResponseData();
                    //HTML
                    updateLog = dataBean.getUpdateLog();
                    newVersion = dataBean.getNewVersion();//样式：1.0.1
                    apkFileUrl = dataBean.getApkFileUrl();
                    //字节
                    targetSize = dataBean.getTargetSize();

                    if (currentVersion != null && !currentVersion.equals(newVersion)) {
                        // 版本号不同
                        ShowDialog(currentVersion, newVersion, updateLog, apkFileUrl);
                    }
                    SPUtils.putBoolean(MainActivity.this,"searchSwitch",dataBean.isSwitchFlag());
                }
            }

            @Override
            public void onFailure(Call<CheckVersionBean> call, Throwable t) {

            }
        });

            //显示首页
//        changeFragment(new HomeFragment());
            loadFragment(0);

        //ViewGroup设置监听事件
        tabContainer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tab_challenge:
                        loadFragment(0);
                        break;
                    case R.id.tab_bookshelf:
                        loadFragment(1);
                        break;
                    case R.id.tab_home:
                        loadFragment(2);
                        break;
                    case R.id.tab_discovery:
                        loadFragment(3);
                        break;
                    case R.id.tab_me:
                        loadFragment(4);
                        break;
                }
               /* //fragment界面切换
                fragContainer.removeAllViews();
                Fragment newFragment = FragmentFactory.getInstance().getFragment(checkedId);
                changeFragment(newFragment);*/

            }
        });
    }

    /**
     * 升级系统
     *
     * @param content
     * @param url
     */
    private void ShowDialog(String vision, final String newversion, String content,
                            final String url) {
        AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                .setTitle("版本更新")
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (NetUtils.isWifi(MainActivity.this)) {
                            pBar = new CommonProgressDialog(MainActivity.this);
                            pBar.setCanceledOnTouchOutside(false);
                            pBar.setTitle("正在下载");
                            pBar.setCustomTitle(LayoutInflater.from(
                                    MainActivity.this).inflate(
                                    R.layout.title_dialog, null));
                            pBar.setMessage("正在下载");
                            pBar.setIndeterminate(true);
                            pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            pBar.setCancelable(true);
                            // downFile(URLData.DOWNLOAD_URL);
                            final DownloadTask downloadTask = new DownloadTask(
                                    MainActivity.this);
                            downloadTask.execute(url);
                            pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true);
                                }
                            });
                        } else {
                            //无wifi状态下
                            final AlertDialog noWifiDialog = new android.app.AlertDialog.Builder(MainActivity.this)
                                    .setTitle("提示")
                                    .setMessage("当前无WIFI连接，是否更新")
                                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            pBar = new CommonProgressDialog(MainActivity.this);
                                            pBar.setCanceledOnTouchOutside(false);
                                            pBar.setTitle("正在下载");
                                            pBar.setCustomTitle(LayoutInflater.from(
                                                    MainActivity.this).inflate(
                                                    R.layout.title_dialog, null));
                                            pBar.setMessage("正在下载");
                                            pBar.setIndeterminate(true);
                                            pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                            pBar.setCancelable(true);
                                            // downFile(URLData.DOWNLOAD_URL);
                                            final DownloadTask downloadTask = new DownloadTask(
                                                    MainActivity.this);
                                            downloadTask.execute(url);
                                            pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                @Override
                                                public void onCancel(DialogInterface dialog) {
                                                    downloadTask.cancel(true);
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                            noWifiDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                            noWifiDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
    }

    /**
     * 下载应用
     *
     * @author Administrator
     */
    class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            File file = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error
                // report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file = new File(Environment.getExternalStorageDirectory(),
                            DOWNLOAD_NAME);

                    if (!file.exists()) {
                        // 判断父文件夹是否存在
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                    }

                } else {
                    Toast.makeText(MainActivity.this, "sd卡未挂载",
                            Toast.LENGTH_LONG).show();
                }
                input = connection.getInputStream();
                output = new FileOutputStream(file);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);

                }
            } catch (Exception e) {
                System.out.println(e.toString());
                return e.toString();

            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            pBar.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            pBar.setIndeterminate(false);
            pBar.setMax(100);
            pBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            pBar.dismiss();
            //权限的申请
            update();
        }
    }

    private void update() {
        //安装应用
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(MainActivity.this, "com.hxjf.dubei.fileprovider", new File(Environment
                    .getExternalStorageDirectory(), DOWNLOAD_NAME));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(
                    Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), DOWNLOAD_NAME)),
                    "application/vnd.android.package-archive");
        }

        startActivity(intent);
    }


    private void requePermiss() {
        PermissionUtil.checkPermission(this, actionBarContainer, mPermissionList, REQUEST_PERMISSION_CODE_TAKE_PIC, new PermissionUtil.permissionInterface() {
            @Override
            public void success() {
                //做相应的处理
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE_TAKE_PIC) {
            if (PermissionUtil.verifyPermissions(grantResults)) {//有权限
                //TODO
            } else {
                //没有权限
                if (!PermissionUtil.shouldShowPermissions(this, permissions)) {//这个返回false 表示勾选了不再提示
                    Snackbar.make(actionBarContainer, "请去设置界面设置权限",
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction("去设置", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, REQUEST_PERMISSION_SEETING);
                                }
                            })
                            .show();
                } else {
                    //表示没有权限 ,但是没勾选不再提示
                    Snackbar.make(actionBarContainer, "请允许权限请求！",
                            Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    ActivityCompat
                                            .requestPermissions(MainActivity.this, mPermissionList,
                                                    REQUEST_PERMISSION_CODE_TAKE_PIC);
                                }
                            })
                            .show();
                    requePermiss();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果是从设置界面返回,就继续判断权限
        if (requestCode == REQUEST_PERMISSION_SEETING) {
            requePermiss();
        }
    }

    private void loadFragment(int position) {
        //从集合中获取相对序号的Fragment
        Fragment f = fragmentList.get(position);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//首先判断mFrag 是否为空，如果不为，先隐藏起来，接着判断从List 获取的Fragment是否已经添加到Transaction中，如果未添加，添加后显示，如果已经添加，直接显示
        if (mFrag != null) {
            fragmentTransaction.hide(mFrag);
        }
        if (!f.isAdded()) {
            fragmentTransaction.add(R.id.frag_container, f);

        } else {
            fragmentTransaction.show(f);
        }
//将获取的Fragment 赋值给声明的Fragment 中，提交
        mFrag = f;
        fragmentTransaction.commit();
    }

    private void loginIM() {
        //获取SDK对象
        ImLoginHelper.getInstance().initIMKit(userbean.getResponseData().getId());
        YWIMKit imKit = ImLoginHelper.getInstance().getIMKit();
        //登录
        IYWLoginService loginService = imKit.getLoginService();
        YWLoginParam loginParam = YWLoginParam.createLoginParam(userbean.getResponseData().getId(), userbean.getResponseData().getPassword());
        loginService.login(loginParam, new IWxCallback() {
            @Override
            public void onSuccess(Object... objects) {
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int i) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if((System.currentTimeMillis()- exitTime)>2000){
            Toast.makeText(MainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }


}
