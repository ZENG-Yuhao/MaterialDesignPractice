package com.esigelec.zengyuhao.materialdesignpractice.Fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * <p>
 * Created by ZENG Yuhao. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */

public abstract class BaseLazyFragment extends Fragment {
    public static final String ARG_MODE = "startup.mode";
    public static final int MODE_NORMAL = 0;
    public static final int MODE_LAZY = 1;
    public static final int MODE_DEEP_LAZY = 2;

    public enum LoadState {
        READY, RUNNING, FINISHED
    }

    protected int mode = MODE_LAZY;
    protected boolean newVisibility = false;
    protected boolean previousVisibility = false;
    protected FragmentVisibilityListener mVisibilityListener;
    protected LoadState mLoadState = LoadState.READY;

    protected ViewGroup mContainerLayout;
    protected View mLazyView;
    protected View mLoadingView;
    protected int position = 0;

    protected Animator mViewDisappearAnim, mViewAppearAnim;

    public BaseLazyFragment() {
        // Required empty public constructor
    }

    /**
     * @return Loading view, make sure you have created view, otherwise you will get null.
     */
    public View getLoadingView() {
        return mLoadingView;
    }

    /**
     * @return Lazy view (main view of your fragment), make sure you have created view, otherwise you will get null.
     */
    public View getLazyView() {
        return mLazyView;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "-->onCreate() " + position);
        if (mode != MODE_NORMAL && mode != MODE_LAZY && mode != MODE_DEEP_LAZY)
            throw new IllegalArgumentException("Unknown mode : mode must be 0 (NORMAL) or 1 (LAZY) or 2 (DEEP LAZY)");

        // init animators
        mViewDisappearAnim = ObjectAnimator.ofFloat(null, "alpha", 1f, 0f);
        mViewAppearAnim = ObjectAnimator.ofFloat(null, "alpha", 0.6f, 1f);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        newVisibility = isVisibleToUser;
        Log.d("TAG", "-->setUserVisibleHint() " + position);
        if (isViewPrepared())
            checkVisibilityChanges();
    }

    /**
     * If we apply this fragment on a {@link android.support.v4.view.ViewPager} and when ViewPager is firstly
     * loaded, this method will be called after {@link #setUserVisibleHint(boolean)}, contrary, when pager is
     * scrolling, because of preload mechanism of ViewPager, this method will be called before
     * {@link #setUserVisibleHint(boolean)}
     */
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        Log.d("TAG", "-->onCreateView() " + position);
        // init container layout
        mContainerLayout = new FrameLayout(getActivity());
        mContainerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT));
        mLoadingView = onCreateLoadingView(mContainerLayout);
        mLazyView = onCreateLazyView(mContainerLayout);
        mContainerLayout.addView(mLoadingView);
        mContainerLayout.addView(mLazyView);
        mLoadingView.bringToFront();
        Log.d("TAG", "-->getUserVisibleHint() " + getUserVisibleHint());
        return mContainerLayout;
    }

    protected boolean isViewPrepared() {
        return (mLoadingView != null && mLazyView != null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("TAG", "-->onViewCreated() " + position);
        super.onViewCreated(view, savedInstanceState);
        if (isViewPrepared())
            checkVisibilityChanges();
    }

    protected void checkVisibilityChanges() {
        Log.d("TAG", "-->checkVisibilityChanges() " + "recent " + previousVisibility + " current " + newVisibility);
        if (newVisibility != previousVisibility) {
            onVisibilityChanged(newVisibility);
            if (mVisibilityListener != null) mVisibilityListener.onVisibilityChanged(newVisibility);
            previousVisibility = newVisibility;
        }
    }

    protected void onVisibilityChanged(boolean isVisibleToUser) {
        Log.d("TAG", "-->onVisibilityChanged() " + isVisibleToUser);
        if (mode == MODE_NORMAL) return;

        if (isVisibleToUser) {
            if (mLoadState == LoadState.READY) {
                mLoadState = LoadState.RUNNING;
                Log.d("TAG", "-->onLazyLoad() " + position);
                onLazyLoad();
            }

            if (mLoadState == LoadState.FINISHED && mode == MODE_DEEP_LAZY) {
                showLazyView();
            }
        } else { // isVisibleToUser == false;
            if (mLoadState == LoadState.RUNNING) {
                onCancelLoading();
                mLoadState = LoadState.READY;
            }

            if (mode == MODE_DEEP_LAZY)
                showLoadingView();
        }
    }

    protected void showLazyView() {
        Log.d("TAG", "-->showLazyView() " + position);
//        mLazyView.bringToFront();
        mViewDisappearAnim.setTarget(mLoadingView);
        mViewAppearAnim.setTarget(mLazyView);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200).play(mViewDisappearAnim).with(mViewAppearAnim);
        set.start();

    }

    protected void showLoadingView() {
        Log.d("TAG", "-->showLoadingView() " + position);
//        mLoadingView.bringToFront();
        mLoadingView.setAlpha(1f);
    }

    protected abstract View onCreateLoadingView(@Nullable ViewGroup parent);

    /**
     * @return view should be shown as a content view to user after lazy loading.
     */
    protected abstract View onCreateLazyView(@Nullable ViewGroup parent);

    /**
     * Load data, you can do it in UI-thread or off-UI-thread.<br>
     * <b>IMPORTANT: </b>you must call
     * {@link #notifyDataLoaded()} when your data is ready, and then {@link #onBindData(View)} will be called.
     */
    protected abstract void onLazyLoad();

    protected void onCancelLoading() {
        Log.d("TAG", "-->onCancelLoading() " + position);
    }

    /**
     * Notify that your data is ready to be bound to the view.
     */
    protected void notifyDataLoaded() {
        Log.d("TAG", "-->notifyDataLoaded() " + position);
        mLoadState = LoadState.FINISHED;
        Log.d("TAG", "-->onBindData() " + position);
        onBindData(mLazyView);
        showLazyView();
    }

    /**
     * Bind data to your lazy loaded view. You must call {@link #notifyDataLoaded()} in {@link #onLazyLoad()} to
     * make this method to be invoked when your data is loaded.
     *
     * @param view view created by {@link #onCreateLazyView(ViewGroup)}
     */
    protected abstract void onBindData(View view);

    public interface FragmentVisibilityListener {
        void onVisibilityChanged(boolean isVisibleToUser);
    }
}