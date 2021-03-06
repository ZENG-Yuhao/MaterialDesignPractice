package com.esigelec.zengyuhao.materialdesignpractice.Notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import com.esigelec.zengyuhao.materialdesignpractice.Others.NavigationDrawerActivity;
import com.esigelec.zengyuhao.materialdesignpractice.R;
import com.esigelec.zengyuhao.materialdesignpractice.Others.ShadowAndClippingActivity;

public class NotificationActivity extends Activity {

    final private static int NOTIFICATION_ID = 110;
    private int number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_devices_black_48dp);
        builder.setContentTitle("Notification");
        builder.setContentText("Hello World.");

        Intent resultIntent = new Intent(this, ShadowAndClippingActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent
                .FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        Button btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());
            }
        });

        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.cancel(NOTIFICATION_ID);
            }
        });

        final NotificationCompat.Builder builder_auto = new NotificationCompat.Builder(this);
        builder_auto.setSmallIcon(R.drawable.ic_devices_black_48dp);
        builder_auto.setContentTitle("AutoCancel Notification");
        builder_auto.setContentText("Hello World.");

        Intent resultIntent_auto = new Intent(this, NavigationDrawerActivity.class);
        PendingIntent resultPendingIntent_auto = PendingIntent.getActivity(this, 0, resultIntent_auto, PendingIntent
                .FLAG_UPDATE_CURRENT);

        builder_auto.setAutoCancel(true);
        builder_auto.setContentIntent(resultPendingIntent_auto);
        Button btn_auto_notification = (Button) findViewById(R.id.btn_auto_cancel_notification);
        btn_auto_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID + 1, builder_auto.build());
            }
        });

        Button btn_update_number = (Button) findViewById(R.id.btn_update_number);
        btn_update_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder_auto.setNumber(++number);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID + 1, builder_auto.build());
            }
        });


        final NotificationCompat.Builder builderComplex = new NotificationCompat.Builder(this);
        builderComplex.setSmallIcon(R.drawable.ic_devices_black_48dp);
        builderComplex.setContentTitle("AutoCancel Notification");
        builderComplex.setContentText("Hello World.");
        builderComplex.setDefaults(Notification.DEFAULT_ALL);
        builderComplex.setPriority(Notification.PRIORITY_MAX);
        builderComplex.setProgress(100, 30, false);

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        String[] events = new String[]{
                "ENZO", "IKEDA", "ZENG", "YUHAO"
        };
        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Event tracker details:");
        // Moves events into the expanded layout
        for (int i = 0; i < events.length; i++) {

            inboxStyle.addLine(events[i]);
        }
        //builderComplex.setStyle(inboxStyle);
        Button btn_complex_notification = (Button) findViewById(R.id.btn_complex_notification);
        btn_complex_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID + 2, builderComplex.build());
            }
        });
    }
}
