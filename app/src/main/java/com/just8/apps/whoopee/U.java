package com.just8.apps.whoopee;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;


/**
 * BB
 * Created by kandinski on 2015-08-18.
 */
public class U {  // U is for UTILITIES


    ////////////////////////////  ANDROID  ////////////////////////////////////////

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        int totalScreenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        return totalScreenHeight - getStatusBarHeight();
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = Resources.getSystem().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean checkExternalMedia() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        if (G.DEBUG) Log.i(U.getTag(), "\n\nExternal Media: readable="
                + mExternalStorageAvailable + " writable=" + mExternalStorageWriteable);

        return mExternalStorageAvailable && mExternalStorageWriteable;
    }

    //Copies res to dst file.
//If the dst file does not exist, it is created
    public static void copyResToFile(Context ctx, Uri uri, File dst) throws IOException {
        InputStream in = ctx.getContentResolver().openInputStream(uri);
        OutputStream out = new FileOutputStream(dst);

// Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }




    public static boolean CopyAssetDir(Context ctx, String assetDirName, String destPath) {
        if (G.DEBUG) Log.i(U.getTag(), assetDirName + " - into - " + destPath);
        boolean retVal = true;
        AssetManager assetManager = ctx.getAssets();
        String[] files = null;
        try {
            files = assetManager.list(assetDirName);
        } catch (IOException e) {
            Log.e(U.getTag(), e.getMessage());
            e.printStackTrace();
            retVal = false;
        }
        for (int i = 0; i < files.length; i++) {
            if (G.DEBUG) Log.d(U.getTag(), "copying " + assetDirName + G.FS + files[i]);

            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(assetDirName + G.FS + files[i]);
                out = new FileOutputStream(destPath + files[i]);
                copyStream(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e) {
                e.printStackTrace();
                retVal = false;
            } //catch
        }   //for
        return retVal;
    } //CopyAssetDir


    ////////////////////////////  FILE  /  STREAMS  / READERS  ////////////////////////////////////////


    public static File[] getFiles(String dir) {
        File d = new File(dir);
        return (d.listFiles());
    }




/*    class ExtensionAwareFilenameFilter implements FilenameFilter {

        private final Set<String> extensions;

        public ExtensionAwareFilenameFilter(String... extensions) {
            this.extensions = extensions == null ?
                    Collections.emptySet() :
                    Arrays.stream(extensions)
                            .map(e -> e.toLowerCase()).collect(Collectors.toSet());
        }

        @Override
        public boolean accept(File dir, String name) {
            return extensions.isEmpty() ||
                    extensions.contains(getFileExtension(name));
        }

        private String getFileExtension(String filename) {
            String ext = null;
            int i = filename .lastIndexOf('.');
            if(i != -1 && i < filename .length()) {
                ext = filename.substring(i+1).toLowerCase();
            }
            return ext;
        }
    }


    @Test
    public void filefilter() {
        Arrays.stream(new File("D:\\downloads").
                list(new ExtensionAwareFilenameFilter("pdf", "txt")))
                .forEach(e -> System.out.println(e));
    }*/




    //Copies src file to dst file.
    //If the dst file does not exist, it is created
    public static void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }



    public static boolean copyStream(InputStream is, OutputStream os) {
        boolean retVal = false;
        byte[] buffer = new byte[1024];
        int read;
        try {
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
        } catch (IOException e) {
            retVal = false;
            e.printStackTrace();
        }
        return retVal;

    }



    /* If the directory does not exists: create-it */
    public static File touchDirectory(String dirname) {
        File f = new File(dirname);
        if (f.exists()) {
            return null;
        } else {
            f.mkdirs();
            return f;
        }
    }





////////////////////////////  ARRAY  ////////////////////////////////////////

    public static String printArray(Object[] o) throws NullPointerException {
//return Arrays.toString(o);
        return U.arrayToString(o);
    }


    public static String printArray(int[] i) throws NullPointerException {
        String st = "";
        for (int x : i) {
            st = st + ",\n " + Integer.toString(x);
        }
        return st;
    }

    public static String printArray(long[] i) throws NullPointerException {
        String st = "";
        for (long x : i) {
            st = st + ", " + Long.toString(x);
        }
        return st;
    }

    public static String arrayToString(Object[] args) {
        StringBuilder sb = new StringBuilder();

        sb.append("{number of entries=").append(args.length).append("}\n");

        int i = 0;

        for (Object obj : args) {
            if (obj != null) {
                sb.append("[").append(i++).append("] ").append(obj.toString()).append("\n");
            } else {
                sb.append("[").append(i++).append("] ").append("null").append("\n");
            }
        }
        return sb.toString();
    }

