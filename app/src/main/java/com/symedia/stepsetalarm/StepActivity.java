package com.symedia.stepsetalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class StepActivity extends AppCompatActivity implements SensorEventListener {
    private int count = 0;
    private int maxSteps = 10;
    private TextView stepDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        stepDisplay = findViewById(R.id.steps);
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor stepSensor = sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sm.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            count++;
            stepDisplay.setText("Total Steps: " + count + "\nRemaining Steps: " + (maxSteps - count));
            if(count == maxSteps){
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(StepActivity.this, MyForegroundService.class);
                Intent intent = new Intent(StepActivity.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(StepActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.cancel(pendingIntent);
                stopService(i);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}