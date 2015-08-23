package com.just8.apps.whoopee;

import android.util.Log;

/**
 * BB
 * Created by kandinski on 2015-08-22.
 */
public class FullSoundManager {

    public void  play(final int delay,
                                          int position,
                                          int leftVolume,
                                          int rightVolume,
                                          int loop,
                                          int speed){

      Float mapLeftVolume = null;
      Float mapRightVolume = null;
      Float mapSpeed = null;

                try{
                    mapLeftVolume = G.volumeMap.get(leftVolume);
                }catch(Exception x) {  U.error(this, "LEFT VOL ISSUE", x);  }

                try{
                    mapRightVolume = G.volumeMap.get(rightVolume);
                }catch(Exception x)  {   U.error(this, "RIGHT VOL ISSUE", x);  }

                try{
                    mapSpeed  = G.pitchMap.get(speed);
                }catch(Exception x) {   U.error(this, "PITCH ISSUE", x);  }

                if(position < 0 || position >7) {
                    Log.e(U.getTag(), Integer.toString(position) + " OUT OF BOUNDS");
                    return;
                }

                if ( G.volumeMap.get(leftVolume) == null  ) {
                    Log.d(U.getTag(), "read left volume "+leftVolume+"Please use ints 0-9 for volume when calling play(...)" );
                    mapLeftVolume = G.volumeMap.get(1);
                }

                if ( G.volumeMap.get(rightVolume) == null  ) {
                    Log.d(U.getTag(), "read right volume "+rightVolume+"Please use ints 0-9 for volume when calling play(...)" );
                    mapRightVolume = G.volumeMap.get(1);
                }

                if ( G.pitchMap.get(speed) == null ) {
                    Log.d(U.getTag(), "read speed "+speed+"Please use ints 0-9 for  speed when calling play(...)" );
                    mapSpeed  = G.pitchMap.get(5);
                }


                playFastSound (delay,
                        position,
                        mapLeftVolume.floatValue(),
                        mapRightVolume.floatValue(),
                        loop,
                        mapSpeed.floatValue());

                }


    public static void playFastSound(final int delay,
                                     final int index,
                                     final float leftVolume,
                                     final float rightVolume,
                                     final int loop,
                                     final float speed)
    {
        FullSoundThread.handler.post(new Runnable() {
            public void run() {
                try {
                    if (G.A_DEBUG) Log.d(U.getTag(), "sleeping " + delay * 1000);
                    Thread.sleep(delay * 1000);
                    //int streamID = spPool.play(spMap.get(index), leftVolume, rightVolume, 1, loop, speed);
                    //if (G.A_DEBUG) Log.d(U.getTag(), "SP playing streamID " + streamID + " from " + index + " maps to " + spMap.get(index));
                } catch (Exception e) {
                    Log.e(U.getTag(), U.getStackTrace(e));
                    return;
                }
            }// run
        }); // runnable
    }//playSound

}//class
