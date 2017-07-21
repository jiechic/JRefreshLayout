package com.jiechic.android.widget;

import android.view.View;

/**
 * Created by jiechic on 2017/7/21.
 */

public abstract class JRefreshDefaultHanlder implements JRefreshHandler {

    public static boolean canChildScrollUp(View view) {
        return view.canScrollVertically(-1);
    }

    /**
     * Default implement for check can perform pull to refresh
     *
     * @param layout
     * @param content
     * @param header
     * @return
     */
    public static boolean checkContentCanBePulledDown(JRefreshLayout layout, View content, View header) {
        return !canChildScrollUp(content);
    }

    @Override
    public boolean checkCanDoRefresh(JRefreshLayout layout, View content, View header) {
        return checkContentCanBePulledDown(layout, content, header);
    }
}
