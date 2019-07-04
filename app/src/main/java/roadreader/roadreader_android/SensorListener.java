package roadreader.roadreader_android;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;


public class SensorListener implements SensorEventListener {

    protected SensorManager sensorManager;
    protected Sensor accelerometer;
    protected Sensor gyroscope;
    protected ArrayList<Float> ax, ay, az, gx, gy, gz;
    FileWriter accel_writer, gyro_writer;
    HashMap< String, ArrayList<Float> > sensor_data;

    float _ax;
    float _ay;
    float _az;

    private SendSensorData ssd;
    private Timer t;

    ReentrantLock lock;
    /**
     * Constructor for SensorListener
     * @param context Context for CameraActivity
     */
    public SensorListener(Context context) {

        sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensor_data = new HashMap<>();
        ax = new ArrayList<>();
        ay = new ArrayList<>();
        az = new ArrayList<>();
        gx = new ArrayList<>();
        gy = new ArrayList<>();
        gz = new ArrayList<>();

        t = new Timer();


        lock = new ReentrantLock();
    }



    @Override
    public void onSensorChanged(SensorEvent event) {

        lock.lock();

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            _ax = event.values[0];
            _ay = event.values[1];
            _az = event.values[2];

            ax.add(_ax);
            ay.add(_ay);
            az.add(_az);

            //sensorActivity.setTextFields(new Float(event.values[0]),new Float(event.values[1]), new Float(event.values[2]));

            Log.d("accelerometer", event.values[0] + " " + event.values[1] + " " + event.values[2] + "\n");
            try {
                accel_writer.write(ax + " " + ay + " " + az + "\n");
            } catch(Exception e) {}
        }
        else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gx.add(new Float(event.values[0]));
            gy.add(new Float(event.values[1]));
            gz.add(new Float(event.values[2]));
            Log.d("gyroscope", event.values[0] + " " + event.values[1] + " " +
                    event.values[2] + "\n");
            try {
                gyro_writer.write(event.values[0] + " " + event.values[1] + " " + event.values[2] + "\n");
            } catch(Exception e) {}
        }

        lock.unlock();
    }

    public void setListener(SendSensorData sendSensorData) {
        ssd = sendSensorData;
    }

    public void setTimer() {
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(ssd != null){
                    ssd.sendData(_ax, _ay, _az);
                }

            }
        }, 0, 1000);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Safe not to implement
    }

    protected void resume() {
        try {
            //File accel = new File()
            accel_writer = new FileWriter("accel.txt", true);
            gyro_writer = new FileWriter("gyro.txt", true);
        } catch(Exception e) {}

    }

    protected void start() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void stop() {
        sensorManager.unregisterListener(this, accelerometer);
        sensorManager.unregisterListener(this, gyroscope);
        sensorManager.unregisterListener(this);
        t.cancel();
    }

    protected void pause() {
        if(accel_writer != null) {
            try {
                accel_writer.close();
            } catch (Exception e) {}
        }

        if (gyro_writer != null) {
            try {
                gyro_writer.close();
            } catch (Exception e) {}
        }
    }

    public HashMap<String, ArrayList<Float>> get_sensor_data() {
        lock.lock();

        sensor_data.put("ax", new ArrayList<Float>(ax));
        sensor_data.put("ay", new ArrayList<Float>(ay));
        sensor_data.put("az", new ArrayList<Float>(az));
        sensor_data.put("gx", new ArrayList<Float>(gx));
        sensor_data.put("gy", new ArrayList<Float>(gy));
        sensor_data.put("gz", new ArrayList<Float>(gz));
        Log.d("trip", "ax: " + ax);

        lock.unlock();

        return new HashMap<>(sensor_data);

    }

    public void reset_sensor_data() {
        lock.lock();

        sensor_data = new HashMap<>();
        ax = new ArrayList<>();
        ay = new ArrayList<>();
        az = new ArrayList<>();
        gx = new ArrayList<>();
        gy = new ArrayList<>();
        gz = new ArrayList<>();

        lock.unlock();
    }



}
