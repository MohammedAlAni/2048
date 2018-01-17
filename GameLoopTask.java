package collision_testing.uwaterloo.ca.collision_testing;


import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.LinkedList;
import java.util.Random;
import java.util.TimerTask;

/**
 * Created by Ryan on 7/3/2017.
 */

public class GameLoopTask extends TimerTask{

    int i = 0;
    private LinkedList<GameBlock> GameBlockList;

    private GameBlock myGameBlock;

    enum DIRECTION {UP, DOWN, LEFT, RIGHT, NO_MOVEMENT};
    private DIRECTION myDirection = DIRECTION.NO_MOVEMENT;

    private int XMax = 395;
    private int YMax = 395;
    private int XMin = -55;
    private int YMin = -55;


    private int timer = 18;
    private int wait = 1000;
    private int temp[] = {0,0};

    private int overcount = 0;

    private boolean collisiontesting = false;
    private int collisiondetetor = 0;

    private boolean alreadygone = false;


    private Context myContext = null;
    private MainActivity myActivity = null;
    private RelativeLayout MyRTL = null;
    private Random RandomGenerator = null;

    public GameLoopTask(Context applicationContext, MainActivity mainActivity, RelativeLayout appLayout) {

        //Setting local variables.
        myContext = applicationContext;
        myActivity = mainActivity;
        MyRTL = appLayout;

        //Creates a linked list for the different GameBlocks
        GameBlockList = new LinkedList<>();
        //Creates a Random number Generator
        RandomGenerator = new Random();

        //Creating GameBlock
        createBlock();
    }

    private void createBlock() {


        //Create 'random' coordinates to fit inside the 4x4 grid.
        int RandomXCoord = (RandomGenerator.nextInt(4) )*163 - 55;
        int RandomYCoord = (RandomGenerator.nextInt(4) )*163 - 55;

        int randomcoordinates[] = new int[2];
        randomcoordinates[0] = RandomXCoord;
        randomcoordinates[1] = RandomYCoord;

        while(isOccupied(randomcoordinates))
        {
            randomcoordinates[0] = (RandomGenerator.nextInt(4) )*163 - 55;
            randomcoordinates[1] = (RandomGenerator.nextInt(4) )*163 - 55;
            RandomXCoord = randomcoordinates[0];
            RandomYCoord = randomcoordinates[1];
            //Log.d("WHILE LOOP", "RANDOM SCRAMBLE");
        }

        while(validspot(randomcoordinates) == false)
        {
            randomcoordinates[0] = (RandomGenerator.nextInt(4) )*163 - 55;
            randomcoordinates[1] = (RandomGenerator.nextInt(4) )*163 - 55;
            RandomXCoord = randomcoordinates[0];
            RandomYCoord = randomcoordinates[1];
        }
        //Sets newBlock Context and X, Y Coordinates.
        GameBlock myGameBlock = new GameBlock(myContext, RandomXCoord, RandomYCoord, MyRTL);
        //Adds newBlock image to the layout.
        MyRTL.addView(myGameBlock);

        //Adds the GameBlock to the linked list
        GameBlockList.add(myGameBlock);
        myGameBlock.myTextview.bringToFront();

    }

    public boolean validspot(int coordinates[])
    {
        int counter = 0;
        for (GameBlock gb : GameBlockList) {

            if(gb.GetCoordinates() == coordinates)
            {
                counter++;
            }
        }
        if(counter > 0)
        {
            return false;
        }
        else
        {
            return true;
        }


    }

    public boolean compare( int []block1, int []block2)
    {
        int block1value = 0;
        int block2value = 0;

        for(GameBlock gb : GameBlockList) {

            int gbcoordinates[] = new int[2];
            gbcoordinates = gb.GetCoordinates();

            if(block1[0] == gbcoordinates[0])
            {
                if(block1[1] == gbcoordinates[1])
                {
                    block1value = gb.Value;
                }
            }
        }
        for(GameBlock gb : GameBlockList) {

            int gbcoordinates[] = new int[2];
            gbcoordinates = gb.GetCoordinates();

            if(block2[0] == gbcoordinates[0])
            {
                if(block2[1] == gbcoordinates[1])
                {
                    block2value = gb.Value;
                }
            }
        }

        if((block1value == block2value) && (block1value != 0) && (block2value != 0) ) {
            return true;
        }
        else {
            return false;
        }
    }

