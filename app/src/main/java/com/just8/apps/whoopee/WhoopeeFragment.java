package com.just8.apps.whoopee;

import android.app.Activity;
import android.content.Intent;
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

import com.just8.apps.afilechooser.utils.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class WhoopeeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    public static final String EXTRA_WHOOPEE_ID = "com.just8.apps.whoopee.whoopee_id";
    public static final int REQUEST_CODE_CHOOSER = 0;

    private static final String ARG_whoopeeDir = "default";
    private static final String ARG_PARAM2 = "param2";
    GridView mGridView;
    private int mGridSize = 4;                          //TODO detect portrait orientation and change to 2
    private WhoopeeManager mWhoopeeManager;
    public Whoopee mWhoopee;
    private String whoopeeName;
    private ArrayList<Whoopee> whoopees;


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
                whoopees = WhoopeeData.get().getWhoopees();
                whoopeeName = getArguments().getString(ARG_whoopeeDir);  //TODO asserts

                for (int i = 0; i < whoopees.size(); i++) {
                    if (whoopees.get(i).getMWhoopeeName().equals(whoopeeName)) {               //TODO how does this work? What does mViewPager do with this int?
                        mWhoopee = WhoopeeData.get().getWhoopee(i);
                        break;
                    }
                }
                mWhoopee.activateSound();
        }
        mWhoopeeManager = new WhoopeeManager(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        View decorView = getActivity().getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        actionBar.hide();
//TODO fix a toorbar sized hole in the view when actionBar.hide. Rolled back AppCompatActivity


        View view = inflater.inflate(R.layout.fragment_whoopee, container, false);
        mGridView = (GridView) view.findViewById(R.id.grid);
        mGridView.setAdapter(new WhoopeeAdapter(mWhoopee));
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

/* WEB URI IN WHOOPEE
            if (uri.getScheme().contains("http")) {
                new DownloadImageTask((ImageView) i).execute(uri.toString());
                Log.d(U.getTag(),"DownloadImageTask"+ uri.toString());
                return i;
            } else {
                i.setImageURI(uri);
                return i;
            }*/

        }//private class WhoopeeAdapter
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(G.DEBUG) Log.v( U.getTag(),"REQUEST_CODE_"+ requestCode);

        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case REQUEST_CODE_CHOOSER:
                final Uri uri = data.getData();
                String path = FileUtils.getPath(G.CTX, uri);
                if(G.DEBUG) Log.v( U.getTag(),"CHOOSER CHOOSE"+ path);
                if (path != null && FileUtils.isLocal(path)) {
                    File file = new File(path);
                }
        }
    }


    //TODO move to utility
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
        if(G.G_DEBUG) Log.v( U.getTag()," ");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(G.G_DEBUG) Log.v( U.getTag()," ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(G.G_DEBUG) Log.v( U.getTag()," ");
        mWhoopee.mSoundPool.release();


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public WhoopeeFragment() {}  // Required empty public constructor


}
