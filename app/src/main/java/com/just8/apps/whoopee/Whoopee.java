package com.just8.apps.whoopee;


import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class Whoopee {
    private ArrayList<Uri> mSoundList = new ArrayList<>();
    private ArrayList<Uri> mSightList = new ArrayList<>();
    private String FILE_BASE;
    private String mMWhoopeeName;
    private String category;
    private static final String JSON_TITLE = "title";
    private static final String JSON_SIGHTS = "sights";
    private static final String JSON_SOUNDS = "sounds";
    private static final String JSON_ID = "uuid";
    private JSONObject mJsonObject;
    private UUID mId;
    private boolean solved;
    private static final int MAX_SOUNDS = 10;
    public SoundPool mSoundPool;
    //TODO abstract sound handling functions

    public   Whoopee(String name) {
        mMWhoopeeName = name;
        setFileBase();
        File whoopeeDir = new File(G.APPDIR +mMWhoopeeName);
        mId = UUID.randomUUID();
        String[] art= whoopeeDir.list();
        for (String fName: art) {
            if (fName.startsWith("s")) {
                Uri sound = Uri.parse(FILE_BASE + fName);
                mSoundList.add(sound) ;
            }
            else mSightList.add(  Uri.parse(FILE_BASE + fName)  );
        }
    }


    public  Whoopee(JSONObject jObject) throws JSONException {
        mId = UUID.fromString(jObject.getString(JSON_ID));
        mMWhoopeeName = jObject.getString(JSON_TITLE);
        JSONArray ja =  jObject.getJSONArray(JSON_SOUNDS);
        int len = ja.length();
        for (int i=0;i<len;i++){
            Uri sound =  Uri.parse( ja.get(i).toString() );
            mSoundList.add(sound);
        }

        ja =  jObject.getJSONArray(JSON_SIGHTS);
        len = ja.length();
        for (int i=0;i<len;i++){
            Uri sight = Uri.parse( ja.get(i).toString() );
            mSightList.add(sight);
        }
        mJsonObject = jObject;
    }

    public SoundPool activateSound(){
        mSoundPool = new SoundPool( MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        for(Uri sound: mSoundList)  mSoundPool.load(sound.getPath(), 1);
        return mSoundPool;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mMWhoopeeName);
        json.put(JSON_SIGHTS, new JSONArray(mSightList));
        json.put(JSON_SOUNDS, new JSONArray(mSoundList));
        mJsonObject = json;
        return json;
    }

    public UUID getId() {
        return mId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public ArrayList<Uri> getSoundList() {
        return mSoundList;
    }

    public ArrayList<Uri> getSightList() {
        return mSightList;
    }

    public Uri getSound(int pos) {
        return mSoundList.get(pos);
    }

    public Uri getSight(int pos) {
        return mSightList.get(pos);
    }

    public void setSound(Uri sound, int pos) {
         mSoundList.set(pos, sound);
    }

    public void setSight(Uri sight, int pos) {
         mSightList.set(pos, sight);
    }
    public String getMWhoopeeName() {
        return mMWhoopeeName;
    }

    public void setMWhoopeeName(String MWhoopeeName) {
        mMWhoopeeName = MWhoopeeName;
    }

    private void setFileBase() {
        StringBuilder sb = new StringBuilder();
        sb.append("file:/");
        sb.append(G.APPDIR);
        sb.append(mMWhoopeeName + G.FS);
        FILE_BASE = sb.toString();
        if (G.DEBUG) Log.d(U.getTag(), "FILE_BASE=" + FILE_BASE);
    }


    public String toString(){
         String s = "\n"+ JSON_ID + G.PS + mId+
                    "\n"+ JSON_TITLE + G.PS + mMWhoopeeName+
                    "\n"+ JSON_SIGHTS + G.PS + U.arrayToString(mSightList.toArray())+
                    "\n\n"+ JSON_SOUNDS + G.PS + U.arrayToString(mSoundList.toArray()) ;
        return s;
    }
}/// the very end
