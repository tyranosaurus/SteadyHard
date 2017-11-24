package com.tyranotyrano.steadyhard.contract.adapter;

import com.tyranotyrano.steadyhard.model.SteadyContent;

/**
 * Created by cyj on 2017-11-24.
 */

public interface SteadyContentAdapterContract {
    interface View {
        void notifyAdapter();
        void setOnItemClickListener(SteadyContentAdapterContract.OnItemClickListener onItemClickListener);
    }

    interface Model {
        void addItem(SteadyContent item);
        SteadyContent getItem(int position);
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }
}
