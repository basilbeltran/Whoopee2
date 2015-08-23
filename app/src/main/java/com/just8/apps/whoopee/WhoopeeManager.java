package com.just8.apps.whoopee;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * BB
 * Created by kandinski on 2015-08-16.
 */
public class WhoopeeManager  implements GestureDetector.OnGestureListener, View.OnTouchListener {

    private List<Whoop> mWhoopList = new ArrayList<>();
    private ArrayList<String> soundList = new ArrayList<>();;
    private ArrayList<String> sightList = new ArrayList<>();;
    private WhoopeeFragment mFragment;
    private static final int MAX_SOUNDS = 5;                    //TODO remove sound handling functions
    private SoundPool mSoundPool;
    private GestureDetector mGestureDetector;
    private View v;
    public WhoopeeManager(String dir) {
        mGestureDetector = new GestureDetector(G.CTX, this, G.UIHandler);
        mSoundPool = new SoundPool( MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);  //if sound system is SoundPool
        loadMgrFromDir(G.APPDIR +dir);
        loadWhoopsFromMgr();
    }

    public List<Whoop> getWhoopList() {
        return mWhoopList;
    }

    public void setFragment(WhoopeeFragment f){
    mFragment = f;
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

    private void loadMgrFromDir(String dir) {
        File whoopeeDir = new File(dir);
        String[] art= whoopeeDir.list();
        for (String fName: art) {
            if (fName.startsWith("s")) soundList.add(dir+G.FS+fName);
            else sightList.add(dir+G.FS+fName);
        }
        if (G.DEBUG) Log.d(U.getTag(), sightList.toString() );
        if (G.DEBUG) Log.d(U.getTag(), soundList.toString() );
    }

    private void loadWhoopsFromMgr() {
        for (int cell = 0; cell < 8; cell++) {
            Whoop w = new Whoop(cell, soundList.get(cell), sightList.get(cell));
            mWhoopList.add(w);
            loadWhoopSound(w);
        }
    }

    private void loadWhoopSound(Whoop w) {
        int soundId = mSoundPool.load(w.getSoundFileName(), 1);
        w.setSoundId(soundId);
    }

    public void play(Whoop w) {
        Integer soundId = w.getSoundId();
        if (soundId == null) { return; }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void release() {
        mSoundPool.release();
    }



    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param view  The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */

    public boolean onTouch(View view, MotionEvent event) {
        if(G.G_DEBUG) Log.v( U.getTag(),"\n\nEVENT:"+U.dumpEvent(event));
        mGestureDetector.onTouchEvent(event);
        return false;
    }



    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */

    public void onClick(View v, Whoop whoop) {
        play(whoop);
        if (G.DEBUG) Log.i(U.getTag(), "onClick shows its view..."+ v.toString());
    }

    protected int getViewY(MotionEvent me){
        int offset =  (int) me.getY();
        return offset;
    }



    public boolean onDown(MotionEvent ev) {

        int position = getCellPosition(ev);
        if(G.DEBUG) Log.v( U.getTag(), position+ " onDown");

        int xTriggerVal= 0;
        int yTriggerVal= 0;

        Whoop w = mWhoopList.get(position);
        int soundId = w.getSoundId();
        if(G.G_DEBUG) Log.v( U.getTag(),
                "\nposition "+ position+
                        "\nsoundId " + soundId);



        int left = v.getLeft();       // the left edge of the view
        int bottom = v.getBottom();   // the bottom of the view (measured from the top of the container)
        int width = v.getWidth();     // width of the view - constant, set in GridView.LayoutParams
        int height = v.getHeight();   // hight
        int x = (int) ev.getX();      // the coordinates of the event in the container
        int y = (int) ev.getY();


        int xOffset =  x - left ;
        int yOffset =  bottom - y;
        int xInterval = width / 9;  // the view is divided into equal intervals and  values mapped for each region
        int yInterval = height / 9; // because soundpool does not respond to volume and pitch values in a linear way
        xTriggerVal = xOffset / xInterval ;
        yTriggerVal = yOffset / yInterval ;

        if(G.G_DEBUG) Log.v( U.getTag(), "touch for sound trigger" +
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
                        "\nyTriggerVal:"+yTriggerVal+
                        "\nxTriggerVal:"+xTriggerVal
                //"\n\nEVENT:"+U.toString(event);
        );

        float vol = G.volumeMap.get(yTriggerVal);
        float pitch = G.pitchMap.get(xTriggerVal);


        try {
            mSoundPool.play(soundId,
                    vol,
                    vol,
                    1,
                    0,
                    pitch);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public void onLongPress(MotionEvent ev) {
        int position = getCellPosition(ev);
        if(G.DEBUG) Log.v( U.getTag(), position+ " onLongPress");
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
        if(G.G_DEBUG) Log.v(U.getTag(),  U.dumpEvent(e1));
        if(G.G_DEBUG) Log.v(U.getTag(), U.dumpEvent(e2));
        return true;
    }


}
