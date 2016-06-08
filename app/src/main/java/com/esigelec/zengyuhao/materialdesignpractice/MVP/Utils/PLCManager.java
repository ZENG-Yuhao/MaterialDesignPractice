package com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;

/**
 * Presenter Loader Callbacks Manager.
 * Created by Enzo on 16/5/27.
 */
public class PLCManager<P extends IPresenter, V extends IView> implements LoaderManager
        .LoaderCallbacks<P> {
    private Context mContext;
    private V mView;
    private IPresenterFactory<P> mFactory;
    private InstanceProvider<P> mInstanceProvider;

    /**
     *
     * @param context used to retrieve application context.
     * @param view used to set and reset the presenter that is bound to current view/activity.
     * @param factory used to offer a factory to create an instance of specific presenter in loader.
     */
    public PLCManager(Context context, V view, IPresenterFactory<P> factory) {
        mContext = context;

        /*
         * Here, in PLCManager, we hold a strong reference of the activity, and the instance of PLCManager is
         * referenced in the activity, this makes a circular reference. System GC can handle this situation, so there
          * is no need to detach the view(activity) and it will not cause a memory leak.
         */
        mView = view;
        mFactory = factory;
    }

    @Override
    public Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(mContext, mFactory);
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
