package com.zjy.girlspic.util.ui;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.zjy.girlspic.util.ThreadUtils;

/**
 * Created by jiyoung.tsang on 16/6/26.
 */
public abstract class OnRecyclerViewScrollListener extends RecyclerView.OnScrollListener{


    private int[] lastPositions;
    private int lastVisibleItemPosition;

    boolean isLoadingMore = false;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if(newState == RecyclerView.SCROLL_STATE_IDLE){
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();

            if(lastVisibleItemPosition>=totalItemCount-1 && isLoadingMore &&visibleItemCount>0){
                ThreadUtils.GENERAL_THREAD_POOL.execute(new Runnable() {
                    @Override
                    public void run() {
                        onLoadMore();
                    }
                });

            }
        }

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(dy>0){
            isLoadingMore = true;
        }else{
            isLoadingMore = false;
        }

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if(layoutManager instanceof StaggeredGridLayoutManager){
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager)layoutManager;
            if(lastPositions == null){
                lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            }
            staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
            lastVisibleItemPosition = findMax(lastPositions);
        }

    }

    private int findMax(int[] lastPositions){
        int max = lastPositions[0];
        for(int value:lastPositions){
            if(value>max){
                max = value;
            }
        }
        return max;
    }

    /**
     * 该方法在异步线程中执行,UI操作请另起主线程执行
     */
    public abstract void onLoadMore();
}
