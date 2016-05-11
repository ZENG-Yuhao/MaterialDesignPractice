package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

public class IntentServiceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service);
    }


    public class MyIntentService extends IntentService{

        /**
         * Creates an IntentService.  Invoked by your subclass's constructor.
         *
         * @param name Used to name the worker thread, important only for debugging.
         */
        public MyIntentService(String name) {
            super(name);
        }

        @Override
        protected void onHandleIntent(Intent intent) {

        }
    }
}
