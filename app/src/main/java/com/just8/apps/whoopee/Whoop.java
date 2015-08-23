package com.just8.apps.whoopee;

import android.util.Log;

/**
 * BB
 * Created by kandinski on 2015-08-19.
 */
public class Whoop {
    private String soundFileName;
    private String sightFileName;
    private Integer soundId;
    private int gridPosition;


    public Whoop(int pos, String soundFileName, String sightFileName) {
        this.soundFileName = soundFileName;
        this.sightFileName = sightFileName;
        this.gridPosition = pos;
        if (G.DEBUG) Log.d(U.getTag(), "new whoop"+G.PS+pos+G.PS+soundFileName+G.PS+sightFileName);
    }

    public String getSightFileName() {
        return sightFileName;
    }

    public void setSightFileName(String sightFileName) {
        this.sightFileName = sightFileName;
    }

    public String getSoundFileName() {
        return soundFileName;
    }

    public void setSoundFileName(String soundFileName) {
        this.soundFileName = soundFileName;
    }

    public Integer getSoundId() {
        return soundId;
    }

    public void setSoundId(Integer soundId) {
        this.soundId = soundId;
    }

    public int getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(int gridPosition) {
        this.gridPosition = gridPosition;
    }
}
