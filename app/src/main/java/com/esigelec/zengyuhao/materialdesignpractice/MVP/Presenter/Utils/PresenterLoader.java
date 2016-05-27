package com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.Utils;

import android.content.Context;
import android.content.Loader;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.IPresenter;

/**
 * Created by Enzo(ZENG Yuhao) on 16/5/26.
 */
public class PresenterLoader<T extends IPresenter> extends Loader<T> {
    private T mPresenter;
    private PresenterFactory<T> mFactory;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     * @param factory instance of IPresenterFactory
     */
    public PresenterLoader(Context context, PresenterFactory factory) {
        super(context);
        mFactory = factory;

    }

    @Override
    protected void onStartLoading() {
        if (mPresenter != null) {
            deliverResult(mPresenter);
        } else {
            forceLoad();
        }

    }

    @Override
    protected void onForceLoad() {

        //mPresenter = mFactory.create();
        deliverResult(mPresenter);
    }

    @Override
    protected void onReset() {
        mPresenter.onDestroy();
        mPresenter = null;
    }


}


