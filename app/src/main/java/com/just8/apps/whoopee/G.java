package com.just8.apps.whoopee;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * BB
 * Created by kandinski on 2015-08-18.
 */
public class G {     //  G...G is for G
    public static String        TAG = "WHOOPEE";                            //default Tag
    public static String        APPNAME = TAG;                              //default Tag
    public static String        configJson = TAG+"FILE.json";               //config file name
    public static boolean       configIsExternal  =  false;
    public static boolean       sdCard;
    public static final String  LF = System.getProperty("line.separator");       //expecting  "\n"
    public static final String  FS = System.getProperty("file.separator");       //expecting  "/"
    public static final String  PS = System.getProperty("path.separator");       //expecting  ":"
    public static boolean       DEBUG = true;       //  use thread interruptions for Thread:Class:Method tagging
    public static boolean       A_DEBUG = true;     //   "      Details of the audio subsystem
    public static boolean       V_DEBUG = true;     //   "      Details of the image subsystem
    public static boolean       G_DEBUG = true;     //   "      Details of the gesture / touch subsystem
    public static Context       CTX;
    //public static String        ExternalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String        ExternalStorage = "/mnt/sdcard";
    public static final String  APPDIR = ExternalStorage +FS+TAG+FS;            // "/mnt/sdcard/WHOOPEE/"
    public static boolean newInstall = false;
    public static final String  DEFAULT_NAME = "default";
    public static final String  DEFAULT_PATH = APPDIR + DEFAULT_NAME + FS;      // "/mnt/sdcard/WHOOPEE/default/"
    public static final ArrayList<Float> pitchMap = new ArrayList<Float> ();
    public static final ArrayList <Float> volumeMap = new ArrayList<Float>();

    public static  boolean              LOGGING = false;     // enable on-device logging & logMail
    public static String                LOG_1                    = APPDIR+"log1.txt";
    public static String                LOG_2                    = APPDIR+"log2.txt";
    public static String                ERROR_FILE               = APPDIR+"error.txt";
    public static final int             LOG_STREAM_BUFFER_SIZE   = 4048;
    public static final int             LOG_FILE_BUFFER_SIZE     = 2048;
    public static int                   ROTATE_LINES             = 1000;   //2000 lines is about 1 Mg

    // ANDROID SPECIFIC
    public static  boolean              SPEAK_NAVIGATION = false;  // can set true if new user
    public static  boolean              SPEAK_HELP = false;              // and/or configure PREF
    public static  SharedPreferences    MACROS;
    public static String                PHONE;
    //public static LogcatActivity        LogDisplay = null;   // an anti-pattern I suppose...but I like it

    // set by call to Autils.showApp() [TODO should move that to guarantee it gets set]
    public static int                 PID;   // used by LogCatThread
    //for ui thread, message dispatching should be enumerated in DroidApp.
    public static Handler UIHandler;

    public static SharedPreferences PREFS;
    public static Resources RES = null;
    public static TelephonyManager TEL_MGR;
    public static AudioManager AUDIO_MGR;
    public static SmsManager SMS_MGR;
    public static WindowManager WIN_MGR;
    public static ActivityManager ACT_MGR;
    public static Vibrator VIBRATE;
    public static Random RANDOM = new Random();
}
