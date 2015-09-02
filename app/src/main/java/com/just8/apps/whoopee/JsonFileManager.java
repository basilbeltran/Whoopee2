package com.just8.apps.whoopee;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


/**
 * BB
 * Created by kandinski on 2015-08-02.
 */
public class JsonFileManager {
    private String mFilename;

    public JsonFileManager(String fileName) {
        mFilename = fileName;
    }

    public ArrayList<Whoopee> loadObjectsFromJSON() throws IOException, JSONException {
        Log.d(U.getTag(), "new ArrayList from " + G.APPDIR + mFilename);

        ArrayList<Whoopee> objects = new ArrayList<>();
        BufferedReader reader = null;

        try {
            InputStreamReader in;

            if(G.configIsExternal) {
                Log.d(U.getTag(), "READ EXTERNAL STORAGE " + G.APPDIR + mFilename);
                in = new InputStreamReader(new FileInputStream(G.APPDIR + G.configJson));
            }
            else if(G.newInstall){
                Log.d(U.getTag(), "READ ASSET STORAGE " + mFilename);
                in = new InputStreamReader( G.CTX.getAssets().open(mFilename));
            }
            else{
                Log.d(U.getTag(), "READ LOCAL STORAGE " + mFilename);
                in = new InputStreamReader(G.CTX.openFileInput(mFilename));
            }

            reader = new BufferedReader(in);
            StringBuilder jsonString = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) { jsonString.append(line); }  // Line breaks are omitted and irrelevant
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();  // Parse the JSON using JSONTokener.
            for (int i = 0; i < array.length(); ++i) {   // ArrayList of objects from JSONObjects
                objects.add(new Whoopee(array.getJSONObject(i)));
                Log.d(U.getTag(), "Populated Whoopee...... " + objects.get(i).getMWhoopeeName());
            }

        } catch (FileNotFoundException e) {
            // Starting without a file, so ignore.
        } finally {
            if (reader != null)
                reader.close();
        }
        return objects;
    }


    public String saveObjects2JSON(ArrayList<Whoopee> objects) throws JSONException, IOException {
        // Build an array in JSON
        JSONObject jWhoop;
        JSONArray array = new JSONArray();

        for (Whoopee w : objects) {
            jWhoop = w.toJSON();
            array.put(jWhoop);
        }
        // Write the file to disk
        OutputStreamWriter writer = null;

            FileOutputStream out = null;
            if(G.configIsExternal) {
                out = new FileOutputStream(G.APPDIR + G.configJson);
                Log.d(U.getTag(), "WROTE EXTERNAL FILE " + G.APPDIR + mFilename);
            }
            else{
                out = G.CTX.openFileOutput(mFilename, Context.MODE_PRIVATE);
                Log.d(U.getTag(), "WROTE LOCAL FILE " + mFilename);
            }

            writer = new OutputStreamWriter(out);
            writer.write(array.toString());

            if (writer != null) writer.close();
            return array.toString();
    }
}


