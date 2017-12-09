package com.tyranotyrano.steadyhard.contract.adapter;

import com.tyranotyrano.steadyhard.model.data.SteadyContent;

/**
 * Created by cyj on 2017-12-08.
 */

public interface ContentByProjectAdapterContract {
    interface View {
        void notifyAdapter();
        void setOnContentByProjectClickListener(ContentByProjectAdapterContract.OnContentByProjectClickListener onContentByProjectClickListener);
    }

    interface Model {
        void addItem(SteadyContent item);
        SteadyContent getSteadyContentItem(int position);
        void clearAdapter();
    }

    interface OnContentByProjectClickListener {
        void onContentByProjectClick(int position);
    }
}
