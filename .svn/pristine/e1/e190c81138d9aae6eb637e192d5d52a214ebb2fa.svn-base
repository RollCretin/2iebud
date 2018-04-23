/*
 * Copyright (C) 2007-2015 FBReader.ORG Limited <contact@fbreader.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.TimeUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.fbreader.fbreader.FBView;
import org.geometerplus.fbreader.util.TextSnippet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.alibaba.mobileim.YWChannel.getResources;

public class SelectionShareAction extends FBAndroidAction {

	private final FBReader context;
	private UMImage image;

	SelectionShareAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
		context = baseActivity;

	}

	@Override
	protected void run(Object ... params) {


		Call<UserDetailBean> myDetailCall = ReaderRetroift.getInstance(context).getApi().myDetailCall();
		myDetailCall.enqueue(new Callback<UserDetailBean>() {
			@Override
			public void onResponse(Call<UserDetailBean> call, Response<UserDetailBean> response) {
				UserDetailBean bean = response.body();
				if (bean != null && bean.getResponseData() != null) {
					share(bean);
				}
			}

			@Override
			public void onFailure(Call<UserDetailBean> call, Throwable t) {

			}
		});

		/*final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT,
			ZLResource.resource("selection").getResource("quoteFrom").getValue().replace("%s", title)
		);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		BaseActivity.startActivity(Intent.createChooser(intent, null));*/
	}

	private void share(UserDetailBean bean) {

		final FBView fbview = Reader.getTextView();
		final TextSnippet snippet = fbview.getSelectedSnippet();
		if (snippet == null) {
			return;
		}
		final String text = snippet.getText();
		final String title = Reader.getCurrentBook().getTitle();
		final String authors = Reader.getCurrentBook().authorsString(",");

		fbview.clearSelection();

		final Dialog bottomDialog = new Dialog(context, R.style.myDialogTheme);
		View contentView = LayoutInflater.from(context).inflate(R.layout.view_dialog_share_text, null);
		final TextView tv_wx = (TextView) contentView.findViewById(R.id.dialog_share_wx);
		final TextView tv_pengyouquan = (TextView) contentView.findViewById(R.id.dialog_share_pengyouquan);
		Button btn_cancel = (Button) contentView.findViewById(R.id.dialog_share_cancel);
		final ScrollView textShareScrollView = (ScrollView) contentView.findViewById(R.id.text_share_scrollView);
		TextView textShareContent = (TextView) contentView.findViewById(R.id.text_share_content);
		TextView tvTitle = (TextView) contentView.findViewById(R.id.text_share_title);
		TextView tvAuthor = (TextView) contentView.findViewById(R.id.text_share_author);
		TextView tvName = (TextView) contentView.findViewById(R.id.text_share_name);
		TextView tvDate = (TextView) contentView.findViewById(R.id.text_share_date);
		ImageView ivQRCode = (ImageView) contentView.findViewById(R.id.text_share_erweima);
		ImageView ivProtrait = (ImageView) contentView.findViewById(R.id.text_share_imageview);
		Glide.with(context).load(ReaderRetroift.IMAGE_URL+bean.getResponseData().getNormalPath()).into(ivProtrait);
		tvName.setText(bean.getResponseData().getNickName());
		tvDate.setText(TimeUtil.getCurrentTime());

		//修改文字内容
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.maohao);
		ImageSpan imgSpan = new ImageSpan(context, b);
		SpannableString spanString = new SpannableString("icon");
		spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textShareContent.setText(spanString);
		textShareContent.append(text);
		tvTitle.setText(title);
		tvAuthor.setText(authors);


		tv_wx.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//分享微信好友
				Bitmap bitmap = getBitmapByView(textShareScrollView);
				StringBuilder sb = new StringBuilder();
				sb.append(text.charAt(0));
				sb.append(text.charAt(text.length()-1));

				savePhotoToSDCard(bitmap, "/sdcard/screenshot", sb.toString());
				File file = new File("/sdcard/screenshot/"+sb.toString()+".png");
				image = new UMImage(context, file);
				image.compressStyle = UMImage.CompressStyle.QUALITY;
				new ShareAction(context).setPlatform(SHARE_MEDIA.WEIXIN).withText(title).withMedia(image).share();
				bottomDialog.dismiss();
			}
		});

		tv_pengyouquan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//分享到盆友圈
				Bitmap bitmap = getBitmapByView(textShareScrollView);
				StringBuilder sb = new StringBuilder();
				sb.append(text.charAt(0));
				sb.append(text.charAt(text.length()-1));

				savePhotoToSDCard(bitmap, "/sdcard/screenshot", sb.toString());
				File file = new File("/sdcard/screenshot/"+sb.toString()+".png");
				image = new UMImage(context, file);
				image.compressStyle = UMImage.CompressStyle.QUALITY;
				new ShareAction(context).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).withText(title).withMedia(image).share();
				bottomDialog.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bottomDialog.dismiss();
			}
		});
		bottomDialog.setContentView(contentView);
		ViewGroup.MarginLayoutParams layoutparams = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
		layoutparams.width = getResources().getDisplayMetrics().widthPixels;
		contentView.setLayoutParams(layoutparams);

		bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
		bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
		bottomDialog.show();
	}

	public static Bitmap getBitmapByView(ScrollView scrollView) {
		int h = 0;
		Bitmap bitmap = null;
		// 获取scrollview实际高度
		for (int i = 0; i < scrollView.getChildCount(); i++) {
			h += scrollView.getChildAt(i).getHeight();
			scrollView.getChildAt(i).setBackgroundColor(
					Color.parseColor("#ffffff"));
		}
		// 创建对应大小的bitmap
		bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
				Bitmap.Config.RGB_565);
		final Canvas canvas = new Canvas(bitmap);
		scrollView.draw(canvas);
		return bitmap;
		/*int h = 0;
		Bitmap bitmap = null;
		for (int i = 0; i < scrollView.getChildCount(); i++) {
			h += scrollView.getChildAt(i).getHeight();
		}
		bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
				Bitmap.Config.RGB_565);
		final Canvas canvas = new Canvas(bitmap);
		scrollView.draw(canvas);
		return bitmap;*/
	}
	public static void savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File photoFile = new File(path, photoName + ".png");
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(photoFile);
			if (photoBitmap != null) {
				if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
					fileOutputStream.flush();
				}
			}
		} catch (FileNotFoundException e) {
			photoFile.delete();
			e.printStackTrace();
		} catch (IOException e) {
			photoFile.delete();
			e.printStackTrace();
		} finally {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