////////////////////////////  DEBUG  ////////////////////////////////////////

    public static String getStackTrace(Exception e) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(stream, true);
        e.printStackTrace(writer);
        return stream.toString();
    }

    public static String getTag(int level)
    { String s=Thread.currentThread().getName();
        if (!G.DEBUG) {s = G.APPNAME;}
        else{
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            String name = ste[4].getClassName();
            s =s+":"+ name.substring(name.lastIndexOf('.')+1)+"#"+ ste[4].getMethodName();
        }
        return s;
    }


    public static String getTag() {
        String s = Thread.currentThread().getName();
        if (!G.DEBUG) {
            s = G.TAG;
        } else {
            try {
                StackTraceElement[] ste = Thread.currentThread().getStackTrace();
                String name = ste[3].getClassName();
                s = s + ":" + name.substring(name.lastIndexOf('.') + 1) + "#" + ste[3].getMethodName();
            } catch (Exception e) {
                return G.TAG+"x";
            }
        }
        return s;
    }

    public static String toString(Object o) {
        StringBuilder result = new StringBuilder();
        result.append( " Object {" );
        result.append(G.LF);
        //public fields in this class
        Class cl = o.getClass();
        //if(cl.getEnclosingClass() !=null)cl = cl.getEnclosingClass();
        Field[] fields = cl.getFields();
        for ( Field field : fields  ) {
            result.append("  ");
            try {
                result.append( field.getName() );
                result.append(": ");
                result.append( field.get(o) );
            } catch ( IllegalAccessException ex ) {          //asking for access to private field:
                Log.e(U.getTag(), "cant access "+field.getName());
            }
            result.append(G.LF);
        }
        result.append("}");
        return result.toString();
    }

    public static   String dumpEvent(MotionEvent event) {
        String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
                "POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("\n ACTION_" ).append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid " ).append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")" );
        }
        sb.append("[" );
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#" ).append(i);
            sb.append("(pid " ).append(event.getPointerId(i));
            sb.append(")=" ).append((int) event.getX(i));
            sb.append("," ).append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append("\n" );
        }
        sb.append("]" );
        final int historySize = event.getHistorySize();
        final int pointerCount = event.getPointerCount();
        sb.append("historySize "+historySize+" pointerCount"+pointerCount);
        for (int h = 0; h < historySize; h++) {
            sb.append("At time " +event.getHistoricalEventTime(h));
            for (int p = 0; p < pointerCount; p++) {
                sb.append("  pointerHXY" + event.getPointerId(p)+
                        ":("+event.getHistoricalX(p, h)+
                        ","+event.getHistoricalY(p, h)+")\n"    );
            }
        }
        sb.append("\nAt time " + event.getEventTime());
        for (int p = 0; p < pointerCount; p++) {
            sb.append("  pointerXY" + event.getPointerId(p)+
                    ":("+ event.getX(p)+
                    ","+event.getY(p)+")\n"    );
        }
        return sb.toString();
    } // dumpEvent

    public static String error(Object o, String s, Exception e) {
        StringBuffer sb =new StringBuffer(s);
        sb.
                append("\n"+getTag(4) ).
                append("\n"+e.getMessage()).
                append("\n" + getStackTrace(e));
        if(o != null) sb.append("\n"+toString(o));
        //AUtils.email(sb.toString());
        U.writeToFile(sb.toString(), G.ERROR_FILE);
        if(G.DEBUG) Log.e(G.TAG+" ERROR", sb.toString());
        return sb.toString();
    }

    public static void writeToFile(String msg, String file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(msg.getBytes());
            fos.close();
        }
        catch (Exception e) {
            error(null, "FILE ISSUE", e);
        }
    }

} // UTILITIES
