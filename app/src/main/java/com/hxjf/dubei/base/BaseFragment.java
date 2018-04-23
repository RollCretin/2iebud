package com.hxjf.dubei.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hxjf.dubei.R;

/**
 * Created by Chen_Zhang on 2017/5/19.
 */

public abstract class BaseFragment extends Fragment {

    private FrameLayout fragmentContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, null);
        fragmentContent = (FrameLayout) view.findViewById(R.id.fragment_content);
        return view;
    }

    /**
     * 页面加载完成之后开始请求数据
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startloadData();
    }

    public abstract void startloadData();

    //子类必须实现--请求数据成功创建的视图
    public abstract View onCreateContentView();


    //网络加载成功的回调
    protected void onDataLoadedSuccess() {
        fragmentContent.addView(onCreateContentView());
    }

    //网络加载失败的回调
    protected void onDataLoadFailed() {

    }

}
