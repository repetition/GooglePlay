package com.example.googleplay.holder;

import android.util.TypedValue;
import android.view.View;
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

    @Override
    public View initView() {

        View view = UIUtils.inflate(R.layout.detail_desinfo);
        mDesInfo = (TextView) view.findViewById(R.id.tv_des);
        mAuthor = (TextView) view.findViewById(R.id.tv_author);
        return view;
    }

    @Override
    protected void refreshView(AppInfo data) {
        mDesInfo.setText(data.des);
        mAuthor.setText(data.author);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.height = getShortHeight();
        mDesInfo.setLayoutParams(params);
    }


    private int getShortHeight() {

        mWidth = mDesInfo.getMeasuredWidth();

        TextView text = new TextView(UIUtils.getContext());
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        text.setMaxLines(7);
        text.setText(getData().des);

        //测量view
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000, MeasureSpec.AT_MOST);
        text.measure(widthMeasureSpec,heightMeasureSpec);

        int shortHeight = text.getMeasuredHeight();
        return shortHeight;
    }
}
