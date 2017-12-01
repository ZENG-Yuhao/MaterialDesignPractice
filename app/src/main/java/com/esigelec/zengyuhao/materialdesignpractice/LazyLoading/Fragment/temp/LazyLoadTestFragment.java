package com.esigelec.zengyuhao.materialdesignpractice.LazyLoading.Fragment.temp;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.LazyLoading.Fragment.BaseLazyFragment;
import com.esigelec.zengyuhao.materialdesignpractice.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LazyLoadTestFragment extends BaseLazyFragment {
    private int position;
    private boolean isCanceled = false;

    public LazyLoadTestFragment() {
        // Required empty public constructor
    }

    public static LazyLoadTestFragment newInstance(int mode, int postion) {
        LazyLoadTestFragment fragment = new LazyLoadTestFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        args.putInt("position", postion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }
    }


    @Override
    public View onCreateLoadingView(@Nullable ViewGroup parent) {
        Log.d("TAG", "-->onCreateLoadingView() " + position);
        return LayoutInflater.from(getActivity()).inflate(R.layout.layout_lazy_fragment_loading_view, parent, false);
    }

    @Override
    public View onCreateLazyView(@Nullable ViewGroup parent) {
        Log.d("TAG", "-->onCreateLazyView() " + position);
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_lazy_load_test, parent, false);
    }

    @Override
    public void onLazyLoad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    if (isCanceled) break;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("TAG", "-->onLazyLoad " + i + "s");
                }
                notifyDataLoaded();
                isCanceled = false;
            }
        }).start();
    }

    /**
     * When fragment is becoming invisible to user, stop your background loading task in this method.
     */
    @Override
    protected void onCancelLoading() {
        isCanceled = true;
    }

    @Override
    public void onBindData(View view) {
        TextView txtvw = (TextView) view.findViewById(R.id.txtvw);
        txtvw.setText("Hello World");
    }


}
