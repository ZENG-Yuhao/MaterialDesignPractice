package com.esigelec.zengyuhao.materialdesignpractice.Service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
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
        Log.i(TAG, "MyIntentService---->" + intent.getStringExtra("secret"));

        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);
        localIntent.putExtra(Constants.EXTENDED_DATA_STATUS, "Work has been done by MyIntentService.");
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    public final class Constants {

        /* Defining app's package name of our own actions is recommended to avoid naming conflict, but it's optional,
         * the example below proved this.
         */

        public static final String BROADCAST_ACTION = "com.lalalalala.BROADCAST";

        public static final String EXTENDED_DATA_STATUS = "com.lalalalala.zengyuhao1.STATUS";
    }
}
