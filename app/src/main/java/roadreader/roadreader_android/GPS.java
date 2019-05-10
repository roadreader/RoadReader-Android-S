package roadreader.roadreader_android;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;

public class GPS implements LocationListener {

    LocationManager locationManager;
    LocationListener locationListener;
    SensorListener sensor;
    Trip trip;
    Date date;

    public GPS(Context context) {

        trip = new Trip();
        sensor = new SensorListener(context);

        date = new Date();
        final long start = date.getTime();

        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double lng = location.getLongitude();
                double lat = location.getLatitude();
                long time = date.getTime() - start;
                Log.d("GPS", "lat: " + lat + ", lng: " + lng + "\n" +
                        time + "\n");
                try {
                    trip.addGPSPoint(sensor.get_sensor_data(), lat, lng, time);
                } catch (Exception e) {
                }
                ;
                sensor.reset_sensor_data();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        String locationProvider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            showAlert(context);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(locationProvider, 1000, 0, locationListener);

    }

    private void showAlert(final Context context) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Enable Location");
        dialog.setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                "use this app");
        dialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(myIntent);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            }
        });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    protected void resume() {
        sensor.resume();
    }

    protected void start() {
        sensor.start();
    }

    protected void stop() {
        sensor.stop();
    }

    protected void pause() {
        sensor.pause();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
