package com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.Utils;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.IPresenter;

/**
 * Created by Enzo on 16/5/27.
 */
public abstract class PresenterLoaderManager<T extends IPresenter> implements LoaderManager.LoaderCallbacks<T> {
    private Context mContext;
    private T mPresenter;
    private PresenterFactory<T> mFactory;

    public PresenterLoaderManager(Context context, T presenter) {
        mContext = context;
    }

    @Override
    public Loader<T> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<T>(mContext, mFactory);
    }

    @Override
    public abstract void onLoadFinished(Loader<T> loader, T presenter);

    @Override
    public void onLoaderReset(Loader<T> loader) {
        mPresenter = null;
    }

    public interface onLoadFinishedListener {
        void onFinished();
    }
}
