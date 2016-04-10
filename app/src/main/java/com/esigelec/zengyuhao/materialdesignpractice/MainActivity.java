package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends Activity {
    private RecyclerView xRecyclerView;
    private RecyclerView.Adapter xAdapter;
    private RecyclerView.LayoutManager xLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this announce to improve performance if the layout size of RecyclerView will not be changed.
        xRecyclerView.setHasFixedSize(true);

        xLayoutManager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(xLayoutManager);

        xAdapter = new MyAdapter(dataGenerator(20));
        xRecyclerView.setAdapter(xAdapter);
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] xDataSet;

        public MyAdapter(String[] dataSet) {
            xDataSet = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
            ViewHolder vh = new ViewHolder((TextView) view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.xTextView.setText(xDataSet[position]);
        }

        @Override
        public int getItemCount() {
            return xDataSet.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public TextView xTextView;

            public ViewHolder(TextView view) {
                super(view);
                xTextView = view;
            }
        }
    }

    private String[] dataGenerator(int n) {
        String[] data = new String[n];
        for (int i = 0; i < n; i++) {
            data[i] = "RecyclerView ListItem " + i;
        }
        return data;
    }
}
