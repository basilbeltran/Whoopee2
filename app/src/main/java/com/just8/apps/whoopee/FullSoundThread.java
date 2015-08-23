package com.just8.apps.whoopee;

/**
 * @author basil
 *
 */

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public  class FullSoundThread extends Thread {


    static public Handler handler;
    private int totalQueued;
    private int totalCompleted;

    public FullSoundThread(String name) {
        super(name); // MEDIA  LOGGING TOKEN 
    }

    public  Handler defineHandler(){
        return new Handler();
    }


    @Override
    public void run() {
        try {
            // preparing a looper on current thread         
            // the current thread is being detected implicitly
            Looper.prepare();
            Log.i(U.getTag(), " entering the loop");

            // the handler will automatically bind to the
            // Looper that is attached to the current thread
            // You don't need to specify the Looper explicitly
            handler = defineHandler();

            // After the following line the thread will start
            // running the message loop and will not normally
            // exit the loop unless a problem happens or you
            // quit() the looper (see below)
            Looper.loop();

            Log.i(U.getTag(), "exiting gracefully");
        } catch (Exception e) {
            Log.e(U.getTag(), " halted due to an error\n"+ U.getStackTrace(e));
        }
    }



    // This method is allowed to be called from any thread
    public synchronized void requestStop() {
        // using the handler, post a Runnable that will quit()
        // the Looper attached to our this Thread
        // all previously queued tasks will be executed
        // before the loop gets the quit Runnable
        handler.post(new Runnable() {
            public void run() {
                // This is guaranteed to run on this thread
                // so we can use myLooper() to get its looper
                Log.i(U.getTag(), "FullSoundThread loop quitting by request");
                Looper.myLooper().quit();
            }
        });
    }



    public synchronized int getTotalQueued() {
        return totalQueued;
    }

    public synchronized int getTotalCompleted() {
        return totalCompleted;
    }


}
