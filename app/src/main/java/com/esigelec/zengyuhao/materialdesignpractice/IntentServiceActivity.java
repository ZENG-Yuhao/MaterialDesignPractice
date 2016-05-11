package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.esigelec.zengyuhao.materialdesignpractice.Service.MyIntentService;

public class IntentServiceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service);

        Button btn_start_service = (Button) findViewById(R.id.btn_start_service);
        btn_start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntentServiceActivity.this, MyIntentService.class);
                intent.putExtra("secret", "There is no secret.");
                startService(intent);
            }
        });
    }

}
