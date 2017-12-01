package com.esigelec.zengyuhao.materialdesignpractice.LazyLoading.Fragment;

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
 * Fragment applying a lazy-load mechanism (Loading of Internet data and Binding of component-views' data will be done
 * only when fragment is visible to user).<br>
 * <p><b>MODE_NORMAL :</b> This mode is only for some special test use-case, a normal {@link Fragment} will be a
 * better choice.</p>
 *
 * <p><b>MODE_LAZY :</b> Those fragment-pages were being initialized/loaded will be kept in memory, and when user scroll
 * back to previous page, there will <b>NOT</b> be a loading view showing.</p>
 *
 * <p><b>MODE_DEEP_LAZY :</b> Those fragment-pages were being initialized/loaded will be kept in memory, but when user
 * scroll back to previous page, there will <b>STILL</b> be a loading view showing. If you have a fragment view which
 * does intensive drawing works (such like a graph with massive points). This mode will reduce GPU rendering pressure
 * when scrolling (because previous page is covered by a simple loading view).</p>
 *
 * <p>
 * <b>Usage :</b><br>
 * 1) Create your own class extending this class, select correctly fragment mode.<br>
 *
 * 2) Create a loading view in {@link #onCreateLoadingView(ViewGroup)} and create a content view in
 * {@link #onCreateLazyView(ViewGroup)}.<br>
 *
 * 3) Load your data in {@link #onLazyLoad()}, you should create you own thread if you want to do it in
 * off-ui-thread, once your data has been loaded, call {@link #notifyDataLoaded()} (if your data loading does not
 * take much time, or it can be done immediately from local, you don't have to do anything in this method but just
 * call {@link #notifyDataLoaded()}).<br>
 *
 * 4) Override {@link #onCancelLoading()} and stop your thread in this method, if you load data in off-ui-thread.<br>
 *
 * 5) Bind your data to view components (TextView, ListView etc.) in {@link #onBindData(View)}<br>
 * </p>
 * <p>
 * Created by ZENG Yuhao. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */

public abstract class BaseLazyFragment extends Fragment {
    private static final String TAG = "BaseLazyFragment";

    public static final String ARG_MODE = "startup.mode";
    public static final int MODE_NORMAL = 0;
    public static final int MODE_LAZY = 1;
    public static final int MODE_DEEP_LAZY = 2;

    private enum LoadState {
        READY, RUNNING, FINISHED
    }

    private int mode = MODE_LAZY;
    private boolean newVisibility = false;
    private boolean previousVisibility = false;
    private FragmentVisibilityListener mVisibilityListener;
    private LoadState mLoadState = LoadState.READY;

    private ViewGroup mContainerLayout;
    private View mLazyView;
    private View mLoadingView;
    protected int position = 0;

    private Animator mViewDisappearAnim, mViewAppearAnim;

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
     * @return Lazy view (content view of your fragment), make sure you have created view, otherwise you will get null.
     */
    public View getLazyView() {
        return mLazyView;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setMode(int mode) {
        if (mode != MODE_NORMAL && mode != MODE_LAZY && mode != MODE_DEEP_LAZY)
            throw new IllegalArgumentException("Unknown mode : mode must be 0 (NORMAL) or 1 (LAZY) or 2 (DEEP LAZY)");
        this.mode = mode;
    }

    public void setVisibilityChangeListener(FragmentVisibilityListener listener) {
        mVisibilityListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "-->onCreate() " + position);
        // init animators
        mViewDisappearAnim = ObjectAnimator.ofFloat(null, "alpha", 1f, 0f);
        mViewAppearAnim = ObjectAnimator.ofFloat(null, "alpha", 0.6f, 1f);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        newVisibility = isVisibleToUser;
        Log.d(TAG, "-->setUserVisibleHint() " + position);
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        Log.d(TAG, "-->onCreateView() " + position);
        if (mode == MODE_NORMAL) return onCreateLazyView(container);
        // init container layout
        mContainerLayout = new FrameLayout(getActivity());
        mContainerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT));

        mLazyView = onCreateLazyView(mContainerLayout);
        mContainerLayout.addView(mLazyView);

        mLoadingView = onCreateLoadingView(mContainerLayout);
        mContainerLayout.addView(mLoadingView);
        mLoadingView.bringToFront();

        return mContainerLayout;
    }

    /**
     * Called when all saved state has been restored into the view hierarchy
     * of the fragment.  This can be used to do initialization based on saved
     * state that you are letting the view hierarchy track itself, such as
     * whether check box widgets are currently checked.  This is called
     * after {@link #onActivityCreated(Bundle)} and before
     * {@link #onStart()}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (isViewPrepared())
            checkVisibilityChanges();
    }

    protected boolean isViewPrepared() {
        return ((mLoadingView != null || mode == MODE_NORMAL) && mLazyView != null);
    }

    private void checkVisibilityChanges() {
        Log.d(TAG, "-->checkVisibilityChanges() " + "recent " + previousVisibility + " current " + newVisibility);
        if (newVisibility != previousVisibility) {
            onVisibilityChanged(newVisibility);
            if (mVisibilityListener != null) mVisibilityListener.onVisibilityChanged(newVisibility);
            previousVisibility = newVisibility;
        }
    }

    private void onVisibilityChanged(boolean isVisibleToUser) {
        Log.d(TAG, "-->onVisibilityChanged() " + isVisibleToUser);
        if (mode == MODE_NORMAL) return;

        if (isVisibleToUser) {
            if (mLoadState == LoadState.READY) {
                mLoadState = LoadState.RUNNING;
                Log.d(TAG, "-->onLazyLoad() " + position);
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

    private void showLazyView() {
        Log.d(TAG, "-->showLazyView() " + position);
//        mLazyView.bringToFront();
        mViewDisappearAnim.setTarget(mLoadingView);
        mViewAppearAnim.setTarget(mLazyView);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(100).play(mViewDisappearAnim).with(mViewAppearAnim);
        set.start();

    }

    private void showLoadingView() {
        Log.d(TAG, "-->showLoadingView() " + position);
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
     * {@link #notifyDataLoaded()} when your data is ready, and then {@link #onBindData(View)} will be called.  If
     * you don't have time-consuming task such like Internet data loading, you need simply call
     * {@link #notifyDataLoaded()} in this method.
     */
    protected abstract void onLazyLoad();

    /**
     * When fragment is becoming invisible to user, stop your background loading task in this method.
     */
    protected void onCancelLoading() {

    }

    /**
     * Notify that your data is ready to be bound to the content view.
     */
    protected void notifyDataLoaded() {
        Log.d(TAG, "-->notifyDataLoaded() " + position);
        if (getActivity() == null)
            throw new RuntimeException("Activity is null, you have called notifyDataLoaded() in a wrong " +
                    "place.");
        mLoadState = LoadState.FINISHED;
        // this method may be called in off-ui-thread
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onBindData(mLazyView);
                showLazyView();
            }
        });
    }

    /**
     * Bind data to your lazy loaded view. You must call {@link #notifyDataLoaded()} in {@link #onLazyLoad()} to
     * make this method being invoked when your data has been loaded.
     *
     * @param lazyView view created by {@link #onCreateLazyView(ViewGroup)}
     */
    protected abstract void onBindData(View lazyView);

    public interface FragmentVisibilityListener {
        void onVisibilityChanged(boolean isVisibleToUser);
    }
}