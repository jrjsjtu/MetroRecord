package com.example.metrorecord;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Accelerometer {
 	SensorManager mSensorManager = null;
 	Sensor aSensor = null;
 	Sensor mSensor = null;
 	Sensor oSensor = null;
 	
    float[] accelerometerValues={0,0,0};  
    float[] magneticFieldValues={0,0,0};  
    float[] orientationvalues=new float[3];  
    float[] rotate=new float[9]; 
    float[] trueacceleration = {0,0,0};
    float[] linear_acce = {0,0,0};
    
    private float[] gravityValues = null;
    private float[] magneticValues = null;
    long last_time;

    float speed_x=0,speed_y=0;
 	
    public BufferedWriter out;
    String mpath;
 	public Accelerometer(SensorManager the_sensor,String path){
 		mSensorManager = the_sensor;
 		mpath = path;
      	aSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      	mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
      	oSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        
        last_time = System.currentTimeMillis();
 	}
 	
 	public void start_record(){
 		try {
 	 		Date time = new Date();
 	 		DateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
			out = new BufferedWriter(new FileWriter(mpath+"/"+format.format(time)+".txt",true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      	mSensorManager.registerListener(myListener, aSensor, SensorManager.SENSOR_DELAY_UI);  
        mSensorManager.registerListener(myListener, mSensor, SensorManager.SENSOR_DELAY_UI); 
        mSensorManager.registerListener(myListener, oSensor, SensorManager.SENSOR_DELAY_UI);  
 	}
 	public void stop_record(){
 		try {
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		mSensorManager.unregisterListener(myListener);
 	}
 	final SensorEventListener myListener=new SensorEventListener(){  
  	  
        public void onAccuracyChanged(Sensor sensor, int accuracy) {  
            // TODO Auto-generated method stub
        	
        }  
  
        public void onSensorChanged(SensorEvent event) {  
            if ((gravityValues != null) && (magneticValues != null) 
                    && (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)) {
            	float[] deviceRelativeAcceleration = new float[4];
                deviceRelativeAcceleration[0] = event.values[0];
                deviceRelativeAcceleration[1] = event.values[1];
                deviceRelativeAcceleration[2] = event.values[2];
                deviceRelativeAcceleration[3] = 0;

                    // Change the device relative acceleration values to earth relative values
                    // X axis -> East
                    // Y axis -> North Pole
                    // Z axis -> Sky

                float[] R = new float[16], I = new float[16], earthAcc = new float[16];
                SensorManager.getRotationMatrix(R, I, gravityValues, magneticValues);
                float[] inv = new float[16];
                android.opengl.Matrix.invertM(inv, 0, R, 0);
                android.opengl.Matrix.multiplyMV(earthAcc, 0, inv, 0, deviceRelativeAcceleration, 0);
                Log.d("Acceleration", "Values: (" + earthAcc[0] + ", " + earthAcc[1] + ", " + earthAcc[2] + ")");  
                String output = null;
                output = (new Date()).getTime()+" "+earthAcc[0]+" "+earthAcc[1]+" "+earthAcc[2]+" "+ event.values[0]
                		+" "+event.values[1]+" "+event.values[2]+"\r\n";
                try {
					out.write(output);
					Log.d("write", "success");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                } else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
                    gravityValues = event.values;
                } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    magneticValues = event.values;
                } 
        }
    };  
}
