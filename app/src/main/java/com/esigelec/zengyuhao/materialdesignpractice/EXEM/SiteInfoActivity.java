package com.esigelec.zengyuhao.materialdesignpractice.EXEM;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.esigelec.zengyuhao.materialdesignpractice.R;

import java.util.ArrayList;

public class SiteInfoActivity extends Activity {
    private Switch switch_edit;
    private EditText editxt_association, editxt_adresse;
    private RecyclerView recyclerView;
    private ArrayList<String> mDataList;
    private PersonneAdapter mAdapter;
    private FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        switch_edit = (Switch) findViewById(R.id.switch_edit);
        editxt_association = (EditText) findViewById(R.id.editxt_association);
        editxt_adresse = (EditText) findViewById(R.id.editxt_adresse);
        setUneditable(editxt_association);
        setUneditable(editxt_adresse);


        switch_edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setEditable(editxt_association);
                    setEditable(editxt_adresse);
                } else {
                    setUneditable(editxt_association);
                    setUneditable(editxt_adresse);
                }
            }
        });

        mDataList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PersonneAdapter(this);
        recyclerView.setAdapter(mAdapter);

        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = mDataList.size() + 1;
                mDataList.add("Personne " + index);
                mAdapter.notifyItemInserted(0);
                recyclerView.scrollToPosition(0);
            }
        });
    }


    public void setEditable(EditText target) {
        target.setTextColor(getResources().getColor(android.R.color.darker_gray));
        //target.setFocusable(true);
        target.setEnabled(true);
    }

    public void setUneditable(EditText target) {
        target.setTextColor(getResources().getColor(android.R.color.black));
        //target.setFocusable(false);
        target.setEnabled(false);
        target.clearFocus();
    }

    private class PersonneAdapter extends RecyclerView.Adapter<ViewHolder> {
        private Context mContext;

        public PersonneAdapter(Context context) {
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.my_text_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Button btn = (Button) holder.itemView;
            btn.setText(mDataList.get((mDataList.size() - 1) - position));
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;
        }

    }
}
