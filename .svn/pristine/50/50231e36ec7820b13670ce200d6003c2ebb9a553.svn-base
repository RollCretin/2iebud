package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.BookTagBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.network.ReaderRetroift;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/7/6.
 */

public class SelectLabelActivity extends BaseActivity {
    @BindView(R.id.label_amount)
    TextView labelAmount;
    @BindView(R.id.cb_label_hulianwang)
    CheckBox cbLabelHulianwang;
    @BindView(R.id.cb_label_jinguan)
    CheckBox cbLabelJinguan;
    @BindView(R.id.cb_label_tourongzi)
    CheckBox cbLabelTourongzi;
    @BindView(R.id.cb_label_shangye)
    CheckBox cbLabelShangye;
    @BindView(R.id.cb_label_yinxiao)
    CheckBox cbLabelYinxiao;
    @BindView(R.id.cb_label_zhichang)
    CheckBox cbLabelZhichang;
    @BindView(R.id.cb_label_jinrong)
    CheckBox cbLabelJinrong;
    @BindView(R.id.cb_label_keji)
    CheckBox cbLabelKeji;
    @BindView(R.id.cb_label_xinlixue)
    CheckBox cbLabelXinlixue;
    @BindView(R.id.cb_label_chuangye)
    CheckBox cbLabelChuangye;
    @BindView(R.id.cb_label_lishi)
    CheckBox cbLabelLishi;
    @BindView(R.id.cb_label_lizhi)
    CheckBox cbLabelLizhi;
    @BindView(R.id.cb_label_zhexue)
    CheckBox cbLabelZhexue;
    @BindView(R.id.cb_label_zhuanji)
    CheckBox cbLabelZhuanji;
    @BindView(R.id.cb_label_xiaoshuo)
    CheckBox cbLabelXiaoshuo;
    @BindView(R.id.label_btn_selected_done)
    Button labelBtnSelectedDone;

    private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
    HashMap<String,CheckBox> chexkBoxMap = new HashMap<>();
    int selectednum = 0;
    private List<BookTagBean.ResponseDataBean> responseDataList;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_select_label;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);

        Call<BookTagBean> bookTagCall = ReaderRetroift.getInstance(this).getApi().getBookTagCall();
       bookTagCall.enqueue(new Callback<BookTagBean>() {
            @Override
            public void onResponse(Call<BookTagBean> call, Response<BookTagBean> response) {
                ResponseBody responseBody = response.errorBody();
                BookTagBean bean = response.body();
                if (bean != null && bean.getResponseCode() == 1){
                    responseDataList = bean.getResponseData();
                }
            }

            @Override
            public void onFailure(Call<BookTagBean> call, Throwable t) {

            }
        });
/*
        for (int i = 0; i <responseDataList.size() ; i++) {
            BookTagBean.ResponseDataBean dataBean = responseDataList.get(i);
       *//* }
        for (BookTagBean.ResponseDataBean databean:responseDataList) {*//*
            switch (dataBean.getCode()){
                case "1"://经管
                    chexkBoxMap.put("1",cbLabelJinguan);
                    break;
                case "2"://营销
                    chexkBoxMap.put("2",cbLabelYinxiao);
                    break;
                case "3"://互联网
                    chexkBoxMap.put("3",cbLabelHulianwang);
                    break;
                case "4"://职场
                    chexkBoxMap.put("4",cbLabelZhichang);
                    break;
                case "5"://传记
                    chexkBoxMap.put("5",cbLabelZhuanji);
                    break;
                case "6"://励志
                    chexkBoxMap.put("6",cbLabelLizhi);
                    break;
                case "7"://哲学
                    chexkBoxMap.put("7",cbLabelZhexue);
                    break;
                case "8"://心理学
                    chexkBoxMap.put("8",cbLabelXinlixue);
                    break;
                case "9"://历史
                    chexkBoxMap.put("9",cbLabelLishi);
                    break;
                case "10"://金融
                    chexkBoxMap.put("10",cbLabelJinrong);
                    break;
                case "11"://小说
                    chexkBoxMap.put("11",cbLabelXiaoshuo);
                    break;
                case "12"://其他
                    chexkBoxMap.put("12",cbLabelKeji);
                    break;
            }
        }*/
        checkBoxList.add(cbLabelHulianwang);
        checkBoxList.add(cbLabelJinguan);
        checkBoxList.add(cbLabelTourongzi);
        checkBoxList.add(cbLabelShangye);
        checkBoxList.add(cbLabelYinxiao);
        checkBoxList.add(cbLabelZhichang);
        checkBoxList.add(cbLabelJinrong);
        checkBoxList.add(cbLabelKeji);
        checkBoxList.add(cbLabelXinlixue);
        checkBoxList.add(cbLabelChuangye);
        checkBoxList.add(cbLabelLishi);
        checkBoxList.add(cbLabelLizhi);
        checkBoxList.add(cbLabelZhexue);
        checkBoxList.add(cbLabelZhuanji);
        checkBoxList.add(cbLabelXiaoshuo);

        for (CheckBox checkbox :checkBoxList){
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        selectednum += 1;
                    }else{
                        selectednum -= 1;
                    }
                    labelAmount.setText("已选择"+selectednum+"个");
                }
            });
        }

        labelBtnSelectedDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();

                for (CheckBox checkbox :checkBoxList) {
                    if (checkbox.isChecked()){
                        switch (checkbox.getId()){
                            case R.id.cb_label_jinguan:
                                sb.append("1,");
                                break;
                            case R.id.cb_label_yinxiao:
                                sb.append("2,");
                                break;
                            case R.id.cb_label_hulianwang:
                                sb.append("3,");
                                break;
                            case R.id.cb_label_zhichang:
                                sb.append("4,");
                                break;
                            case R.id.cb_label_zhuanji:
                                sb.append("5,");
                                break;
                            case R.id.cb_label_lizhi:
                                sb.append("6,");
                                break;
                            case R.id.cb_label_zhexue:
                                sb.append("7,");
                                break;
                            case R.id.cb_label_xinlixue:
                                sb.append("8,");
                                break;
                            case R.id.cb_label_lishi:
                                sb.append("9,");
                                break;
                            case R.id.cb_label_jinrong:
                                sb.append("10,");
                                break;
                            case R.id.cb_label_xiaoshuo:
                                sb.append("11,");
                                break;
                            case R.id.cb_label_chuangye:
                                sb.append("102,");
                                break;
                            case R.id.cb_label_tourongzi:
                                sb.append("103,");
                                break;
                            case R.id.cb_label_keji:
                                sb.append("104,");
                                break;
                            default:
                                sb.append("12,");
                                break;
                        }
                    }
                }

                String str = sb.toString();
                if(str.endsWith(",")){
                    str = str.substring(0, str.length() - 1);
                }
                Call<ModifyBean> modifyBeanCall = ReaderRetroift.getInstance(SelectLabelActivity.this).getApi().ModifybooktagCall(str);
                modifyBeanCall.enqueue(new Callback<ModifyBean>() {
                    @Override
                    public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                        ModifyBean bean = response.body();
                        if (bean != null){
                            //进入主界面
                            if (bean.getResponseCode() == 1){
                                Intent intent = new Intent(SelectLabelActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModifyBean> call, Throwable t) {

                    }
                });


            }
        });
    }

}
