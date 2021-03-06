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
                Log.e(U.getTag(), "Error using defult config, REMOVING !!! "+G.APPDIR  ,e);
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

    public static void reread() {
        mData = null;
        mData = new WhoopeeData();
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
            Log.e(U.getTag(), "whoopee not found Incorrect config, ............uninstalling!!",e);
            U.DeleteRecursive( new File(G.APPDIR));
        }
        return null;
    }

    public Whoopee getWhoopeeTemplate() {
        Whoopee tWhoopee = null;
        for (int i = 0; i < mWhoopees.size(); i++) {
            if (mWhoopees.get(i).getMWhoopeeName().equals("template")) {
                tWhoopee = WhoopeeData.get().getWhoopee(i);
                Log.d(U.getTag(), "FOUND TEMPLATE.... " + tWhoopee.getMWhoopeeName());
                break;
            }
        }
        Whoopee nWhoopee = new Whoopee(tWhoopee.getSoundList(), tWhoopee.getSightList());
        addWhoopee(nWhoopee);
        Log.d(U.getTag(), "returning.... " + nWhoopee.getMWhoopeeName());
        return nWhoopee;
    }

    public void addWhoopee(Whoopee c) {
        mWhoopees.add(c);
        Log.d(U.getTag(), "ADDED to mWhoopees **************ArraySize:" + mWhoopees.size() + " : " + c.getMWhoopeeName());
        saveWhoopees();
    }

    public void deleteWhoopee(Whoopee c) {

        mWhoopees.remove(c);
    }

    public boolean saveWhoopees() {
        try {
            Log.d(U.getTag(), "writing mWhoopees to file. ArraySize:"+mWhoopees.size());
            String outfile = mObjects2JSON.saveObjects2JSON((ArrayList) mWhoopees);
            Log.d(U.getTag(), "mWhoopees saved to file \n" +outfile);
            reread();
            return true;
        } catch (Exception e) {
            Log.e(U.getTag(), "Error saving mWhoopees: ", e);
            return false;
        }
    }
}