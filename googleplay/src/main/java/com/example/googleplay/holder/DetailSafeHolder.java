package com.example.googleplay.holder;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.googleplay.R;
import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.utils.CacheUtils;
import com.example.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by v_bzhzhang on 2016/6/24.
 */
public class DetailSafeHolder extends BaseHolder<AppInfo> {

    private ImageView[] mSafeIcons;
    private ImageView[] mDesIcons;
    private TextView[] mDesTexts;
    private LinearLayout[] mSafeDesBars;
    private ImageView mToggle;
    private LinearLayout mLLDesRoot;
    private int mSafeDesHeight;

    private boolean isOpen;
    private ValueAnimator mAnimator;
    private LinearLayout.LayoutParams mLLDesRootLayoutParams;

    @Override
    public View initView() {

        View view = UIUtils.inflate(R.layout.detail_safeinfo);
        /*初始化安全图片*/
        mSafeIcons = new ImageView[4];
        mSafeIcons[0] = (ImageView) view.findViewById(R.id.iv_safe1);
        mSafeIcons[1] = (ImageView) view.findViewById(R.id.iv_safe2);
        mSafeIcons[2] = (ImageView) view.findViewById(R.id.iv_safe3);
        mSafeIcons[3] = (ImageView) view.findViewById(R.id.iv_safe4);
        /*初始化描述对号  */
        mDesIcons = new ImageView[4];
        mDesIcons[0] = (ImageView) view.findViewById(R.id.iv_des1);
        mDesIcons[1] = (ImageView) view.findViewById(R.id.iv_des2);
        mDesIcons[2] = (ImageView) view.findViewById(R.id.iv_des3);
        mDesIcons[3] = (ImageView) view.findViewById(R.id.iv_des4);
        /*初始化描述*/
        mDesTexts = new TextView[4];
        mDesTexts[0] = (TextView) view.findViewById(R.id.tv_des1);
        mDesTexts[1] = (TextView) view.findViewById(R.id.tv_des2);
        mDesTexts[2] = (TextView) view.findViewById(R.id.tv_des3);
        mDesTexts[3] = (TextView) view.findViewById(R.id.tv_des4);

        /*初始化描述布局*/
        mSafeDesBars = new LinearLayout[4];
        mSafeDesBars[0] = (LinearLayout) view.findViewById(R.id.ll_des1);
        mSafeDesBars[1] = (LinearLayout) view.findViewById(R.id.ll_des2);
        mSafeDesBars[2] = (LinearLayout) view.findViewById(R.id.ll_des3);
        mSafeDesBars[3] = (LinearLayout) view.findViewById(R.id.ll_des4);

        mLLDesRoot = (LinearLayout) view.findViewById(R.id.ll_desRoot);
        mToggle = (ImageView) view.findViewById(R.id.iv_arrow_down);

        RelativeLayout mSafeDesBar = (RelativeLayout) view.findViewById(R.id.rl_safeDesBar);
        mSafeDesBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });


        return view;
    }

    /**
     * 设置 安全描述 显示隐藏动画
     */
    private void toggle() {
        ValueAnimator animator; //描述隐藏的动画
        RotateAnimation rotateAnimation; //箭头旋转动画

        if (isOpen) {
            isOpen = false;
            //属性动画
            animator = ValueAnimator.ofInt(mSafeDesHeight, 0);
            rotateAnimation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            isOpen = true;
            animator = ValueAnimator.ofInt(0, mSafeDesHeight);
            rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //设置控件维持动画结束状态
            rotateAnimation.setFillAfter(true);
        }

        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //给描述模块时时设置高度,实现过渡效果
                Integer updateHeight = (Integer) animation.getAnimatedValue();
                mLLDesRootLayoutParams.height = updateHeight;
                mLLDesRoot.setLayoutParams(mLLDesRootLayoutParams);
            }
        });
        animator.start();

        rotateAnimation.setDuration(300);
        mToggle.startAnimation(rotateAnimation);
    }

    @Override
    protected void refreshView(AppInfo data) {

        ArrayList<AppInfo.SafeInfo> safeInfos = data.safe;
        //写成4 遍历的时候,有就显示,没有就隐藏
        for (int i = 0; i < 4; i++) {
            if (i < safeInfos.size()) {
                AppInfo.SafeInfo safeInfo = safeInfos.get(i);
                //设置SafeIcon
                CacheUtils.newInstance().display(mSafeIcons[i], HttpHelper.URL_HOST + "image?name=" + safeInfo.safeUrl);
                //设置
                CacheUtils.newInstance().display(mDesIcons[i], HttpHelper.URL_HOST + "image?name=" + safeInfo.safeDesUrl);
                mDesTexts[i].setText(safeInfo.safeDes);
            } else {
                mSafeIcons[i].setVisibility(View.GONE);
                mSafeDesBars[i].setVisibility(View.GONE);
            }
        }

        //获取安全描述的 高度
        mLLDesRoot.measure(0, 0);
        mSafeDesHeight = mLLDesRoot.getMeasuredHeight();

        Log.i("DetailSafeHolder", "安全描述高度:" + mSafeDesHeight);
        //将安全描述信息隐藏掉
        mLLDesRootLayoutParams = (LinearLayout.LayoutParams) mLLDesRoot.getLayoutParams();
        mLLDesRootLayoutParams.height = 0;
        mLLDesRoot.setLayoutParams(mLLDesRootLayoutParams);

    }
}
