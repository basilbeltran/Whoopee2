package com.just8.apps.whoopee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * BB
 * Created by kandinski on 2015-08-26.
 */
public class WhoopeeListFragment extends Fragment {  
    private boolean mSubtitleVisible;
    private ArrayList<Whoopee> mObjectList;

    private RecyclerView mRecyclerView;
    private RvAdapter mRvAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mSubtitleVisible = false;
        getActivity().setTitle(R.string.);
        mObjectList = WhoopeeManager.get(getActivity()).getWhoopees();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle  savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_layout, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvRootId);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mSubtitleVisible) {
            getActivity().getActionBar().setSubtitle(R.string.subtitle);
        }
        updateUI();
        return view;
    }

    private void updateUI() {
        WhoopeeManager mWhoopeeManager = WhoopeeManager.get(getActivity());
        List<Whoopee> Whoopees = mWhoopeeManager.getWhoopees();


        if (mRvAdapter == null) {
            mRvAdapter = new RvAdapter(Whoopees, this.getActivity());
            mRecyclerView.setAdapter(mRvAdapter);
        } else { mRvAdapter.notifyDataSetChanged(); }
        Log.v(U.getTag(), "updateUI() called");

    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_whoopee_list, menu);
        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible && showSubtitle != null) {
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Whoopee crime = new Whoopee();
                WhoopeeManager.get(getActivity()).addCrime(crime);
                startCrimePager(crime.getId());
                return true;
            case R.id.menu_item_show_subtitle:
                if (getActivity().getActionBar().getSubtitle() == null) {
                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    mSubtitleVisible = true;
                    item.setTitle(R.string.hide_subtitle);
                } else {
                    getActivity().getActionBar().setSubtitle(null);
                    mSubtitleVisible = false;
                    item.setTitle(R.string.show_subtitle);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.whoopee_list_item_context, menu);
    }


    public void startCrimePager(UUID id){
        Intent i = new Intent(getActivity(), WhoopeePagerActivity.class);
        i.putExtra(WhoopeeFragment.EXTRA_WHOOPEE_ID, id);
        startActivity(i);
    }

}