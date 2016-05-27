package com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils;

import android.content.Context;
import android.content.Loader;

/**
 * Created by Enzo(ZENG Yuhao) on 16/5/26.
 */
public class PresenterLoader<P extends IPresenter> extends Loader<P> {
    private P mPresenter;
    private IPresenterFactory<P> mFactory;

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
    public PresenterLoader(Context context, IPresenterFactory<P> factory) {
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
        mPresenter = mFactory.create();
        deliverResult(mPresenter);
    }


    @Override
    protected void onReset() {
        mPresenter.onDestroy();
        mPresenter = null;
    }


}


