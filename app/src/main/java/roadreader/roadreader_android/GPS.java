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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;

public class GPS implements LocationListener {

    LocationManager locationManager;
    LocationListener locationListener;
    SensorListener sensor;
    Trip trip;
    GPSTask gpsTask;

    public GPS(Context context, String id) {

        trip = new Trip(id);
        sensor = new SensorListener(context);

        final long start_time = System.currentTimeMillis();

        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double lng = location.getLongitude();
                double lat = location.getLatitude();
                long time = System.currentTimeMillis() - start_time;
                Log.d("trip", "lat: " + lat + ", lng: " + lng + ", " +
                        "time: " + time + "\n");
                try {
                    Log.d("trip", "Adding GPS Point...");
                    trip.addGPSPoint(sensor.get_sensor_data(), lat, lng, time);
                    Log.d("trip", "Added.");
                } catch (Exception e) {
                    Log.d("trip", "Could not add GPS Point");
                    Log.d("trip", e.toString());
                    e.printStackTrace();
                    System.exit(-1);
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
            Log.d("trip", "Location disabled\n");
            return;
        }
        gpsTask = new GPSTask();
        sensor.start();
        gpsTask.execute();
        //locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
        //locationManager.requestSingleUpdate(locationProvider, locationListener);

    }

    public void track_on (Context context) {

    }

    public void track_off(Context context) {

    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }



    class GPSTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() throws SecurityException {
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            String locationProvider = LocationManager.GPS_PROVIDER;

            Looper.prepare();


            try {
                Log.d("trip", "Updating Location...");
                locationManager.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
                Log.d("trip", "Location Updated");
            } catch (SecurityException s) {
                Log.d("trip", "Failed to get GPS permission");
                return false;
            };


            Looper.loop();

            return true;
        }
    }











    protected void resume() {
        sensor.resume();
    }

    protected void start() {
        sensor.start();
    }

    protected void stop() {
        locationManager.removeUpdates(locationListener);
        gpsTask.cancel(true);
        sensor.stop();
    }

    protected void pause() {
        sensor.pause();
    }

    public Trip getTrip() {
        return trip;
    }

    @Override
    public void onLocationChanged(Location location) {
        //locationListener.onLocationChanged(location);
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
