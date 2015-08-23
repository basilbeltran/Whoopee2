package com.just8.apps.whoopee;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WhoopeeRvFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WhoopeeRvFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhoopeeRvFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_whoopeeDir = "default";
    private static final String ARG_PARAM2 = "param2";
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


    public static WhoopeeRvFragment newInstance(String dir, String param2) {
        WhoopeeRvFragment fragment = new WhoopeeRvFragment();
        Bundle args = new Bundle();
        args.putString(ARG_whoopeeDir, dir);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WhoopeeRvFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            whoopeeDir = getArguments().getString(ARG_whoopeeDir);  //TODO assert
            mParam2 = getArguments().getString(ARG_PARAM2);

            mWhoopeeManager = new WhoopeeManager(whoopeeDir);
            glm = new GridLayoutManager(getActivity(), mGridSize);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmant_whoopee_recycler, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.fragment_whoopee_recycler_view);

        recyclerView.setLayoutManager(glm);
//***************************************************************************************************
        recyclerView.setAdapter(new rvAdapter(mWhoopeeManager.getWhoopList()));
//***************************************************************************************************

        return view;
    }

    private class rvAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<Whoop> mWhoops;

        public rvAdapter(List<Whoop> whoops) {
            mWhoops = whoops;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new ItemHolder(inflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            Whoop w = mWhoops.get(position);
            holder.bindWhoop(w);
        }

        @Override
        public int getItemCount() {
            return mWhoops.size();
        }
    }


    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        //TODO remove listener implementations after deriving whoop from view


        private ImageButton mButton;

        private Whoop whoop;

        public ItemHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.whoop_item, container, false));
            mButton = (ImageButton) itemView.findViewById(R.id.list_item_sound_button);
            mButton.setScaleType(ImageView.ScaleType.FIT_XY);
            mButton.setOnClickListener(this);
            mButton.setOnTouchListener(this);
        }

        public void bindWhoop(Whoop w) {
            whoop = w; // TODO im sure your not supposed to hold on to this, see above TODO
            Bitmap bmp = BitmapFactory.decodeFile(w.getSightFileName());
            mButton.setImageBitmap(bmp);
        }

        @Override
        public void onClick(View v) {
            if(G.G_DEBUG) Log.v( U.getTag(), "first click");
            mWhoopeeManager.onClick(v, whoop);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(G.G_DEBUG) Log.v( U.getTag(), "first touch");
            mWhoopeeManager.onTouch(v, event);
            return false;
        }
    }

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
}
