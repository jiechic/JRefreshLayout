package com.jiechic.android.widget;

import android.view.View;

/**
 * Created by jiechic on 2017/7/21.
 */

public interface JRefreshHandler {
    /**
     * Check can do refresh or not. For example the content is empty or the first child is in view
     * <p>
     * {@link JRefreshDefaultHanlder}
     *
     * @param layout
     * @param content
     * @param header
     * @return
     */
    boolean checkCanDoRefresh(final JRefreshLayout layout, final View content, final View header);

    /**
     * When refresh begin
     *
     * @param layout
     */
    void onRefreshBegin(final JRefreshLayout layout);
}
