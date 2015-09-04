package com.just8.apps.whoopee;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

public class WhoopeeEditFragment extends Fragment {
    public static final String EXTRA_WHOOPEE_ID = "com.just8.apps.whoopee.whoopee_id";
    public static final int REQUEST_CODE_EDIT = 0;
    public static final int REQUEST_CODE_CHOOSE_IMAGE_FILE = 1;
    public static final int REQUEST_CODE_CHOOSE_SOUND_FILE = 2;
    public static final int REQUEST_CODE_EDIT_SOUND = 3;
    public static final String EXTRA_NAME_INDEX = "com.just8.apps.whoopee.index";
    public static final String EXTRA_NAME_POSITION = "com.just8.apps.whoopee.position";


    private static final String ARG_whoopeeDir = "default";
    private GridView mGridView;
    private int mGridSize = 4;                          //TODO detect portrait orientation and change to 2
    private WhoopeeEditListener mWhoopeeManager;
    private WhoopeeAdapter mWhoopeeAdapter;
    public Whoopee mWhoopee;
    private int edit_cell_position;

    public static WhoopeeEditFragment newInstance() {
        WhoopeeEditFragment fragment = new WhoopeeEditFragment();
        return fragment;
    }

    public GridView getGridView() {
        return mGridView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        int index = intent.getIntExtra(EXTRA_NAME_INDEX, 0);
        edit_cell_position = intent.getIntExtra(EXTRA_NAME_POSITION, 0);
        Log.d(U.getTag(), "WILL EDIT " + index+":"+edit_cell_position);

        mWhoopee = WhoopeeData.get().getWhoopee(index);
        mWhoopee.activateSound();
        mWhoopeeManager = new WhoopeeEditListener(this);
    }

    public void  newWhoopee(){
        mWhoopee.mSoundPool.release();
        mWhoopee = WhoopeeData.get().getWhoopeeTemplate();
        mWhoopee.activateSound();
        mWhoopeeAdapter.notifyDataSetChanged();
        mWhoopeeAdapter = new WhoopeeAdapter(mWhoopee);
        mGridView.setAdapter(mWhoopeeAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (G.G_DEBUG) Log.v(U.getTag(), " ");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (G.G_DEBUG) Log.v(U.getTag(), " ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (G.G_DEBUG) Log.v(U.getTag(), " ");
        mWhoopee.mSoundPool.release();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_whoopee, container, false);
        mGridView = (GridView) view.findViewById(R.id.grid);
        mGridView.setBackgroundColor(Color.GRAY);
        mWhoopeeAdapter = new WhoopeeAdapter(mWhoopee);
        mGridView.setAdapter(mWhoopeeAdapter);
        mGridView.setOnTouchListener(mWhoopeeManager);
        return view;
    }


    private class WhoopeeAdapter extends BaseAdapter {
        private Whoopee mWhoopee;

        public WhoopeeAdapter(Whoopee whoop) {
            mWhoopee = whoop;
        }

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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (G.G_DEBUG) Log.v(U.getTag(), "FOR POSITION " + position);

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


            switch (position) {
                case 0:
                    imageView.setImageURI(mWhoopee.getSight(edit_cell_position));
                    break;
                case 1:
                    imageView.setImageResource(R.mipmap.edit_sound);
                    break;
                case 2:
                    imageView.setImageResource(R.mipmap.edit_search);
                    break;
                case 3:
                    imageView.setImageResource(R.mipmap.edit_share);
                    break;
                case 4:
                    imageView.setImageResource(R.mipmap.edit_next );
                    break;
                case 5:
                    imageView.setImageResource(R.mipmap.edit_images );
                    break;
                case 6:
                    imageView.setImageResource(R.mipmap.edit_photo );
                    break;
                case 7:
                    imageView.setImageResource(R.mipmap.edit_save);
                    break;
                default:
                    break;
            }

            return imageView;
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

    public void save() {
        WhoopeeData.get().saveWhoopees();
        mWhoopee.mSoundPool.release();
        if (G.DEBUG) Log.v(U.getTag(), "   " );

    }

    public void next() {
        edit_cell_position = (edit_cell_position+1) % 8 ;
        mWhoopeeAdapter.notifyDataSetChanged();
    }

    public void startSoundEditor(Uri uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_EDIT, uri);
            //intent.putExtra("was_get_content_intent", mWasGetContentIntent);
            intent.setClassName("com.ringdroid", "com.ringdroid.RingdroidEditActivity");
            startActivityForResult(intent, REQUEST_CODE_EDIT_SOUND);
        } catch (Exception e) {
            Log.e("Ringdroid", "Couldn't start editor");
        }
    }

    public void startChooseImageFile() {
        Intent i = new Intent(getActivity(), com.just8.apps.afilechooser.FileChooserActivity.class);
        startActivityForResult(i, REQUEST_CODE_CHOOSE_IMAGE_FILE);
    }

    public void startChooseSoundFile() {
        Intent i = new Intent(getActivity(), com.just8.apps.afilechooser.FileChooserActivity.class);
        startActivityForResult(i, REQUEST_CODE_CHOOSE_SOUND_FILE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (G.DEBUG) Log.v(U.getTag(), "REQUEST_CODE:RESULT_CODE   " + requestCode+":"+resultCode);
        //todo if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_IMAGE_FILE:
                //mFilename = intent.getData().toString().replaceFirst("file://", "").replaceAll("%20", " ");
                Uri image = data.getData();
                mWhoopee.setSight(image, edit_cell_position);
                mWhoopeeAdapter.notifyDataSetChanged();
                break;
            case REQUEST_CODE_CHOOSE_SOUND_FILE:
                //mFilename = intent.getData().toString().replaceFirst("file://", "").replaceAll("%20", " ");
                Uri sound = data.getData();
                mWhoopee.setSound(sound, edit_cell_position);
                mWhoopee.activateSound();
                break;
            case REQUEST_CODE_EDIT_SOUND:
                break;
        }
    }


    public WhoopeeEditFragment() {
    }  // Required empty public constructor


    public int getEdit_cell_position() {
        return edit_cell_position;
    }




}
