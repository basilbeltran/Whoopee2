package com.just8.apps.whoopee;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;


/**
 *
 * Created by kandinski on 2015-08-11.
 */
public class WhoopeeListAdapter extends RecyclerView.Adapter<WhoopeeListAdapter.RvViewHolder> {
    private LayoutInflater inflator;
    private List<Whoopee> mObjectList;
    private Context mContext;

    public WhoopeeListAdapter(List<Whoopee> objectList, Context context) {
        mObjectList = objectList;
        inflator = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public WhoopeeListAdapter.RvViewHolder onCreateViewHolder(ViewGroup recyclerView, int i) {
        View itemView = inflator.inflate(R.layout.fragmant_whoopee_recycler, recyclerView, false);
        RvViewHolder mRvViewHolder = new RvViewHolder(itemView);
        Log.v(U.getTag(), "onCreateViewHolder() called");
        return mRvViewHolder;
    }

    @Override
    public void onBindViewHolder(RvViewHolder viewHolder, int position) {
        Whoopee mWhoopee = mObjectList.get(position);
        viewHolder.bindView(mWhoopee);
        Log.v(U.getTag(), "onBindViewHolder() called");

    }

    @Override
    public int getItemCount() {
        return mObjectList.size();
    }


    class RvViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox checkBoxHolder;
        TextView titleTextViewholder;
        TextView dateTextViewholder;
        Whoopee mWhoopee;

        public RvViewHolder(View itemViewRoot) {
            super(itemViewRoot);
            itemView.setOnClickListener(this);
            checkBoxHolder = (CheckBox) itemView.findViewById(R.id.rvCheckBox);
            titleTextViewholder = (TextView) itemView.findViewById(R.id.rvTitleTextView);
            dateTextViewholder = (TextView) itemView.findViewById(R.id.CategoryTextView);
        }

        public void bindView(Whoopee Whoopee) {
            mWhoopee=Whoopee;
            checkBoxHolder.setChecked(mWhoopee.isSolved());
            titleTextViewholder.setText(mWhoopee.getMWhoopeeName());
            dateTextViewholder.setText(mWhoopee.getCategory());
        }

        @Override
        public void onClick(View v) {

            //getAdapterPosition();
            Log.d(U.getTag(), "CRIME SELECTED = " + mWhoopee.getMWhoopeeName());
            Intent i = new Intent(mContext, WhoopeePagerActivity.class);
            i.putExtra(WhoopeePageFragment.EXTRA_WHOOPEE_ID, mWhoopee.getId());
            mContext.startActivity(i);
        }
    }//class

}//file