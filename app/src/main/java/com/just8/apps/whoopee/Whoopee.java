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


    public  Whoopee() {
    }

    public   Whoopee(ArrayList sound, ArrayList sight) {
        mSoundList = sound;
        mSightList = sight;
        mId = UUID.randomUUID();
        mMWhoopeeName = "temp";
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
        if(mSoundPool != null) mSoundPool.release();
        mSoundPool = new SoundPool( MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        for(Uri sound: mSoundList)  mSoundPool.load(sound.getPath(), 1);
        return mSoundPool;
    }

    public JSONObject toJSON() throws JSONException {
        JSONArray sights = new JSONArray();
        JSONArray sounds = new JSONArray();

        for(Uri uri : mSightList){
            sights.put(uri);
        }

        for(Uri uri : mSoundList){
            sounds.put(uri);
        }

        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mMWhoopeeName);
        json.put(JSON_SIGHTS, sights);
        json.put(JSON_SOUNDS, sounds);

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


    public String toString(){
         String s = "\n"+ JSON_ID + G.PS + mId+
                    "\n"+ JSON_TITLE + G.PS + mMWhoopeeName+
                    "\n"+ JSON_SIGHTS + G.PS + U.arrayToString(mSightList.toArray())+
                    "\n\n"+ JSON_SOUNDS + G.PS + U.arrayToString(mSoundList.toArray()) ;
        return s;
    }
}/// the very end
