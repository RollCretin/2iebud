package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.BSBookAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.BSBookListBean;
import com.hxjf.dubei.bean.HotBookBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.GlideLoadUtils;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.widget.MyGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/7/29.
 */

public class ChallegeBookActivity extends BaseActivity {
    private static final int TYPE_CHALLENGE_DETAIL = 200;
    @BindView(R.id.challege_book_search)
    ImageView challegeBookSearch;
    @BindView(R.id.challege_book_return)
    ImageView challegeBookReturn;
    @BindView(R.id.challege_book_container_book)
    LinearLayout challegeBookContainerBook;
    @BindView(R.id.discovery_sv_book_list)
    HorizontalScrollView discoverySvBookList;
    @BindView(R.id.challege_book_gv)
    MyGridView challegeBookGv;
    @BindView(R.id.challege_book_ll_hotbook)
    LinearLayout challegeBookLlHotbook;

    private List<HotBookBean.ResponseDataBean.ListBean> hotBookList;
    private List<BSBookListBean.ResponseDataBean> booklist;
    private boolean searchSwitch;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_challege_book;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        searchSwitch = SPUtils.getBoolean(ChallegeBookActivity.this, "searchSwitch", false);
        if (searchSwitch){
            challegeBookSearch.setVisibility(View.VISIBLE);
        }
        Call<HotBookBean> hotBookCall = ReaderRetroift.getInstance(this).getApi().hotBookCall(0, 10);
        hotBookCall.enqueue(new Callback<HotBookBean>() {
            @Override
            public void onResponse(Call<HotBookBean> call, Response<HotBookBean> response) {
                HotBookBean bean = response.body();
                if (bean != null) {
                    hotBookList = bean.getResponseData().getList();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<HotBookBean> call, Throwable t) {

            }
        });
    }

    private void initData() {
        //热门书籍
        if (hotBookList.size() == 0 || searchSwitch == false){
            challegeBookLlHotbook.setVisibility(View.GONE);
            challegeBookContainerBook.setVisibility(View.GONE);
        }else {
            challegeBookLlHotbook.setVisibility(View.VISIBLE);
            challegeBookContainerBook.setVisibility(View.VISIBLE);

        }
        for (int i = 0; i < hotBookList.size(); i++) {
            final HotBookBean.ResponseDataBean.ListBean bean = hotBookList.get(i);

            View child = View.inflate(this, R.layout.item_discovery_book, null);
            ImageView ivimage = (ImageView) child.findViewById(R.id.discovery_book_image);
            TextView tvname = (TextView) child.findViewById(R.id.discovery_book_name);
            GlideLoadUtils.getInstance().glideLoad(this, ReaderRetroift.IMAGE_URL + bean.getCoverPath(), ivimage, 0);
            tvname.setText(bean.getName());
            //item设置外边距
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(30, 50, 0, 50);
            param.gravity = Gravity.TOP;

            child.setLayoutParams(param);
            final int finalI = i;
            // item设置点击事件
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailIntent = new Intent(ChallegeBookActivity.this, SelectBookActivity.class);
                    detailIntent.putExtra("bookid", bean.getId());
                    startActivity(detailIntent);
                }
            });
            challegeBookContainerBook.addView(child);
        }

        LinearLayout.LayoutParams endview_param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        endview_param.setMargins(30, 50, 0, 50);
        endview_param.gravity = Gravity.TOP;
        View endView = View.inflate(this, R.layout.item_challenge_book_end, null);
        endView.setLayoutParams(endview_param);
        endView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChallegeBookActivity.this, MoreBookActivity.class);
                startActivity(intent);
            }
        });
        challegeBookContainerBook.addView(endView);

        //我的书架--书籍
        Call<BSBookListBean> bsBookListCall = ReaderRetroift.getInstance(this).getApi().BSBookListCall();
        bsBookListCall.enqueue(new Callback<BSBookListBean>() {
            @Override
            public void onResponse(Call<BSBookListBean> call, Response<BSBookListBean> response) {
                BSBookListBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    booklist = bean.getResponseData();
                    BSBookAdapter myBookAdapter = new BSBookAdapter(challegeBookGv, booklist, ChallegeBookActivity.this);
                    challegeBookGv.setAdapter(myBookAdapter);
                    challegeBookGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (booklist.get(position).getBookInfo() == null || booklist.get(position).getBookInfo().getPath() == null || "".equals(booklist.get(position).getBookInfo().getPath()) || booklist.get(position).getBookInfo().getStatus() == 0) {
                                Toast.makeText(ChallegeBookActivity.this, "该书已下架", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(ChallegeBookActivity.this, BookDetailActivity.class);
                                intent.putExtra("bookid", booklist.get(position).getBookId());
                                intent.putExtra("type", TYPE_CHALLENGE_DETAIL);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<BSBookListBean> call, Throwable t) {

            }
        });


    }

    @OnClick({R.id.challege_book_search, R.id.challege_book_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.challege_book_search:
                Intent intent = new Intent(ChallegeBookActivity.this, SearchBookActivity.class);
                startActivity(intent);
                break;
            case R.id.challege_book_return:
                finish();
                break;
        }
    }

}
