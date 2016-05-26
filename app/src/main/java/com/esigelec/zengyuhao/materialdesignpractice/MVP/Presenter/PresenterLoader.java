package com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter;

import android.content.Context;
import android.content.Loader;

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
     */
    public PresenterLoader(Context context) {
        super(context);
    }

    public interface PresenterFactory<T>{
        T create();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    @Override
    protected void onStopLoading() {

    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
    }
}


