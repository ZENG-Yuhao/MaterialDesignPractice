package com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.Utils;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.IPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.IView;

/**
 * PresenterLoader's callbacks manager.
 * Created by Enzo on 16/5/27.
 */
public class PLCallBack<P extends IPresenter, V extends IView> implements LoaderManager
        .LoaderCallbacks<P> {
    private Context mContext;
    private V mView;
    private InstanceProvider<P> mInstanceProvider;

    public PLCallBack(Context context, V view) {
        mContext = context;
        mView = view;
    }

    @Override
    public Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(mContext, new IPresenterFactory<P>() {
            @Override
            public P create() {
                return mInstanceProvider.provide();
            }
        });
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P presenter){
        mView.setPresenter(presenter);
    }

    @Override
    public void onLoaderReset(Loader<P> loader){
        mView.setPresenter(null);
    }

    public void setInstanceProvider(InstanceProvider<P> provider){
        mInstanceProvider = provider;
    }
    public interface InstanceProvider<P> {
        P provide();
    }
}
