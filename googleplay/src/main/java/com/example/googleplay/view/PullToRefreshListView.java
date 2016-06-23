package com.example.googleplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.example.googleplay.R;

/**
 * Created by LIANGSE on 2016/4/16.
 */
public class PullToRefreshListView extends ListView {
    public PullToRefreshListView(Context context) {
        super(context);
        init(context);
    }


    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        View headerView = View.inflate(context, R.layout.headerview, null);
        addHeaderView(headerView);
        headerView.measure(0, 0);
        Log.i("PullToRefreshListView", "HeaderView高度:" + headerView.getMeasuredHeight());
        headerView.setPadding(0, -headerView.getMeasuredHeight(), 0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.onTouchEvent(ev);
    }
}
