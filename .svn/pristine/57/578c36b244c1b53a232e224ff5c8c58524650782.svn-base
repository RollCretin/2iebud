package com.hxjf.dubei.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.hxjf.dubei.R;

import cn.bingoogolapple.progressbar.BGAProgressBar;

/**
 * Created by Chen_Zhang on 2017/11/2.
 */

public class DownloadProgressDialog extends Dialog {
    Context mContext;
    private BGAProgressBar progressbar;
    private static DownloadProgressDialog progressDialog;

    public DownloadProgressDialog(Context context) {
        super(context);
        this.mContext = context;
        setCustomDialog();
    }

    private void setCustomDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_download_progress, null);
        progressbar = (BGAProgressBar) view.findViewById(R.id.dialog_download_progress);
        super.setContentView(view);
    }

    public void setProgress(int Max,int progress){
        progressbar.setMax(Max);
        progressbar.setProgress(progress);
    }


}
