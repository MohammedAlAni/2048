package collision_testing.uwaterloo.ca.collision_testing;

/**
 * Created by Ryan on 7/3/2017.
 */

import android.widget.TextView;


public class motionStateMachine {

    //FSMStates
    enum FSMStates {
        WAIT,               //Waiting for onset of response
        INCREASING,         //Acceleration is increasing ( for primary response)
        DECREASING,         //Acceleration is decreasing ( for inverse response)
        CONFIRM_RESPONSE,   //Confirm response by waiting for values to settle
        END_RESPONSE
    }

    /*Responses Primary and inverse
        basically two opposite motions
        right-left
        up-down
        in-out
    */
    enum MOTION{INVERSE, PRIMARY, NONE}

    //Constants for array lookup
    final int initialSlope =0;
    final int responsePeak = 1;
    final int responseConfirm =  2;

    //Response thresholds. Assigned in the constructor
    private float[] primaryResponseThreshold;
    private float[] invertResponseThreshold;

    //Stores the previous value of acceleration
    private float previousVal;

    //Number of sample to wait to confirm response
    //40 seems to be a reasonable number, but causes noticeable delays
    //Should consider revising
    private final int confirmResponseConstant = 40;
    private int responseConfirmSample ;

    //TextView to set the direction of the motion
    private TextView tvDetectedMotion;
    //String to display for primary and secondary motion (right and left say)
    private String firstMotionString;
    private String invertMotionString;

    //Dected motion and currentState
    private MOTION motionDetected = MOTION.NONE;
    private FSMStates currentState;

    private GameLoopTask myGameLoop;


    public motionStateMachine(TextView tvout, float[] response, float[] inverseResponse, String motionString1, String motionString2, GameLoopTask GameLoopTask) {

        tvDetectedMotion = tvout;
        firstMotionString = motionString1;
        invertMotionString = motionString2;
        primaryResponseThreshold = response;
        invertResponseThreshold = inverseResponse;


        //Sets local myGameLoop.
        myGameLoop = GameLoopTask;


        reset();
    }

    public void reset() {
        currentState = FSMStates.WAIT;
        previousVal = 0;
        motionDetected = MOTION.NONE;
        responseConfirmSample = confirmResponseConstant;
    }

    public void update(float currentAcceleration) throws InterruptedException {

        switch(currentState)
        {
            case WAIT:
                /*if the increase in acceleration is above (or below if negative) the initialSlope
                  expected, then change the state to either increasing or decreasing depending of the
                  direction of the change
                */
                if(currentAcceleration - previousVal>= primaryResponseThreshold[initialSlope]){
                    currentState = FSMStates.INCREASING;
                }
                else if (currentAcceleration - previousVal <= invertResponseThreshold[initialSlope]){
                    currentState = FSMStates.DECREASING;
                }
                break;
            case INCREASING:
                /*Primary motion
                    Wait till the the acceleration starts decreasing
                    Then if the max value (previous value) is greater than the expected response Peak
                    move to the next state

                    Else reset to initial state
                */
                if(currentAcceleration - previousVal <= 0){
                    if(previousVal >= primaryResponseThreshold[responsePeak]) {
                        currentState = FSMStates.CONFIRM_RESPONSE;
                        motionDetected = MOTION.PRIMARY;
                    }
                    else
                    {
                        reset();
                    }
                }
                break;
            case DECREASING:
                /*Inverse motion
                    Wait till the the acceleration starts increasing
                    Then if the max value (previous value) is greater than the expected response Peak
                    move to the next state (since the value is negative, inequalities are changed to
                    account for the sign)

                    Else reset to initial state
                */
                if(currentAcceleration - previousVal >= 0){

                    if(previousVal <= invertResponseThreshold[responsePeak]) {
                        currentState = FSMStates.CONFIRM_RESPONSE;
                        motionDetected = MOTION.INVERSE;
                    }
                    else
                    {
                        reset();
                    }
                }
                break;
            case CONFIRM_RESPONSE:
                //Wait till expected samples come in

                responseConfirmSample--;

                if(responseConfirmSample == 0) {
                    // Depending on which motion we are interested in, compare the responseConfirm values and moe to the next state
                    if     (    motionDetected == MOTION.PRIMARY && Math.abs(currentAcceleration) < primaryResponseThreshold[responseConfirm]
                            ||  motionDetected == MOTION.INVERSE && Math.abs(currentAcceleration) < invertResponseThreshold[responseConfirm] )
                    {
                        //Do Nothing Special
                    }
                    else {
                        motionDetected = MOTION.NONE;
                    }
                    currentState = FSMStates.END_RESPONSE;
                }
                break;
            case END_RESPONSE:
                //Set text based on the motion detected
                switch(motionDetected) {

                    case INVERSE:

                        //This Sets the TextView
                        tvDetectedMotion.setText(invertMotionString);

                        //This sets the correct Direction
                        switch (invertMotionString){
                            case "DOWN":
                                myGameLoop.setDirection(GameLoopTask.DIRECTION.DOWN);
                                break;
                            case "LEFT":
                                myGameLoop.setDirection(GameLoopTask.DIRECTION.LEFT);
                                break;
                        }
                        break;

                    case PRIMARY:

                        //This sets the TextView.
                        tvDetectedMotion.setText(firstMotionString);

                        //This sets the correct Direction
                        switch (firstMotionString){
                            case "UP":
                                myGameLoop.setDirection(GameLoopTask.DIRECTION.UP);
                                break;
                            case "RIGHT":
                                myGameLoop.setDirection(GameLoopTask.DIRECTION.RIGHT);
                                break;
                        }
                        break;
                }

                //Reset to initial state
                reset();
                break;

            default:
                reset();
                break;
        }
        previousVal = currentAcceleration;
    }
}

