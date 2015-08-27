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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


/**
 * BB
 * Created by kandinski on 2015-08-02.
 */
public class JsonFileManager {
    private static final String TAG = "JsonFileManager";
    private Context mContext;
    private String mFilename;

    public JsonFileManager(Context c, String fileName) {
        mContext = c;
        mFilename = fileName;
    }


    public ArrayList<Whoopee> loadObjectsFromJSON() throws IOException, JSONException {
        ArrayList<Whoopee> objects = new ArrayList<>();
        BufferedReader reader = null;

        try {
            FileInputStream in;
            in = mContext.openFileInput(mFilename);
            Log.d(TAG, "READ LOCAL STORAGE " + mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) { jsonString.append(line); }  // Line breaks are omitted and irrelevant
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();  // Parse the JSON using JSONTokener.
            for (int i = 0; i < array.length(); ++i)    // ArrayList of objects from JSONObjects
                objects.add(new Whoopee(array.getJSONObject(i)));
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
        Log.d(U.getTag(), "jSights " + "ENTERING" );

        for (Whoopee w : objects) {
            jWhoop = w.toJSON();
            JSONArray jSights = jWhoop.getJSONArray("sights");
            Log.d(U.getTag(), "jSights " +  jSights.toString()  );
            array.put(jWhoop);
        }
        // Write the file to disk
        OutputStreamWriter writer = null;

            FileOutputStream out = null;

            out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            Log.d(U.getTag(), "WROTE LOCAL FILE " + mFilename);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());

            if (writer != null) writer.close();
            return array.toString();

    }


}


