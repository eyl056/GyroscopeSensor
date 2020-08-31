package org.techtown.gyroscopesensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private SensorEventListener gyroListener;
    private Sensor mGyroscope;

    private double roll;
    private double pitch;
    private double yaw;

    private double timestamp=0.0;
    private double dt;

    private double rad_to_dgr = 180/Math.PI;
    private static final float NS2S = 1.0f/1000000000.0f;

    TextView x;
    TextView y;
    TextView z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        x = (TextView)findViewById(R.id.x);
        y = (TextView)findViewById(R.id.y);
        z = (TextView)findViewById(R.id.z);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gyroListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                double gyroX = sensorEvent.values[0];
                double gyroY = sensorEvent.values[1];
                double gyroZ = sensorEvent.values[2];

                dt = (sensorEvent.timestamp - timestamp)*NS2S;
                timestamp = sensorEvent.timestamp;

                if(dt - timestamp*NS2S != 0) {
                    pitch = pitch + gyroY*dt;
                    roll = roll + gyroX*dt;
                    yaw = yaw + gyroZ*dt;

                    x.setText("[roll]"+String.format("%.lf",roll*rad_to_dgr));
                    y.setText("[Pitch]"+String.format("%.lf",pitch*rad_to_dgr));
                    z.setText("[yaw]"+String.format("%.lf",yaw*rad_to_dgr));
                }
                else {
                    x.setText("No X");
                    y.setText("No Y");
                    z.setText("No Z");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(gyroListener, mGyroscope, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(gyroListener);
    }

    protected void onStop() {
        super.onStop();
    }
}