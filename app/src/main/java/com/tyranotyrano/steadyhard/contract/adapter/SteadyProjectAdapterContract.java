package com.tyranotyrano.steadyhard.contract.adapter;

import com.tyranotyrano.steadyhard.model.data.SteadyProject;

/**
 * Created by cyj on 2017-11-24.
 */

public interface SteadyProjectAdapterContract {
    interface View {
        void notifyAdapter();
        void setOnItemClickListener(SteadyProjectAdapterContract.OnItemClickListener onItemClickListener);
    }

    interface Model {
        void addItem(SteadyProject item);
        SteadyProject getItem(int position);
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }
}
