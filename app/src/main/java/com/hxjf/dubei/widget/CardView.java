package com.hxjf.dubei.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.drawable.BottomDrawable;
import com.hxjf.dubei.drawable.CenterDrawable;
import com.hxjf.dubei.drawable.TopDrawable;
import com.hxjf.dubei.util.ScreenSizeUtil;

/**
 *
 */
public class CardView extends LinearLayout {
    RelativeLayout topLayout;
    ImageView centerLayout;
    TextView content;
    EditText comment;

    TopDrawable topDrawable;
    CenterDrawable centerDrawable;
    BottomDrawable bottomDrawable;
    GradientDrawable myGrad;
    LinearLayout cardLayout;

    public CardView(Context context) {
        super(context);
        init(context);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
/*
    public CardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }*/

    public void init(final Context context) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_home_read_change, null);
        layout.setLayoutParams(new LayoutParams(ScreenSizeUtil.Dp2Px(getContext(), 300), ScreenSizeUtil.Dp2Px(getContext(), 420)));
        layout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//没有这句不显示

        cardLayout = (LinearLayout) layout.findViewById(R.id.card_layout);
//        cardLayout.layout(50,0,getMeasuredWidth(),getMeasuredHeight());
        topDrawable = new TopDrawable();
        topDrawable.setColor(Color.GRAY);
        cardLayout.setBackground(topDrawable);

        /*topLayout = (RelativeLayout) layout.findViewById(R.id.top);
        centerLayout = (ImageView) layout.findViewById(R.id.center);
        content = (TextView) layout.findViewById(R.id.content);
        comment = (EditText) layout.findViewById(R.id.comment);
        myGrad = (GradientDrawable) content.getBackground();*/

       /* topDrawable = new TopDrawable();
        topLayout.setBackground(topDrawable);

        centerDrawable = new CenterDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.quote));
        centerLayout.setBackground(centerDrawable);

        bottomDrawable = new BottomDrawable();
        comment.setBackground(bottomDrawable);*/

        addView(layout);
    }

    public void changeTheme(final int color) {
        //文字背景颜色
        myGrad.setColor(color);
        //顶部阴影颜色
        topDrawable.setColor(color);
//        中部阴影颜色
        centerDrawable.setColor(color);
//        //底部阴影颜色
        bottomDrawable.setColor(color);
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}
