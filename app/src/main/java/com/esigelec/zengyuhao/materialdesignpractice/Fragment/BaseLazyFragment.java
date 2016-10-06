package com.esigelec.zengyuhao.materialdesignpractice.Fragment;


import android.animation.Animator;
import android.animation.ObjectAnimator;
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
    public static final int MODE_DEEP_LAZY = 2;

    protected int mode = MODE_NORMAL;
    protected boolean isVisibleToUser = false;
    protected boolean isLoaded = false;
    protected boolean isOnCreateViewCalled = false;
    protected boolean isOnUserVisibleCalled = false;

    protected FrameLayout mContainerLayout;
    protected View mLazyView;
    protected View mLoadingView;
    protected int position = 0;

    protected Animator mLoadingViewDisappearAnim;

    public BaseLazyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //Log.i("BaseLazyFragment", "-->onCreate : " + System.currentTimeMillis());

        super.onCreate(savedInstanceState);

        if (mode != MODE_NORMAL && mode != MODE_LAZY && mode != MODE_DEEP_LAZY)
            throw new IllegalArgumentException("Unknown mode : mode must be 0 (NORMAL) or 1 (LAZY) or 2 (DEEP LAZY)");

        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_MODE);
            position = getArguments().getInt("position");
        }
        Log.d("TAG", "-->onCreate() " + position);

        // init container layout
        mContainerLayout = new FrameLayout(getActivity());
        mContainerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT));

        // init animators
        mLoadingViewDisappearAnim = ObjectAnimator.ofFloat(null, "alpha", 1f, 0f);
        mLoadingViewDisappearAnim.setDuration(100);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Log.d("TAG", "-->setUserVisibleHint() " + isVisibleToUser + " pos:" + position);
        //Log.i("BaseLazyFragment", "-->setUserVisibleHint : " + System.currentTimeMillis());
        if (getUserVisibleHint()) {
            this.isVisibleToUser = true;
            onUserVisible();

        } else {
            this.isVisibleToUser = false;
            onUserInvisible();
        }
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

        if (mode == MODE_NORMAL) {
            mLazyView = onCreateLazyView(mContainerLayout);
            mContainerLayout.addView(mLazyView);
        } else {
            isOnCreateViewCalled = true;
            prepareView();
            requestViewShowing();
        }
        return mContainerLayout;
    }


    protected void onUserVisible() {
        if (mode == MODE_NORMAL) return;
        Log.d("TAG", "-->onUserVisible() " + position);

        isOnUserVisibleCalled = true;

        if (mode == MODE_LAZY && !isLoaded) {
            prepareView();
            onLazyLoad();
        }

        if (mode == MODE_DEEP_LAZY) {
            if (!isLoaded) {
                prepareView();
                onLazyLoad();
            } else {
                requestViewShowing();
            }
        }
    }

    protected void onUserInvisible() {
        if (mode == MODE_NORMAL) return;
        Log.d("TAG", "-->onUserInvisible() " + position);

//        isOnCreateViewCalled = false;
//        isOnUserVisibleCalled = false;
        cancelAllAnimations();

        // if it's MODE_DEEP_LAZY means that when scrolling back to previous page, there will be also a loading effect
        // so we have to initiate these views.
        if (mode == MODE_DEEP_LAZY) {
            mLazyView.setVisibility(View.INVISIBLE);
            mLoadingView.setAlpha(1);
        }
    }

    /**
     * Will be called in {@link #onUserVisible()} and {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, this
     * method works at 1st time that it was called. This mechanism make sure that no matter what kind of loading the
     * fragment is (normal loading or pre-loading by ViewPager), there are always loading-view and lazy-view prepared.
     */
    protected void prepareView() {
        // nothing to be prepared
        if (mLazyView != null && mLoadingView != null) return;
        Log.d("TAG", "-->onPrepareView() " + position);

        mLazyView = onCreateLazyView(mContainerLayout);
        mContainerLayout.addView(mLazyView);

        mLoadingView = onCreateLoadingView(mContainerLayout);
        mContainerLayout.addView(mLoadingView);
    }

    /**
     * Will be called in {@link #onUserVisible()} and {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, this
     * method will actually work at 2nd time that it was called.
     */
    protected void requestViewShowing() {
        // onUserVisible(),  onCreateView()
        // since we are not sure which above method will call this method first, we add this condition to make sure
        // that this method only responses to the last one.
        if (isOnUserVisibleCalled && isOnCreateViewCalled) {
            Log.d("TAG", "-->onRequestViewShowing() " + position);
            mLoadingView.bringToFront();
            mLazyView.setVisibility(View.VISIBLE);
            mLoadingViewDisappearAnim.setTarget(mLoadingView);
            mLoadingViewDisappearAnim.start();
        }
    }

    protected void cancelAllAnimations() {
        Log.d("TAG", "-->cancelAllAnimations() " + position);
        if (mLoadingViewDisappearAnim.isRunning())
            mLoadingViewDisappearAnim.cancel();
    }

    public abstract View onCreateLoadingView(@Nullable ViewGroup parent);

    /**
     * @return view should be shown to user after lazy load.
     */
    public abstract View onCreateLazyView(@Nullable ViewGroup parent);

    /**
     * Load data, you can do it in UI thread or off UI thread.<br>
     * <b>IMPORTANT: </b>you must call
     * {@link #notifyDataLoaded()} when your data is ready, and then {@link #onBindData(View)} will be called.
     */
    public abstract void onLazyLoad();


    /**
     * Notify that your data is ready to be bound to the view.
     */
    protected void notifyDataLoaded() {
        Log.d("TAG", "-->notifyDataLoaded() " + position);
        onBindData(mLazyView);
        requestViewShowing();
        isLoaded = true;
    }


    /**
     * Bind data to your lazy loaded view. You must call {@link #notifyDataLoaded()} in {@link #onLazyLoad()} to
     * make this method to be invoked when your data is loaded.
     *
     * @param view view created by {@link #onCreateLazyView(ViewGroup)}
     */
    public abstract void onBindData(View view);


}
