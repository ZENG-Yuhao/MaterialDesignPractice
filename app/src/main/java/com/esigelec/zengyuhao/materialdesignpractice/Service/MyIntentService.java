package com.esigelec.zengyuhao.materialdesignpractice.Service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Enzo(ZENG Yuhao) on 16/5/11.
 */
public class MyIntentService extends IntentService {
    final static String TAG = "MyIntentService";

    /**
     * Default constructor insisted by AndroidManifest.xml
     */
    public MyIntentService() {
        super("Default work thread for MyIntentService");
    }

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
        Log.i(TAG, "MyIntentService---->"+ intent.getStringExtra("secret"));
    }
}
