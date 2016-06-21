package com.esigelec.zengyuhao.materialdesignpractice.EXEM;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esigelec.zengyuhao.materialdesignpractice.R;


public class BlankFragment extends Fragment {
    private static final String ARG_PAGE_NAME = "page_name";
    private static final String ARG_PAGE_NUMBER = "page_number";

    private String pageName;
    private String pageNumber;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pageName   Parameter 1.
     * @param pageNumber Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    public static BlankFragment newInstance(String pageName, String pageNumber) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PAGE_NAME, pageName);
        args.putString(ARG_PAGE_NUMBER, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageName = getArguments().getString(ARG_PAGE_NAME);
            pageNumber = getArguments().getString(ARG_PAGE_NUMBER);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        TextView txt_page_name = (TextView) view.findViewById(R.id.txt_page_name);
        TextView txt_page_number = (TextView) view.findViewById(R.id.txt_page_number);
        txt_page_name.setText(pageName);
        txt_page_number.setText(pageNumber);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (Integer.valueOf(pageNumber) % 2 == 0) {
            inflater.inflate(R.menu.menu_items, menu);

        } else {
            inflater.inflate(R.menu.menu_items1, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
