package com.esigelec.zengyuhao.materialdesignpractice.Fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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


public abstract class BaseLazyFragment extends Fragment {
    public static final String ARG_MODE = "startup.mode";
    public static final int MODE_NORMAL = 0;
    public static final int MODE_LAZY = 2;

    protected int mode = MODE_NORMAL;
    protected boolean isVisibleToUser = false;
    protected boolean isLoaded = false;
    protected FrameLayout mContainerLayout;
    private View mLazyView;
    private View mLoadingView;
    private int position;

    private Animator mLazyViewAppearAnim, mLoadingViewDisappearAnim;
    private AnimatorSet animSet;

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

        // init animators
        mLazyViewAppearAnim = ObjectAnimator.ofFloat(null, "alpha", 0f, 1f);
        mLoadingViewDisappearAnim = ObjectAnimator.ofFloat(null, "alpha", 1f, 0f);
        animSet = new AnimatorSet();
        animSet.playTogether(mLazyViewAppearAnim, mLoadingViewDisappearAnim);
        animSet.setDuration(100);
    }

    /**
     * if we apply this fragment on a {@link android.support.v4.view.ViewPager} and when ViewPager is firstly loading,
     * this method will be called after {@link #setUserVisibleHint(boolean)}, contrary, when pager is scrolling,
     * because of preload mechanism of ViewPager, this method will be called before {@link #setUserVisibleHint(boolean)}
     */
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        Log.d("TAG", "-->onCreateView() " + position);
        if (isLoaded) return mContainerLayout;

        if (mode == MODE_LAZY) {
            mLoadingView = onCreateLoadingView(mContainerLayout);

            mLazyView = onCreateLazyView(mContainerLayout);
            mLazyView.setAlpha(0f);
            mContainerLayout.addView(mLazyView);
        } else
            mLoadingView = onCreateLazyView(mContainerLayout);
        mContainerLayout.addView(mLoadingView);
        return mContainerLayout;
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

    protected void onUserVisible() {
        if (mode == MODE_NORMAL) return;
        if (isVisibleToUser && !isLoaded)
            lazyLoad();
    }

    protected void onUserInvisible() {

    }

    protected void lazyLoad() {
        Log.d("TAG", "-->lazyLoad() " + position);
        //mLazyView = onCreateLazyView(mContainerLayout);
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

        // if this fragment is not in a ViewPager, normally, this method will be called before onCreateView().
        // At this moment, all views are null, so we call onCreateView() manually for having views inflated and
        // prepared. Once this method is called, isLoaded = true, thus if onCreateView() is called later, it will do
        // nothing but return a container layout.
        if (mLazyView == null) {
            onCreateView(null, null, null);
        }
        onBindData(mLazyView);
        replaceContentView(mLazyView);
        isLoaded = true;

    }

    protected void replaceContentView(View view) {
        Log.d("TAG", "-->replaceContentView() " + position);
        if (mLazyViewAppearAnim.isRunning())
            mLazyViewAppearAnim.cancel();

        if (mLoadingViewDisappearAnim.isRunning())
            mLoadingViewDisappearAnim.cancel();

        if (animSet.isRunning())
            animSet.cancel();

        mLazyViewAppearAnim.setTarget(view);
        mLoadingViewDisappearAnim.setTarget(mLoadingView);

        //view.setVisibility(View.VISIBLE);
        animSet.start();
        animSet.removeAllListeners();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoadingView.setVisibility(View.GONE);
            }
        });
    }


    /**
     * Bind data to your lazy loaded view. You must call {@link #notifyDataLoaded()} in {@link #onLoadData()} to
     * make this method to be invoked when your data is loaded.
     *
     * @param view view created by {@link #onCreateLazyView(ViewGroup)}
     */
    abstract void onBindData(View view);

}
