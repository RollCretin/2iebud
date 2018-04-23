package com.hxjf.dubei.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.AttentionListBean;
import com.hxjf.dubei.bean.FavoriteBookListbean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.MyAccountActivity;
import com.hxjf.dubei.util.GlideLoadUtils;
import com.hxjf.dubei.util.ScreenSizeUtil;
import com.hxjf.dubei.zxing.view.ViewfinderView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/4/10 0010.
 */

public class MyAttentionListAdapter extends BaseAdapter {
    Context mContext;
    List<AttentionListBean.ResponseDataBean> mList;

    public MyAttentionListAdapter(Context mContext, List<AttentionListBean.ResponseDataBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        final AttentionListBean.ResponseDataBean responseDataBean = mList.get(position);
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_my_attention, null);
            holder.mPratroit =(CircleImageView) convertView.findViewById(R.id.item_attention_pratroit);
            holder.mName = (TextView) convertView.findViewById(R.id.item_attention_name);
            holder.mAttention = (TextView) convertView.findViewById(R.id.item_attention_btn);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final boolean[] flag = {true};
        GlideLoadUtils.getInstance().glideLoad(mContext, ReaderRetroift.IMAGE_URL+responseDataBean.getAttentionUserHeadPath(),holder.mPratroit,0);
        holder.mName.setText(responseDataBean.getAttentionUserName());
        final ViewHolder finalHolder = holder;
        holder.mAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[0]){
                    //取消关注
                    final Dialog detailDialog = new Dialog(mContext, R.style.myDialogTheme);
                    View contentView = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_cancel_attention, null);
                    TextView done = (TextView) contentView.findViewById(R.id.dialog_cancel_attention_done);
                    TextView dlater = (TextView) contentView.findViewById(R.id.dialog_cancel_attention_later);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            finalHolder.mAttention.setEnabled(false);
                            finalHolder.mAttention.setText("关注");
                            flag[0] = false;
                            Call<ModifyBean> cancelAttentionCall = ReaderRetroift.getInstance(mContext).getApi().cancelAttentionCall(responseDataBean.getAttentionUserId());
                            cancelAttentionCall.enqueue(new Callback<ModifyBean>() {
                                @Override
                                public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                                    finalHolder.mAttention.setEnabled(true);
                                    detailDialog.dismiss();
                                    Toast.makeText(mContext, response.body().getResponseMsg(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<ModifyBean> call, Throwable throwable) {

                                }
                            });

                        }
                    });
                    dlater.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            detailDialog.dismiss();
                        }
                    });
                    detailDialog.setContentView(contentView);
                    detailDialog.show();
                    android.view.WindowManager.LayoutParams p = detailDialog.getWindow().getAttributes(); //获取对话框当前的参数值
                    p.width = (int) (ScreenSizeUtil.getScreenWidth(mContext) * 0.7); //宽度设置为屏幕的
                    detailDialog.getWindow().setAttributes(p); //设置生效
                }else{
                    //关注
                    finalHolder.mAttention.setEnabled(false);
                    finalHolder.mAttention.setText("已关注");
                    flag[0] = true;
                    Call<ModifyBean> addAttentionCall = ReaderRetroift.getInstance(mContext).getApi().addAttentionCall(responseDataBean.getAttentionUserId());
                    addAttentionCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            finalHolder.mAttention.setEnabled(true);
                            Toast.makeText(mContext, response.body().getResponseMsg(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ModifyBean> call, Throwable throwable) {

                        }
                    });
                }
            }
        });
        return convertView;
    }

    class ViewHolder{
        CircleImageView mPratroit;
        TextView mName;
        TextView mAttention;
    }
}
