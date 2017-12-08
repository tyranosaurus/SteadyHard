package com.tyranotyrano.steadyhard.contract.adapter;

import com.tyranotyrano.steadyhard.model.data.SteadyProject;

/**
 * Created by cyj on 2017-11-24.
 */

public interface SteadyProjectAdapterContract {
    interface View {
        void notifyAdapter();
        void notifyAdapterDelete(int deletePosition);
        void setOnSteadyProjectClickListener(SteadyProjectAdapterContract.OnSteadyProjectClickListener onSteadyProjectClickListener);
    }

    interface Model {
        void addItem(SteadyProject item);
        void addNewItem(SteadyProject item);
        void clearAdapter();
        SteadyProject getSteadyProjectItem(int position);
        void deleteSteadyProject(SteadyProject deleteItem);
        void modifySteadyProject(int modifyPosition, SteadyProject modifySteadyProject);
    }

    interface OnSteadyProjectClickListener {
        void onSteadyProjectClick(int position);
    }
}
