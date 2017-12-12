package com.tyranotyrano.steadyhard.contract.adapter;

import com.tyranotyrano.steadyhard.model.data.SteadyContent;

/**
 * Created by cyj on 2017-11-24.
 */

public interface SteadyContentAdapterContract {
    interface View {
        void notifyAdapter();
        void setOnItemClickListener(SteadyContentAdapterContract.OnSteadyContentClickListener onSteadyContentClickListener);
    }

    interface Model {
        void addItem(SteadyContent item);
        SteadyContent getSteadyContentItem(int position);
        void modifySteadyContent(int modifyPosition, SteadyContent modifySteadyContent);
        void clearAdapter();
    }

    interface OnSteadyContentClickListener {
        void onSteadyContentClick(int position);
    }
}