    public void merge( int []Block1, int []Block2)
    {
        Log.d("MERGING ALGORITHM", "GGGGGGGGGGGGGGGGGGGG");

        int block1[] = new int[2];
        block1 = Block1;

        int block2[] = new int[2];
        block2 = Block2;

        for(GameBlock gb1 : GameBlockList) {

            int gbcoordinates[] = new int[2];
            gbcoordinates = gb1.GetCoordinates();

            if(block2[0] == gbcoordinates[0])
            {
                Log.d("MERGING ALGORITHM", "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
                if(block2[1] == gbcoordinates[1])
                {
                    gb1.Value = (gb1.Value) * 2;
                    gb1.settext();
                }
            }
        }

        for(GameBlock gb1 : GameBlockList) {

            int gbcoordinates[] = new int[2];
            gbcoordinates = gb1.GetCoordinates();

            if(block1[0] == gbcoordinates[0])
            {
                if(block1[1] == gbcoordinates[1])
                {
                    gb1.destroyThisBlock();
                    gb1.settonull();
                    gb1 = null;
                    GameBlockList.remove(gb1);
                    //System.gc();
                }
            }
        }
    }

    public void setDirection(DIRECTION direction) {

        //This is a setter method.
        DIRECTION myDirection = direction;

        int blockcounter = 0;

        int check1[] = new int[2];
        int check2[] = new int[2];
        int check3[] = new int[2];
        int check4[] = new int[2];

        boolean array[][] = new boolean[4][4];
        boolean coord[][] = new boolean[4][4];

        switch (myDirection) {
            case RIGHT:
            case LEFT:
                for (int a = 0; a < 4; a++) {
                    check1[1] = a * 163 - 55;
                    check2[1] = a * 163 - 55;
                    check3[1] = a * 163 - 55;
                    check4[1] = a * 163 - 55;


                    check1[0] = -55;
                    check2[0] = 108;
                    check3[0] = 271;
                    check4[0] = 434;
                    if (isOccupied(check1) && isOccupied(check2) && isOccupied(check3) && isOccupied(check4)) //If all 4 are the same
                    {
                        if (compare(check1, check2) && compare(check3, check4)) {
                            merge(check1,check2);
                            merge(check3, check4);
                            break;
                        }
                    }

                    check1[0] = -55;
                    check2[0] = 108;
                    if (isOccupied(check1) && isOccupied(check2)) // case 1
                    {
                        Log.d("MERGING ALGORITHM", "111111111111111111111");
                        if (compare(check1, check2)) {
                            Log.d("MERGING ALGORITHM", "ZZZZZZZZZZZZZZZZZZZZZ");
                            merge(check1, check2);
                            break;
                        }
                    }

                    check1[0] = -55;
                    check2[0] = 108;
                    check3[0] = 271;
                    if (isOccupied(check1) && !isOccupied(check2) && isOccupied(check3)) // case 2
                    {
                        Log.d("MERGING ALGORITHM", "22222222222222222222222");
                        if (compare(check1, check3)) {
                            Log.d("MERGING ALGORITHM", "ZZZZZZZZZZZZZZZZZZZZZ");
                            merge(check1, check3);
                            break;
                        }
                    }

                    check1[0] = -55;
                    check2[0] = 108;
                    check3[0] = 271;
                    check4[0] = 434;
                    if (isOccupied(check1) && !isOccupied(check2) && !isOccupied(check3) && isOccupied(check4)) // case 3
                    {
                        Log.d("MERGING ALGORITHM", "33333333333333333333333");
                        if (compare(check1, check4)) {
                            Log.d("MERGING ALGORITHM", "ZZZZZZZZZZZZZZZZZZZZZ");
                            merge(check1, check4);
                            break;
                        }
                    }

                    check1[0] = 108;
                    check2[0] = 271;
                    if (isOccupied(check1) && isOccupied(check2)) // case 4
                    {
                        Log.d("MERGING ALGORITHM", "444444444444444444444444");
                        if (compare(check1, check2)) {
                            Log.d("MERGING ALGORITHM", "ZZZZZZZZZZZZZZZZZZZZZ");
                            merge(check1, check2);
                            break;
                        }
                    }

                    check1[0] = 271;
                    check2[0] = 434;
                    if (isOccupied(check1) && isOccupied(check2)) // case 5
                    {
                        Log.d("MERGING ALGORITHM", "5555555555555555555555555");
                        if (compare(check1, check2)) {
                            Log.d("MERGING ALGORITHM", "ZZZZZZZZZZZZZZZZZZZZZ");
                            merge(check1, check2);
                            break;
                        }
                    }

                    check1[0] = 108;
                    check2[0] = 271;
                    check3[0] = 434;
                    if (isOccupied(check1) && !isOccupied(check2) && isOccupied(check3)) // case 6
                    {
                        Log.d("MERGING ALGORITHM", "6666666666666666666666666");
                        if (compare(check1, check3)) {
                            Log.d("MERGING ALGORITHM", "ZZZZZZZZZZZZZZZZZZZZZ");
                            merge(check1, check3);
                            break;
                        }
                    }
                }
                break;
            case UP:
            case DOWN:
                for (int a = 0; a < 4; a++) {
                    check1[0] = a * 163 - 55;
                    check2[0] = a * 163 - 55;
                    check3[0] = a * 163 - 55;
                    check4[0] = a * 163 - 55;

                    check1[1] = -55;
                    check2[1] = 108;
                    check3[1] = 271;
                    check4[1] = 434;
                    if (isOccupied(check1) && isOccupied(check2) && isOccupied(check3) && isOccupied(check4)) //If all 4 are the same
                    {
                        if (compare(check1, check2) && compare(check3, check4)) {
                            merge(check1,check2);
                            merge(check3, check4);
                            break;
                        }
                    }

                    check1[1] = -55;
                    check2[1] = 108;
                    if (isOccupied(check1) && isOccupied(check2)) // case 1
                    {
                        if (compare(check1, check2)) {
                            merge(check1, check2);
                            break;
                        }
                    }

                    check1[1] = -55;
                    check2[1] = 108;
                    check3[1] = 271;
                    if (isOccupied(check1) && !isOccupied(check2) && isOccupied(check3)) // case 2
                    {
                        if (compare(check1, check3)) {
                            merge(check1, check3);
                            break;
                        }
                    }

                    check1[1] = -55;
                    check2[1] = 108;
                    check3[1] = 271;
                    check4[1] = 434;
                    if (isOccupied(check1) && !isOccupied(check2) && !isOccupied(check3) && isOccupied(check4)) // case 3
                    {
                        if (compare(check1, check4)) {
                            merge(check1, check4);
                            break;
                        }
                    }

                    check1[1] = 108;
                    check2[1] = 271;
                    if (isOccupied(check1) && isOccupied(check2)) // case 4
                    {
                        if (compare(check1, check2)) {
                            merge(check1, check2);
                            break;
                        }
                    }

                    check1[1] = 271;
                    check2[1] = 434;
                    if (isOccupied(check1) && isOccupied(check2)) // case 5
                    {
                        if (compare(check1, check2)) {
                            merge(check1, check2);
                            break;
                        }
                    }

                    check1[1] = 108;
                    check2[1] = 271;
                    check3[1] = 434;
                    if (isOccupied(check1) && !isOccupied(check2) && isOccupied(check3)) // case 6
                    {
                        if (compare(check1, check3)) {
                            merge(check1, check3);
                            break;
                        }
                    }
                }
                break;
        }


        //Cycles through all gameblocks and sets the direction before creating a new block.
        for(GameBlock gb : GameBlockList)
        {
            // get coordinates fo current gameblock.
            int coordinates[] = new int[2];
            coordinates = gb.GetCoordinates();

            switch (myDirection)
            {
                case RIGHT:
                    for(GameBlock gb1 : GameBlockList) {
                        int gb1coordinates[] = new int[2];
                        gb1coordinates = gb1.GetCoordinates();

                        if(coordinates == gb1coordinates) { // if it is the same gameblock
                            overcount++;
                        }
                        else
                        {
                            if(coordinates[1] == gb1coordinates[1]) // if the y coordinates are the same (Same row)
                            {
                                if(coordinates[0] < gb1coordinates[0]) //if the X coordinate is further to the right than the OG block
                                {
                                    blockcounter++;
                                }
                            }
                        }
                    }
                    gb.setBlockDirection(myDirection, blockcounter); // blockcounter positioning
                    blockcounter = 0; //reset blockcounter
                    overcount = 0;
                    break;
                case LEFT:
                    for(GameBlock gb1 : GameBlockList) {
                        int gb1coordinates[] = new int[2];
                        gb1coordinates = gb1.GetCoordinates();

                        if(coordinates == gb1coordinates) { // if it is the same gameblock
                            overcount++;
                        }
                        else
                        {
                            if(coordinates[1] == gb1coordinates[1]) // if the y coordinates are the same (Same row)
                            {
                                if(coordinates[0] > gb1coordinates[0]) //if the X coordinate is further to the left than the OG block
                                {
                                    blockcounter++;
                                }
                            }
                        }
                    }
                    gb.setBlockDirection(myDirection, blockcounter); // blockcounter positioning
                    blockcounter = 0; //reset blockcounter
                    overcount = 0;
                    break;
                case UP:
                    for(GameBlock gb1 : GameBlockList) {

                        int gb1coordinates[] = new int[2];
                        gb1coordinates = gb1.GetCoordinates();

                        if(coordinates == gb1coordinates) { // if it is the same gameblock
                            overcount++;
                        }
                        else
                        {
                            if(coordinates[0] == gb1coordinates[0]) // if the x coordinates are the same (Same column)
                            {
                                if(coordinates[1] > gb1coordinates[1]) //if the Y coordinate is further UP than the OG block
                                {
                                    blockcounter++;
                                }
                            }
                        }
                    }
                    gb.setBlockDirection(myDirection, blockcounter); // blockcounter positioning
                    blockcounter = 0; //reset blockcounter
                    overcount = 0;
                    break;
                case DOWN:
                    for(GameBlock gb1 : GameBlockList) {

                        int gb1coordinates[] = new int[2];
                        gb1coordinates = gb1.GetCoordinates();

                        if(coordinates == gb1coordinates) { // if it is the same gameblock
                            overcount++;
                        }
                        else
                        {
                            if(coordinates[0] == gb1coordinates[0]) // if the x coordinates are the same (Same column)
                            {
                                if(coordinates[1] < gb1coordinates[1]) //if the Y coordinate is further UP than the OG block
                                {
                                    blockcounter++;
                                }
                            }
                        }
                    }
                    gb.setBlockDirection(myDirection, blockcounter); // blockcounter positioning
                    blockcounter = 0; //reset blockcounter
                    overcount = 0;
                    break;
                case NO_MOVEMENT:
                    break;
            }
        }
        collisiontesting = true;

        // Adds a new block everytime a motion is detected.
        //createBlock();
    }

    @Override
    public void run() {
        myActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        //Confirms Run function works every 50ms
                        //Log.d("Time", String.valueOf(i));
                        //i++;

                        if(collisiontesting == false)
                        {
                            //Cycles through all gameblocks and tells them to move due to the gesture.
                            for(GameBlock gb : GameBlockList)
                            {
                                gb.move();
                            }
                        }
                        else if(collisiontesting == true)
                        {
                            for(GameBlock gb : GameBlockList)
                            {
                                gb.move();
                            }
                            collisiondetetor++;
                            if(collisiondetetor >= 17)
                            {
                                collisiontesting = false;
                                collisiondetetor =0;
                                createBlock();
                            }
                        }
                        if(loose())
                        {
                            for(GameBlock gb : GameBlockList)
                            {
                                    for(GameBlock gb2 : GameBlockList)
                                    {
                                        String string = "L";
                                        gb2.setwinningtext(string);
                                        Log.d("WINNING", "WONNNNNN");
                                    }
                                }

                        }
                        for(GameBlock gb : GameBlockList)
                        {
                            if(gb.Value >= 30000)
                            {
                                for(GameBlock gb2 : GameBlockList)
                                {
                                    String string = "WIN";
                                    gb2.setwinningtext(string);
                                    Log.d("WINNING", "WONNNNNN");
                                }
                            }
                        }

                    }
                }
        );
    }
    public boolean loose(){
        if(GameBlockList.size() == 16)
        {
            for(int i =0; i<4; i++)
            {
                for(int a = 0; a < 4; a++)
                {
                    int x = a*163-55;
                    int y = i*163-55;

                    if(i ==0)
                    {
                        if(getValueAt(x,y) == getValueAt(x, (y+163)))
                        {
                            return false;
                        }

                    }
                    if(a ==0)
                    {
                        if(getValueAt(x,y) == getValueAt((x+163), y))
                        {
                            return false;
                        }
                    }
                    if(a ==3)
                    {
                        if(getValueAt(x,y) == getValueAt((x-163), y))
                        {
                            return false;
                        }
                    }
                    if(i ==3)
                    {
                        if(getValueAt(x,y) == getValueAt(x, (y-163)))
                        {
                            return false;
                        }
                    }
                    if((x+163 <=434 && getValueAt(x,y) == getValueAt(x+163,y) )||
                            ((x-163>=-55) &&   getValueAt(x,y) == getValueAt(x-163,y)) ||
                            ( (y+163 <=434) && getValueAt(x,y) == getValueAt(x,y+163) ) ||
                            ((y-163 >= -55) && getValueAt(x,y) == getValueAt(x,y-163)) )
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        else
            return false;

    }
    public boolean isOccupied(int coordinates[])
    {
        boolean occupied = false;

        //Searches through all of the gameblocks in the linked list
        for(GameBlock gb : GameBlockList)
        {
            int gb1coordinates[] = new int[2];
            gb1coordinates = gb.GetCoordinates();

            //if coordinates are the same then avaiable is true.
            if(coordinates[0] == gb1coordinates[0] )
            {
                if(coordinates[1] == gb1coordinates[1] )
                {
                    occupied = true;
                }
            }

        }
        return occupied;
    }
    public int getValueAt(int x, int y)
    {
        boolean occupied = false;

        //Searches through all of the gameblocks in the linked list
        for(GameBlock gb : GameBlockList)
        {
            int gb1coordinates[] = new int[2];
            gb1coordinates = gb.GetCoordinates();

            //if coordinates are the same then avaiable is true.
            if(x == gb1coordinates[0] )
            {
                if(y == gb1coordinates[1] )
                {
                    return gb.Value;
                }
            }

        }
        return -1;
    }
    public void nothing()
    {
        ///use the isOccupy method.
        //if occupied, returns
        //get current position and get target position
        //make sure block is not created on a current position of a another block.
        //
        //random.nextInt(10) for 11 open spaces, not occupied.
        //create a 4x4 boolean array inside the gameblock method, iterate through the linked list and find the target
        //position of every element inside the list.
        //blocks that are already occupied return a true, empty spaces return false.
        //anything inside gameblock get deleted -> don't use/ do global
        //give the command to move the block, then delete/remove other blocks.
        //always use target
        //MERGING ALGORITHM
        //WHEN MOVING TO RIGHT
        //There are 3 cases, 0 merge, 1 merge or 2 merge - per row.
        //there are different case scenarios for 1 merge or 0 merge but that doesn't matter.
        //FOR 1 MERGE
        //42-2
        //2-22
        //4-42
        //2242
        //list out all the merges
        //FOR 0 MERGE
        //---2
        //4--2    x3
        //4-82    x3
        //8424
        //FOR 2 MERGE
        // RETURN mytarget position and myNumber for other blocks = mytarget position and mynumber merging block.
        //allows for proper merging.
        //numbers need to merge after position is set the same after merge.
        //in order to do so, the guy who marks itself to merge is makred ToBeRemoved (Both image and text).
        //the other remaining block number gets doubled in the proper merged position.
        //HOW DO YOU REMOVE THE GAMEBLOCK?
        //have to make sure every reference in the gameblock is set to null ********
        //GameBlock1 = null;       ????
        //










//        switch (myDirection)
//        {
//            case UP:
//                //Checks to see how many other blocks are in the gameblocks path in the other 3 or less than 3 positions.
//                int [] Up =  gb.GetCoordinates();
//                if(Up[1] == YMin) {
//                }
//                else
//                {
//                    while (Up[1] >= YMin) {
//                        Up[1] = Up[1] + 112;
//                        if (isOccupied(Up)) {
//                            blockcount++;
//                        }
//                    }
//                }
//                break;
//            case DOWN:
//                //Checks to see how many other blocks are in the gameblocks path in the other 3 or less than 3 positions.
//                int [] Down =  gb.GetCoordinates();
//                if(Down[1] == YMax) {
//                }
//                else
//                {
//                    while (Down[1] <= YMax) {
//                        Down[1] = Down[1] - 112;
//                        if (isOccupied(Down)) {
//                            blockcount++;
//                        }
//                    }
//                }
//                break;
//
//            case RIGHT:
//                //Checks to see how many other blocks are in the gameblocks path in the other 3 or less than 3 positions.
//                int [] Right =  gb.GetCoordinates();
//                if(Right[0] == XMax) {
//                }
//                else
//                {
//                    while (Right[1] <= XMax) {
//                        Right[1] = Right[1] + 112;
//                        if (isOccupied(Right)) {
//                            blockcount++;
//                        }
//                    }
//                }
//                break;
//            case LEFT:
//                //Checks to see how many other blocks are in the gameblocks path in the other 3 or less than 3 positions.
//                int [] Left =  gb.GetCoordinates();
//                if(Left[0] == XMin) {
//                }
//                else
//                {
//                    while (Left[1] >= YMin) {
//                        Left[1] = Left[1] - 112;
//                        if (isOccupied(Left)) {
//                            blockcount++;
//                        }
//                    }
//                }
//                break;
//
//            case NO_MOVEMENT:
//                break;
//
//        }
    }
    public void nothing1()
    {

    }
}

