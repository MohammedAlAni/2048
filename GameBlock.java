package collision_testing.uwaterloo.ca.collision_testing;

/**
 * Created by Ryan on 7/3/2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Ryan on 7/3/2017.
 */

public class GameBlock extends android.support.v7.widget.AppCompatImageView {

    private float IMAGE_SCALE = 0.65f;
    private int myCoordX;
    private int myCoordY;
    private Context myContext;
    private RelativeLayout MyRTL;

    public TextView myTextview;
    private int textviewoffset = 100;

    private int[] coordinates;

    private int XMax = 434;
    private int YMax = 434;
    private int XMin = -55;
    private int YMin = -55;

    public int Value = 0;

    private int newlocationcoordinate[] = {0,0};
    private int blockcounter;


    private GameLoopTask.DIRECTION myDirection;

    private Random RandomGenerator = null;


    public GameBlock(Context mycontext, int coordX, int coordY, RelativeLayout appLayout) {

        //Allowing ImageView to work off of the same Context.
        super(mycontext);

        //Setting local variables.
        myContext = mycontext;
        myCoordX = coordX;
        myCoordY = coordY;
        MyRTL = appLayout;

        //Every GameBlock Created will have these properties.
        this.setImageResource(R.drawable.gameblock);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);

        //Sets the X,Y Coordinates
        this.setX(myCoordX);
        this.setY(myCoordY);

        //Creates a textview to display block value.
        myTextview = new TextView(mycontext);
        myTextview.bringToFront();
        myTextview.setX(myCoordX + textviewoffset);
        myTextview.setY(myCoordY + textviewoffset);
        myTextview.setTextSize(40);
        myTextview.setTextColor(Color.BLACK);

        //Assigns a 'Random' number to block (2 or 4).
        RandomGenerator = new Random();
        Value = (RandomGenerator.nextInt(2)+1 )*2;
        myTextview.setText("" + Value);

        //Adding the Textview to the layout and bringing it to the front.
        appLayout.addView(myTextview);
    }

    public void setBlockDirection(GameLoopTask.DIRECTION direction, int Blockcounter)
    {
        //Sets local Direction
        myDirection = direction;
        blockcounter = Blockcounter;

        switch (myDirection)
        {
            case RIGHT:
                //sets the newlocation or target
                newlocationcoordinate[0] = 434-163*blockcounter;
                break;
            case LEFT:
                //sets the newlocation or target
                newlocationcoordinate[0] = -55 +163*blockcounter;
                break;
            case UP:
                //sets the newlocation or target
                newlocationcoordinate[1] = -55 +163*blockcounter;
                break;
            case DOWN:
                //sets the newlocation or target
                newlocationcoordinate[1] = 434-163*blockcounter;
                break;
        }

        //Make sure that local Direction is set properly
        //Log.d("DIRECTION", String.valueOf(myDirection));
    }

    public void settext()
    {
        myTextview.setText("" + Value);
    }
    public void move()
    {
        //These are the four corners of my personal phone.
        //topleft = -1*55, -1*55
        //topright = 395, -1*55
        //bottomleft = -1*55, 395
        //bottomright = 395, 395
        //405+55 = 460/4 = 115

        if(myDirection == GameLoopTask.DIRECTION.UP)
        {
            if( myCoordY > newlocationcoordinate[1])
            {
                this.setY(myCoordY);
                myTextview.setY(myCoordY+textviewoffset);
                myTextview.bringToFront();
                myTextview.setText("" + Value);

                myCoordY = myCoordY - 30;
            }
            else if(myCoordY <= newlocationcoordinate[1]){
                myCoordY = newlocationcoordinate[1];

                this.setY(myCoordY);
                myTextview.setY(myCoordY+textviewoffset);
                myTextview.bringToFront();
                myTextview.setText("" + Value);
            }
        }
        else if(myDirection == GameLoopTask.DIRECTION.DOWN){

            if( myCoordY < newlocationcoordinate[1])
            {
                this.setY(myCoordY);
                myTextview.setY(myCoordY+textviewoffset);
                myTextview.bringToFront();
                myTextview.setText("" + Value);

                myCoordY = myCoordY + 30;
            }
            else if(myCoordY >= newlocationcoordinate[1]){
                myCoordY = newlocationcoordinate[1];

                this.setY(myCoordY);
                myTextview.setY(myCoordY+textviewoffset);
                myTextview.bringToFront();
                myTextview.setText("" + Value);
            }
        }
        else if(myDirection == GameLoopTask.DIRECTION.RIGHT){

            if( myCoordX < newlocationcoordinate[0])
            {
                this.setX(myCoordX);
                myTextview.setX(myCoordX+textviewoffset);
                myTextview.bringToFront();
                myTextview.setText("" + Value);

                myCoordX = myCoordX + 30;
            }
            else if(myCoordX >= newlocationcoordinate[0]){
                myCoordX = newlocationcoordinate[0];

                this.setX(myCoordX);
                myTextview.setX(myCoordX+textviewoffset);
                myTextview.bringToFront();
                myTextview.setText("" + Value);
            }
        }
        else if(myDirection == GameLoopTask.DIRECTION.LEFT){

            if( myCoordX > newlocationcoordinate[0])
            {
                this.setX(myCoordX);
                myTextview.setX(myCoordX+textviewoffset);
                myTextview.bringToFront();
                myTextview.setText("" + Value);

                myCoordX = myCoordX - 30;
            }
            else if(myCoordX <= newlocationcoordinate[0]){

                myCoordX = newlocationcoordinate[0];

                this.setX(myCoordX);
                myTextview.setX(myCoordX+textviewoffset);
                myTextview.bringToFront();
                myTextview.setText("" + Value);
            }
        }
    }

    public void setwinningtext(String string)
    {
        myTextview.setText(string);
    }

    public void settonull()
    {
        this.setImageDrawable(null);
        myTextview.setText(null);
        myCoordX = -999999;
        myCoordY = -999999;
        myTextview.setX(-1*999999);
        myTextview.setY(-1*999999);
       // myContext = null;
       // MyRTL = null;
        //myDirection = null;
        //RandomGenerator = null;
    }

    public void destroyThisBlock()
    {
        myTextview.setVisibility(View.GONE);
        this.setVisibility(View.GONE);
    }

    //Creat a method to get the current position of the gameblock.
    public int[] GetCoordinates()
    {
        coordinates = new int[2];
        coordinates[0] = myCoordX;
        coordinates[1] = myCoordY;

       // Log.d("Coordinates", String.valueOf(coordinates[0]) + " " + String.valueOf(coordinates[1]));
        return coordinates;
    }

    public int[] GetTargetPosition()
    {
        switch(myDirection)
        {
            case UP:
                return new int[]{myCoordX,YMin};
            case DOWN:
                return new int[]{myCoordX, YMax};
            case RIGHT:
                return new int[]{XMax, myCoordY};
            case LEFT:
                return new int[]{XMin, myCoordY};
            case NO_MOVEMENT:
                return new int[] {999,999};
            default:
                return new int[] {999,999};

        }
    }
}
