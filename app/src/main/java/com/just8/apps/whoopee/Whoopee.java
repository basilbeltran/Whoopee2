package com.just8.apps.whoopee;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class Whoopee {

//TODO decompose Whoopee into constituent frames to enable multiple image animation
//TODO andriod.net.Uri  might be a bad idea figure this out... or use android package on server?
//TODO need write and read from dir methods and compression and up/download

    //private WhoopeeActivity activity;
    private String whoopeeName;
    private File newWhoopeeDir;
    private File newSndDir;
    private File newPixDir;
    Context mContext;

    private String FILE_BASE;
    private Uri[] curPix = new Uri[8];
    private Uri[] curSnd = new Uri[8];
    private Uri[] keySnd = new Uri[8];
    private String[] caption = new String[8];


///CONSTRUCTORS

    /**
     */
//directly reading /sdcard/Whoopee/whoopeeName/
    public Whoopee(String s) {
        whoopeeName = s;
        setFileBase();
        for (int cell = 0; cell < 8; cell++) {
            String pixEnd = "/p" + cell + getExtension("/p", cell);
            String sndEnd = "/s" + cell + getExtension("/s", cell);
            curPix[cell] = Uri.parse(FILE_BASE + pixEnd);
            curSnd[cell] = Uri.parse(FILE_BASE + sndEnd);
            if (G.DEBUG) Log.d(U.getTag(), "ASSIGNING= " + pixEnd);
            if (G.DEBUG) Log.d(U.getTag(), "ASSIGNING= " + sndEnd);
            keySnd = curSnd;
            //swapSound(0,1); //used as prototype for Mixing up puzzle...doesnt work at the moment
            caption = stuff;
        }
        if (G.V_DEBUG) Log.i(G.TAG, U.printArray(getCurPix()));
        if (G.A_DEBUG) Log.i(G.TAG, U.printArray(getCurSnd()));
        mContext = G.CTX;
    }

    private String getExtension(String type, int pos) {
        String s = "";
        File[] files = U.getFiles(G.APPDIR + whoopeeName);
        for (File f : files) {
            String p = f.getPath();
            if (p.contains(type + String.valueOf(pos))) {
                if (G.DEBUG) Log.d(U.getTag(), p);
                return p.substring(p.lastIndexOf("."));
            }
        }//for
        return s;
    }//end

    private void setFileBase() {
        StringBuilder sb = new StringBuilder();
        sb.append("file:/");
        sb.append(G.APPDIR);
        sb.append(whoopeeName);
        FILE_BASE = sb.toString();
        if (G.DEBUG) Log.d(U.getTag(), "FILE_BASE=" + FILE_BASE);

    }




/*public Whoopee (String name, Uri[] images, Uri[] sounds, String[] words){
    whoopeeName = name;
    curPix = images;
    curSnd=sounds;
    caption= words;
    mContext = WhoopeeApp.getAppContext();
    Log.d( U.getTag(), whoopeeName );
}*/


    //use the db to populate the object
    public Whoopee(String whoopeeName, String DbName) {
    }

//OTHER DB FUNCTIONS

    //create db record referencing directory structure
    public boolean saveWhoopeeToDb(String whoopeeName) {
        return true;
    }

    //delete db record referencing directory structure
    public boolean delWhoopeeInDb(String whoopeeName) {
        return true;
    }

    public boolean saveWhoopee(boolean overWrite, String name) {
        whoopeeName = name;
        newWhoopeeDir = U.touchDirectory(G.APPDIR + whoopeeName);
        if (newWhoopeeDir == null) {
            Log.d(U.getTag(), "a whoopee with this name already exists");
            if (!overWrite)
                return false;
        } else {

            // U.touchFile(newWhoopeeDir.getAbsolutePath() +"/captions.txt");
            writeSoundFilesToDevice();

        }// else
        return true;
    }// end

    public boolean deleteWhoopee() {
        return true;
    }


    private boolean writeSoundFilesToDevice() {
        for (int cell = 0; cell < 7; cell++) {
            try {
                File dest = new File(newSndDir, "s" + cell);
                String scheme = curSnd[cell].getScheme();
                Log.i(U.getTag(), "sound scheme " + scheme);
                if (scheme.contains("file")) {
                    File f = new File(new URI(curSnd[cell].toString()));
                    U.copyFile(f, dest);
                } else if (scheme.contains("resource")) {
                    U.copyResToFile(mContext, curSnd[cell], dest);
                } else if (scheme.contains("http")) {
                    //still thinking about this
                }
            } catch (Resources.NotFoundException nfe) {
                // noop ignore
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }// for
        return true;
    }//

    ///TRANSPORT FUNCTIONS
//zip
    public String packageWhoopee(String whoopeeName) {
        return "zip file name";
    }

/*//un-package zipped
public boolean Whoopee (File whoopeeName){
 return true;
}*/

    //send it to the mothership
    public boolean uploadWhoopee(String whoopeeName) {
        return true;
    }


////UTILITY FUNCTIONS

    public void swapSound(int from, int to) {
        Uri temp = curSnd[to];
        curSnd[to] = curSnd[from];
        curSnd[from] = temp;
    }

    public boolean checkSoundKey(int dest) {
        if (curSnd[dest].compareTo(keySnd[dest]) == 0) {
            return true;
        }
        return false;
    }


    public boolean isPuzzleSolved() {
        try {
            if (G.DEBUG) Log.d(U.getTag(),
                    "compare\n" + U.printArray(curSnd) + "\n"
                            + U.printArray(keySnd));
        } catch (Exception e) {
            U.getStackTrace(e);
        }
        if (Arrays.equals(curSnd, keySnd)) {
            Log.d(U.getTag(), "PUZZLE SOLVED");
            return true;
        }
        return false;
    }


/////////GETTERS n SETTERS


    /**
     * @return the curPix
     */
    public Uri[] getCurPix() {
        return curPix;
    }

    public Uri getCurPix(int cell) {
        return curPix[cell];
    }

    /**
     * @param curPix the curPix to set
     */
    public void setCurPix(Uri[] curPix) {
        this.curPix = curPix;
    }

    public void setCurPix(Uri curPix, int position) {
        this.curPix[position] = curPix;
    }

    /**
     * @return the curSnd
     */
    public Uri[] getCurSnd() {
        return curSnd;
    }

    public Uri getCurSnd(int position) {
        return curSnd[position];
    }

    public String getCurPath(int position) {
        return curSnd[position].getPath();
    }

    /**
     * @param curSnd the curSnd to set
     */
    public void setCurSnd(Uri[] curSnd) {
        this.curSnd = curSnd;
    }

    public void setCurSnd(Uri curSnd, int position) {
        this.curSnd[position] = curSnd;
    }

    /**
     * @return the keySnd
     */
    public Uri[] getKeySnd() {
        return keySnd;
    }

    public Uri getKeySnd(int position) {
        return keySnd[position];
    }

    /**
     * @param keySnd the keySnd to set
     */
    public void setKeySnd(Uri[] keySnd) {
        this.keySnd = keySnd;
    }

    public void setKeySnd(Uri keySnd, int position) {
        this.keySnd[position] = keySnd;
    }

    public String getWhoopeeName() {
        return whoopeeName;
    }

    public void setWhoopeeName(String whoopeeName) {
        this.whoopeeName = whoopeeName;
    }

    public File getNewWhoopeeDir() {
        return newWhoopeeDir;
    }

    public void setNewWhoopeeDir(File newWhoopeeDir) {
        this.newWhoopeeDir = newWhoopeeDir;
    }

    public String[] getCaption() {
        return caption;
    }

    public void setCaption(String[] caption) {
        this.caption = caption;
    }

    public String getCaption(int position) {
        return caption[position];
    }

    public void setCaption(String caption, int position) {
        this.caption[position] = caption;
    }


    private String[] stuff = {
            "Umm, the beer was a bad idea, so, ahh, don't tell your mother, ok?",
            "Sorry babe",
            "Activate AquaChick Overdrive",
            "The Vulcans should really have their own deck",
            "I spend my whole life doing this",
            "Honey come here, Does my breath smell??",
            "Can somebody help me get off the stage?",
            "She's got to be kidding"
    };


}/// the very end
