package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.PromoteDubiAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.PromoteDubiDean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/25.
 */

public class PromoteDubiActivity extends BaseActivity {

    @BindView(R.id.promote_dubi_back)
    ImageView promoteDubiBack;
    @BindView(R.id.promote_dubi_gridview)
    GridView promoteDubiGridview;
    List<PromoteDubiDean> list;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_promote_dubi;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        list = new ArrayList<>();
        PromoteDubiDean bean1 = new PromoteDubiDean(R.mipmap.invite_changdu_card,"邀请获畅读卡","邀请好友获畅读卡","+100阅力");
        PromoteDubiDean bean2 = new PromoteDubiDean(R.mipmap.daily_login,"每日登录","登录获取阅力值","已完成 +2");
        PromoteDubiDean bean3 = new PromoteDubiDean(R.mipmap.read_task, "阅读任务", "每天阅读一小时", "+10阅力");
        PromoteDubiDean bean4 = new PromoteDubiDean(R.mipmap.update_collectbook, "上传藏书", "每上传5本藏书", "+20阅力");
        PromoteDubiDean bean5 = new PromoteDubiDean(R.mipmap.contribute_booklist, "创建书单", "每提交审核通过奖励一次", "+50阅力");
        PromoteDubiDean bean6 = new PromoteDubiDean(R.mipmap.contribute_chaidu, "拆读投稿", "每提交审核通过奖励一次", "+200阅力");
        PromoteDubiDean bean7 = new PromoteDubiDean(R.mipmap.contribute_note, "笔记投稿", "每提交审核通过奖励一次", "+50阅力");
        PromoteDubiDean bean8 = new PromoteDubiDean(R.mipmap.recommand_zone, "推荐阅读空间", "推荐上线奖励一次", "+200阅力");
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        list.add(bean5);
        list.add(bean6);
        list.add(bean7);
        list.add(bean8);

        PromoteDubiAdapter adapter = new PromoteDubiAdapter(this,list);
        promoteDubiGridview.setAdapter(adapter);
        promoteDubiGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PromoteDubiDean promoteDubiDean = list.get(position);
                if (promoteDubiDean.getTitle().equals("创建书单") || promoteDubiDean.getTitle().equals("拆读投稿") || promoteDubiDean.getTitle().equals("笔记投稿")){
                    Intent contributeIntent = new Intent(PromoteDubiActivity.this, WelcomeContributeActivity.class);
                    contributeIntent.putExtra("value",promoteDubiDean.getTitle());
                    startActivity(contributeIntent);
                }
            }
        });
    }

    @OnClick(R.id.promote_dubi_back)
    public void onViewClicked() {
        finish();
    }
}
