package com.just8.apps.whoopee;

import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * BB
 * Created by kandinski on 2015-08-16.
 */
public class WhoopeeGridListener implements
        GestureDetector.OnGestureListener,
        View.OnTouchListener {

    private GestureDetector mGridGestureDetector;
    private View v;
    private WhoopeePageFragment mFragment;
    private boolean wEditMode=false;



    public WhoopeeGridListener(WhoopeePageFragment f) {
        mFragment = f;
        mGridGestureDetector = new GestureDetector(G.CTX, this, G.UIHandler);
        mGridGestureDetector.setIsLongpressEnabled(true);
    }

    public int getCellPosition(MotionEvent me) {
        int position = mFragment.getGridView().pointToPosition((int) me.getX(), (int) me.getY());
        if (position > 7 || position < 0) {
            Log.e(U.getTag(), "pointToPosition  OUT OF BOUNDS "+position) ;
            new Exception("pointToPosition  OUT OF BOUNDS");
            return -1 ;
        }
        else {
            v = mFragment.getGridView().getChildAt(position);
            return position;
        }
    }

    public boolean onTouch(View view, MotionEvent event) {


        int action = event.getAction();

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d(U.getTag(),"Action was DOWN");
            case (MotionEvent.ACTION_MOVE) :
                Log.d(U.getTag(),"Action was MOVE");

            case (MotionEvent.ACTION_UP) :
                Log.d(U.getTag(),"Action was UP");

            case (MotionEvent.ACTION_CANCEL) :
                Log.d(U.getTag(),"Action was CANCEL");
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d(U.getTag(),"Movement occurred outside bounds " +
                        "of current screen element");
            default :
        }

        if(G.G_DEBUG) Log.v( U.getTag(),"\n\nEVENT:"+U.dumpEvent(event));
        mGridGestureDetector.onTouchEvent(event);
        return false;
    }

    public void onLongPress(MotionEvent ev) {
        int position = getCellPosition(ev);
        if(G.DEBUG) Log.v( U.getTag(), " onLongPress  EDIT" + position );
        mFragment.edit(position);
    }


    public boolean onUp(MotionEvent ev) {
        if(G.G_DEBUG) Log.v( U.getTag(),"");

        return false;
    }

    public boolean onDown(MotionEvent ev) {

        int position = getCellPosition(ev);
        if(G.DEBUG) Log.v( U.getTag(), position+ " onDown");
        play(position, ev );

        return true;
    }

    public boolean onSingleTapUp(MotionEvent ev) {
        int position = getCellPosition(ev);
        if(G.DEBUG) Log.v(U.getTag(), position + " onSingleTapUp");
        return true;
    }

    public boolean onSingleTapConfirmed(MotionEvent ev) {
        int position = getCellPosition(ev);
        if(G.DEBUG) Log.v( U.getTag(), position+ " onSingleTapConfirmed");
        return true;
    }


    public boolean onDoubleTapEvent(MotionEvent ev) {
        int position = getCellPosition(ev);
        if(G.DEBUG) Log.v( U.getTag(), position+ " onDoubleTapEvent");
        return true;
    }

    public void onShowPress(MotionEvent ev) {
        int position = getCellPosition(ev);
        if(G.DEBUG) Log.v( U.getTag(), position+ " onShowPress");
    }


    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
        int position1 = getCellPosition(e1);
        int position2 = getCellPosition(e2);
        if(G.DEBUG) Log.v( U.getTag(), "onScroll  "+position1+":"+position2+":"+distanceX+":"+distanceY);
        return true;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
        int position1 = getCellPosition(e1);
        int position2 = getCellPosition(e2);
        if(G.G_DEBUG) Log.v( U.getTag(), "onFling  "+position1+":"+position2+":"+velocityX+":"+velocityY);
        if(G.G_DEBUG) Log.v(U.getTag(), U.dumpEvent(e1));
        if(G.G_DEBUG) Log.v(U.getTag(), U.dumpEvent(e2));
        return true;
    }

    public void play(int position, MotionEvent ev){

        float pressure = ev.getPressure();
        float divisor= 26;
        int xTriggerVal= 0;
        int yTriggerVal= 0;
        int left = v.getLeft();       // the left edge of the view
        int bottom = v.getBottom();   // the bottom of the view (measured from the top of the container)
        int width = v.getWidth();     // width of the view - constant, set in GridView.LayoutParams
        int height = v.getHeight();   // hight
        int x = (int) ev.getX();      // the coordinates of the event in the container
        int y = (int) ev.getY();


        int xOffset =  x - left ;
        int yOffset =  bottom - y;
        int xInterval = width / 50;  // the view is divided into equal intervals and  values mapped for each region
        int yInterval = height / 9; // because soundpool does not respond to volume and pitch values in a linear way
        xTriggerVal = xOffset / xInterval ;
        yTriggerVal = yOffset / yInterval ;
        float pitch = xTriggerVal/divisor;

        //if (pitch > 2) pitch = 2;

        if(G.G_DEBUG) Log.v( U.getTag(), "touch for sound trigger" +
                        "\nPressure:"+ pressure+
                        "\nScreenWidth:"+ U.getScreenWidth()+
                        "\nScreenHeight:"+ U.getScreenHeight()+
                        "\nleft:"+ left+
                        "\nbottom:"+ bottom+
                        "\nwidth:"+ width+
                        "\nheight:"+ height+
                        "\nx:"+ x+
                        "\ny:"+ y+
                        "\nxOffset:"+ xOffset+
                        "\nyOffset:"+yOffset+
                        "\nxInterval:"+ xInterval+
                        "\nyInterval:"+yInterval+
                        "\nxTriggerVal:"+xTriggerVal+
                        "\nyTriggerVal:"+yTriggerVal+
                        "\ndivisor:"+divisor+
                        "\npitch:"+pitch

                //"\n\nEVENT:"+U.toString(event);
        );

        float vol = G.volumeMap.get(yTriggerVal);
        //float pitch = G.pitchMap.get(xTriggerVal);

        try {
            mFragment.mWhoopee.mSoundPool.play(
                    position+1,
                    vol,
                    vol,
                    1,
                    0,
                    pitch);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
