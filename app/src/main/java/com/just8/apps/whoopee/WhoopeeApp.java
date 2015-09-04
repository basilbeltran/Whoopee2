package com.just8.apps.whoopee;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;

/**
 * BB
 * Created by kandinski on 2015-08-18.
 */
public class WhoopeeApp extends Application {
    private static WhoopeeApp whoopee;


    public static  WhoopeeApp getInstance(){   return whoopee;  }

    @Override
    public void onCreate() {
        super.onCreate();

        G.CTX = getApplicationContext();            //allow static access to context
        checkDeviceStorage();                       // Is device storage currently readable/writable ?
        checkNewInstall();                          // A new installation needs an application directory
        if (G.newInstall) {                            //Application initialization
            copyFilesToSD();
        }
        setPreferences();
    }//onCreate


    private boolean checkDeviceStorage() {
        try {
            G.sdCard = U.checkExternalMedia();
            if (G.DEBUG) Log.i(U.getTag(), ">>>>>>>>>>>>>  read/write ExternalMedia = " + G.sdCard);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return G.sdCard;
    } //checkDevice


    private boolean checkNewInstall() {
        try {
            File base = U.touchDirectory(G.APPDIR);
            if (base != null){
                G.newInstall = true;
                Log.i(U.getTag(), ">>>>>>>>>>>>>  CREATED Application Directory " + G.APPDIR);
            }else {
                Log.i(U.getTag(), ">>>>>>>>>>>>>  Application Directory FOUND" + G.APPDIR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    } //checkNewInstall


    private boolean copyFilesToSD() { // moves from assets in apk to system or sdcard if available
        try {
            U.touchDirectory(G.APPDIR + G.DEFAULT_NAME);
            U.touchDirectory(G.APPDIR + G.TRAINING_NAME);
            U.CopyAssetDir(this, G.DEFAULT_NAME, G.DEFAULT_PATH);
            U.CopyAssetDir(this, G.TRAINING_NAME, G.TRAINING_PATH);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean setPreferences() {

        try {

            //load local phone number into approvedSMSOriginators list ("numbers")
/*            SharedPreferences numbers = G.CTX.getSharedPreferences("approvedNumbers", Activity.MODE_PRIVATE);
            SharedPreferences.Editor numbersEditor = numbers.edit();
            numbersEditor.clear();
            G.PHONE = G.TEL_MGR.getLine1Number();
            numbersEditor.putString(G.PHONE, "local");
            numbersEditor.commit();*/


            //load example & test SMS macros  ("smsMacros")
            G.MACROS = G.CTX.getSharedPreferences("smsMacros", Activity.MODE_PRIVATE);
            SharedPreferences.Editor spe = G.MACROS.edit();
            spe.putString("01-sounds", "&031505&032505&033505&034505&035505&036505&037505&038505#");
            spe.putString("02-volume", "&037105&037205&037305&037405&037505&037605&037705&037805&037905#");
            spe.putString("03-pitch", "&037501&037502&037503&037504&037505&037506&037507&037508&037509#");
            spe.putString("04-repeat", "&037555#");
            spe.putString("05-text to speech", "$00say excuse me after you&031505");
            spe.commit();

            // SoundPool parameters are difficult to control accurately:
            // the following 2 maps set  ints 0 through 9 to effective values for the SoundPool API
            G.pitchMap.add(new Float(.1)); // position 0
            G.pitchMap.add(new Float(.2));
            G.pitchMap.add(new Float(.3));
            G.pitchMap.add(new Float(.5));
            G.pitchMap.add(new Float(.7));
            G.pitchMap.add(new Float(1));
            G.pitchMap.add(new Float(1.3));
            G.pitchMap.add(new Float(1.5));
            G.pitchMap.add(new Float(1.8));
            G.pitchMap.add(new Float(2));     //position 9
            G.volumeMap.add(new Float(.012)); //position 0
            G.volumeMap.add(new Float(.025));
            G.volumeMap.add(new Float(.05));
            G.volumeMap.add(new Float(.1));
            G.volumeMap.add(new Float(.2));
            G.volumeMap.add(new Float(.3));
            G.volumeMap.add(new Float(1));
            G.volumeMap.add(new Float(1.5));
            G.volumeMap.add(new Float(2));
            G.volumeMap.add(new Float(14.9)); //position 9
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (G.DEBUG) Log.i(U.getTag(), "OK");
        return true;
    }
}//class

