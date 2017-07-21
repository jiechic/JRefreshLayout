package com.jiechic.android.widget;

import android.view.View;

/**
 * Created by jiechic on 2017/7/21.
 */

public interface JRefreshUIHandler {
    /**
     * When the content view has reached top and refresh has been completed, view will be reset.
     *
     * @param layout
     */
    public void onUIReset(JRefreshLayout layout);

    /**
     * prepare for loading
     *
     * @param layout
     */
    public void onUIRefreshPrepare(JRefreshLayout layout);

    /**
     * perform refreshing UI
     */
    public void onUIRefreshBegin(JRefreshLayout layout);

    /**
     * perform UI after refresh
     */
    public void onUIRefreshComplete(JRefreshLayout layout);

    public void onUIPositionChange(JRefreshLayout layout, boolean isUnderTouch, byte status, JRefreshIndicator indicator);

}
