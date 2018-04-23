package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.network.ReaderRetroift;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/6/20.
 */

public class MyProfileActivity extends BaseActivity {
    @BindView(R.id.profile_back)
    ImageView profileBack;
    @BindView(R.id.profile_save)
    TextView profileSave;
    @BindView(R.id.profile_edittext)
    EditText profileEdittext;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_profile;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String profile = bundle.getString("send_profile");
        profileEdittext.setText(profile);
    }

    @OnClick({R.id.profile_back, R.id.profile_save, R.id.profile_edittext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_back:
                finish();
                break;
            case R.id.profile_save:
                save();
                break;
            case R.id.profile_edittext:
                profileEdittext.setCursorVisible(true);
                break;
        }
    }

    private void save() {
        final String intro = profileEdittext.getText().toString();
        Call<ModifyBean> modifyBeanCall = ReaderRetroift.getInstance(this).getApi().modifyIntroCall(intro);
        modifyBeanCall.enqueue(new Callback<ModifyBean>() {
            @Override
            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                ModifyBean bean = response.body();
                if (bean != null) {
                    String responseMsg = bean.getResponseMsg();
                    Toast.makeText(MyProfileActivity.this, responseMsg, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra("profile", intro);
                    MyProfileActivity.this.setResult(RESULT_OK, intent);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<ModifyBean> call, Throwable t) {

            }
        });
    }
}
