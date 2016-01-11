package es.tio.radarestio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;

public class ProximityIntentReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        Boolean entering = intent.getBooleanExtra(key, false);

        Intent pintent = new Intent(context,MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), pintent, 0);

        Notification notification = new Notification.Builder(context)
                .setContentTitle("Radares DGT")
                .setContentText("Zona vigilada")
                .setContentIntent(pIntent).build();


        // hide the notification after its selected
        // notification.when = System.currentTimeMillis();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.ledARGB = Color.WHITE;
        notification.ledOnMS = 1500;
        notification.ledOffMS = 1500;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);

    }

}