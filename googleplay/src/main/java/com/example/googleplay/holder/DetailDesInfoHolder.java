package com.example.googleplay.holder;

import android.animation.ValueAnimator;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.googleplay.R;
import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.utils.UIUtils;

import static android.view.View.MeasureSpec;

/**
 * Created by LIANGSE on 2016/6/28.
 */
public class DetailDesInfoHolder extends BaseHolder<AppInfo> {

    private TextView mDesInfo;
    private TextView mAuthor;
    private int mWidth;
    private ImageView mArrowUp;

    private boolean isOpen;
    private LinearLayout.LayoutParams params;

    @Override
    public View initView() {

        View view = UIUtils.inflate(R.layout.detail_desinfo);
        mDesInfo = (TextView) view.findViewById(R.id.tv_des);
        mAuthor = (TextView) view.findViewById(R.id.tv_author);
        mArrowUp = (ImageView) view.findViewById(R.id.iv_arrow_up);
        mArrowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    private void toggle() {
        ValueAnimator animator;
        RotateAnimation rotateAnimation;
        if (isOpen) {
            isOpen = false;
            animator = ValueAnimator.ofInt(getLongHeight(), getShortHeight());
            rotateAnimation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            isOpen = true;
            animator = ValueAnimator.ofInt(getShortHeight(), getLongHeight());
            rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setFillAfter(true);
        }
        animator.setDuration(400);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int updateHeight = (int) animation.getAnimatedValue();
                params.height = updateHeight;
                mDesInfo.setLayoutParams(params);
                Log.i("DetailDesInfoHolder", "upDateHeight:" + updateHeight);
            }
        });
        rotateAnimation.setDuration(400);
        mArrowUp.startAnimation(rotateAnimation);
    }

    @Override
    protected void refreshView(AppInfo data) {
        mDesInfo.setText(data.des);
        mAuthor.setText(data.author);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.height = getShortHeight();
        mDesInfo.setLayoutParams(params);
    }

    /**
     * 获取一个7行textView的高度
     *
     * @return
     */
    private int getShortHeight() {

        mWidth = mDesInfo.getMeasuredWidth();

        TextView text = new TextView(UIUtils.getContext());
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        text.setMaxLines(7);
        text.setText(getData().des);
        int padding = (int) UIUtils.dip2px(3);
        text.setPadding(padding, padding, padding, padding);
        //测量view
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY); //width确定 match_parent
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000, MeasureSpec.AT_MOST);   //height不确定, wrap_content
        text.measure(widthMeasureSpec, heightMeasureSpec);

        int shortHeight = text.getMeasuredHeight();
        Log.i("DetailDesInfoHolder", "shortHeight:" + shortHeight);
        return shortHeight;
    }

    /**
     * 获取实际TextView大小
     * @return
     */
    private int getLongHeight() {
        /*重新测量*/
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY); //width确定 match_parent
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(3000, MeasureSpec.AT_MOST);   //height不确定, wrap_content
        mDesInfo.measure(widthMeasureSpec,heightMeasureSpec);
        int longHeight = mDesInfo.getMeasuredHeight();
        Log.i("DetailDesInfoHolder", "longHeight:" + longHeight);
        return longHeight;
    }


}
