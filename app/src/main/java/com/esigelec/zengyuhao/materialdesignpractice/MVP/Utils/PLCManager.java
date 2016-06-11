package com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.os.Debug;

/**
 * Presenter Loader Callbacks Manager.
 * Tips:
 * 1. In each Activity or Fragment, an instance of LoaderManager will be instantiated, and it will be controlled by his
 * owner (Activity/Fragment)
 * 2. The instance of LoaderManager has the same lifecycle with his owner, normally it will die with his owner,
 * however it will be kept when and only when his owner is destroyed because of configuration change (for example,
 * screen rotating).
 *
 * Created by Enzo on 16/5/27.
 */
public class PLCManager<P extends IPresenter, V extends IView> implements LoaderManager
        .LoaderCallbacks<P> {
    private Context mContext;
    private V mView;
    private IPresenterFactory<P> mFactory;
    private InstanceProvider<P> mInstanceProvider;

    /**
     * @param context used to retrieve application context.
     * @param view    used to set and reset the presenter that is bound to current view/activity.
     * @param factory used to offer a factory to create an instance of specific presenter in loader.
     */
    public PLCManager(Context context, V view, IPresenterFactory<P> factory) {
        mContext = context;

        /*
         * Here, in PLCManager, we hold a strong reference of the Activity, and the instance of PLCManager is
         * referenced in the Activity, also PLCManager has implemented LoaderCallbacks interface, which will be
         * passed to and will be saved in the instance of LoaderManager when initLoader() or restartLoader(), just,
         * loaderManager(instance) has the same lifecycle with Activity, their relationships are like below:
         *        Activity <--> PLCManager <-- loaderManager --> Activity
         *
         * All these make a circular reference, but system GC can handle this situation, so there is no need to
         * detach the view(activity) here and it will not cause a memory leak.
         */
        mView = view;
        mFactory = factory;
    }

    @Override
    public Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(mContext, mFactory);
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P presenter) {
        mView.setPresenter(presenter);
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
            mView.setPresenter(null);
    }

    public void setInstanceProvider(InstanceProvider<P> provider) {
        mInstanceProvider = provider;
    }

    public interface InstanceProvider<P> {
        P provide();
    }
}
