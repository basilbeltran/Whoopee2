package com.just8.apps.whoopee;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WhoopeeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WhoopeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhoopeeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_whoopeeDir = "default";
    private static final String ARG_PARAM2 = "param2";

    GridView mGridView;
    private int mGridSize = 4;
    private WhoopeeManager mWhoopeeManager;
    GridLayoutManager glm;

    // TODO: Rename and change types of parameters
    private String whoopeeDir;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dir Parameter 1. Location of selected directory under /ExternalStorage/WHOOPEE/
     * @param param2 Parameter 2.
     * @return A new instance of fragment WhoopeeFragment.
     */


    public static WhoopeeFragment newInstance(String dir, String param2) {
        WhoopeeFragment fragment = new WhoopeeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_whoopeeDir, dir);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            whoopeeDir = getArguments().getString(ARG_whoopeeDir);  //TODO assert
            mParam2 = getArguments().getString(ARG_PARAM2);

            mWhoopeeManager = new WhoopeeManager(whoopeeDir);
            mWhoopeeManager.setFragment(this);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_whoopee, container, false);
        mGridView = (GridView) view.findViewById(R.id.grid);
        mGridView.setAdapter(new WhoopeeAdapter(mWhoopeeManager.getWhoopList()));
        mGridView.setOnTouchListener(mWhoopeeManager);
        return view;
    }

    public GridView getGridView(){
        return mGridView;
    }

    private class WhoopeeAdapter  extends BaseAdapter {
        private List<Whoop> mWhoops;

        public WhoopeeAdapter(List<Whoop> whoops) {
            mWhoops = whoops;
        }

        public int    getCount() { return(8);  }
        public Object getItem(int position) { return null; }
        public long   getItemId(int position) { return 0; }

        /**
         * Get a View that displays the data at the specified position in the data set. You can either
         * create a View manually or inflate it from an XML layout file. When the View is inflated, the
         * parent View (GridView, ListView...) will apply default layout parameters unless you use
         * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
         * to specify a root view and to prevent attachment to the root.
         *
         * @param position    The position of the item within the adapter's data set of the item whose view
         *                    we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *                    is non-null and of an appropriate type before using. If it is not possible to convert
         *                    this view to display the correct data, this method can create a new view.
         *                    Heterogeneous lists can specify their number of view types, so that this View is
         *                    always of the right type (see {@link #getViewTypeCount()} and
         *                    {@link #getItemViewType(int)}).
         * @param parent      The parent that this view will eventually be attached to
         * @return A View corresponding to the data at the specified position.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i;
            if (convertView == null) {
                i = new ImageView(G.CTX);
                i.setLayoutParams(new GridView.LayoutParams( U.getScreenWidth() / 4,  U.getScreenHeight() / 2)  );
                i.setScaleType(ImageView.ScaleType.FIT_XY);
                i.setId(position);
                //i.setOnTouchListener(mWhoopeeManager);

            } else {
                i = (ImageView) convertView;
                Log.d(G.TAG, "recycled #" + i.getId());
            }

            Bitmap bmp = BitmapFactory.decodeFile(mWhoops.get(position).getSightFileName());
            i.setImageBitmap(bmp);
            return i;
        }
    }//private class WhoopeeAdapter



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mWhoopeeManager.release(); }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    public WhoopeeFragment() {}  // Required empty public constructor


}
