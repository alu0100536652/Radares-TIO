package es.tio.radarestio;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private ArrayList<InterestPoint> list;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //puntos
        list = new ArrayList<>();
        list.add(new InterestPoint(28.481364, -16.321526, false, "Entrada ETSII"));
        list.add(new InterestPoint(28.482743, -16.322151, false, "Cafeteria ETSII"));
        list.add(new InterestPoint(28.489568, -16.337893, false, "Rotonda"));
        list.add(new InterestPoint(28.482937, -16.314857, false, "Bares"));
        list.add(new InterestPoint(28.493580, -16.383589, false, "Casa"));

        list.add(new InterestPoint(28.490936, -16.375007, false, "Zona controlada por radar (TF-5 KM 15)"));
        list.add(new InterestPoint(28.476200, -16.307223, false, "Zona controlada por radar (TF-13 KM 1)"));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lon = location.getLongitude();
        double lat = location.getLatitude();
        //Toast.makeText(this, "Lon: " + lon + " | Lat: " + lat, Toast.LENGTH_SHORT).show();
        for (InterestPoint item: list) {
            double meters = measure(lat,lon, item.getLatitude(),item.getLongitude());
            if(( meters <= 50)&&(!item.getStatus())) {
                notification(item.getMessage());
                item.setStatus(true);
            }
            else if(( meters > 50)&&(item.getStatus())) {
                item.setStatus(false);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        String message = provider + " status changed to " + status;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        String message = "Provider " + provider + " enabled";
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        String message = "Provider " + provider + " disabled";
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
    }

    private double measure(double lat1, double lon1, double lat2, double lon2)
    {
        double R = 6378.137; // Radius of earth in KM
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // meters
    }

    private void notification(String message)
    {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        final Notification notification = new Notification.Builder(this)
                .setContentTitle("Radares DGT")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_network_check_white_24dp)
                .setContentIntent(pendingIntent).build();
        // hide the notification after its selected
        notification.when = 5000;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.ledARGB = Color.WHITE;
        notification.ledOnMS = 1500;
        notification.ledOffMS = 1500;

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification);

        android.os.Handler h = new android.os.Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationManager.cancel(0);
            }
        },15000);
    }
}
