package com.just8.apps.whoopee;


import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * BB
 * Created by kandinski on 2015-07-21.
 */
public class WhoopeeData {

    private static WhoopeeData mData;
    private List<Whoopee> mWhoopees;

    private JsonFileManager mObjects2JSON;
    private static final String FILENAME = G.configJson;


    private WhoopeeData() {
        mObjects2JSON = new JsonFileManager(FILENAME);
        try {
            mWhoopees = mObjects2JSON.loadObjectsFromJSON();
            Log.e(U.getTag(), "Populated mWhoopees: "+mWhoopees.size());

        } catch (Exception e) {
            Log.e(U.getTag(), "Error loading with config, using defult config: ", e);
            G.newInstall=true;
            try {
                mWhoopees = mObjects2JSON.loadObjectsFromJSON();
            } catch (Exception x) {
                Log.e(U.getTag(), "Error using defult config, removing "+G.APPDIR  ,e);
                U.DeleteRecursive(new File(G.APPDIR));
            }


        }
        //populate();
    }

    public static WhoopeeData get() {
        if (mData == null) {
            mData = new WhoopeeData();
        }
        return mData;
    }

    public static void reset() {
        mData = null;
    }

    public ArrayList<Whoopee> getWhoopees(){
        return (ArrayList) mWhoopees;
    }


    public Whoopee getWhoopee(UUID id) {
        for (Whoopee w : mWhoopees) {
            if (w.getId().equals(id))
                return w;
        }
        return null;
    }

    public Whoopee getWhoopee(int position) {
        try {
            return mWhoopees.get(position);
        } catch (Exception e) {
            Log.e(U.getTag(), "Incorrect config, ............uninstalling \n",e);
            U.DeleteRecursive( new File(G.APPDIR));
        }
        return null;
    }


    public void addWhoopee(Whoopee c) {

        mWhoopees.add(c);
        Log.d(U.getTag(), "added to mWhoopees \n" + c.getMWhoopeeName());

    }

    public void deleteWhoopee(Whoopee c) {
        mWhoopees.remove(c);
    }

    public boolean saveWhoopees() {
        try {
            Log.d(U.getTag(), "writing mWhoopees to file. ArraySize:"+mWhoopees.size());
            String outfile = mObjects2JSON.saveObjects2JSON((ArrayList) mWhoopees);
            Log.d(U.getTag(), "mWhoopees saved to file \n" +outfile);
            return true;
        } catch (Exception e) {
            Log.e(U.getTag(), "Error saving mWhoopees: ", e);
            return false;
        }
    }

    private void populate(){
        for (int i = 0; i < 2; i++) {
            Whoopee c = new Whoopee();
            c.setMWhoopeeName("Whoopee #" + i);
            c.setCategory("test");
            c.setSolved(i % 2 == 0); // Every other one
            mWhoopees.add(c);
        }
    }
}