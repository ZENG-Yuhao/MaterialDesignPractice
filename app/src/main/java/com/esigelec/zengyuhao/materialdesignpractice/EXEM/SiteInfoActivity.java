package com.esigelec.zengyuhao.materialdesignpractice.EXEM;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.esigelec.zengyuhao.materialdesignpractice.R;

public class SiteInfoActivity extends Activity {
    private Switch switch_edit;
    private EditText editxt_association, editxt_adresse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_info);

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
    }

    public void setEditable(EditText target) {
        target.setTextColor(getResources().getColor(android.R.color.darker_gray));
        target.setEnabled(true);
    }

    public void setUneditable(EditText target){
        target.setTextColor(getResources().getColor(android.R.color.black));
        target.setEnabled(false);
    }
}
