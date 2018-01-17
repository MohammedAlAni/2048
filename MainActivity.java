package collision_testing.uwaterloo.ca.collision_testing;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Created the Relative layout.
        RelativeLayout appLayout = (RelativeLayout) findViewById(R.id.Layout);

        //Creates LineGraphView object.
        LineGraphView lgvAccelerometer = new LineGraphView(getApplicationContext(), 100, Arrays.asList("x","y", "threshold[1]", "threshold[2]" , "threshold[3]", "threshold[4]" , "threshold[5]", "threshold[6]"  ));
        lgvAccelerometer.setVisibility(View.VISIBLE);
        //appLayout.addView((lgvAccelerometer));


        //Sets up relative layout size and background.
        appLayout.getLayoutParams().width = 645;
        appLayout.getLayoutParams().height = 645;
        appLayout.setBackgroundResource(R.drawable.gameboard);


        //Creating GameLoopTask as a function of TimerTask.
        GameLoopTask myGameLoopTask = new GameLoopTask(getApplicationContext(), this, appLayout);
        Timer myGameLoop = new Timer();
        myGameLoop.schedule(myGameLoopTask, 50, 50);

        //Creates a TextView to display hand gestures over top of the background.
        TextView tvCurrentDirection = new TextView(getApplicationContext());
        tvCurrentDirection.setTextColor(Color.BLACK);
        tvCurrentDirection.setTextSize(30);
        tvCurrentDirection.setText("Waiting...");
        appLayout.addView(tvCurrentDirection);


        //Creates motionDetect object to get accelerometer readings.
        AccelerometerSensorEventListener motionDetect = new AccelerometerSensorEventListener(lgvAccelerometer,tvCurrentDirection, myGameLoopTask);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(motionDetect, accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);

    }

}

