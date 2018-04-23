package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.TagBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.widget.AddLabelDialog;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/6/19.
 */

public class MyLabelActivity extends BaseActivity {
    @BindView(R.id.label_back)
    ImageView labelBack;
    @BindView(R.id.label_save)
    TextView labelSave;
    @BindView(R.id.label_select)
    TextView labelSelect;
    @BindView(R.id.label_tagfl)
    TagFlowLayout labelTagfl;
    @BindView(R.id.label_add)
    TextView labelAdd;
    private ArrayList<String> list;
    private StringBuilder sb;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_label;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        String str = "选择<font color = '#000000'> 3</font> 个最合适您的标签";
        labelSelect.setText(Html.fromHtml(str));
        list = new ArrayList<>();
        //获取标签数据
        Call<TagBean> tagCall = ReaderRetroift.getInstance(this).getApi().getTagBean();
        tagCall.enqueue(new Callback<TagBean>() {
            @Override
            public void onResponse(Call<TagBean> call, Response<TagBean> response) {
                TagBean tagBean = response.body();
                if (tagBean!=null){
                    List<TagBean.ResponseDataBean> responseData = tagBean.getResponseData();
                    for (TagBean.ResponseDataBean bean : responseData) {
                        list.add(bean.getTagName());
                    }
                }
                labelTagfl.setAdapter(new TagAdapter<String>(list) {

                    @Override
                    public View getView(FlowLayout flowLayout, int i, String o) {
                        TextView tv = (TextView) View.inflate(MyLabelActivity.this, R.layout.label_tv, null);
                        tv.setText(o);
                        return tv;
                    }
                });
            }

            @Override
            public void onFailure(Call<TagBean> call, Throwable t) {

            }
        });

    }


    @OnClick({R.id.label_back, R.id.label_save,R.id.label_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.label_back:
                finish();
                break;
            case R.id.label_save:
                save_label();
                break;
            case R.id.label_add:
                add_label();
                break;
        }
    }

    private void save_label() {
        sb = new StringBuilder();
        Set<Integer> set = labelTagfl.getSelectedList();
        Iterator<Integer> it = set.iterator();
        while(it.hasNext()){
            Integer i = it.next();
            String str = list.get(i);
                sb.append(str+"、");
        }
        if (sb.toString().length() == 0){
            Toast.makeText(this, "未选择标签", Toast.LENGTH_SHORT).show();
        }else{
            final String newStr = sb.substring(0, sb.toString().length() - 1);
            //保存标签
            Call<ModifyBean> saveTagCall = ReaderRetroift.getInstance(this).getApi().getSaveTagBean(sb.toString());
            saveTagCall.enqueue(new Callback<ModifyBean>() {
                @Override
                public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                    ModifyBean bean = response.body();
                    if (bean != null) {
                        Toast.makeText(MyLabelActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("label", newStr);
                        MyLabelActivity.this.setResult(RESULT_OK, intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ModifyBean> call, Throwable t) {

                }
            });

        }
    }

    private void add_label() {
        //自定义dialog
        final AddLabelDialog dialog = new AddLabelDialog(this);
        dialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newlabel = dialog.getEditTextString();
                if (newlabel == null || newlabel.length() == 0){
                    Toast.makeText(MyLabelActivity.this, "标签名不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    list.add(newlabel);
                    dialog.dismiss();
                    //更新界面
                    labelTagfl.setAdapter(new TagAdapter<String>(list) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, String o) {
                            TextView tv = (TextView) View.inflate(MyLabelActivity.this, R.layout.label_tv, null);
                            tv.setText(o);
                            return tv;
                        }
                    });

                    //上传至服务端
                    Call<ModifyBean> addTagCall = ReaderRetroift.getInstance(MyLabelActivity.this).getApi().getaddTagBean(newlabel);
                    addTagCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean bean = response.body();
                            if (bean != null){
                                String responseMsg = bean.getResponseMsg();
                            }
                        }

                        @Override
                        public void onFailure(Call<ModifyBean> call, Throwable t) {

                        }
                    });
                }
            }
        });
        dialog.show();
    }
}
