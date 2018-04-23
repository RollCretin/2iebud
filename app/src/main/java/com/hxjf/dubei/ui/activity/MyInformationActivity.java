package com.hxjf.dubei.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.ProtraitBean;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.CommonUtils;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.widget.AddLabelDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.umeng.socialize.utils.ContextUtil.getContext;

/**
 * Created by Chen_Zhang on 2017/6/19.
 */

public class MyInformationActivity extends BaseActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_LOAD_PROFILE = 2;
    private static final int RESULT_LOAD_LABEL = 3;
    private static final int MY_PERMISSIONS_REQUEST_SELECT_PICTURE = 5;

    @BindView(R.id.info_back)
    ImageView infoBack;
    @BindView(R.id.info_name)
    TextView infoName;
    @BindView(R.id.info_sex)
    TextView infoSex;
    @BindView(R.id.info_label)
    TextView infoLabel;
    @BindView(R.id.info_number)
    TextView infoNumber;
    @BindView(R.id.info_tv_profile)
    TextView infoTvProfile;
    @BindView(R.id.info_ll_profile)
    LinearLayout infoLlProfile;
    @BindView(R.id.info_portrait)
    CircleImageView infoPortrait;
    private String picPath;
    /*  @BindView(R.id.info_tv_experence)
    TextView infoTvExperence;
    @BindView(R.id.info_ll_experence)
    LinearLayout infoLlExperence;
    @BindView(R.id.info_tv_background)
    TextView infoTvBackground;
    @BindView(R.id.info_ll_background)
    LinearLayout infoLlBackground;*/

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_information;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Call<UserDetailBean> myDetailCall = ReaderRetroift.getInstance(getContext()).getApi().myDetailCall();
        myDetailCall.enqueue(new Callback<UserDetailBean>() {
            @Override
            public void onResponse(Call<UserDetailBean> call, Response<UserDetailBean> response) {
                UserDetailBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    Glide.with(MyInformationActivity.this).load(ReaderRetroift.IMAGE_URL + bean.getResponseData().getNormalPath()).into(infoPortrait);
                    UserDetailBean.ResponseDataBean databean = bean.getResponseData();
                    infoName.setText(databean.getNickName());
                    infoSex.setText(databean.getSexValue());
                    if (databean.getTag() != null) {
                        infoLabel.setText(databean.getTag() + "");
                    }
                    infoNumber.setText(databean.getTelephone());
                    if (databean.getIntro() != null) {
                        infoTvProfile.setText(databean.getIntro() + "");

                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetailBean> call, Throwable t) {

            }
        });


    }

    @OnClick({R.id.info_back, R.id.info_sex, R.id.info_label, R.id.info_number, R.id.info_ll_profile, R.id.info_portrait, R.id.info_name/*, R.id.info_ll_experence, R.id.info_ll_background*/})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_name:
                break;
            case R.id.info_back:
                finish();
                break;
            case R.id.info_sex:
                change_sex();
                break;
            case R.id.info_label:
                Intent labelIntent = new Intent(this, MyLabelActivity.class);
                startActivityForResult(labelIntent, RESULT_LOAD_LABEL);
                break;
            case R.id.info_number:
                break;
            case R.id.info_ll_profile:
                Intent profileIntent = new Intent(this, MyProfileActivity.class);
                profileIntent.putExtra("send_profile", infoTvProfile.getText().toString());
                startActivityForResult(profileIntent, RESULT_LOAD_PROFILE);
                break;
            case R.id.info_portrait:
                break;
           /* case R.id.info_ll_experence:
                Intent experienceIntent = new Intent(this,MyExperienceActivity.class);
                startActivity(experienceIntent);
                break;
            case R.id.info_ll_background:
                Intent backgroundIntent = new Intent(this,MybackgroundActivity.class);
                startActivity(backgroundIntent);
                break;*/
        }
    }

    //修改昵称
    private void change_name() {
        final AddLabelDialog nameDialog = new AddLabelDialog(this);
        nameDialog.setTitle("昵称");
        nameDialog.setEdittextString(infoName.getText().toString());
        nameDialog.goneDes();
        nameDialog.setPositiveButtonName("确定");
        nameDialog.setNegativeButtonName("取消");
        nameDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nameDialog.getEditTextString())) {
                    Toast.makeText(MyInformationActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    infoName.setText(nameDialog.getEditTextString());
                    Call<ModifyBean> modifyNickCall = ReaderRetroift.getInstance(MyInformationActivity.this).getApi().getModifyNickBean(nameDialog.getEditTextString().toString());
                    modifyNickCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean bean = response.body();
                            Toast.makeText(MyInformationActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ModifyBean> call, Throwable t) {

                        }
                    });

                    nameDialog.dismiss();
                }
            }
        });
        nameDialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameDialog.dismiss();
            }
        });
        nameDialog.show();
    }

    //更换头像
    private void changePortrait() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MyInformationActivity.this);
        String[] strarr = {"相册", "取消"};

        builder.setItems(strarr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (ContextCompat.checkSelfPermission(MyInformationActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat
                                .requestPermissions(
                                        MyInformationActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_SELECT_PICTURE);

                    } else {
                        //调用本地相册
                        selectPicture();
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void selectPicture() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();//获取android返回的自定的uri
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            infoPortrait.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            File file = new File(picturePath);

            //上传准备
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("picFile", file.getName(), requestFile);
            //将图片上传至服务器
            Call<ProtraitBean> protraitBeanCall = ReaderRetroift.getInstance(this).getApi().uploadPortraitCall(part);
            protraitBeanCall.enqueue(new Callback<ProtraitBean>() {
                @Override
                public void onResponse(Call<ProtraitBean> call, Response<ProtraitBean> response) {
                    ProtraitBean bean = response.body();
                    if (bean != null) {
                        picPath = bean.getResponseData().getPicPath();
                        Toast.makeText(MyInformationActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProtraitBean> call, Throwable t) {
                    Log.d("TAG", "onFailure: " + t.toString());
                }
            });
        }
        if (requestCode == RESULT_LOAD_PROFILE && resultCode == RESULT_OK && null != data) {
            String profile = data.getStringExtra("profile");
            infoTvProfile.setText(profile);
        }

        if (requestCode == RESULT_LOAD_LABEL && resultCode == RESULT_OK && null != data) {
            String label = data.getStringExtra("label");
            infoLabel.setText(label);
        }
    }


    //修改性别
    private void change_sex() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyInformationActivity.this);
        String[] strarr = {"男", "女"};

        builder.setItems(strarr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击后，性别发生变化
                if (which == 0) {
                    infoSex.setText("男");
                } else {
                    infoSex.setText("女");
                }
                String sexvalue = "";
                if (infoSex.getText().toString().equals("男")) {
                    sexvalue = "1";
                } else {
                    sexvalue = "2";
                }
                Call<ModifyBean> modifySexCall = ReaderRetroift.getInstance(MyInformationActivity.this).getApi().getModifySexBean(sexvalue);
                modifySexCall.enqueue(new Callback<ModifyBean>() {
                    @Override
                    public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                        ModifyBean bean = response.body();
                        if (bean != null) {
                            Toast.makeText(MyInformationActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModifyBean> call, Throwable t) {

                    }
                });
            }
        });
        builder.show();
    }

    //修改手机号码
    private void change_number() {
        final AddLabelDialog numberDialog = new AddLabelDialog(this);
        numberDialog.setTitle("手机号码");
        numberDialog.setEdittextString(infoNumber.getText().toString());
        numberDialog.goneDes();
        numberDialog.setPositiveButtonName("确定");
        numberDialog.setNegativeButtonName("取消");
        numberDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTextString = numberDialog.getEditTextString();
                boolean isvalid = CommonUtils.isValidTelNumber(editTextString);
                if (isvalid) {
                    infoNumber.setText(editTextString);
                    numberDialog.dismiss();
                } else {
                    Toast.makeText(MyInformationActivity.this, "号码格式不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
        numberDialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberDialog.dismiss();
            }
        });
        numberDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String string = SPUtils.getString(this, "bindbean", "");
        Gson gson = new Gson();
        UserDetailBean bean = gson.fromJson(string, UserDetailBean.class);

        //将所有信息bindbean保存到SP
        UserDetailBean.ResponseDataBean dataBean = bean.getResponseData();
        if (picPath != null) {
            dataBean.setNormalPath(picPath);
        }
        dataBean.setNickName(infoName.getText().toString());
        dataBean.setSexValue(infoSex.getText().toString());
        dataBean.setTag(infoLabel.getText().toString());
        dataBean.setTelephone(infoNumber.getText().toString());
        dataBean.setIntro(infoTvProfile.getText().toString());
        bean.setResponseData(dataBean);

        String jsonstr = gson.toJson(bean);
        SPUtils.putString(this, "bindbean", jsonstr);

    }
}
