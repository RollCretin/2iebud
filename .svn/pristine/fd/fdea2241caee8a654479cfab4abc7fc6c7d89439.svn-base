package com.hxjf.dubei.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.util.DensityUtil;

/**
 * Created by Chen_Zhang on 2017/7/5.
 */

public class UpAndBottomSlideView extends RelativeLayout {

    private Activity mActivity;

    public static enum Position {
        TOP, BOTTOM
    }
    private Context mContext;
    private Scroller mScroller;
    private int mScreenHeight;

    private View mMenuView;
//    private View mMenutopView;
//    private View mMenubottomView;
    //是否在移动
    private boolean mIsMoving = false;
    //菜单高度
    private int mMenuHeight = 0;
//    private int mMenutopHeight = 0;
//    private int mMenubottomHeight = 0;
    //蒙层界面
    private View mMaskView;
    //是否显示
    private boolean mShow = false;
    private int mDuration = 600;
    //缺省滑动方向为上
    private Position mPosition = Position.TOP;
    private UpAndBottomSlideView mOtherView;

    public UpAndBottomSlideView(Context context) {
        this(context,null);
    }
    public UpAndBottomSlideView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public UpAndBottomSlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 创建view
     */
    public static UpAndBottomSlideView create(Activity activity){
        UpAndBottomSlideView view = new UpAndBottomSlideView(activity);
        return view;
    }

    public static UpAndBottomSlideView create(Activity activity, Position position) {
        UpAndBottomSlideView view = new UpAndBottomSlideView(activity);
        view.mPosition = position;
        return view;
    }

    private void init(Context context) {
        mContext = context;
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setFocusable(true);
        mScroller = new Scroller(context);
        mScreenHeight = DensityUtil.getScreenWidthAndHeight(context)[1];

        attachToContentView((Activity) context);
    }
    public void addOtherView(UpAndBottomSlideView otherview){
        mOtherView = otherview;
    }
    /**
     * 创建蒙层,点击此处上下两个view同时消失
     */
    private void attachToContentView(Activity activity){
        ViewGroup contentFrameLayout = (ViewGroup) activity.findViewById(android.R.id.content);
        ViewGroup contentView = (ViewGroup) contentFrameLayout.getChildAt(0);
        mMaskView = new View(activity);
        mMaskView.setBackgroundColor(mContext.getResources().getColor(R.color.touming));
        contentView.addView(mMaskView,contentView.getLayoutParams());
        mMaskView.setVisibility(View.GONE);
        mMaskView.setClickable(true);
        mMaskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShow()) {
                    dismiss();
                }
                if (mOtherView != null && mOtherView.isShow()){
                    mOtherView.dismiss();
                }
            }
        });
    }

    private void switchMaskView(boolean bShow){
        if(bShow){
            mMaskView.setVisibility(View.VISIBLE);
            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(mDuration);
            mMaskView.startAnimation(animation);
        }else{
            mMaskView.setVisibility(View.GONE);
        }
    }

    //菜单的高度
    public void setMenuHeight(int height){
        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.height = height;
        mMenuHeight = height;
        this.setLayoutParams(params);
    }

    //设置上下滑菜单，并添加
    public void setMenuView(Activity activity,View view){
        mActivity = activity;
        mMenuView = view;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mMenuView.setLayoutParams(params);

        addView(mMenuView,params);



        mMenuView.post(new Runnable() {
            @Override
            public void run() {
                mMenuHeight = mMenuView.getHeight();
                switch (mPosition){
                    case  TOP:
                        UpAndBottomSlideView.this.scrollTo(0, mScreenHeight);
                        break;
                    case BOTTOM:
                        UpAndBottomSlideView.this.scrollTo(0, -mScreenHeight);
                        break;
                }
            }
        });

        ViewGroup contentFrameLayout = (ViewGroup) activity.findViewById(android.R.id.content);
        ViewGroup contentView = contentFrameLayout;
        contentView.addView(this);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.getLayoutParams();
        switch (mPosition){
            case TOP:
                layoutParams.gravity = Gravity.TOP;
                layoutParams.topMargin = 0;
                break;
            case BOTTOM:
                layoutParams.gravity = Gravity.BOTTOM;
                layoutParams.bottomMargin = 0;
                break;
        }
        TextView titleFrameLayout = (TextView) activity.findViewById(android.R.id.title);
        if( titleFrameLayout != null){
            layoutParams.topMargin = DensityUtil.getStatusBarHeight(mContext);
        }
        int flags =  mActivity.getWindow().getAttributes().flags;
        int flag = (flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if(flag == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS){
            //说明状态栏使用沉浸式
            layoutParams.topMargin = DensityUtil.getStatusBarHeight(mContext);
        }
        this.setLayoutParams(layoutParams);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(isShow()){
                    return true;
                }
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    //显示菜单
    public void show(){
        if(isShow() && !mIsMoving)
            return;
        switch (mPosition){
            case TOP:
                startScroll(mMenuHeight,-mMenuHeight,mDuration);
                break;
            case BOTTOM:
                startScroll(-mMenuHeight,mMenuHeight,mDuration);
                break;
        }
        mShow = true;
        switchMaskView(true);
    }
    //关闭菜单
    public void dismiss(){
        if (!isShow() && !mIsMoving){
            return ;
        }
        switch (mPosition){
            case TOP:
                startScroll(UpAndBottomSlideView.this.getScrollY(),mMenuHeight,mDuration);
                break;
            case BOTTOM:
                startScroll(UpAndBottomSlideView.this.getScrollY(),-mMenuHeight,mDuration);
                break;
        }
        mShow = false;
        switchMaskView(false);
    }

    public boolean isShow(){
        return mShow;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            //更新界面
            postInvalidate();
            mIsMoving = true;
        }else{
            mIsMoving = false;
        }
        super.computeScroll();
    }
    public void startScroll(int startY, int dY,int duration){
        mIsMoving = true;
        mScroller.startScroll(0,startY,0,dY,duration);
        invalidate();
    }










}
