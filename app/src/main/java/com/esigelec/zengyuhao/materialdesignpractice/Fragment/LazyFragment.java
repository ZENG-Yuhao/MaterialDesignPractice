package com.esigelec.zengyuhao.materialdesignpractice.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class LazyFragment extends Fragment {
    protected static final String ARG_MODE = "startup.mode";
    protected static final int MODE_NORMAL = 0;
    protected static final int MODE_LAZY = 1;

    protected int mode = MODE_NORMAL;
    protected boolean isVisible = false;
    protected boolean isLoaded = false;

    private View mLazyView;

    public LazyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mode != MODE_NORMAL && mode != MODE_LAZY)
            throw new IllegalArgumentException("Unknown mode : mode must be 0 (NORMAL) or 1 (LAZY)");

        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_MODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onUserVisible();

        } else {
            isVisible = false;
            onUserInvisible();
        }
    }

    protected void onUserVisible() {
        if (mode == MODE_NORMAL) return;
        if (isVisible && !isLoaded)
            lazyLoad();
    }

    protected void onUserInvisible() {

    }

    protected void lazyLoad() {
        mLazyView = onCreateLazyView();
        onLoadData();
    }

    /**
     * @return view should be shown to user after lazy load.
     */
    abstract View onCreateLazyView();

    /**
     * Load data, you can do it in UI thread or off UI thread.<br>
     * <b>IMPORTANT: </b>you must call
     * {@link #notifyDataLoaded()} when your data is ready, and then {@link #onBindData(View)} will be called.
     */
    abstract void onLoadData();

    /**
     * Notify that your data is ready to be bound to the view.
     */
    protected void notifyDataLoaded() {
        if (mLazyView != null) {
            onBindData(mLazyView);
            replaceContentView(mLazyView);
        }
    }

    protected void replaceContentView(View view) {
        View currentView = getView();

        ViewGroup parentView = null;
        if (currentView.getParent() != null) {
            parentView = (ViewGroup) currentView.getParent();
            Log.i("LazyFragment", "---> parentView : " + parentView);
            parentView.removeAllViews();
            parentView.addView(view);
        }
    }


    /**
     * Bind data to your lazy loaded view. You must call {@link #notifyDataLoaded()} in {@link #onLoadData()} to
     * make this method to be invoked when your data is loaded.
     *
     * @param view view created by {@link #onCreateLazyView()}
     */
    abstract void onBindData(View view);

}
