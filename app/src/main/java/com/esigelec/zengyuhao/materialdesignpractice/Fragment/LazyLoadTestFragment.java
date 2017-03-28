package com.esigelec.zengyuhao.materialdesignpractice.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LazyLoadTestFragment extends OldBaseLazyFragment {
    private int position;

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
        notifyDataLoaded();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                notifyDataLoaded();
//            }
//        }, 70);
    }

    @Override
    public void onBindData(View view) {
        TextView txtvw = (TextView) view.findViewById(R.id.txtvw);
        txtvw.setText("Hello World");
    }


}
