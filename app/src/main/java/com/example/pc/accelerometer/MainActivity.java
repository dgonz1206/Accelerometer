package com.example.pc.accelerometer;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity implements SensorEventListener {
    private static final String FILE_NAME = "example.txt";
    private TextView xText, yText, zText;
    private Sensor mySensor;
    private SensorManager SM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create our Sensor Manager
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Register sensor Listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Assign TextView
        //xText = (TextView) findViewById(R.id.xText);
        yText = (TextView) findViewById(R.id.yText);
        zText = (TextView) findViewById(R.id.zText);

        int repeatTime = 30;  //Repeat alarm time in seconds
        AlarmManager processTimer = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, processTimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,  intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Repeat alarm every second
        processTimer.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),repeatTime*1000, pendingIntent);
    }
    //This is called every second (depends on repeatTime)
    public class processTimerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Do something every 30 seconds
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }


    @Override
    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        return super.openFileOutput(name, mode);
    }

    public void writingFile(SensorEvent event){

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        //xText.setText("X: " + sensorEvent.values[0]);
        //yText.setText("Y: " + sensorEvent.values[1]);
        //zText.setText("Z: " + sensorEvent.values[2]);

        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];

        double pitch = Math.atan(x/Math.sqrt(Math.pow(y,2) + Math.pow(z,2)));
        double roll = Math.atan(y/Math.sqrt(Math.pow(x,2) + Math.pow(z,2)));
        //convert radians into degrees
        pitch = pitch * (180.0/3.14);
        roll = roll * (180.0/3.14) ;
        pitch= (double)Math.round(pitch * 10d) / 10;
        roll= (double)Math.round(roll * 10d) / 10d;

        yText.setText("Axis: " + String.valueOf(pitch)+"\u00b0");
        zText.setText("Axis: " + String.valueOf(roll)+"\u00b0");

//        xText.setText("X: " + event.values[0]);
//        yText.setText("Y: " + event.values[1]);
//        zText.setText("Z: " + event.values[2]);
//        File dir = new File(getFilesDir().toString());
//        Boolean dirsMade = dir.mkdir();
//
//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "example.txt");
//
//        String text = "X: " + event.values[0] + "\n" + "Y: " + event.values[1] +  "\n" + "Z: " + event.values[2];
//        FileOutputStream fos = null;

//        try {
//
//            fos = new FileOutputStream(file);
//            //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
//            fos.write(text.getBytes());
//            fos.close();
//            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,
//                    Toast.LENGTH_LONG).show();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }




//        try {
//            FileOutputStream f = new FileOutputStream(file, true);
//            try {
//                f.write(Byte.valueOf("hello world"));
////                f.write(Byte.valueOf("X: ") +(byte)event.values[0]);
////                f.write(Byte.valueOf("Y: ") +(byte)event.values[1]);
////                f.write(Byte.valueOf("Z: ") +(byte)event.values[2]);
//                f.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        catch(FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
