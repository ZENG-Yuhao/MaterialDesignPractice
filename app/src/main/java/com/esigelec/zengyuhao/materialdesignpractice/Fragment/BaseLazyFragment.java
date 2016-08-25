package com.esigelec.zengyuhao.materialdesignpractice.Fragment;


import android.animation.LayoutTransition;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public abstract class BaseLazyFragment extends Fragment {
    public static final String ARG_MODE = "startup.mode";
    public static final int MODE_NORMAL = 0;
    public static final int MODE_LAZY = 1;

    protected int mode = MODE_NORMAL;
    protected boolean isVisible = false;
    protected boolean isLoaded = false;
    protected FrameLayout mContainerLayout;
    private View mLazyView;
    private View mCurrView;
    private int position;

    public BaseLazyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //Log.i("BaseLazyFragment", "-->onCreate : " + System.currentTimeMillis());

        super.onCreate(savedInstanceState);

        if (mode != MODE_NORMAL && mode != MODE_LAZY)
            throw new IllegalArgumentException("Unknown mode : mode must be 0 (NORMAL) or 1 (LAZY)");

        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_MODE);
            position = getArguments().getInt("position");
        }
        Log.d("TAG", "-->onCreate() " + position);

        // init container layout
        mContainerLayout = new FrameLayout(getActivity());
        mContainerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams
                .MATCH_PARENT));
        LayoutTransition transition = new LayoutTransition();
        transition.setDuration(150);
        mContainerLayout.setLayoutTransition(transition);
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        Log.d("TAG", "-->onCreateView() " + position);
        if (isLoaded) return mContainerLayout;

        if (mode == MODE_LAZY)
            mCurrView = onCreateLoadingView(mContainerLayout);
        else
            mCurrView = onCreateLazyView(mContainerLayout);
        mContainerLayout.addView(mCurrView);
        return mContainerLayout;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Log.d("TAG", "-->setUserVisibleHint() " + isVisibleToUser + " pos:" + position);
        //Log.i("BaseLazyFragment", "-->setUserVisibleHint : " + System.currentTimeMillis());
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
        Log.d("TAG", "-->lazyLoad() " + position);
        mLazyView = onCreateLazyView(mContainerLayout);
        onLoadData();
    }

    abstract View onCreateLoadingView(@Nullable ViewGroup parent);

    /**
     * @return view should be shown to user after lazy load.
     */
    abstract View onCreateLazyView(@Nullable ViewGroup parent);

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
        Log.d("TAG", "-->notifyDataLoaded() " + position);
        if (mLazyView != null) {
            onBindData(mLazyView);
            replaceContentView(mLazyView);
            isLoaded = true;
        }
    }

    protected void replaceContentView(View view) {
        Log.d("TAG", "-->replaceContentView() " + position);
        //mContainerLayout.removeAllViews();
        mContainerLayout.addView(view);
        //Log.i("BaseLazyFragment", "--> replaceContentView : " + System.currentTimeMillis());
    }


    /**
     * Bind data to your lazy loaded view. You must call {@link #notifyDataLoaded()} in {@link #onLoadData()} to
     * make this method to be invoked when your data is loaded.
     *
     * @param view view created by {@link #onCreateLazyView(ViewGroup)}
     */
    abstract void onBindData(View view);

}
