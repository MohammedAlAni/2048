package collision_testing.uwaterloo.ca.collision_testing;

/**
 * Created by Ryan on 7/3/2017.
 */

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class AccelerometerSensorEventListener implements SensorEventListener{


    private LineGraphView lgvOutput;                //LineGraphView


    float[] filteredReadings = null;                //Reading after low pass filtering
    float filtering_constant = 6f;                  //Filtering constant


    //State machines for x and y axis
    private motionStateMachine xMotionStateMachine;
    private motionStateMachine yMotionStateMachine;


    private GameLoopTask myGameLoopTask = null;


    public AccelerometerSensorEventListener(LineGraphView lineGraphView, TextView tvDirection, GameLoopTask GameLoopTask) {
        super();

        //Sets local LineGraphView
        lgvOutput = lineGraphView;


        filteredReadings = new float[8];


        //X axis threshold (right) and the inverse (left) values for the state machine
        float[] xThreshold = {0.7f,1f ,0.4f};
        float[] xInverseThreshold = {-0.7f,-1f,0.4f};


        //Y axis threshold (up) and the inverse (down) values for the state machine
        float[] yThreshold = {0.7f,1f ,0.4f};
        float[] yInverseThreshold = {-0.7f,-1f,0.4f};


        myGameLoopTask = GameLoopTask;


        //Creates FSM for X and Y axis.
        xMotionStateMachine = new motionStateMachine(tvDirection,xThreshold,xInverseThreshold,"RIGHT", "LEFT", myGameLoopTask );
        yMotionStateMachine = new motionStateMachine(tvDirection,yThreshold,yInverseThreshold,"UP", "DOWN", myGameLoopTask);
    }

    public void onSensorChanged(SensorEvent se)
    {
        if(se.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            try {
                localUpdate(se.values);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //calls local update. Local update must be implemented by the deriving class
        }
    }

    public void localUpdate(float[] values) throws InterruptedException {

        //Applying a low pass filter on the values and allowing the Thresholds to show on LineGraphView.
        filteredReadings[0] += (values[0] - filteredReadings[0]) / filtering_constant;
        filteredReadings[1] += (values[1] - filteredReadings[1]) / filtering_constant;
        filteredReadings[2] = 0.7f;
        filteredReadings[3] = 1f;
        filteredReadings[4] = 0.4f;
        filteredReadings[5] = -0.7f;
        filteredReadings[6] = -1f;
        filteredReadings[7] = -0.4f;

        //Adding a point to the graph
        lgvOutput.addPoint(filteredReadings);

        //Update the state machine based on the filtered values (x and y)
        xMotionStateMachine.update(filteredReadings[0]);
        yMotionStateMachine.update(filteredReadings[1]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
