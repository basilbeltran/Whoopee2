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

import java.io.InputStream;
import java.util.ArrayList;

public class WhoopeePageFragment extends Fragment {


    public static final String EXTRA_WHOOPEE_ID = "com.just8.apps.whoopee.id";
    private static final String ARG_whoopeeIndex = "index";
    GridView mGridView;
    private int mGridSize = 4;                          //TODO detect portrait orientation and change to 2
    private WhoopeeGridListener mWhoopeeManager;
    private WhoopeeAdapter mWhoopeeAdapter;
    public Whoopee mWhoopee;
    private int mIndex;
    private ArrayList<Whoopee> whoopees;

    public static WhoopeePageFragment newInstance(int index) {
        WhoopeePageFragment fragment = new WhoopeePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_whoopeeIndex, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARG_whoopeeIndex);
            mWhoopee = WhoopeeData.get().getWhoopee(mIndex);
            mWhoopee.activateSound();        }
        mWhoopeeManager = new WhoopeeGridListener(this);
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_whoopee, container, false);
        mGridView = (GridView) view.findViewById(R.id.grid);
        mWhoopeeAdapter = new WhoopeeAdapter(mWhoopee);
        mGridView.setAdapter(mWhoopeeAdapter);
        mGridView.setOnTouchListener(mWhoopeeManager);
        return view;
    }

    private void refresh(){
        if (G.DEBUG) Log.v(U.getTag(), "refreshrefreshrefreshrefreshrefresh *************  ");

        mWhoopee = WhoopeeData.get().getWhoopee(mIndex);
        mWhoopee.activateSound();
        mWhoopeeAdapter = new WhoopeeAdapter(mWhoopee);
        mGridView.setAdapter(mWhoopeeAdapter);
    }

    private class WhoopeeAdapter extends BaseAdapter {
        private Whoopee mWhoopee;

        public WhoopeeAdapter(Whoopee whoop) {
            mWhoopee = whoop;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (G.G_DEBUG) Log.v(U.getTag(), "FOR POSITION " + position);

            Uri uri = null;
            uri = mWhoopee.getSight(position);
            ImageView imageView;

            if (convertView == null) {
                imageView = new ImageView(G.CTX);
                imageView.setLayoutParams(new GridView.LayoutParams(U.getScreenWidth() / 4, U.getScreenHeight() / 2));
                //TODO i.setLayoutParams(new GridView.LayoutParams( U.getScreenWidth() / 2,  U.getScreenHeight() / 4)  );
                //TODO also need new portrait xml if it chooses to use that orientation
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setId(position);
                //i.setOnTouchListener();  //  hierarchy is PageView -> GridView -> ImageView

            } else {
                imageView = (ImageView) convertView;
                Log.d(U.getTag(), "recycled #" + imageView.getId());
            }

            imageView.setImageURI(uri);
            return imageView;

/* WEB URI IN WHOOPEE
            if (uri.getScheme().contains("http")) {
                new DownloadImageTask((ImageView) imageView).execute(uri.toString());
                Log.d(U.getTag(),"DownloadImageTask"+ uri.toString());
                return imageView;
            } else {
                imageView.setImageURI(uri);
                return imageView;
            }*/

        }//private class WhoopeeAdapter


        public int getCount() {
            if (G.G_DEBUG) Log.v(U.getTag(), "");
            return (8);
        }

        public Object getItem(int position) {
            if (G.G_DEBUG) Log.v(U.getTag(), "");
            return null;
        }

        public long getItemId(int position) {
            if (G.G_DEBUG) Log.v(U.getTag(), String.valueOf(position));
            return 0;
        }
    }


    //TODO move to utility
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            if (G.G_DEBUG) Log.v(U.getTag(), String.valueOf(imageView.getId()));

            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            if (G.G_DEBUG) Log.v(G.TAG + "doInBackground", urldisplay);

            Bitmap result = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                result = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(Bitmap result) {
            if (G.G_DEBUG) Log.v(G.TAG + "onPostExecute", "");
            imageView.setImageBitmap(result);
        }
    }

    ////COMMUNICATION

    public void edit(int position) {
        Intent intent = new Intent(G.CTX, com.just8.apps.whoopee.WhoopeeEditActivity.class);
        intent.putExtra(WhoopeeEditFragment.EXTRA_NAME_POSITION, position);
        intent.putExtra(WhoopeeEditFragment.EXTRA_NAME_INDEX, mIndex);

        Log.d(U.getTag(), "startActivityForResult index:position" + mIndex+":"+position);

        startActivityForResult(intent, WhoopeeEditFragment.REQUEST_CODE_EDIT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (G.DEBUG) Log.v(U.getTag(), "REQUEST_CODE:RESULT_CODE *************  " + requestCode+":"+resultCode);
        //if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case WhoopeeEditFragment.REQUEST_CODE_EDIT:
                if (G.DEBUG) Log.v(U.getTag(), "refresh *************  ");
                refresh();
                mWhoopeeAdapter.notifyDataSetChanged();
                break;
        }
    }

    public GridView getGridView() {
        return mGridView;
    }

    public WhoopeePageFragment() {}  // Required empty public constructor


}
