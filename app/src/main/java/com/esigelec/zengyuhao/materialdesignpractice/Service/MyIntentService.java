package com.esigelec.zengyuhao.materialdesignpractice.Service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Enzo(ZENG Yuhao) on 16/5/11.
 */
public class MyIntentService extends IntentService{
    /**
     * Default constructor insisted by AndroidManifest
     */
    public MyIntentService(){
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

    }
}
