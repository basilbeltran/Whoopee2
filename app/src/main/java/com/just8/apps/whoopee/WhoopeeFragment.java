package com.just8.apps.whoopee;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class WhoopeeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    public static final String EXTRA_WHOOPEE_ID = "com.just8.apps.whoopee.whoopee_id";
    private static final String ARG_whoopeeDir = "default";
    private static final String ARG_PARAM2 = "param2";

    GridView mGridView;
    private int mGridSize = 4;                          //TODO detect portrait orientation and change to 2
    private WhoopeeManager mWhoopeeManager;
    public Whoopee mWhoopee;
    private String whoopeeName;
    JsonFileManager jfm = new JsonFileManager(G.CTX, "whoopees.json");
    private ArrayList<Whoopee> mWhoopees = new ArrayList<>();

    public static WhoopeeFragment newInstance(String dir) {
        WhoopeeFragment fragment = new WhoopeeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_whoopeeDir, dir);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if(G.isNewUser) {
                Log.i(U.getTag(), " NEW USER:" + G.isNewUser);

                whoopeeName = getArguments().getString(ARG_whoopeeDir);  //TODO asserts
                mWhoopee = new Whoopee(whoopeeName);
                mWhoopee.activateSound();
                mWhoopees.add(mWhoopee);

                try {
                    String outfile = jfm.saveObjects2JSON(mWhoopees);
                    if(G.G_DEBUG) Log.v( U.getTag(),"\n\nOUTFILE:"+ outfile);
                } catch (JSONException e) {e.printStackTrace();
                } catch (IOException e) {e.printStackTrace();}


            }
            else{

                try {   mWhoopees = jfm.loadObjectsFromJSON();
                } catch (IOException e) {e.printStackTrace();
                } catch (JSONException e) {e.printStackTrace();}
                if(G.G_DEBUG) Log.v( U.getTag(),"\n\nWHOOPEESSSSSSS SIZE:"+mWhoopees.size() );

                mWhoopee = mWhoopees.get(0);
                mWhoopee.activateSound();
            }

            mWhoopeeManager = new WhoopeeManager(this);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_whoopee, container, false);
        mGridView = (GridView) view.findViewById(R.id.grid);
        mGridView.setAdapter( new WhoopeeAdapter(mWhoopee) );
        mGridView.setOnTouchListener(mWhoopeeManager);
        return view;
    }

    public GridView getGridView(){
        return mGridView;
    }

    private class WhoopeeAdapter extends BaseAdapter {
        private Whoopee mWhoopee;

        public WhoopeeAdapter(Whoopee whoop) {
            mWhoopee = whoop;
        }

        public int getCount() {
            if(G.G_DEBUG) Log.v( U.getTag(),"");
            return (8);
        }

        public Object getItem(int position) {
            if(G.G_DEBUG) Log.v( U.getTag(),"");
            return null;
        }

        public long getItemId(int position) {
            if(G.G_DEBUG) Log.v( U.getTag(), String.valueOf(position));
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(G.G_DEBUG) Log.v( U.getTag(),"FOR POSITION "+ position);

            Uri uri = null;
            uri = mWhoopee.getSight(position);
            ImageView i;

            if (convertView == null) {
                i = new ImageView(G.CTX);
                i.setLayoutParams(new GridView.LayoutParams(U.getScreenWidth() / 4, U.getScreenHeight() / 2));
                //TODO i.setLayoutParams(new GridView.LayoutParams( U.getScreenWidth() / 2,  U.getScreenHeight() / 4)  );
                //TODO also need new portrait xml if it chooses to use that orientation
                i.setScaleType(ImageView.ScaleType.FIT_XY);
                i.setId(position);
                //i.setOnTouchListener(mWhoopeeManager);

            } else {
                i = (ImageView) convertView;
                Log.d(U.getTag(), "recycled #" + i.getId());
            }

            i.setImageURI(uri);
            return i;

/*            if (uri.getScheme().contains("http")) {
                new DownloadImageTask((ImageView) i).execute(uri.toString());
                Log.d(U.getTag(),"DownloadImageTask"+ uri.toString());
                return i;
            } else {
                i.setImageURI(uri);
                return i;
            }*/
        }//private class WhoopeeAdapter
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            if(G.G_DEBUG) Log.v( U.getTag(), String.valueOf(bmImage.getId()));

            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            if(G.G_DEBUG) Log.v( G.TAG+"doInBackground",urldisplay);

            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if(G.G_DEBUG) Log.v( G.TAG+"onPostExecute","");
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mWhoopee.mSoundPool.release();

        try {
            String outfile = jfm.saveObjects2JSON(mWhoopees);
            if(G.G_DEBUG) Log.v( U.getTag(),"\n\nOUTFILE:"+ outfile);
        } catch (JSONException e) {e.printStackTrace();
        } catch (IOException e) {e.printStackTrace();}


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public WhoopeeFragment() {}  // Required empty public constructor


}
