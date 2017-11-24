package com.tyranotyrano.steadyhard.contract.base;

/**
 * Created by cyj on 2017-11-24.
 */

public interface BasePresenter<T> {
    public void attachView(T view);
    public void detachView();
}
